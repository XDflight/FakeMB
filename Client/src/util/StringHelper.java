package util;

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
