public class ALU
{
    Word op1;
    Word op2;
    Word result;

    // Constructor for ALU.
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

        // Decision tree to determine the operation.
        // Checks if first bit is 1.
        if (operation[0].getValue() == true)
        {
            // Checks if second bit is 0.
            if (operation[1].getValue() == false)
            {
                // Checks if third bit is 0.
                if (operation[2].getValue() == false)
                {
                    // Checks if fourth bit is 0.
                    // AND: 1000
                    if (operation[3].getValue() == false)
                    {
                        result = op1.and(op2);
                    }
                    // Fourth bit is 1.
                    // OR: 1001
                    else
                    {
                        result = op1.or(op2);
                    }
                }
                // Third bit is 1.
                else
                {
                    // Checks if fourth bit is 0.
                    // XOR: 1010
                    if (operation[3].getValue() == false)
                    {
                        result = op1.xor(op2);
                    }
                    // Fourth bit is 1.
                    // NOT: 1011
                    else
                    {
                        result = op1.not();
                    }
                }
            }
            // Second bit is 1.
            else
            {
                // Checks if third bit is 0.
                if (operation[2].getValue() == false)
                {
                    // Checks if fourth bit is 0.
                    // LEFT SHIFT: 1100
                    if (operation[3].getValue() == false)
                    {
                        shiftAmount = calculateShiftAmount(op2);
                        result = op1.leftShift(shiftAmount);
                    }
                    // Fourth bit is 1.
                    // RIGHT SHIFT: 1101
                    else
                    {
                        shiftAmount = calculateShiftAmount(op2);
                        result = op1.rightShift(shiftAmount);
                    }
                }
                // Third bit is 1.
                else
                {
                    // Checks if fourth bit is 0.
                    // ADD: 1110
                    if (operation[3].getValue() == false)
                    {
                        result = add(op1, op2);
                    }
                    // Fourth bit is 1.
                    // SUBTRACT: 1111
                    else
                    {
                        result = subtract(op1, op2);
                    }
                }
            }
        }
        // First bit is 0.
        else if (operation[0].getValue() == false)
        {
            // Checks if second bit is 1.
            if (operation[1].getValue() == true)
            {
                // Checks if third bit is 1.
                if (operation[2].getValue() == true)
                {
                    // Checks if fourth bit is 1.
                    // MULTIPLY: 0111
                    if (operation[3].getValue() == true)
                    {
                        result = multiply(op1, op2);
                    }
                }
            }
        }
        // If code does not exist.
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

    // Adds four bits with carry in extending add2.
    public Bit[] add4(Bit a, Bit b, Bit c, Bit d, Bit carryIn)
    {
        // Calculates intermediate sums.
        Bit abSum = a.xor(b);
        Bit cdSum = c.xor(d);

        // Calculates intermediate carries.
        Bit abCarry = a.and(b);
        Bit cdCarry = c.and(d);

        // Calculates final sum considering the carryIn.
        Bit sumWithoutCarryIn = abSum.xor(cdSum);
        Bit finalSum = sumWithoutCarryIn.xor(carryIn);

        // Calculates carries that need to be propagated to the final carry.
        Bit carryFromABtoCD = abSum.and(cdSum);
        Bit carryFromCDtoFinal = cdSum.and(carryIn);
        Bit carryFromABtoFinal = abSum.and(carryIn);

        // Calculates final carry.
        Bit finalCarry = abCarry.or(cdCarry).or(carryFromABtoCD).or(carryFromCDtoFinal).or(carryFromABtoFinal);

        return new Bit[] {finalSum, finalCarry};
    }
}