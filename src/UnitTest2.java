import org.junit.*;
public class UnitTest2
{
    // Private ALU initialized for all unit tests to use for ease of use.
    private ALU alu;

    @Before
    public void setUp()
    {
        alu = new ALU();
    }

    @Test
    public void additionTest()
    {
        // Does the following operation: 32 + 10 = 42
        alu.op1.set(32);
        alu.op2.set(10);
        // 1110 for ADD
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(false)});
        Assert.assertEquals(42, alu.result.getSigned());
    }

    @Test
    public void subtractionTest()
    {
        // Does the following operation: 15 - 5 = 10
        alu.op1.set(15);
        alu.op2.set(5);
        // 1111 for SUBTRACT
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        Assert.assertEquals(10, alu.result.getSigned());
    }

    @Test
    public void multiplicationTest()
    {
        // Does the following operation: 6 * 7 = 42
        alu.op1.set(6);
        alu.op2.set(7);
        // 0111 for MULTIPLY
        alu.doOperation(new Bit[]{new Bit(false), new Bit(true), new Bit(true), new Bit(true)});
        Assert.assertEquals(42, alu.result.getSigned());
    }

    @Test
    public void ANDtest()
    {
        // Does the following operation: 12 & 10 = 8
        alu.op1.set(12);
        alu.op2.set(10);
        // 1000 for AND
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(false)});
        Assert.assertEquals(8, alu.result.getSigned());
    }

    @Test
    public void ORtest()
    {
        // Does the following operation: 12 | 10 = 14
        alu.op1.set(12);
        alu.op2.set(10);
        // 1001 for OR
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(false), new Bit(true)});
        Assert.assertEquals(14, alu.result.getSigned());
    }

    @Test
    public void XORtest()
    {
        // Does the following operation: 12 ^ 10 = 6
        alu.op1.set(12);
        alu.op2.set(10);
        // 1010 for XOR
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(false)});
        Assert.assertEquals(6, alu.result.getSigned());
    }

    @Test
    public void NOTtest()
    {
        alu.op1.set(10);
        // 1011 for NOT
        alu.doOperation(new Bit[]{new Bit(true), new Bit(false), new Bit(true), new Bit(true)});
        Assert.assertEquals(-11, alu.result.getSigned());
    }

    @Test
    public void leftShiftTest()
    {
        // Does the following operation: 1 << 2 = 4
        alu.op1.set(1);
        alu.op2.set(2);
        // 1100 for LEFT SHIFT
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(false)});
        Assert.assertEquals(4, alu.result.getSigned());
    }

    @Test
    public void rightShiftTest()
    {
        // Does the following operation: 4 >> 2 = 1
        alu.op1.set(4);
        alu.op2.set(2);
        // 1101 for RIGHT SHIFT
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(false), new Bit(true)});
        Assert.assertEquals(1, alu.result.getSigned());
    }

    @Test
    public void add2NoCarryTest()
    {
        // Adds 0, 1.
        Bit a = new Bit(false);
        Bit b = new Bit(true);
        Bit carryIn = new Bit(false);
        Bit[] result = alu.add2(a, b, carryIn);
        // Expect sum = 1.
        Assert.assertTrue(result[0].getValue());
        // Expect carry = 0.
        Assert.assertFalse(result[1].getValue());
    }

    @Test
    public void add2WithCarryTest()
    {
        // Adds 1, 1.
        Bit a = new Bit(true);
        Bit b = new Bit(true);
        Bit carryIn = new Bit(false);
        Bit[] result = alu.add2(a, b, carryIn);
        // Expect sum = 0.
        Assert.assertFalse(result[0].getValue());
        // Expect carry = 1.
        Assert.assertTrue(result[1].getValue());
    }

    @Test
    public void add4NoFinalCarryTest()
    {
        // Adds 1, 0, 1, 0.
        Bit a = new Bit(true);
        Bit b = new Bit(false);
        Bit c = new Bit(true);
        Bit d = new Bit(false);
        Bit carryIn = new Bit(false);
        Bit[] result = alu.add4(a, b, c, d, carryIn);
        // Expect sum = 0.
        Assert.assertFalse(result[0].getValue());
        // Expect carry = 1.
        Assert.assertTrue(result[1].getValue());
    }

    @Test
    public void add4WithFinalCarryTest()
    {
        // Adds 1, 1, 1, 1.
        Bit a = new Bit(true);
        Bit b = new Bit(true);
        Bit c = new Bit(true);
        Bit d = new Bit(true);
        Bit carryIn = new Bit(false);
        Bit[] result = alu.add4(a, b, c, d, carryIn);
        // Expect sum = 0.
        Assert.assertFalse(result[0].getValue());
        // Expect carry = 1.
        Assert.assertTrue(result[1].getValue());
    }
}
