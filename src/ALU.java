import java.util.ArrayList;
import java.util.List;

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
        // Directly use add2 for adding two Word objects
        return add2(a, b);
    }


    // Subtracts using add and not.
    private Word subtract(Word a, Word b)
    {
        // Calculates the two's complement of b.
        Word twoComplementOfB = twosComplement(b);

        // Add a (minuend) and the two's complement of b
        return add2(a, twoComplementOfB);
    }

    // A helper method to perform the twos compliment for subtraction.
    private Word twosComplement(Word word)
    {
        Word complement = new Word();
        // Start with a carry of 1 for the addition of 1 in two's complement
        Bit carry = new Bit(true);

        // First, invert all bits
        for (int i = 31; i >= 0; i--) {
            Bit invertedBit = word.getBit(i).not();
            // Add inverted bit and carry
            Bit sum = invertedBit.xor(carry);
            carry = invertedBit.and(carry);
            complement.setBit(i, sum);
        }

        return complement;
    }

    // Multiplies using shift and add.
    private Word multiply(Word multiplicand, Word multiplier) {
        List<Word> partialProducts = new ArrayList<>();

        // Generates partial products bit by bit.
        for (int i = 31; i >= 0; i--)
        {
            if (multiplier.getBit(i).getValue())
            {
                partialProducts.add(multiplicand.leftShift(31 - i));
            }

            // Creates a new word when multiplying 0, does not affect multiplication.
            else
            {
                partialProducts.add(new Word());
            }
        }

        // Processes the partial products in rounds.
        List<Word> currentRoundResults = partialProducts;
        while (currentRoundResults.size() > 1)
        {
            // Secondary list to hold results.
            List<Word> nextRoundResults = new ArrayList<>();

            // Starts with groups of 4 words (a set of 32 words then a set of 8 words).
            for (int i = 0; i < currentRoundResults.size(); i += 4)
            {
                Word sum = new Word();

                // Checks to see if there are enough words to call add4 on.
                if (currentRoundResults.size() - i >= 4)
                {
                    sum = add4(currentRoundResults.get(i),
                            currentRoundResults.get(i + 1),
                            currentRoundResults.get(i + 2),
                            currentRoundResults.get(i + 3));
                }

                // Takes the remaining words and calls add2 to get final result.
                else
                {
                    // Handles remaining operands with add2.
                    for (int j = i; j < currentRoundResults.size(); j++)
                    {
                        sum = add2(sum, currentRoundResults.get(j));
                    }
                }

                // Adds result to new arrayList for next round.
                nextRoundResults.add(sum);
            }

            // Sets the resultant words as the next set of words to process.
            currentRoundResults = nextRoundResults;
        }

        // Returns the final result using the only remaining word left.
        return currentRoundResults.get(0);
    }


    // Adds two bits with carry in.
    public Word add2(Word a, Word b)
    {
        Word sum = new Word();
        Bit carry = new Bit(false);
        for (int i = 31; i >= 0; i--)
        {
            Bit aBit = a.getBit(i);
            Bit bBit = b.getBit(i);

            Bit sumBit = aBit.xor(bBit).xor(carry);
            carry = aBit.and(bBit).or(aBit.xor(bBit).and(carry));

            sum.setBit(i, sumBit);
        }
        return sum;
    }

    // Adds four bits with carry in extending add2.
    public Word add4(Word a, Word b, Word c, Word d)
    {
        Word sum = new Word();
        Bit carry = new Bit(false);

        for (int i = 31; i >= 0; i--)
        {
            // Get bits from each word at position i
            Bit bitA = a.getBit(i);
            Bit bitB = b.getBit(i);
            Bit bitC = c.getBit(i);
            Bit bitD = d.getBit(i);

            // Calculate the sum and carry for these four bits and the current carry
            Bit[] sumAndCarry = addFourBitsAndCarry(bitA, bitB, bitC, bitD, carry);

            // Set the resulting bit in the result word
            sum.setBit(i, sumAndCarry[0]);

            // Update the carry for the next iteration
            carry = sumAndCarry[1];
        }

        return sum;
    }

    // Helper method to add4 to add together 4 bits along with the carry.
    private Bit[] addFourBitsAndCarry(Bit a, Bit b, Bit c, Bit d, Bit carryIn)
    {
        // Calculates individual sums and intermediate carries.
        Bit sumAB = a.xor(b);
        Bit carryAB = a.and(b);

        Bit sumCD = c.xor(d);
        Bit carryCD = c.and(d);

        // Takes results then does similar operation like in add2.
        Bit sumABCD = sumAB.xor(sumCD).xor(carryIn);
        Bit carryABCD = sumAB.and(sumCD).or(carryIn.and(sumAB.xor(sumCD)));

        // Calculates final carry.
        Bit finalCarry = carryAB.or(carryCD).or(carryABCD);

        // Returns the sum and final carry.
        return new Bit[]{sumABCD, finalCarry};
    }
}