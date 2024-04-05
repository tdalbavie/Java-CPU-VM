package cpu;

import org.junit.*;

import java.util.LinkedList;

public class UnitTest6
{
    // Covers all important aspects of the lexer to ensure working order.
    @Test
    public void lexerTest()
    {
        String str = "math add R1 R2 R3 \n" +
                "shift left R1 R2 \n" +
                "copy R1 20 \n" +
                "halt \n" +
                "branch equal R20 R30 \n" +
                "store R15 R7 20 \n" +
                "load 100 R1 \n";
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();

        // Checks to see if all tokens are there, lexer adds a newline at the end for safety when reading from file.
        Assert.assertEquals(32, Tokens.size());
        // Checks second token to show MOP function is correctly tokenized.
        Assert.assertEquals(Token.TokenType.ADD, Tokens.get(1).getType());
        // Checks third token to show register token and value are correct.
        Assert.assertEquals(Token.TokenType.REGISTER, Tokens.get(2).getType());
        Assert.assertEquals("1", Tokens.get(2).getValue());
        // Checks 14th token to show number token and value are correct.
        Assert.assertEquals(Token.TokenType.NUMBER, Tokens.get(13).getType());
        Assert.assertEquals("20", Tokens.get(13).getValue());
        // Checks 19th token to show BOP function is correctly tokenized.
        Assert.assertEquals(Token.TokenType.EQUALS, Tokens.get(18).getType());

        // Prints all tokens to show correctness of string that was input.
        for (Token token : Tokens)
        {
            System.out.println(token.toStringValue());
        }
    }

    // This test covers one of each instruction from Math instruction type.
    @Test
    public void ParserMathTest()
    {
        // Tests 3R instruction.
        String str = "math subtract R1 R2 R3 \n" +
                // Tests 2R instruction.
                "math multiply R10 R15 \n" +
                // Tests copy (1R instruction).
                "copy R6 5 \n" +
                // Tests halt (0R instruction).
                "halt \n" +
                // Tests shift (3R instruction).
                "shift left R1 R2 R3 \n" +
                // Tests shift (2R instruction).
                "shift right R1 R2 \n";
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();

        // instruction for math subtract R1 R2 R3.
        Assert.assertEquals("00000000000100001111110000100010", codes.get(0));
        // instruction for math multiply R10 R15.
        Assert.assertEquals("00000000000000111101110101000011", codes.get(1));
        // instruction for copy R6 5.
        Assert.assertEquals("00000000000000010100000011000001", codes.get(2));
        // instruction for halt.
        Assert.assertEquals("00000000000000000000000000000000", codes.get(3));
        // instruction for shift left R1 R2 R3.
        Assert.assertEquals("00000000000100001111000000100010", codes.get(4));
        // instruction for shift right R1 R2.
        Assert.assertEquals("00000000000000001011010000100011", codes.get(5));
    }

    // This test covers one of each instruction from Branch instruction type.
    @Test
    public void ParserBranchTest()
    {
        // Tests 3R instruction.
        String str = "branch equal R1 R2 R3 6 \n" +
                // Tests 2R instruction.
                "branch notequal R20 R30 14 \n" +
                // Tests jump (Uses a throw away register to select 1R instruction).
                "jump R1 5 \n" +
                // Tests jump (0R instruction).
                "jump 5 \n";
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();

        // instruction for branch equal R1 R2 R3 6.
        Assert.assertEquals("00000110000100001100000000100110", codes.get(0));
        // instruction for branch notequal R20 R30 14.
        Assert.assertEquals("00000000011101111000011010000111", codes.get(1));
        // instruction for jump R1 5.
        Assert.assertEquals("00000000000000010100000000100101", codes.get(2));
        // instruction for jump 5.
        Assert.assertEquals("00000000000000000000000010100100", codes.get(3));
    }

    // This test covers one of each instruction from Call instruction type.
    @Test
    public void ParserCallTest()
    {
        // Tests 3R instruction.
        String str = "call less R1 R2 R3 6 \n" +
                // Tests 2R instruction.
                "call greaterOrEqual R20 R30 14 \n" +
                // Tests 1R instruction.
                "call R1 105 \n" +
                // Tests 0R instruction.
                "call 255 \n";
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();

        // instruction for call less R1 R2 R3 6.
        Assert.assertEquals("00000110000100001100100000101010", codes.get(0));
        // instruction for call greaterOrEqual R20 R30 14.
        Assert.assertEquals("00000000011101111000111010001011", codes.get(1));
        // instruction for jump R1 105.
        Assert.assertEquals("00000000000110100100000000101001", codes.get(2));
        // instruction for call 255.
        Assert.assertEquals("00000000000000000001111111101000", codes.get(3));
    }

    // This test covers one of each instruction from Push instruction type.
    @Test
    public void ParserPushTest()
    {
        // Tests 3R instruction.
        String str = "push not R1 R2 R3 \n" +
                // Tests 2R instruction.
                "push xor R4 R5 \n" +
                // Tests 1R instruction.
                "push or R8 23 \n";
        // No 0R instruction for Push.
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();

        // instruction for push not R1 R2 R3.
        Assert.assertEquals("00000000000100001110110000101110", codes.get(0));
        // instruction for push xor R4 R5.
        Assert.assertEquals("00000000000000010110100010001111", codes.get(1));
        // instruction for push or R8 23.
        Assert.assertEquals("00000000000001011110010100001101", codes.get(2));
    }

    // This test covers one of each instruction from Load instruction type.
    @Test
    public void ParserLoadTest()
    {
        // Tests 3R instruction (R1 is a throw away to get 3R).
        String str = "load R1 R2 R3 \n" +
                // Tests 2R instruction (R1 is a throw away to get 2R).
                "load R1 R10 20 \n" +
                // Tests 1R instruction.
                "load R5 15 \n" +
                // Tests return (0R instruction).
                "return \n";
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();

        // instruction for load R1 R2 R3.
        Assert.assertEquals("00000000000100001100000000110010", codes.get(0));
        // instruction for load R1 R10 20.
        Assert.assertEquals("00000000101000101000000000110011", codes.get(1));
        // instruction for load R5 15.
        Assert.assertEquals("00000000000000111100000010110001", codes.get(2));
        // instruction for return.
        Assert.assertEquals("00000000000000000000000000010000", codes.get(3));
    }

    // This test covers one of each instruction from Store instruction type.
    @Test
    public void ParserStoreTest()
    {
        // Tests 3R instruction.
        String str = "store R29 R30 R31 \n" +
                // Tests 2R instruction.
                "store R1 R2 14 \n" +
                // Tests 1R instruction.
                "store R1 5 \n";
        // No 0R instruction for Store.
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();

        // instruction for store R29 R30 R31.
        Assert.assertEquals("00000000111101111100001110110110", codes.get(0));
        // instruction for store R1 R2 14.
        Assert.assertEquals("00000000011100001000000000110111", codes.get(1));
        // instruction for store R1 5.
        Assert.assertEquals("00000000000000010100000000110101", codes.get(2));
    }

    // This test covers one of each instruction from Pop/Interrupt instruction type.
    @Test
    public void ParserPopTest()
    {
        // Tests peek (3R instruction).
        String str = "peek R1 R2 R3 \n" +
                // Tests peek (2R instruction).
                "peek R5 R10 4 \n" +
                // Tests 1R instruction.
                "pop R20 \n";
        // No 0R instruction for Pop/Interrupt (no interrupt).
        Lexer lex = new Lexer(str);
        LinkedList<Token> Tokens = lex.Lex();
        Parser pars = new Parser(Tokens);
        LinkedList<String> codes = pars.ParseOperation();
        System.out.println(codes);

        // instruction for peek R1 R2 R3.
        Assert.assertEquals("00000000000100001100000000111010", codes.get(0));
        // instruction for peek R5 R10 4.
        Assert.assertEquals("00000000001000101000000010111011", codes.get(1));
        // instruction for pop R20.
        Assert.assertEquals("00000000000000000000001010011001", codes.get(2));
    }
}
