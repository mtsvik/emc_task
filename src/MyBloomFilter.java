import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public class MyBloomFilter {
    private final BitArray bits;
    private final int numHashFunctions;


    public MyBloomFilter(BitArray bits, int numHashFunctions) {
        this.bits = bits;
        this.numHashFunctions = numHashFunctions;
    }


}
