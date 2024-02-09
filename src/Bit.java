public class Bit
{
    // Holds the value of the bit.
    private boolean value;

    // Constructor sets value to default of false (or 0).
    public Bit()
    {
        value = false;
    }

    // Constructor for returning new bit after logic operation.
    public Bit(boolean value)
    {
        this.value = value;
    }

    // Sets the value of the bit.
    public void set(boolean value)
    {
        this.value = value;
    }

    // Changes the value from true to false or false to true.
    public void toggle()
    {
        if (value == true)
            value = false;
        else
            value = true;
    }

    // Sets the bit to true.
    public void set()
    {
        value = true;
    }

    // Sets the bit to false.
    public void clear()
    {
        value = false;
    }

    // Returns the current value.
    public boolean getValue()
    {
        return value;
    }

    // Performs and on two bits and returns a new bit set to the result.
    public Bit and(Bit other)
    {
        if (value == other.getValue())
            if (value == true)
                return new Bit(true);
            else
                return new Bit(false);
        else
            return new Bit(false);
    }

    // Performs or on two bits and returns a new bit set to the result.
    public Bit or(Bit other)
    {
        if (value == true)
            return new Bit(true);
        else if(other.getValue() == true)
            return new Bit(true);
        else
            return new Bit(false);
    }

    // Performs xor on two bits and returns a new bitset to the result.
    public Bit xor(Bit other)
    {

        if (value == true)
            if (other.getValue() == false)
                return new Bit(true);
            else
                return new Bit(false);
        else if(other.getValue() == true)
            return new Bit(true);

        else
            return new Bit(false);
    }

    // Performs not on the existing bit, returning the result as a new bit.
    public Bit not()
    {
        if (value == true)
            return new Bit(false);
        else
            return new Bit(true);
    }

    // Returns "t" or "f" based on bit value.
    public String toString()
    {
        if (value == true)
            return "t";
        else
            return "f";
    }
}
