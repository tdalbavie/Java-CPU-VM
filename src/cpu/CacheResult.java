package cpu;

// Helper class to signify L2Cache hit or miss.
public class CacheResult
{
    public final Word word;
    public final boolean hit;
    public int groupIndex;

    public CacheResult(Word word, boolean hit, int groupIndex)
    {
        this.word = word;
        this.hit = hit;
        this.groupIndex = groupIndex;
    }
}
