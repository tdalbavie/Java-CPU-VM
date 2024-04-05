package cpu;

import java.util.LinkedList;
import java.util.Optional;
import static cpu.Token.TokenType.*;

public class Parser
{
    TokenHandler th;
    // A linked list containing all generated codes.
    LinkedList<String> codes = new LinkedList<>();
    // Counts the total number of registers in a current instruction, used to identify instruction format.
    int RegisterCounter;
    // Holds the different parts of an instruction code, these will be concatenated and added to the LinkedList.
    String Opcode, rd, function, rs1, rs2, immediate;

    // Initializes the cpu.TokenHandler by passing the token list from cpu.Lexer.
    public Parser(LinkedList<Token> tokens)
    {
        th = new TokenHandler(tokens);
        // Initialize the fields to empty strings.
        clearFields();
    }

    // Set public for testing purposes.
    public LinkedList<String> ParseOperation()
    {
        return ParseCode();
    }

    // This will concatenate the code from strings and add it to the LinkedList of codes.
    private LinkedList<String> ParseCode()
    {
        ParseRegistersAndImmediate();

        // Starts building the instruction code.
        StringBuilder code = new StringBuilder(immediate);
        if (RegisterCounter == 0)
        {
            code.append(Opcode).append("00");
        }
        else if (RegisterCounter == 1)
        {
            code.append(function);
            code.append(rd);
            code.append(Opcode).append("01");
        }
        else if (RegisterCounter == 2)
        {
            code.append(rs1);
            code.append(function);
            code.append(rd);
            code.append(Opcode).append("11");
        }
        else if (RegisterCounter == 3)
        {
            code.append(rs1);
            code.append(rs2);
            code.append(function);
            code.append(rd);
            code.append(Opcode).append("10");
        }

        // Adds the code to the list.
        codes.add(code.toString());

        // Resets fields for the next instruction.
        clearFields();
        // Clears the counter for the next code (if there is).
        RegisterCounter = 0;
        // Moves past all newline tokens.
        AcceptNewlines();
        // Checks if there are any more tokens after moving past newlines.
        if (th.HasMoreTokens())
        {
            // Recursively continues adding to the linked list.
            return ParseCode();
        }

        // Returns the final list of generated codes.
        return codes;
    }

    // Parses register(s) and immediate (if present).
    private void ParseRegistersAndImmediate()
    {
        ParseBOPFunction();
        // Register and number (treated as immediate)
        Optional<Token> registerOrNumber;
        // Handles up to 3 registers that are given (over 3 registers will throw).
        while ((registerOrNumber = th.MatchAndRemove(REGISTER)).isPresent())
        {
            // Takes some number string and changes it to binary ("5" turns into "101").
            String regNum = stringNumberToStringBinary(registerOrNumber.get().getValue());
            // Takes the binary string and pads it with leading 0s to fit 5 bit size of registers.
            regNum = padBinaryString(regNum, 5);

            // Checks how many Registers have been created to insert next register into respective register slot.
            if (RegisterCounter == 0)
                rd = regNum;
            else if (RegisterCounter == 1)
                rs1 = regNum;
            else if (RegisterCounter == 2)
                rs2 = regNum;
            // Throws if too many registers are given.
            else
                throw new IllegalArgumentException("Error: Too many registers given.");

            RegisterCounter++;
        }

        // Handles the immediate (if given).
        if ((registerOrNumber = th.MatchAndRemove(NUMBER)).isPresent())
        {
            // Gets the binary string form of some string number.
            String regNum = stringNumberToStringBinary(registerOrNumber.get().getValue());

            // Pads number with leading 0s based on which instruction format is in use.
            if (RegisterCounter == 0)
                immediate = padBinaryString(regNum, 27);
            else if (RegisterCounter == 1)
                immediate = padBinaryString(regNum, 18);
            else if (RegisterCounter == 2)
                immediate = padBinaryString(regNum, 13);
            else if (RegisterCounter == 3)
                immediate = padBinaryString(regNum, 8);
        }
        // If no immediate was given, defaults to 0.
        else
        {
            // Creates a string to fill with 0s based on the instruction format used.
            if (RegisterCounter == 0)
                immediate = padBinaryString("0", 27);
            else if (RegisterCounter == 1)
                immediate = padBinaryString("0", 18);
            else if (RegisterCounter == 2)
                immediate = padBinaryString("0", 13);
            else if (RegisterCounter == 3)
                immediate = padBinaryString("0", 8);
        }
    }

    // Parses the boolean functions (if present).
    private void ParseBOPFunction()
    {
        ParseMOPFunction();
        // equal, unequal, greater, less, greaterOrEqual, lessOrEqual
        if (th.MatchAndRemove(EQUALS).isPresent())
            function = "0000";

        if (th.MatchAndRemove(UNEQUAL).isPresent())
            function = "0001";

        if (th.MatchAndRemove(LESS).isPresent())
            function = "0010";

        if (th.MatchAndRemove(GREATEROREQUAL).isPresent())
            function = "0011";

        if (th.MatchAndRemove(GREATER).isPresent())
            function = "0100";

        if (th.MatchAndRemove(LESSOREQUAL).isPresent())
            function = "0101";

        // If after all functions no function is given, defaults to 0.
        if (function.isEmpty())
            function = "0000";

    }

    // Parses the math functions (if present).
    private void ParseMOPFunction()
    {
        ParseKeywords();
        // add, subtract, multiply, and, or, not, xor, left (for shift), right (for shift)
        if (th.MatchAndRemove(AND).isPresent())
            function = "1000";

        if (th.MatchAndRemove(OR).isPresent())
            function = "1001";

        if (th.MatchAndRemove(XOR).isPresent())
            function = "1010";

        if (th.MatchAndRemove(NOT).isPresent())
            function = "1011";

        if (th.MatchAndRemove(LEFT).isPresent())
            function = "1100";

        if (th.MatchAndRemove(RIGHT).isPresent())
            function = "1101";

        if (th.MatchAndRemove(ADD).isPresent())
            function = "1110";

        if (th.MatchAndRemove(SUBTRACT).isPresent())
            function = "1111";

        if (th.MatchAndRemove(MULTIPLY).isPresent())
            function = "0111";
    }

    // Parses the keyword of the instruction (i.e. math, branch, call, etc.).
    private void ParseKeywords()
    {
        // Checks the keywords to get the first 3 bits of the opcode.
        if (th.MatchAndRemove(MATH).isPresent())
            Opcode = "000";

        if (th.MatchAndRemove(SHIFT).isPresent())
            Opcode = "000";

        if (th.MatchAndRemove(COPY).isPresent())
            Opcode = "000";

        if (th.MatchAndRemove(HALT).isPresent())
            Opcode = "000";

        if (th.MatchAndRemove(BRANCH).isPresent())
            Opcode = "001";

        if (th.MatchAndRemove(JUMP).isPresent())
            Opcode = "001";

        if (th.MatchAndRemove(CALL).isPresent())
            Opcode = "010";

        if (th.MatchAndRemove(PUSH).isPresent())
            Opcode = "011";

        if (th.MatchAndRemove(LOAD).isPresent())
            Opcode = "100";

        if (th.MatchAndRemove(RETURN).isPresent())
            Opcode = "100";

        if (th.MatchAndRemove(STORE).isPresent())
            Opcode = "101";

        if (th.MatchAndRemove(PEEK).isPresent())
            Opcode = "110";

        if (th.MatchAndRemove(POP).isPresent())
            Opcode = "110";
    }

    // Takes all newlines in the list and moves past them.
    private boolean AcceptNewlines()
    {
        // Flags if a newline was found when MatchAndRemove was called.
        boolean hasNewline = false;

        // Only uses AcceptNewlines if there are more tokens
        if (th.HasMoreTokens())
        {
            // Saves the result from MatchAndRemove
            Optional<Token> optionalToken = th.MatchAndRemove(NEWLINE);

            // If optionalToken has a newline token it sets flag as true.
            if (optionalToken.isPresent())
                hasNewline = true;

            // Continues to remove newline until the next token is no longer a newline.
            while (optionalToken.isPresent())
            {
                optionalToken = th.MatchAndRemove(NEWLINE);
            }
        }

        // Returns the flag which should return true when at least one newline is removed.
        return hasNewline;
    }

    // Method that converts a String number to a String binary number.
    public static String stringNumberToStringBinary(String integer)
    {
        return Integer.toBinaryString(Integer.parseInt(integer));
    }

    // Method that adds leading 0s based on the total length given (i.e. "101", 5 = "00101").
    public static String padBinaryString(String binaryString, int totalLength)
    {
        // Calculate the number of zeros needed as padding
        int paddingLength = totalLength - binaryString.length();

        // Prepend the zeros to the binary string
        String paddedString = "0".repeat(Math.max(0, paddingLength)) + binaryString;

        return paddedString;
    }

    // method that clears the fields for the next instruction code.
    private void clearFields()
    {
        Opcode = "";
        rd = "";
        function = "";
        rs1 = "";
        rs2 = "";
        immediate = "";
    }
}
