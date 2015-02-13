import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Segment implements Entity {

    private BitArray bitArray;
    private int length;

    public Segment(BitArray charsBinaryCode) {
        this.bitArray = charsBinaryCode;
        this.length = charsBinaryCode.length();
    }

    @Override
    public BitArray getBitArray() {
        return bitArray;
    }

    @Override
    public String getString() {
        String buffer = "";
        for (int i = 0; i < bitArray.length(); i++) {
            buffer += bitArray.get(i);
        }
        return buffer;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void clear() {
        bitArray = null;
        length = 0;
    }


}
