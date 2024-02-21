public class Processor
{
    // Program Counter.
    private Word PC = new Word();
    // Stack Pointer.
    private Word SP = new Word();
    // Indicator for if the processor is halted.
    private Bit halted = new Bit(false);
    private Word currentInstruction = new Word();

    public Processor()
    {
        // Sets the beginning of memory (Starts at address 0).
        PC.set(0);
        // Sets the end of memory (Ends at address 1023 or the end of the 1024-word memory).
        SP.set(1023);
    }

    public void run()
    {
        while (!halted.getValue())
        {
            fetch();
            decode();
            execute();
            store();
        }
    }

    private void fetch()
    {
        // Fetches instruction from memory.
        currentInstruction.copy(MainMemory.read(PC));
        // Increments PC to point to the next instruction.
        PC.increment();
    }

    private void decode()
    {

    }

    private void execute()
    {

    }

    private void store()
    {

    }
}
