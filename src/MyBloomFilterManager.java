import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 20.02.15
 */

public class MyBloomFilterManager implements BloomFilterManager {
    private int shingleLength;
    private MyBloomFilter<Shingle> bloomFilter;

    public MyBloomFilterManager(int shingleLength) {
        this.shingleLength = shingleLength;
    }

    @Override
    public void fillFilter(Entity standart) {
        bloomFilter = new MyBloomFilter<>(standart.getLength() * 8 - shingleLength + 1, 0.01);
        BitArray buffer = new BitArray(shingleLength);
        BitArray standartSegment = new BitArray(standart.getLength() * 8, standart.getByteArray());
        for (int i = 0; i < standartSegment.length() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, standartSegment.get(counter));
                counter++;
            }
            Shingle shingle = new Shingle(buffer, shingleLength, i);
            bloomFilter.put(shingle, standart);
            buffer = new BitArray(shingleLength);
        }
    }

    @Override
    public double getSimilarity(Entity e1) {
        double same = 0;
        BitArray buffer = new BitArray(shingleLength);
        BitArray segment = new BitArray(e1.getLength() * 8, e1.getByteArray());
        for (int i = 0; i < segment.length() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, segment.get(counter));
                counter++;
            }
            ;
            Shingle shingle = new Shingle(buffer, shingleLength, i);
            if (bloomFilter.contain(shingle, e1)) same++;
            buffer = new BitArray(shingleLength);
        }
        return Math.round((same / (double) (e1.getLength() * 8 - shingleLength + 1)) * 100);
    }
}
