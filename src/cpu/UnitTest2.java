package cpu;

import org.junit.*;
public class UnitTest2
{
    // Private cpu.ALU initialized for all unit tests to use for ease of use.
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

        // Does the following operation: 5 - 15 = -10
        alu.op1.set(5);
        alu.op2.set(15);
        // 1111 for SUBTRACT
        alu.doOperation(new Bit[]{new Bit(true), new Bit(true), new Bit(true), new Bit(true)});
        Assert.assertEquals(-10, alu.result.getSigned());
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
    public void add2Test()
    {
        Word a = new Word();
        Word b = new Word();

        // Sets the new words.
        a.set(1);
        b.set(2);

        // Passes in values to make calculation.
        Word result = alu.add2(a, b);
        // Checks result correctness.
        Assert.assertEquals(3, result.getSigned());
    }

    @Test
    public void add2WithCarryTest()
    {
        Word a = new Word();
        Word b = new Word();

        // Sets to max int value to cause an overflow.
        a.set(Integer.MAX_VALUE);
        // Set to 1 to cause an overflow.
        b.set(1);

        // Passes in values to make calculation.
        Word result = alu.add2(a, b);
        // Result should be -2^31 due to overflow.
        Assert.assertEquals(Integer.MIN_VALUE, result.getSigned());
    }

    @Test
    public void add4Test()
    {
        Word a = new Word();
        Word b = new Word();
        Word c = new Word();
        Word d = new Word();

        // Sets the new words.
        a.set(1);
        b.set(2);
        c.set(3);
        d.set(4);

        // Passes in values to make calculation.
        Word result = alu.add4(a, b, c, d);
        // Checks result correctness.
        Assert.assertEquals(10, result.getSigned());
    }

    @Test
    public void add4WithCarryTest()
    {
        Word a = new Word();
        Word b = new Word();
        Word c = new Word();
        Word d = new Word();

        // Sets the new words.
        a.set(Integer.MAX_VALUE);
        b.set(1);
        c.set(1);
        d.set(1);

        // Passes in values to make calculation.
        Word result = alu.add4(a, b, c, d);

        // Gets expected value by getting max int value and adding 1 to signify a carry.
        long expectedResult = (long) Integer.MAX_VALUE + 1;
        // Checks result correctness.
        Assert.assertEquals(expectedResult, result.getUnsigned());
    }
}
