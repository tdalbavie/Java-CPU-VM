public class ALU
{
    Word op1;
    Word op2;
    Word result;

    public ALU()
    {
        this.op1 = new Word();
        this.op2 = new Word();
        this.result = new Word();
    }

    // Determines and performs the operation based on a 4-bit operation code.
    public void doOperation(Bit[] operation)
    {
        // Used to calculate the total shift.
        int shiftAmount;

        // Makes sure exactly 4 bits are provided.
        if (operation.length != 4)
        {
            throw new IllegalArgumentException("Operation code must be 4 bits.");
        }

        // Gets each bit for ease of use.
        boolean bit0 = operation[0].getValue();
        boolean bit1 = operation[1].getValue();
        boolean bit2 = operation[2].getValue();
        boolean bit3 = operation[3].getValue();

        // Decision tree to determine the operation.
        // AND: 1000
        if (bit0 && !bit1 && !bit2 && !bit3)
        {
            result = op1.and(op2);
        }
        // OR: 1001
        else if (bit0 && !bit1 && !bit2 && bit3)
        {
            result = op1.or(op2);
        }
        // XOR: 1010
        else if (bit0 && !bit1 && bit2 && !bit3)
        {
            result = op1.xor(op2);
        }
        // NOT: 1011
        else if (bit0 && !bit1 && bit2 && bit3)
        {
            result = op1.not();
        }
        // LEFT SHIFT: 1100
        else if (bit0 && bit1 && !bit2 && !bit3)
        {
            shiftAmount = calculateShiftAmount(op2);
            result = op1.leftShift(shiftAmount);
        }
        // RIGHT SHIFT: 1101
        else if (bit0 && bit1 && !bit2 && bit3)
        {
            shiftAmount = calculateShiftAmount(op2);
            result = op1.rightShift(shiftAmount);
        }
        // ADD: 1110
        else if (bit0 && bit1 && bit2 && !bit3)
        {
            result = add(op1, op2);
        }
        // SUBTRACT: 1111
        else if (bit0 && bit1 && bit2 && bit3)
        {
            result = subtract(op1, op2);
        }
        // MULTIPLY: 0111
        else if (!bit0 && bit1 && bit2 && bit3)
        {
            result = multiply(op1, op2);
        }
        // Throws in the case of invalid code.
        else
        {
            throw new IllegalArgumentException("Invalid operation code provided.");
        }
    }

    // Helper method to calculate shift amount.
    private int calculateShiftAmount(Word op2)
    {
        // Ignores all but lowest 5 bits.
        int shiftAmount = (int) op2.getUnsigned();

        // Makes sure shiftAmount falls within 0-31 range.
        if (shiftAmount >= 32)
        {
            shiftAmount -= 32 * (shiftAmount / 32);
        }

        return shiftAmount;
    }

    // Adds using add2.
    private Word add(Word a, Word b)
    {
        Word sum = new Word();
        Bit carry = new Bit(false);
        // Gets the sum for all bits in both words.
        for (int i = 31; i >= 0; i--)
        {
            Bit[] result = add2(a.getBit(i), b.getBit(i), carry);
            sum.setBit(i, result[0]);
            carry = result[1];
        }
        // Returns the resulting word.
        return sum;
    }

    // Subtracts using add and not.
    private Word subtract(Word a, Word b)
    {
        // Negate b using not.
        Word negatedB = b.not();
        Word one = new Word();
        // Sets the least significant bit to 1.
        one.setBit(31, new Bit(true));
        // Returns the value after adding a positive and negative.
        return add(a, add(negatedB, one));
    }

    // Multiplies using shift and add.
    private Word multiply(Word a, Word b)
    {
        Word product = new Word();
        for (int i = 31; i >= 0; i--)
        {
            if (b.getBit(i).getValue() == true)
            {
                // Checks if bit is 1, then add a shifted left by (31 - i) positions.
                product = add(product, a.leftShift(31 - i));
            }
        }
        return product;
    }

    // Adds two bits with carry in.
    public Bit[] add2(Bit a, Bit b, Bit carryIn)
    {
        // Uses provided formula to add.
        Bit sum = a.xor(b).xor(carryIn);
        Bit carryOut = a.and(b).or(a.xor(b).and(carryIn));
        return new Bit[] {sum, carryOut};
    }

    // Adds four bits with carry in, extends add2 logic.
    public Bit[] add4(Bit a, Bit b, Bit c, Bit d, Bit carryIn)
    {
        // Gets first add2 result.
        Bit[] result1 = add2(a, b, carryIn);
        // Gets second add2 result.
        Bit[] result2 = add2(c, d, result1[1]);
        // Gets a final result using previous two results.
        Bit[] finalResult = add2(result1[0], result2[0], new Bit(false));
        // Checking for a carry.
        Bit finalCarry = result1[1].or(result2[1]).or(finalResult[1]);
        return new Bit[] {finalResult[0], finalCarry};
    }
}