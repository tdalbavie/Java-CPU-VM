public class Word
{
    // Holds a set of 32 bits.
    private Bit[] BitArray;

    public Word()
    {
        BitArray = new Bit[32];
        // Populates the array with default bits (all bits will be set to false).
        for (int i = 0; i < BitArray.length; i++)
            BitArray[i] = new Bit();
    }

    // Simple constructor used to create deep copy of a word to read from memory.
    public Word(Word wordToRead)
    {
        BitArray = new Bit[32];
        // Makes a copy of the array from the word that is going to be read.
        System.arraycopy(wordToRead.getBitArray(), 0, BitArray, 0, BitArray.length);
    }

    // Get a new Bit that has the same value as bit i.
    public Bit getBit(int i)
    {
        if (BitArray[i].getValue() == true)
            return new Bit(true);
        else
            return new Bit(false);
    }

    // Set bit i's value.
    public void setBit(int i, Bit value)
    {
        boolean val = value.getValue();
        BitArray[i].set(val);
    }

    // and two words, returning a new Word.
    public Word and(Word other)
    {
        Word newWord = new Word();

        for(int i = 0; i < BitArray.length; i++)
        {
            // Uses Bit and operation and inserts result into newWord for each bit in the words.
            Bit newBit = BitArray[i].and(other.getBit(i));
            newWord.setBit(i, newBit);
        }

        // Returns newly created Word after processing.
        return newWord;
    }

    // or two words, returning a new Word.
    public Word or(Word other)
    {
        Word newWord = new Word();

        for(int i = 0; i < BitArray.length; i++)
        {
            // Uses Bit and operation and inserts result into newWord for each bit in the words.
            Bit newBit = BitArray[i].or(other.getBit(i));
            newWord.setBit(i, newBit);
        }

        // Returns newly created Word after processing.
        return newWord;
    }

    // xor two words, returning new Word.
    public Word xor(Word other)
    {
        Word newWord = new Word();

        for(int i = 0; i < BitArray.length; i++)
        {
            // Uses Bit and operation and inserts result into newWord for each bit in the words.
            Bit newBit = BitArray[i].xor(other.getBit(i));
            newWord.setBit(i, newBit);
        }

        // Returns newly created Word after processing.
        return newWord;
    }

    // negate this word, creating a new Word.
    public Word not()
    {
        Word newWord = new Word();

        for(int i = 0; i < BitArray.length; i++)
        {
            Bit newBit = BitArray[i].not();
            newWord.setBit(i, newBit);
        }

        // Returns newly created Word after processing.
        return newWord;
    }

    // Left shift this word by amount bits, creating a new Word.
    public Word leftShift(int amount)
    {
        Word shiftedWord = new Word();

        for (int i = 0; i < BitArray.length; i++)
        {
            if (i + amount < BitArray.length)
            {
                shiftedWord.BitArray[i] = BitArray[i + amount];
            }

            else
            {
                shiftedWord.BitArray[i] = new Bit(false); // Filling with 0s
            }
        }
        return shiftedWord;
    }

    // Right shift this word by amount bits, creating a new Word.
    public Word rightShift(int amount)
    {
        Word shiftedWord = new Word();

        for (int i = 0; i < BitArray.length; i++)
        {
            if (i - amount >= 0)
            {
                shiftedWord.BitArray[i] = BitArray[i - amount];
            }

            else
            {
                shiftedWord.BitArray[i] = new Bit(false); // Filling with 0s
            }
        }
        return shiftedWord;
    }

    // Returns a comma separated string "t"'s and "f"'s.
    public String toString()
    {
        String bitValues = "";

        // Concatenates each string value to bitValues string.
        for (int i = 0; i < BitArray.length; i++)
            bitValues += BitArray[i].toString() + ",";

        // Removes the last "," in the string.
        bitValues = bitValues.replaceAll(",$", "");

        return bitValues;
    }

    // Returns the value of this word as a long.
    public long getUnsigned()
    {
        long value = 0;
        for (int i = 0; i < BitArray.length; i++)
        {
            if (BitArray[i].getValue())
            {
                value += Math.pow(2, 31 - i);
            }
        }
        return value;
    }

    // Returns the value of this word as an int.
    public int getSigned()
    {
        // Check if the number is negative (MSB is 1)
        boolean isNegative = BitArray[0].getValue();
        int value = 0;

        if (isNegative == false)
        {
            for (int i = 0; i < BitArray.length; i++)
            {
                if (BitArray[i].getValue())
                {
                    value += Math.pow(2, 31 - i);
                }
            }
        }

        else
        {

        }
        // If the number is negative, convert from two's complement
        if (isNegative)
        {
            for (int i = 0; i < BitArray.length; i++)
            {
                if (BitArray[i].getValue() == false)
                {
                    value += Math.pow(2, 31 - i);
                }
            }
            value = -(value + 1);
        }

        return value;
    }

    // Copies the values of the bits from another Word into this one.
    public void copy(Word other)
    {
        for (int i = 0; i < BitArray.length; i++)
        {
            Bit newBit;

            // Checks the bit value of other Word.
            if (other.getBit(i).getValue() == true)
                newBit = new Bit(true);
            else
                newBit = new Bit(false);

            // Inserts the bit value at same position in this Word.
            BitArray[i] = newBit;
        }
    }

    // Set the value of the bits of this Word (used for tests)
    public void set(int value)
    {
        // Checks for a negative value.
        boolean isNegative = value < 0;

        // Changes value to positive.
        if(isNegative)
            value = -value;

        // Overrides existing information so new bit value can be written.
        for (int i = 0; i < BitArray.length; i++)
            BitArray[i] = new Bit(false);

        for (int i = 31; i >= 0; i--)
        {
            if (value != 0)
            {
                BitArray[i] = new Bit(value % 2 == 1);
                value = value / 2;
            }
            else
            {
                BitArray[i] = new Bit(false);
            }
        }

        // Applies the two's compliment to convert to a negative binary value.
        if (isNegative)
        {
            invertBits();
            addOne();
        }
    }

    // Helper method to invert bits for two's compliment.
    private void invertBits()
    {
        for (int i = 0; i < BitArray.length; i++)
        {
            // Inverts each bit value.
            BitArray[i].set(BitArray[i].getValue() == false);
        }
    }

    // Helper method to add one to the beginning of number for two's compliment.
    private void addOne()
    {
        for (int i = BitArray.length - 1; i >= 0; i--)
        {
            // Check if the bit needs to be carried over.
            if (BitArray[i].getValue() == false)
            {
                BitArray[i].set(true);
                break;
            }

            BitArray[i].set(false);
        }
    }

    // Used to increment the word by 1.
    public void increment()
    {
        // Starts with a carry to add 1.
        Bit carry = new Bit(true);

        // Loops through the array of bits to increment the word.
        for (int i = 31; i >= 0 && carry.getValue(); i--)
        {
            Bit currentBit = BitArray[i];
            // XOR the current bit with carry to get the next bit.
            Bit nextBit = currentBit.xor(carry);
            // AND the current bit with carry to determine the next carry.
            carry = currentBit.and(carry);
            // Sets the next bit as the current bit.
            BitArray[i] = nextBit;
        }
    }

    public void decrement()
    {
        // Borrow to use for subtracting 1.
        Bit borrow = new Bit(true);

        for (int i = 31; i >= 0; i--)
        {
            Bit currentBit = BitArray[i];
            // If there's no borrow, no need to change the current bit.
            if (!borrow.getValue())
            {
                // Exit loop early since no further changes will occur.
                break;
            }

            // If the current bit is 1, subtracting 1 sets it to 0 with no borrow
            if (currentBit.getValue())
            {
                BitArray[i].set(false);
                // Borrow has happened and no longer needs to be used.
                borrow.set(false);
            }
            // If the current bit is 0, subtracting 1 sets it to 1, and we still have a borrow.
            else
            {
                BitArray[i].set(true);
                // borrow remains true.
            }
        }
    }

    public Bit[] getBitArray()
    {
        return BitArray;
    }
}
