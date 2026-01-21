
package bf;

import java.util.ArrayList;
import java.util.List;

public final  class Lexer{

    private static final String VALID = "><+-,.[]";

    private Lexer(){}// class only has static methods, so overwrite 

    public static List<Character>lex(String src){
        List<Character>tokens = new ArrayList<>();
        for(int i = 0; i < src.length(); i ++ ){
            char c= src.charAt(i);
            if(VALID.indexOf(c)!=-1){
                tokens.add(c);
            } // otherwise we skip
        }

        return tokens;
    }
}