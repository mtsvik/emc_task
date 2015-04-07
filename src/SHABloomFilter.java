import sun.security.util.BitArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 06.04.15
 */

public class SHABloomFilter<T extends Shingle> {
    private int expectedInsertions;
    private int numOfHashFunctions;
    private int numOfBits;
    private BitArray bits;
    private MessageDigest md;


    public SHABloomFilter(int expectedInsertions, double fpp) throws NoSuchAlgorithmException {
        this.expectedInsertions = expectedInsertions;
        this.numOfBits = optimalNumOfBits(expectedInsertions, fpp);
        this.numOfHashFunctions = 5;
        this.bits = new BitArray(numOfBits);
        for (int i = 0; i < bits.length(); i++) bits.set(i, false);
        this.md = MessageDigest.getInstance("SHA1");
    }

    private int optimalNumOfBits(int n, double p) {
        if (p == 0) p = Double.MIN_VALUE;
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    public void put(T item) throws NoSuchAlgorithmException {
        int[] hashes = createHashes(item);
        for (int hash : hashes) {
            bits.set((int) Util.mod(hash,numOfBits), true);

        }
    }

    public boolean contain(T item) throws NoSuchAlgorithmException {
        int[] hashes = createHashes(item);
        for (int hash : hashes) {
            if (!bits.get((int) Util.mod(hash,numOfBits))) return false;
        }
        return true;
    }

    private int[] createHashes(T item) throws NoSuchAlgorithmException {
        int[] indexes = new int[numOfHashFunctions];
        md.update(item.getBitArray().toByteArray());
        byte[] output = md.digest();
        byte[] buffer = new byte[output.length / numOfHashFunctions];
        for (int j = 0, counter = 0, index = 0; index < 5; j++, counter++) {
            buffer[counter] = output[j];
            if (counter == buffer.length - 1) {
                indexes[index] = java.nio.ByteBuffer.wrap(buffer).getInt();
                index++;
                counter = 0;
            }
        }
        return indexes;
    }
}