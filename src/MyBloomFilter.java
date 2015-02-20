import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public class MyBloomFilter<T extends Shingle> {
    private int expectedInsertions;
    private int numOfHashFunctions;
    private int numOfBits;
    private BitArray bits;
    private int[] simpleNumbers = {71, 139, 241, 311, 457, 569, 613, 727, 857, 911, 277, 53, 179, 359, 419, 541, 631, 769};
    private int[] prevHashIndexes;
    private int prevHashId = 0;
    private int counter = 0;
    private long matrixOfFirts[][];


    public MyBloomFilter(int expectedInsertions, double fpp) {
        this.expectedInsertions = expectedInsertions;
        this.numOfBits = optimalNumOfBits(expectedInsertions, fpp);
        this.numOfHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numOfBits);
        this.bits = new BitArray(numOfBits);
        for (int i = 0; i < bits.length(); i++) bits.set(i, false);
        prevHashIndexes = new int[numOfHashFunctions];
        prevHashId = 0;
    }

    private int optimalNumOfBits(int n, double p) {
        if (p == 0) p = Double.MIN_VALUE;
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    private int optimalNumOfHashFunctions(long n, long m) {
        int num = Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
        if (num > simpleNumbers.length) return simpleNumbers.length;
        return num;
    }

    public void put(T item, Entity segment) {
        if (counter == 0) matrixOfFirts = segment.getMatrix(simpleNumbers, numOfHashFunctions, item.getLength());
        int[] hashes = createHashes(item);
        for (int hash : hashes) {
            System.out.println(hash);
            bits.set(hash % numOfBits, true);
        }
        System.out.println("putted");
        System.out.println();
        counter++;
        if (counter == expectedInsertions) counter = 0;
    }

    public void contain(T item) {

    }

    private int[] createHashes(T item) {
        int base;
        BitArray shingleBits = item.getBitArray();
        int[] indexes = null;
        if (counter == 0) {
            indexes = new int[numOfHashFunctions];
            for (int k = 0; k < numOfHashFunctions; k++) {
                base = simpleNumbers[k];
                int counter = 0;
                int b1 = item.getBitArray().get(counter) ? 1 : 0;
                long index = fastCompute(base, item, counter, b1) + item.getId();
                prevHashIndexes[k] = (int) index;
                indexes[k] = (int) index;
            }
        } else {
            indexes = prevHashIndexes;
            for (int k = 0; k < numOfHashFunctions; k++) {
                base = simpleNumbers[k];
                long prevIndex = indexes[k] - prevHashId;
                long newIndex = (Util.mod((base * (prevIndex - matrixOfFirts[k][prevHashId])), Util.MODULE) + ((shingleBits.get(shingleBits.length() - 1) ? 1 : 0) * base)) % Util.MODULE;
                newIndex += item.getId();
                prevHashIndexes[k] = (int) newIndex;
                indexes[k] = (int) newIndex;
            }
            prevHashId = item.getId();
        }
        return indexes;
    }

    private long fastCompute(int base, T item, int k, long b1) {
        int b2 = item.getBitArray().get(k + 1) ? 1 : 0;
        long answ = (base * b1 + b2) % Util.MODULE;
        k++;
        if (k == item.getBitArray().length() - 1) return (base * answ) % Util.MODULE;
        return fastCompute(base, item, k, answ);
    }

}
