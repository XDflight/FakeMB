package commandNodes;

public class CommandNodeInput extends CommandNode {

    public String type;
    public String hashType;
    public CommandNodeInput(String nameIn, String typeIn) {
        name = nameIn;
        type = typeIn;
        hashType = null;
    }
    public CommandNodeInput(String nameIn, String typeIn, String hashType){
        this.name = nameIn;
        this.type = typeIn;
        this.hashType = hashType;
    }
}
