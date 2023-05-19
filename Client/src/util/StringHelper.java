package util;

import com.sun.xml.internal.ws.wsdl.writer.UsingAddressing;
import commands.UserCommand;

import java.util.ArrayList;

public class StringHelper {
    public static ArrayList<String> breakDownString(String rawString){

        ArrayList<String>atoms=new ArrayList<>();
        while( rawString.length()>0 ){
            char now=rawString.charAt(0);
            if(now==' '){
                rawString=rawString.substring(1);
                continue;
            }else if(now=='"'){

                String piece="";

                rawString=rawString.substring(1);
                int keyFrame;
                while(true){
                    keyFrame=rawString.indexOf('"');
                    if(keyFrame>0&&rawString.charAt(keyFrame - 1) == '\\'){
                        piece+=rawString.substring(0,keyFrame-1);
                        piece+='\"';
                        rawString=rawString.substring(keyFrame+1);
                    }else{
                        piece+=rawString.substring(0,keyFrame);
                        rawString=rawString.substring(keyFrame+1);
                        break;
                    }
                }
                atoms.add(piece);
                continue;
            }else{
                int keyFrame=rawString.indexOf(' ')==-1?rawString.length():rawString.indexOf(' ');
                String piece=rawString.substring(0,keyFrame);

                atoms.add(piece);

                rawString=rawString.substring(keyFrame);
            }
        }
        return atoms;
    }
}
