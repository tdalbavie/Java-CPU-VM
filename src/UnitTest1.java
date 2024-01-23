import org.junit.*;
public class UnitTest1
{
    // Tests the set, clear, toggle, and getValue methods in the Bit class
    @Test
    public void bitAccessorMutatorTest()
    {
        // Creates a new bit with default false value.
        Bit testBit = new Bit();
        Assert.assertFalse(testBit.getValue());

        // Sets the bit to be true.
        testBit.set();
        Assert.assertTrue(testBit.getValue());

        // Clears bit data and makes it false.
        testBit.clear();
        Assert.assertFalse(testBit.getValue());

        // Toggles bit to be true again.
        testBit.toggle();
        Assert.assertTrue(testBit.getValue());

        // Toggles bit again to be false.
        testBit.toggle();
        Assert.assertFalse(testBit.getValue());
    }

    // Tests getBit and setBit methods in Word class.
    @Test
    public void wordAccessorMutatorTest()
    {
        // Assume tests work in all indices of the array.
        Word testWord = new Word();
        Assert.assertFalse(testWord.getBit(5).getValue());

        // Sets bit to new value then uses getBit to assert.
        testWord.setBit(5, new Bit(true));
        Assert.assertTrue(testWord.getBit(5).getValue());

        // Sets bit to new value then uses getBit to assert.
        testWord.setBit(5, new Bit(false));
        Assert.assertFalse(testWord.getBit(5).getValue());
    }

    // Tests toString and set in Word (Bit toString is tested through Word toString).
    @Test
    public void ToStringTest()
    {
        Word testWord = new Word();

        testWord.set(85);
        Assert.assertEquals(testWord.toString(),
                "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,t,f,t");

        testWord.set(1234);
        Assert.assertEquals(testWord.toString(),
                "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,t,f,t,f,f,t,f");

        testWord.set(-1234);
        Assert.assertEquals(testWord.toString(),
                "t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,f,t,t,f,f,t,f,t,t,t,f");
    }

    // Tests and operation in Word (Bit operation is being tested through Word)
    @Test
    public void andTest()
    {
        Word testWord = new Word();
        Word otherTestWord = new Word();

        testWord.set(5);
        otherTestWord.set(6);

        Word andTestWord = testWord.and(otherTestWord);

        // First two on right side will be set to false as 5 = 101 and 6 = 110
        Assert.assertEquals(andTestWord.toString(),
                "t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,f,f");
    }

    // Tests or operation in Word (Bit operation is being tested through Word)
    @Test
    public void orTest()
    {
        Word testWord = new Word();
        Word otherTestWord = new Word();

        testWord.set(5);
        otherTestWord.set(6);

        Word andTestWord = testWord.or(otherTestWord);

        // First three on right side will be set to true as 5 = 101 and 6 = 110
        Assert.assertEquals(andTestWord.toString(),
                "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,t");
    }

    // Tests xor operation in Word (Bit operation is being tested through Word)
    @Test
    public void xorTest()
    {
        Word testWord = new Word();
        Word otherTestWord = new Word();

        testWord.set(5);
        otherTestWord.set(6);

        Word andTestWord = testWord.xor(otherTestWord);

        // First two on right side will be set to true as 5 = 101 and 6 = 110
        Assert.assertEquals(andTestWord.toString(),
                "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t");
    }

    // Tests not operation in Word (Bit operation is being tested through Word)
    @Test
    public void notTest()
    {
        Word testWord = new Word();
        testWord.set(1234);

        // Returns an inverted testWord.
        Word notTestWord = testWord.not();
        Assert.assertEquals(notTestWord.toString(),
                "t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,f,t,t,f,f,t,f,t,t,f,t");
    }

    // Tests left and right shift operations.
    @Test
    public void shiftTest()
    {
        Word testWord = new Word();
        testWord.set(1234);

        // Checks value of left bitshift by 2.
        Word leftShiftTestWord = testWord.leftShift(2);
        Assert.assertEquals(leftShiftTestWord.toString(),
                "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,t,f,t,f,f,t,f,f,f");

        // Checks value of right bitshift by 2.
        Word rightShiftTestWord = testWord.rightShift(2);
        Assert.assertEquals(rightShiftTestWord.toString(),
                "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,t,f,t,f,f");

    }

    // Tests to ensures the return of a signed int
    @Test
    public void getSignedUnsignedTest()
    {
        Word testWord = new Word();

        // Checks unsigned and signed value of positive number.
        testWord.set(1234);
        Assert.assertEquals(testWord.getSigned(), 1234);
        Assert.assertEquals(testWord.getUnsigned(), 1234L);

        // Checks unsigned and signed value of positive number.
        testWord.set(-1234);
        Assert.assertEquals(testWord.getSigned(), -1234);
        Assert.assertEquals(testWord.getUnsigned(), 4294966062L);
    }
}
