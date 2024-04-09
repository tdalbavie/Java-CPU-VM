package cpu;

import java.util.LinkedList;

public class Main
{
    // Used to load the programs in string form for parser.
    private static String str;
    private static Lexer lex;
    private static Parser pars;
    private static LinkedList<Token> Tokens;
    private static LinkedList<String> codes;
    private static Processor processor;
    public static void main(String[] args)
    {
        // Loads values into memory for array programs.
        str = """
                copy R1 100 \n
                store R1 1 \n
                copy R1 101 \n
                store R1 2 \n
                copy R1 102 \n
                store R1 3 \n
                copy R1 103 \n
                store R1 4 \n
                copy R1 104 \n
                store R1 5 \n
                copy R1 105 \n
                store R1 6 \n
                copy R1 106 \n
                store R1 7 \n
                copy R1 107 \n
                store R1 8 \n
                copy R1 108 \n
                store R1 9 \n
                copy R1 109 \n
                store R1 10 \n
                copy R1 110 \n
                store R1 11 \n
                copy R1 111 \n
                store R1 12 \n
                copy R1 112 \n
                store R1 13 \n
                copy R1 113 \n
                store R1 14 \n
                copy R1 114 \n
                store R1 15 \n
                copy R1 115 \n
                store R1 16 \n
                copy R1 116 \n
                store R1 17 \n
                copy R1 117 \n
                store R1 18 \n
                copy R1 118 \n
                store R1 19 \n
                copy R1 119 \n
                store R1 20 \n
                halt \n
                """;
        lex = new Lexer(str);
        Tokens = lex.Lex();
        pars = new Parser(Tokens);
        codes = pars.ParseOperation();
        MainMemory.load(codes.toArray(new String[0]));
        processor = new Processor();
        processor.run();

        // Loads values into memory for LinkedList program.
        str = """
                copy R1 200 \n
                store R1 1 \n
                copy R1 201 \n
                store R1 202 \n
                copy R1 202 \n
                store R1 2 \n
                copy R1 203 \n
                store R1 204 \n
                copy R1 204 \n
                store R1 3 \n
                copy R1 205 \n
                store R1 206 \n
                copy R1 206 \n
                store R1 4 \n
                copy R1 207 \n
                store R1 208 \n
                copy R1 208 \n
                store R1 5 \n
                copy R1 209 \n
                store R1 210 \n
                copy R1 210 \n
                store R1 6 \n
                copy R1 211 \n
                store R1 212 \n
                copy R1 212 \n
                store R1 7 \n
                copy R1 213 \n
                store R1 214 \n
                copy R1 214 \n
                store R1 8 \n
                copy R1 215 \n
                store R1 216 \n
                copy R1 216 \n
                store R1 9 \n
                copy R1 217 \n
                store R1 218 \n
                copy R1 218 \n
                store R1 10 \n
                copy R1 219 \n
                store R1 220 \n
                copy R1 220 \n
                store R1 11 \n
                copy R1 221 \n
                store R1 222 \n
                copy R1 222 \n
                store R1 12 \n
                copy R1 223 \n
                store R1 224 \n
                copy R1 224 \n
                store R1 13 \n
                copy R1 225 \n
                store R1 226 \n
                copy R1 226 \n
                store R1 14 \n
                copy R1 227 \n
                store R1 228 \n
                copy R1 228 \n
                store R1 15 \n
                copy R1 229 \n
                store R1 230 \n
                copy R1 230 \n
                store R1 16 \n
                copy R1 231 \n
                store R1 232 \n
                copy R1 232 \n
                store R1 17 \n
                copy R1 233 \n
                store R1 234 \n
                copy R1 234 \n
                store R1 18 \n
                copy R1 235 \n
                store R1 236 \n
                copy R1 236 \n
                store R1 19 \n
                copy R1 237 \n
                store R1 238 \n
                copy R1 238 \n
                store R1 20 \n
                copy R1 239 \n
                store R1 0 \n
                halt \n
                """;
        lex = new Lexer(str);
        Tokens = lex.Lex();
        pars = new Parser(Tokens);
        codes = pars.ParseOperation();
        MainMemory.load(codes.toArray(new String[0]));
        processor = new Processor();
        processor.run();

        // This is the program to sum 20 integers in an array (array is preloaded into memory starting at 100).
        str = """
                copy R1 0 \n
                load R2 R0 100 \n
                math add R1 R2 \n
                load R2 R0 101 \n
                math add R1 R2 \n
                load R2 R0 102 \n
                math add R1 R2 \n
                load R2 R0 103 \n
                math add R1 R2 \n
                load R2 R0 104 \n
                math add R1 R2 \n
                load R2 R0 105 \n
                math add R1 R2 \n
                load R2 R0 106 \n
                math add R1 R2 \n
                load R2 R0 107 \n
                math add R1 R2 \n
                load R2 R0 108 \n
                math add R1 R2 \n
                load R2 R0 109 \n
                math add R1 R2 \n
                load R2 R0 110 \n
                math add R1 R2 \n
                load R2 R0 111 \n
                math add R1 R2 \n
                load R2 R0 112 \n
                math add R1 R2 \n
                load R2 R0 113 \n
                math add R1 R2 \n
                load R2 R0 114 \n
                math add R1 R2 \n
                load R2 R0 115 \n
                math add R1 R2 \n
                load R2 R0 116 \n
                math add R1 R2 \n
                load R2 R0 117 \n
                math add R1 R2 \n
                load R2 R0 118 \n
                math add R1 R2 \n
                load R2 R0 119 \n
                math add R1 R2 \n
                halt \n
                """;
        lex = new Lexer(str);
        Tokens = lex.Lex();
        pars = new Parser(Tokens);
        codes = pars.ParseOperation();
        MainMemory.load(codes.toArray(new String[0]));
        processor = new Processor();
        processor.run();
        // Shows the result which is stored at register 1 and the clock cycles to do so.
        System.out.println("Sum of array: " + processor.getRegisterValue(1).getUnsigned());
        System.out.println("Clock Cycles: " + processor.getClockCycle());

        // This is the program to sum 20 integers in a LinkedList (LinkedList values are preloaded into memory starting at 200).
        str = """
                copy R1 0 \n
                copy R3 200 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 201 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 203 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 205 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 207 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 209 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 211 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 213 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 215 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 217 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 219 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 221 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 223 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 225 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 227 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 229 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 231 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 233 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 235 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                load R3 R0 237 \n
                load R2 R0 R3 \n
                math add R1 R2 \n
                halt \n
                """;
        lex = new Lexer(str);
        Tokens = lex.Lex();
        pars = new Parser(Tokens);
        codes = pars.ParseOperation();
        MainMemory.load(codes.toArray(new String[0]));
        processor = new Processor();
        processor.run();
        // Shows the result which is stored at register 1 and the clock cycles to do so.
        System.out.println("Sum of LinkedList: " + processor.getRegisterValue(1).getUnsigned());
        System.out.println("Clock Cycles: " + processor.getClockCycle());

        // This is the program to sum 20 integers in an array in reverse (array is preloaded into memory starting at 100).
        str = """
                copy R1 0 \n
                load R2 R0 119 \n
                math add R1 R2 \n
                load R2 R0 118 \n
                math add R1 R2 \n
                load R2 R0 117 \n
                math add R1 R2 \n
                load R2 R0 116 \n
                math add R1 R2 \n
                load R2 R0 115 \n
                math add R1 R2 \n
                load R2 R0 114 \n
                math add R1 R2 \n
                load R2 R0 113 \n
                math add R1 R2 \n
                load R2 R0 112 \n
                math add R1 R2 \n
                load R2 R0 111 \n
                math add R1 R2 \n
                load R2 R0 110 \n
                math add R1 R2 \n
                load R2 R0 109 \n
                math add R1 R2 \n
                load R2 R0 108 \n
                math add R1 R2 \n
                load R2 R0 107 \n
                math add R1 R2 \n
                load R2 R0 106 \n
                math add R1 R2 \n
                load R2 R0 105 \n
                math add R1 R2 \n
                load R2 R0 104 \n
                math add R1 R2 \n
                load R2 R0 103 \n
                math add R1 R2 \n
                load R2 R0 102 \n
                math add R1 R2 \n
                load R2 R0 101 \n
                math add R1 R2 \n
                load R2 R0 100 \n
                math add R1 R2 \n
                halt \n
                """;
        lex = new Lexer(str);
        Tokens = lex.Lex();
        pars = new Parser(Tokens);
        codes = pars.ParseOperation();
        MainMemory.load(codes.toArray(new String[0]));
        processor = new Processor();
        processor.run();
        // Shows the result which is stored at register 1 and the clock cycles to do so.
        System.out.println("Sum of reversed array: " + processor.getRegisterValue(1).getUnsigned());
        System.out.println("Clock Cycles: " + processor.getClockCycle());
    }
}
