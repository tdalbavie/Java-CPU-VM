package cpu;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;

public class Lexer
{
    private final StringHandler sh;
    private int lineNumber;
    private int charPosition;
    private HashMap<String, Token.TokenType> types;


    public Lexer(String fileContents)
    {
        sh = new StringHandler(fileContents);
        lineNumber = 1;
        charPosition = 1;
        HashMapPopulator();
    }

    public LinkedList<Token> Lex()
    {
        // Holds length of string to make sure there is still data in cpu.StringHandler
        int stringLength = sh.Remainder().length() - 1;
        LinkedList<Token> tokens = new LinkedList<Token>();

        // Makes sure that file is not already empty
        if (stringLength >= 0)
        {
            // Loops until no more characters exist.
            while (sh.IsDone() == false)
            {
                // Skips tab or space and moves to the next line
                if (sh.Peek(0) == ' ' || sh.Peek(0) == '\t')
                {
                    // Makes sure there is another character after whitespace otherwise exits loop
                    if(sh.Remainder().length() - 1 > 0)
                    {
                        sh.Swallow(1);
                        charPosition++;
                    }
                    else
                        break;
                }

                // Creates a separator token and moves to the next line.
                else if (sh.Peek(0) == '\n')
                {
                    tokens.add(new Token(Token.TokenType.NEWLINE, lineNumber, charPosition));
                    sh.Swallow(1);
                    lineNumber++;
                    charPosition = 1;

                }

                // Ignores carriage return like a whitespace.
                else if(sh.Peek(0) == '\r')
                {
                    // Checks if return carriage is the last character in the string.
                    if(sh.Remainder().length() - 1 > 0)
                    {
                        sh.Swallow(1);
                        charPosition++;
                    }
                    else
                        break;
                }

                // Identifies the start of a word then loops until the full word is found and creates a token.
                else if (Character.isAlphabetic(sh.Peek(0)))
                {
                    tokens.add(ProcessWord());
                }

                // Identifies the start of a number and loops until the full number is found.
                else if (Character.isDigit(sh.Peek(0)))
                {
                    tokens.add(ProcessNumber());
                }

                // Returns an error for unknown character and stops program.
                else
                {
                    throw new InputMismatchException("Character not valid: " + sh.Peek(0));
                }
            }
        }

        // Adds a newline token to the end of the token list.
        tokens.add(new Token(Token.TokenType.NEWLINE, lineNumber, charPosition));

        return tokens;
    }

    // Peeks at following characters until full word is found.
    private Token ProcessWord()
    {
        // Holds the word to be added to the token
        StringBuilder word = new StringBuilder();
        // Holds first character position of the number
        int charPositionCount = charPosition;

        // Loops until it finds a character that does not match awk word syntax.
        while(Character.isAlphabetic(sh.Peek(0)) || Character.isDigit(sh.Peek(0)))
        {
            word.append(sh.GetChar());
            charPosition++;
        }

        // Processes one of the keywords or registers and stores it as a token.
        if (types.containsKey(word.toString()))
            return new Token(types.get(word.toString()), lineNumber, charPositionCount);
        // If the word is a register, the value after R is saved for register index.
        else if (word.toString().matches("R([0-2]?\\d|3[01])"))
            return new Token(word.substring(1),Token.TokenType.REGISTER, lineNumber, charPositionCount);
        else
            throw new IllegalArgumentException("Error: Invalid syntax for: " + word);
    }

    // Peeks at following characters until full number is found.
    private Token ProcessNumber()
    {
        // Holds the number to be added to the token
        StringBuilder number = new StringBuilder();
        // Holds first character position of the number
        int charPositionCount = charPosition;

        // Loops until it finds a character that does not match number syntax.
        while(Character.isDigit(sh.Peek(0)))
        {
            number.append(sh.GetChar());
            charPosition++;
        }

        return new Token(number.toString(), Token.TokenType.NUMBER, lineNumber, charPositionCount);
    }

    // Called by constructor to populate the HashMap
    private void HashMapPopulator()
    {
        types = new HashMap<String, Token.TokenType>();
        types.put("math", Token.TokenType.MATH);
        types.put("shift", Token.TokenType.SHIFT);
        types.put("copy", Token.TokenType.COPY);
        types.put("halt", Token.TokenType.HALT);
        types.put("branch", Token.TokenType.BRANCH);
        types.put("jump", Token.TokenType.JUMP);
        types.put("call", Token.TokenType.CALL);
        types.put("push", Token.TokenType.PUSH);
        types.put("load", Token.TokenType.LOAD);
        types.put("return", Token.TokenType.RETURN);
        types.put("store", Token.TokenType.STORE);
        types.put("peek", Token.TokenType.PEEK);
        types.put("pop", Token.TokenType.POP);
        types.put("add", Token.TokenType.ADD);
        types.put("subtract", Token.TokenType.SUBTRACT);
        types.put("multiply", Token.TokenType.MULTIPLY);
        types.put("and", Token.TokenType.AND);
        types.put("or", Token.TokenType.OR);
        types.put("not", Token.TokenType.NOT);
        types.put("xor", Token.TokenType.XOR);
        types.put("left", Token.TokenType.LEFT);
        types.put("right", Token.TokenType.RIGHT);
        types.put("equal", Token.TokenType.EQUALS);
        types.put("notequal", Token.TokenType.UNEQUAL);
        types.put("greater", Token.TokenType.GREATER);
        types.put("less", Token.TokenType.LESS);
        types.put("greaterOrEqual", Token.TokenType.GREATEROREQUAL);
        types.put("lessOrEqual", Token.TokenType.LESSOREQUAL);

    }
}

