import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 13.02.15
 */

public class Shingle {
    private BitArray bitArray;
    private int length;
    private byte id;

    public Shingle(BitArray bitArray, int length, byte id) {
        this.bitArray = bitArray;
        this.length = length;
        this.id = id;
    }

    public BitArray getBitArray() {
        return bitArray;
    }

    public int getLength() {
        return length;
    }

    public byte getId() {
        return id;
    }
}
