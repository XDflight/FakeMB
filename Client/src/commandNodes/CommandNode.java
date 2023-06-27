package commandNodes;

import security.LoginStatus;
import security.OperatorLevel;
import server.DataManager;
import server.structs.AccountData;
import server.structs.annotations.HashElement;
import server.structs.annotations.LoginRequired;
import server.structs.annotations.RegisterRequired;
import util.Logger;
import server.structs.DataClass;
import util.ReflectHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static commands.SystemCommand.saveDbChanges;
import static server.structs.DataClass.fromParam;
import static util.ObjectBridge.rowToEntry;
import static util.StringHelper.clearSuffix;
import static util.StringHelper.getTrueTypeString;

public class CommandNode {
    public String name;
    private Consumer<Context> executable;
    private Supplier<Boolean> hasPower;

    Map<String, CommandNode> children = new HashMap<>();
    boolean hasFork = false;
    boolean isParameterStarter = false;
    String nextParameter;

    Logger LOGGER = new Logger();

    public boolean hasFork() {
        return hasFork;
    }

    public boolean isEnd() {
        return !hasFork;
    }

    public String toStringTop() {
        return name + "\n" + children.toString();
    }

    public String toString() {
        return children.toString();
    }

    public static CommandNode makeAccountCommands(DataManager dataManager, Class<? extends DataClass> dataType, boolean loginOrRegister) {
        String prefix = loginOrRegister ? "login" : "register";
        CommandNode commandNode = new CommandNodeFork(
                prefix
        );
        CommandNode bridgeCommandNode = new CommandNodeFork(
                "via"
        );
        CommandNode subCommandNode = new CommandNodeFork(
                clearSuffix(dataType.getSimpleName(),"Data")
        );

        bridgeCommandNode.then(subCommandNode);
        commandNode.then(bridgeCommandNode);

        ArrayList<Field> vars;
        vars=loginOrRegister?
                ReflectHelper.getCertainFields(
                        dataType,(field)->field.isAnnotationPresent(LoginRequired.class)
                )
                :
                ReflectHelper.getCertainFields(
                        dataType,(field)->field.isAnnotationPresent(RegisterRequired.class)
                )
        ;
        subCommandNode.makeAccountCommands_recursive(dataManager, dataType, vars, loginOrRegister);
        return commandNode;
    }

    public CommandNode makeAccountCommands_recursive(DataManager dataManager, Class<? extends DataClass> dataType, ArrayList<Field> vars, boolean loginOrRegister) {
        if (vars.size() <= 0) {
            this.end(
                    (context) -> {
                        DataClass entry = rowToEntry(dataType,context.getParameters());
                        if (loginOrRegister) {
                            if (!LoginStatus.loggedIn()) {
                                boolean hasEntry = dataManager.queryLogin(entry);
                                if (hasEntry) {
                                    LoginStatus.setUser((AccountData)entry);
                                    System.out.println("Login Success, Access Granted");
                                } else {
                                    System.out.println("Login Failed");
                                }
                            } else {
                                System.out.println("Already logged into an account!");
                            }
                        } else {
                            dataManager.addEntry(entry);
                            //User operations were less frequent, so we choose to dump cache directly to storage
                            saveDbChanges();
                            System.out.println("added object");
                        }
                    },
                    0
            );
        } else {
            Field registeredField=vars.get(0);
            CommandNode commandNode = new CommandNodeInput(
                    registeredField.getName(),
                    registeredField.getClass().toString(),
                    registeredField.isAnnotationPresent(HashElement.class)?
                            registeredField.getAnnotation(HashElement.class).hashType()
                            :
                            null
            );

            vars.remove(0);
            this.then(commandNode.makeAccountCommands_recursive(dataManager, dataType, vars, loginOrRegister));
        }
        return this;
    }

    public CommandNode then(CommandNode in) {
        if (in instanceof CommandNodeInput || in instanceof CommandNodeTags) {
            if (isParameterStarter) {
                try {
                    throw new Exception("Multiple Parameter Fork, unable to handle");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                nextParameter = in.name;
                isParameterStarter = true;
            }
        }
        if (children.containsKey(in.name)) {
            for (String key :
                    in.children.keySet()) {
                children.get(in.name).then(in.children.get(key));
            }
        } else {
            children.put(in.name, in);
        }
        hasFork = true;
        return this;
    }

    public CommandNode end(Consumer<Context> in) {
        return end(in,3);
    }

    public CommandNode end(Consumer<Context> in, int level) {
        executable = in;
        int operatorLevel = OperatorLevel.ADMIN;
        Supplier<Boolean> predicate = () -> {
            return LoginStatus.hasPermissionLevel(level);
        };
        hasPower = predicate;
        return this;
    }

    public CommandNode end(Consumer<Context> in, Supplier<Boolean> predicate) {
        executable = in;
        hasPower = predicate;
        return this;
    }

    public CommandNode progress(String in) {
        if (isParameterStarter) {
            return progress();
        } else {
            return children.containsKey(in) ? children.get(in) : null;
        }
    }

    public CommandNode progress() {
        return children.get(nextParameter);
    }

    public void run(Context params) {
        if (hasPower.get()) {
            executable.accept(params);
        } else {
            LOGGER.warn("no permission / not logged in");
        }
    }
}