package cpu;

import java.util.InputMismatchException;

public class StringHandler
{
    // Contains the awk file as a String.
    private String fileContents;
    // Keeps track of where it is in the string.
    private int index;

    // Constructor initializes fileContents and index.
    public StringHandler(String fileContents)
    {
        this.fileContents = fileContents;
        index = 0;
    }

    // Looks “i” characters ahead and returns that character but does not move the index.
    public char Peek(int i)
    {
        if (index + i <= fileContents.length() - 1)
            return fileContents.charAt(index + i);
        else
            return '\0';
    }

    // Returns a string of the next “i” characters but does not move the index.
    public String PeekString(int i)
    {
        if (index + i <= fileContents.length())
            return fileContents.substring(index, index + i);
        else
            return "\0";
    }

    // Returns the next character and moves the index.
    public char GetChar()
    {
        char nextChar = fileContents.charAt(index);
        index++;
        return nextChar;
    }

    // Moves the index ahead “i” positions.
    public void Swallow(int i)
    {
        index += i;
    }

    // Returns true if we are at the end of the document.
    public boolean IsDone()
    {
        if (index == (fileContents.length()))
            return true;
        else
            return false;
    }

    // Returns the rest of the document as a string.
    public String Remainder()
    {
        return fileContents.substring(index);
    }
}
