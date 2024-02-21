public class MainMemory
{
    private static final Word[] memory = new Word[1024];

    // Initializes memory with new words.
    private static void initializeMemory()
    {
        for (int i = 0; i < memory.length; i++)
        {
            if (memory[i] == null)
            {
                memory[i] = new Word();
            }
        }
    }

    // Used to read words from memory without directly referencing the word from memory for security.
    public static Word read(Word address)
    {
        // Ensures the memory is initialized.
        initializeMemory();
        // Converts address to an integer.
        int addr = (int) address.getUnsigned();
        // Creates a deep copy of the word for read access.
        return new Word(memory[addr]);
    }

    // Writes a new word to memory.
    public static void write(Word address, Word value)
    {
        // Ensures the memory is initialized.
        initializeMemory();
        // Converts address to an integer.
        int addr = (int)address.getUnsigned();
        // Copies the value to the memory location (without replacing the Word object).
        memory[addr].copy(value);
    }

    // Loads new words to memory from a string array.
    public static void load(String[] data)
    {
        // Ensures the memory is initialized.
        initializeMemory();
        for (int i = 0; i < data.length && i < memory.length; i++)
        {
            for (int bit = 0; bit < 32 && bit < data[i].length(); bit++)
            {
                // Gets the bit value at the current bit location of the string.
                char bitValue = data[i].charAt(bit);
                // Changes the bit from the bit position it is processing.
                memory[i].setBit(bit, new Bit(bitValue == '1'));
            }
        }
    }
}
