package cpu;

public class InstructionCache
{
    // Cache storage.
    private final Word[] cache;
    // The starting address of the cached block.
    private final Word startAddress;
    // Flag to check if the cache has been initialized.
    private final boolean isInitialized;
    private final L2Cache l2Cache;

    public InstructionCache()
    {
        this.cache = new Word[8];
        this.startAddress = new Word();
        this.isInitialized = false;
        this.l2Cache = new L2Cache();
        for (int i = 0; i < this.cache.length; i++)
        {
            this.cache[i] = new Word();
        }
    }

    public Word read(Word address, Processor processor)
    {
        long addressValue = address.getUnsigned();
        long startAddressValue = this.startAddress.getUnsigned();

        if (isInitialized && addressValue >= startAddressValue && addressValue < startAddressValue + 8)
        {
            // Cache hit.
            processor.incrementClockCycle(10);
            return cache[(int) (addressValue - startAddressValue)];
        }
        else
        {
            CacheResult l2Result = l2Cache.read(address, processor);
            if (l2Result.hit)
            {
                fillCacheFromL2Cache(l2Result.groupIndex, processor);
                // Increases clock cycle for transferring data from L2Cache to here.
                processor.incrementClockCycle(50);
            }
            // Returns the result from L2Cache or MainMemory.
            return l2Result.word;
        }
    }

    // Fills the cache with instructions from MainMemory (This was used before L2Cache was implemented).
    private void fillCache(Word address, Processor processor)
    {
        long baseAddress = address.getUnsigned() - (address.getUnsigned() % 8);
        this.startAddress.set((int) baseAddress);

        for (int i = 0; i < this.cache.length; i++)
        {
            Word fetchAddress = new Word();
            fetchAddress.set((int) (baseAddress + i));
            this.cache[i] = MainMemory.read(fetchAddress);
        }
    }

    // Fills the cache with a group from L2Cache.
    private void fillCacheFromL2Cache(int groupIndex, Processor processor)
    {
        Word[] l2CacheGroup = l2Cache.getCacheGroup(groupIndex);
        long baseAddress = l2Cache.getGroupBaseAddress(groupIndex).getUnsigned();
        this.startAddress.set((int) baseAddress);

        for (int i = 0; i < this.cache.length; i++)
        {
            this.cache[i] = l2CacheGroup[i];
        }
    }
}