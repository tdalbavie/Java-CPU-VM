import org.junit.*;

public class UnitTest5
{
    // Pre-generated comparison codes for BOP.
    static Bit[] eqCode;
    static Bit[] neqCode;
    static Bit[] ltCode;
    static Bit[] geCode;
    static Bit[] gtCode;
    static Bit[] leCode;

    @BeforeClass
    public static void setup()
    {
        eqCode = new Bit[4]; // 0000
        eqCode[0] = new Bit(false);
        eqCode[1] = new Bit(false);
        eqCode[2] = new Bit(false);
        eqCode[3] = new Bit(false);

        neqCode = new Bit[4]; // 0001
        neqCode[0] = new Bit(false);
        neqCode[1] = new Bit(false);
        neqCode[2] = new Bit(false);
        neqCode[3] = new Bit(true);

        ltCode = new Bit[4]; // 0010
        ltCode[0] = new Bit(false);
        ltCode[1] = new Bit(false);
        ltCode[2] = new Bit(true);
        ltCode[3] = new Bit(false);

        geCode = new Bit[4]; // 0011
        geCode[0] = new Bit(false);
        geCode[1] = new Bit(false);
        geCode[2] = new Bit(true);
        geCode[3] = new Bit(true);

        gtCode = new Bit[4]; // 0100
        gtCode[0] = new Bit(false);
        gtCode[1] = new Bit(true);
        gtCode[2] = new Bit(false);
        gtCode[3] = new Bit(false);

        leCode = new Bit[4]; // 0101
        leCode[0] = new Bit(false);
        leCode[1] = new Bit(true);
        leCode[2] = new Bit(false);
        leCode[3] = new Bit(true);
    }

    // Test case to ensure BOP is working accurately.
    @Test
    public void testComparisons()
    {
        Word word1 = new Word();
        Word word2 = new Word();

        word1.set(6);
        word2.set(7);

        Processor comparison = new Processor();


        Assert.assertTrue(comparison.BOP(word1, word1, eqCode)); // 6 == 6
        Assert.assertFalse(comparison.BOP(word1, word2, eqCode)); // 6 == 7

        Assert.assertFalse(comparison.BOP(word1, word1, neqCode)); // 6 != 6
        Assert.assertTrue(comparison.BOP(word1, word2, neqCode)); // 6 != 7

        Assert.assertFalse(comparison.BOP(word1, word1, ltCode)); // 6 < 6
        Assert.assertTrue(comparison.BOP(word1, word2, ltCode)); // 6 < 7
        Assert.assertFalse(comparison.BOP(word2, word1, ltCode)); // 7 < 6

        Assert.assertTrue(comparison.BOP(word1, word1, geCode)); // 6 >= 6
        Assert.assertFalse(comparison.BOP(word1, word2, geCode)); // 6 >= 7
        Assert.assertTrue(comparison.BOP(word2, word1, geCode)); // 7 >= 6

        Assert.assertFalse(comparison.BOP(word1, word1, gtCode)); // 6 > 6
        Assert.assertFalse(comparison.BOP(word1, word2, gtCode)); // 6 > 7
        Assert.assertTrue(comparison.BOP(word2, word1, gtCode)); // 7 > 6

        Assert.assertTrue(comparison.BOP(word1, word1, leCode)); // 6 <= 6
        Assert.assertTrue(comparison.BOP(word1, word2, leCode)); // 6 <= 7
        Assert.assertFalse(comparison.BOP(word2, word1, leCode)); // 7 <= 6
    }

    @Test
    public void testBranch0R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000000000000010100100", // BRANCH 0R 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 6 as it gets incremented once in fetch when HALT is called.
        Assert.assertEquals(6, processor.getPC().getUnsigned());
    }

    @Test
    public void testBranch1R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000010100000000000101", // BRANCH 1R 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 7 since PC = 2 (total instructions) + 5 (immediate value).
        Assert.assertEquals(7, processor.getPC().getUnsigned());
    }

    @Test
    public void testBranch2R()
    {
        // Tests true case (PC gets set).
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000000001010000100100001000111", // BRANCH 2R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 9 since PC = 4 (total instructions) + 5 (immediate value).
        Assert.assertEquals(9, processor.getPC().getUnsigned());

        // Tests false case (PC does not get set).
        processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000000001010001000100000100111", // BRANCH 2R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 4 (total instructions) as it never gets set by branch.
        Assert.assertEquals(4, processor.getPC().getUnsigned());
    }

    @Test
    public void testBranch3R()
    {
        // Tests true case (PC gets set).
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000101000010001000100000000110", // BRANCH 3R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 9 since PC = 4 (total instructions) + 5 (immediate value).
        Assert.assertEquals(9, processor.getPC().getUnsigned());

        // Tests false case (PC does not get set).
        processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6, R1
                "00000000000000011100000001000001", // MATH DestOnly 7, R2
                "00000101000100000100100000000110", // BRANCH 3R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 4 (total instructions) as it never gets set by branch.
        Assert.assertEquals(4, processor.getPC().getUnsigned());
    }

    // Same as Branch, just checks if PC was stored in MainMemory.
    @Test
    public void testCall0R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000000000000010101000", // CALL 0R 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 6 as it gets incremented once in fetch when HALT is called.
        Assert.assertEquals(6, processor.getPC().getUnsigned());
        // Value of old PC is 1, PC gets incremented before it gets stored.
        Assert.assertEquals(1, MainMemory.read(processor.getSP()).getUnsigned());
    }

    // Same as Branch, just checks if PC was stored in MainMemory.
    @Test
    public void testCall1R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000010100000000001001", // CALL 1R 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 7 since PC = 2 (total instructions) + 5 (immediate value).
        Assert.assertEquals(7, processor.getPC().getUnsigned());
        // Value of old PC is 1, PC gets incremented before it gets stored.
        Assert.assertEquals(1, MainMemory.read(processor.getSP()).getUnsigned());
    }

    // Same as Branch, just checks if PC was stored in MainMemory.
    @Test
    public void testCall2R()
    {
        // Tests true case (PC gets set).
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000000001010000100100001001011", // CALL 2R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 9 since PC = 4 (total instructions) + 5 (immediate value).
        Assert.assertEquals(9, processor.getPC().getUnsigned());
        // Value of old PC is 3, PC gets incremented before it gets stored.
        Assert.assertEquals(3, MainMemory.read(processor.getSP()).getUnsigned());

        // Tests false case (No change to PC so no need to check MainMemory as PC never gets stored).
        processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000000001010001000100000101011", // CALL 2R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 4 (total instructions) as it never gets set by branch.
        Assert.assertEquals(4, processor.getPC().getUnsigned());
    }

    // Same as Branch, just checks if PC was stored in MainMemory.
    @Test
    public void testCall3R()
    {
        // Tests true case (PC gets set).
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000101000010001000100000001010", // CALL 3R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 9 since PC = 4 (total instructions) + 5 (immediate value).
        Assert.assertEquals(9, processor.getPC().getUnsigned());
        // Value of old PC is 3, PC gets incremented before it gets stored.
        Assert.assertEquals(3, MainMemory.read(processor.getSP()).getUnsigned());

        // Tests false case (No change to PC so no need to check MainMemory as PC never gets stored).
        processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000101000100000100100000001010", // CALL 3R R1 LE R2 5
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // PC will be 4 (total instructions) as it never gets set by branch.
        Assert.assertEquals(4, processor.getPC().getUnsigned());
    }

    // Push 0R is unused, nothing to test.
    @Test
    public void testPush1R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000100000000000100001", // MATH DestOnly 8 R1
                "00000000000000001111100000101101", // PUSH 1R R1 ADD 3
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // Adds 8 and 3 then stores it in MainMemory.
        Assert.assertEquals(11, MainMemory.read(processor.getSP()).getUnsigned());
    }

    @Test
    public void testPush2R()
    {
        // Tests true case (PC gets set).
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000011000000000100001", // MATH DestOnly 6 R1
                "00000000000000011100000001000001", // MATH DestOnly 7 R2
                "00000000001010001001110000101111", // PUSH 2R R1 MULTIPLY R2
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // Multiplies 6 and 7 then stores it in MainMemory.
        Assert.assertEquals(42, MainMemory.read(processor.getSP()).getUnsigned());
    }

    @Test
    public void testPush3R()
    {
        // Tests true case (PC gets set).
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000010100000000100001", // MATH DestOnly 5 R1
                "00000000000000101000000001000001", // MATH DestOnly 10 R2
                "00000101000010001011110000001110", // PUSH 3R R1 SUBTRACT R2
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // Subtracts 10 from 5 then stores it in MainMemory.
        Assert.assertEquals(-5, MainMemory.read(processor.getSP()).getSigned());
    }

    @Test
    public void testReturn()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000111111111100000000100001", // MATH DestOnly 1023 R1
                "00000000000000001100000000110101", // STORE 1R 3
                "00000000000000000000000000010000", // RETURN
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        // 3 gets stored at SP index, PC will change to 3 from return, halt will increment it once more.
        Assert.assertEquals(4, processor.getPC().getUnsigned());
    }

    // TODO: Add Load to StoreAndLoad tests
    @Test
    public void testStoreAndLoad1R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000100100000000100001", // MATH DestOnly 9 R1
                "00000000000000011100000000110101", // STORE 1R 7
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        Word address = new Word();
        address.set(9);

        // Subtracts 10 from 5 then stores it in MainMemory.
        Assert.assertEquals(7, MainMemory.read(address).getSigned());
    }

    @Test
    public void testStoreAndLoad2R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000010100000000100001", // MATH DestOnly 5 R1
                "00000000000000001100000001000001", // MATH DestOnly 3 R2
                "00000000001100001000000000110111", // STORE 2R R1 ADD 6 R2
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        Word address = new Word();
        address.set(11);

        // Stores R2 into MainMemory at index R1 + 6.
        Assert.assertEquals(3, MainMemory.read(address).getSigned());
    }

    @Test
    public void testStoreAndLoad3R()
    {
        Processor processor = new Processor();
        MainMemory.load(new String[] {
                "00000000000000010000000000100001", // MATH DestOnly 4 R1
                "00000000000000001100000001000001", // MATH DestOnly 3 R2
                "00000000000000001000000001100001", // MATH DestOnly 2 R3
                "00000000000100001100000000110110", // STORE 3R R1 ADD R2 R3
                "00000000000000000000000000000000" // HALT
        });
        processor.run();

        Word address = new Word();
        address.set(7);

        // Stores R2 into MainMemory at index R1 + 6.
        Assert.assertEquals(2, MainMemory.read(address).getSigned());
    }

    // TODO: Test Peek and Pop
    @Test
    public void testPeekAndPop()
    {

    }
}
