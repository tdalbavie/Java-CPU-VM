import org.junit.*;

public class UnitTest4
{
    @Test
    public void testProgram() {
        Processor processor = new Processor();
        MainMemory.load(new String[]{
                "00000000000000010100000000100001", // MATH DestOnly 5, R1
                "0000000000010001000100001000011", // MATH ADD R1 R1 R2
                "0000000000100010001000100000011", // MATH ADD R2 R2 R2
                "0000000000100001000110001000011", // MATH ADD R2 R1 R3
                "00000000000000000000000000000000" // HALT
        });

        processor.run();

        // Gets the value for R3 in the processor.
        Word R3 = processor.getRegisterValue(3);

        long R3Value = R3.getUnsigned();

        // Checks the value for the operation.
        Assert.assertEquals(25, R3Value);
    }
}
