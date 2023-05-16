package util;

import java.util.ArrayList;

public class StringHelper {
    public static ArrayList<String> breakDownString(String rawString){
        ArrayList<String>atoms=new ArrayList<>();
        while( rawString.length()>0 ){
            if(rawString.charAt(0)==' '){
                rawString=rawString.substring(1);
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
