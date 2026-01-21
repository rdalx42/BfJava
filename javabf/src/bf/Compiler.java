
package bf;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class Compiler{

  
    public static  enum Opcode{
        MOV,
        ADD, // no need for substract.
        OUTPUT,
        INPUT,
        JMP_FWD, // cell == 0 jmp
        JMP_BCK, // cell!=0  jmp
    };

    private static final int MAX_MEM = 3000;

    private Compiler(){}

    public static final class Instruction {
        public final Opcode op;
        public int operand;

        public Instruction(Opcode op, int operand){
            this.op = op;
            this.operand = operand;
        }

        @Override 
        public String toString(){
            return operand != 0 ? op + " " + operand : op.toString();
        }
    }   

    public static List<Instruction>compile(List<Character>tokens){
        List<Instruction> code = new ArrayList<>();
        Stack<Integer> loopStack = new Stack<>();

        for(int i = 0; i < tokens.size();){
            final char t = tokens.get(i);
            if(t=='+'||t=='-'){
                int count=0;
                while(i<tokens.size()){
                    final char c = tokens.get(i);
                    if(c=='+'){
                        count++;
                    }else if(c=='-'){
                        count--;
                    }else{
                        break;
                    }
                    i++;
                }
                if(count!=0){
                    code.add(new Instruction(Opcode.ADD,count));
                }
                continue;
            }
            if(t=='>'||t=='<'){
                int count=0;
                while(i<tokens.size()){
                    final char c = tokens.get(i);
                    if(c=='>'){
                        count++;
                    }else if(c=='<'){
                        count--;
                    }else{
                        break;
                    }
                    i++;
                }
                if(count!=0){
                    code.add(new Instruction(Opcode.MOV,count));
                }
                continue;
            }
            switch(t){
                case '.' -> code.add(new Instruction(Opcode.OUTPUT,0));
                case ',' -> code.add(new Instruction(Opcode.INPUT,0));
                case '[' -> {
                    code.add(new Instruction(Opcode.JMP_FWD,0)); // placeholder
                    loopStack.push(code.size()-1);
                }
                case ']' -> {
                    if(loopStack.isEmpty()){
                        throw new RuntimeException("Unmatched closing bracket at position " + i);
                    }
                    int jmpFwdIndex = loopStack.pop();
                    code.add(new Instruction(Opcode.JMP_BCK,jmpFwdIndex));
                   
                    code.get(jmpFwdIndex).operand = code.size() - 1;
                }
            }
            i++;
        }
        if(!loopStack.empty()){
            throw new IllegalStateException("Unmatched '['");
        }
        return code;
    }  

    public static void execute(List<Instruction>code){
        byte[] memory = new byte[MAX_MEM];
        int ptr = 0;
        for(int pc = 0; pc < code.size(); pc++){
            Instruction inst = code.get(pc);
            switch(inst.op){
                case MOV -> ptr += inst.operand;
                case ADD -> memory[ptr] += inst.operand;
                case OUTPUT -> System.out.print((int) memory[ptr] + " ");
                case INPUT -> {
                    try{
                        int input = System.in.read();
                        memory[ptr] = (byte)input;
                    }catch(Exception e){
                        throw new RuntimeException(e);
                    }
                }
                case JMP_FWD -> {
                    if(memory[ptr]==0){
                        pc = inst.operand;
                    }
                }
                case JMP_BCK -> {
                    if(memory[ptr]!=0){
                        pc = inst.operand;
                    }
                }
            }
        }
    }
}