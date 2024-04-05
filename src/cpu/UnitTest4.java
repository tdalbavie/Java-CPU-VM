package cpu;

import org.junit.*;

public class UnitTest4
{
    // This test already covers all formats, however I will run a couple more tests on different math operations.
    @Test
    public void AddTest()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                // Start with 1R.
                "00000000000000010100000000100001", // MATH DestOnly 5, R1.
                // Then on to 3R.
                "00000000000010000111100001000010", // MATH ADD R1 R1 R2.
                // Then on to 2R.
                "00000000000000001011100001000011", // MATH ADD R2 R2 R2.
                // Then on to 3R.
                "00000000000100000111100001100010", // MATH ADD R2 R1 R3.
                // Then on to 0R.
                "00000000000000000000000000000000" // HALT.
        });

        processor.run();

        // Gets the value for R3 in the processor.
        Word R3 = processor.getRegisterValue(3);

        long R3Value = R3.getUnsigned();

        // Checks the value for the operation.
        Assert.assertEquals(25, R3Value);
    }

    // For simplicity, a similar structure will be used for this test.
    @Test
    public void MultiplyTest()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                // Start with 1R.
                "00000000000000001000000000100001", // MATH DestOnly 2, R1
                // Then on to 3R.
                "00000000000010000101110001000010", // MATH MULTIPLY R1 R1 R2
                // Then on to 2R.
                "00000000000000001001110001000011", // MATH MULTIPLY R2 R2 R2
                // Then on to 3R.
                "00000000000100000101110001100010", // MATH MULTIPLY R2 R1 R3
                // Then on to 0R.
                "00000000000000000000000000000000" // HALT
        });

        processor.run();

        // Gets the value for R3 in the processor.
        Word R3 = processor.getRegisterValue(3);

        long R3Value = R3.getUnsigned();

        // Checks the value for the operation.
        Assert.assertEquals(32, R3Value);
    }

    // For simplicity, a similar structure will be used for this test.
    @Test
    public void SubtractTest()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                // Start with 1R.
                "00000000000000010100000000100001", // MATH DestOnly 5, R1.
                // Then on to 3R.
                "00000000000010000111100001000010", // MATH ADD R1 R1 R2.
                // Then on to 2R.
                "00000000000000001011100001000011", // MATH ADD R2 R2 R2.
                // Then on to 3R.
                "00000000000100000111110001100010", // MATH SUBTRACT R2 R1 R3
                // Then on to 0R.
                "00000000000000000000000000000000" // HALT
        });

        processor.run();

        // Gets the value for R3 in the processor.
        Word R3 = processor.getRegisterValue(3);

        long R3Value = R3.getUnsigned();

        // Checks the value for the operation.
        Assert.assertEquals(15, R3Value);
    }
}
