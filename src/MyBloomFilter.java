import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public class MyBloomFilter<T extends Shingle> {
    private int expectedInsertions;
    private double fpp;
    private int numOfHashFunctions;
    private int numOfBits;
    private BitArray bits;
    private int[] simpleNumbers = {65713, 77101, 75853, 97943, 92849, 75437, 82009, 79627, 98893, 91757};
    private int[] prevHashIndexes;
    private int[] prevHashFirst;
    private int prevHashId;
    private int counter = 0;


    public MyBloomFilter(int expectedInsertions, double fpp) {
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.numOfBits = optimalNumOfBits(expectedInsertions, fpp);
        this.numOfHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numOfBits);
        this.bits = new BitArray(numOfBits);
        for (int i = 0; i < bits.length(); i++) bits.set(i, false);
        prevHashIndexes = new int[numOfHashFunctions];
        prevHashFirst = new int[numOfHashFunctions];
        prevHashId = 0;
    }

    private int optimalNumOfBits(int n, double p) {
        if (p == 0) p = Double.MIN_VALUE;
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    private void put(T item) {
        int[] hashes = createHashes(item);
        for (int hash : hashes) {
            bits.set(hash % numOfBits, true);
        }
        counter++;
        if (counter == expectedInsertions) counter = 0;
    }

    public int[] createHashes(T item) {
        int base;
        BitArray bitArray = item.getBitArray();
        int[] indexes = null;
        if (counter == 0) {
            indexes = new int[numOfHashFunctions];
            for (int k = 0; k < numOfHashFunctions; k++) {
                base = simpleNumbers[k];
                int index = 0;
                for (int i = 1; i < bitArray.length(); i++) {
                    int number = bitArray.get(i) ? 1 : 0;
                    index += number * Math.pow(base, bitArray.length() - i);
                }
                int first = (int) ((bitArray.get(0) ? 1 : 0) * Math.pow(base, bitArray.length()));
                index += first;
                index += item.getId();

                prevHashIndexes[k] = index;
                prevHashFirst[k] = first;
                prevHashId = item.getId();
                indexes[k] = index;
            }
        } else {
            indexes = prevHashIndexes;
            for (int k = 0; k < numOfHashFunctions; k++) {
                base = simpleNumbers[k];
                int prevIndex = indexes[k] - prevHashId;
                int newIndex = base * (prevIndex - prevHashFirst[k]) + (bitArray.get(bitArray.length()) ? 1 : 0) * base;
                newIndex += item.getId();
                int first = (int) ((bitArray.get(0) ? 1 : 0) * Math.pow(base, bitArray.length()));

                prevHashIndexes[k] = newIndex;
                prevHashFirst[k] = first;
                prevHashId = item.getId();
                indexes[k] = newIndex;
            }
        }
        return indexes;
    }

    public void contain(T item) {

    }

}
