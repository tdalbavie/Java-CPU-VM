import org.junit.*;

public class UnitTest3
{
    @BeforeClass
    public static void setup()
    {
        String[] data = {
            "00000000000000000000000000000110", // 6
            "00000000000000000000000000000111"  // 7
            };
        // Loads the two string numbers into memory.
        MainMemory.load(data);
    }

    @Test
    public void testRead()
    {
        // Creating a Word object for the address and setting its value
        Word address = new Word();
        // Sets address to the first word that was loaded in.
        address.set(0);

        // Reads the first word that was loaded into memory.
        Word readWord = MainMemory.read(address);
        long readValue = readWord.getUnsigned();

        Assert.assertEquals(6, readValue);

        // Sets address to the second word that was loaded in.
        address.set(1);

        // Reads the second word that was loaded into memory.
        readWord = MainMemory.read(address);
        readValue = readWord.getUnsigned();

        Assert.assertEquals(7, readValue);
    }

    @Test
    public void testWrite()
    {
        // Creating Word objects for address and value
        Word address = new Word();
        Word value = new Word();
        // Writes to index 2 in memory.
        address.set(2);
        // Sets the value of the word to insert into memory.
        value.set(42);

        // Writes word into memory.
        MainMemory.write(address, value);

        // Reads back word to check the value.
        Word readWord = MainMemory.read(address);
        long readValue = readWord.getUnsigned();

        Assert.assertEquals(42, readValue);
    }

    @Test
    public void testIncrement()
    {
        // Create new word to increment.
        Word word = new Word();
        // Initializes word to 0.
        word.set(0);
        // Increments the word to 1.
        word.increment();

        Assert.assertEquals(1, word.getUnsigned());

        // Sets word to bigger number that will cause a carry of bits.
        word.set(255);
        // Increments the word to 256.
        word.increment();

        Assert.assertEquals(256, word.getUnsigned());
    }
}
