import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public class GoogleBloomFilterManager implements BloomFilterManager {
    private Entity original;
    private int shingleLength;
    private BloomFilter<Shingle> bloomFilter;
    private Funnel<Shingle> funnel = new Funnel<Shingle>() {

        @Override
        public void funnel(Shingle shingle, PrimitiveSink primitiveSink) {
            primitiveSink.putBytes(shingle.getBitArray().toByteArray()).putByte(shingle.getId());
        }
    };

    public GoogleBloomFilterManager(int shingleLength) {
        this.shingleLength = shingleLength;
    }

    public void fillFilter(Entity standart) {
        original = standart;
        bloomFilter = BloomFilter.create(funnel, original.getLength() * 8 - shingleLength + 1, 0.001);
        MyBitArray buffer = new MyBitArray(shingleLength);
        MyBitArray standartSegment = new MyBitArray(standart.getLength() * 8, standart.getByteArray());
        for (int i = 0; i < standartSegment.length() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, standartSegment.get(counter));
                counter++;
            }
            Shingle shingle = new Shingle(buffer, shingleLength, (byte) i);
            bloomFilter.put(shingle);
            buffer = new MyBitArray(shingleLength);
        }
    }

    public double getSimilarity(Entity e1) {
        double same = 0;
        MyBitArray buffer = new MyBitArray(shingleLength);
        MyBitArray segment = new MyBitArray(e1.getLength() * 8, e1.getByteArray());
        for (int i = 0; i < segment.length() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, segment.get(counter));
                counter++;
            }
            Shingle shingle = new Shingle(buffer, shingleLength, (byte) i);
            if (bloomFilter.mightContain(shingle)) same++;
            buffer = new MyBitArray(shingleLength);
        }
        return Math.round((same / (double) (original.getLength() - shingleLength + 1)) * 100);
    }



}
