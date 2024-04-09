package cpu;

public class L2Cache {
    private final Word[][] cache = new Word[4][8];
    // Stores the starting address of each group.
    private final Word[] groupAddresses = new Word[4];
    // Holds the next group to replace with MainMemory instructions (oldest first).
    private int nextGroupToReplace = 0;

    public L2Cache()
    {
        for (int i = 0; i < 4; i++)
        {
            groupAddresses[i] = new Word();
            groupAddresses[i].set(-9);
            for (int j = 0; j < 8; j++)
            {
                cache[i][j] = new Word();
            }
        }
    }

    // Reads from L2 cache, if it hits, the group will move to InstructionCache.
    public CacheResult read(Word address, Processor processor)
    {
        long addressValue = address.getUnsigned();
        for (int groupIndex = 0; groupIndex < groupAddresses.length; groupIndex++)
        {
            long startAddressValue = groupAddresses[groupIndex].getUnsigned();
            if (addressValue >= startAddressValue && addressValue < startAddressValue + 8)
            {
                // Cache hit within L2.
                processor.incrementClockCycle(20);
                return new CacheResult(cache[groupIndex][(int)(addressValue - startAddressValue)], true, groupIndex);
            }
        }

        // Cache miss within L2, need to fetch from main memory.
        int currentGroupToReplace = nextGroupToReplace;
        fillGroupFromMainMemory(currentGroupToReplace, address, processor);
        // Move to next group for future replacements.
        nextGroupToReplace = (nextGroupToReplace + 1) % 4;
        processor.incrementClockCycle(350);

        // Return the word from the newly filled group, ensuring we use the current group index
        long baseAddress = address.getUnsigned() - (address.getUnsigned() % 8);
        int wordIndex = (int)(addressValue - baseAddress);
        return new CacheResult(cache[currentGroupToReplace][wordIndex], false, currentGroupToReplace);
    }

    // Fills a group based on the nextGroupToReplace counter.
    private void fillGroupFromMainMemory(int groupIndex, Word address, Processor processor)
    {
        long baseAddress = address.getUnsigned() - (address.getUnsigned() % 8);
        groupAddresses[groupIndex].set((int) baseAddress);
        for (int i = 0; i < 8; i++)
        {
            Word fetchAddress = new Word();
            fetchAddress.set((int) (baseAddress + i));
            cache[groupIndex][i] = MainMemory.read(fetchAddress);
        }
    }

    // Gets a group for transfer to InstructionCache.
    public Word[] getCacheGroup(int groupIndex)
    {
        if (groupIndex >= 0 && groupIndex < cache.length)
        {
            return cache[groupIndex];
        }
        else
        {
            throw new IllegalArgumentException("Invalid group index");
        }
    }

    // Gets a groups base address for use in InstructionCache.
    public Word getGroupBaseAddress(int groupIndex)
    {
        if (groupIndex >= 0 && groupIndex < groupAddresses.length)
        {
            return groupAddresses[groupIndex];
        }
        else
        {
            throw new IllegalArgumentException("Invalid group index");
        }
    }
}
