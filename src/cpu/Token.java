package cpu;

public class Token
{
    public enum TokenType {NUMBER, REGISTER, MATH, SHIFT, COPY,
        HALT, BRANCH, JUMP, CALL, PUSH, LOAD, RETURN, STORE, PEEK,
        POP, ADD, SUBTRACT, MULTIPLY, AND, OR, NOT, XOR, LEFT, RIGHT,
        EQUALS, UNEQUAL, GREATER, LESS, GREATEROREQUAL, LESSOREQUAL,
        NEWLINE}
    private final TokenType type;
    private String value;
    private final int lineNumber;
    private final int charPosition;

    // Takes in token type and position of token.
    public Token(TokenType type, int lineNumber, int charPosition)
    {
        this.lineNumber = lineNumber;
        this.charPosition = charPosition;
        this.type = type;
    }

    // Takes in first constructor but sets value for cases like abstract words or numbers.
    public Token(String value, TokenType type, int lineNumber, int charPosition)
    {
        this(type, lineNumber, charPosition);
        this.value = value;
    }

    public TokenType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public int getCharPosition()
    {
        return charPosition;
    }

    public String toStringValue()
    {
        if (value != null)
            return new String(type + "(" + value + ")");
        else
            return type.toString();
    }

    public String toStringPosition()
    {
        return new String("Line Number: " + lineNumber + " Character Position: " + charPosition);
    }
}
