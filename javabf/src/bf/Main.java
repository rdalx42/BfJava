
package  bf;
import java.util.List;

public class Main{
    public static void main(String[] args){
        final String program = ">++++++++[<+++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>++++++[<+++++++>-]<++.------------.>++++++[<+++++++++>-]<+.<.+++.------.--------.>>>++++[<++++++++>-]<+.";
        List<Character> tokens = Lexer.lex(program);
        List<Compiler.Instruction>code = Compiler.compile(tokens);
        System.out.println("Tokens: " + tokens);
        System.out.println("Instructions: " + code);
        
        final long start_time = System.currentTimeMillis();
        Compiler.execute(code);
        final long end_time = System.currentTimeMillis();
        System.out.println("\ran in (ms): " + (end_time - start_time));
    }
}