import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public class MyBloomFilterManager implements BloomFilterManager {
    private Entity original;
    private int shingleLength;
    private BloomFilter<Shingle> bloomFilter;
    private Funnel<Shingle> funnel = new Funnel<Shingle>() {

        @Override
        public void funnel(Shingle shingle, PrimitiveSink primitiveSink) {
            primitiveSink.putBytes(shingle.getBitArray().toByteArray()).putByte(shingle.getId());
        }
    };

    public MyBloomFilterManager(int shingleLength) {
        this.shingleLength = shingleLength;
    }

    public void fillFilter(Entity standart) {
        original = standart;
        bloomFilter = BloomFilter.create(funnel, original.getLength() - shingleLength + 1, 0.001);
        BitArray buffer = new BitArray(shingleLength);
        for (int i = 0; i < original.getLength() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, original.getByteArray().get(counter));
                counter++;
            }
            Shingle shingle = new Shingle(buffer, shingleLength, (byte) i);
            bloomFilter.put(shingle);
            buffer = new BitArray(shingleLength);
        }
    }

    public double getSimilarity(Entity e1) {
        double same = 0;
        BitArray buffer = new BitArray(shingleLength);
        for (int i = 0; i < e1.getLength() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, e1.getByteArray().get(counter));
                counter++;
            }
            Shingle shingle = new Shingle(buffer, shingleLength, (byte) i);
            if (bloomFilter.mightContain(shingle)) {
                same++;
            }
            buffer = new BitArray(shingleLength);
        }
        return Math.round((same / (double) (original.getLength() - shingleLength + 1)) * 100);
    }

}
