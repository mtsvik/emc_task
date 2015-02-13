/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Segment implements Entity {

    private byte[] bytesArray;
    private int length;

    public Segment(byte[] charsBinaryCode) {
        this.bytesArray = charsBinaryCode;
        this.length = charsBinaryCode.length;
    }

    @Override
    public byte[] getByteArray() {
        return bytesArray;
    }

    @Override
    public String getString() {
        String buffer = "";
        for (int i = 0; i < bytesArray.length; i++) {
            buffer += bytesArray[i];
        }
        return buffer;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void clear() {
        bytesArray = null;
        length = 0;
    }


}
