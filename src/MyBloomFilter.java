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
    private int[] simpleNumbers = {71, 139, 241, 311, 457, 569, 613, 727, 857, 911};
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

    public void put(T item) {
        int[] hashes = createHashes(item);
        for (int hash : hashes) {
            bits.set(hash % numOfBits, true);
        }
        counter++;
        if (counter == expectedInsertions) counter = 0;
    }

    public void contain(T item) {

    }

    private int[] createHashes(T item) {
        int base;
        BitArray bitArray = item.getBitArray();
        int[] indexes = null;
        if (counter == 0) {
            indexes = new int[numOfHashFunctions];
            for (int k = 0; k < numOfHashFunctions; k++) {
                base = simpleNumbers[k];
                long index = 0;
                for (int i = 1; i < bitArray.length(); i++) {
                    int number = bitArray.get(i) ? 1 : 0;
                    index += (number * Math.pow(base, bitArray.length() - i)) % 1073741824;
                }
                long first = (long) ((bitArray.get(0) ? 1 : 0) * Math.pow(base, bitArray.length())) % 1073741824;
                index = (index + first) % 1073741824;
                index += item.getId();

                prevHashIndexes[k] = (int) index;
                prevHashFirst[k] = (int) first;
                prevHashId = item.getId();
                indexes[k] = (int) index;
            }
        } else {
            indexes = prevHashIndexes;
            for (int k = 0; k < numOfHashFunctions; k++) {
                base = simpleNumbers[k];
                long prevIndex = indexes[k] - prevHashId;
                long step1 = (base * (prevIndex - prevHashFirst[k])) % 1073741824;
                long step2 = ((bitArray.get(bitArray.length() - 1) ? 1 : 0) * base);
                long newIndex = (step1 + step2) % 1073741824;
//                long newIndex = ((base * (prevIndex - prevHashFirst[k])) % 1073741824 + ((bitArray.get(bitArray.length() - 1) ? 1 : 0) * base)) % 1073741824;
                newIndex += item.getId();
                long first = (long) (((bitArray.get(0) ? 1 : 0) * Math.pow(base, bitArray.length())) % 1073741824);

                prevHashIndexes[k] = (int) newIndex;
                prevHashFirst[k] = (int) first;
                indexes[k] = (int) newIndex;
            }
            prevHashId = item.getId();
        }
        return indexes;
    }


}
