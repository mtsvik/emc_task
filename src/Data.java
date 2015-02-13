/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Data {
    private byte[] data;
    private long size;

    public Data(byte[] data) {
        this.data = data;
        this.size = data.length;
    }

    public String toString() {
        String string = "";
        for (int i = 0; i < size; i++) {
            string += data[i];
        }
        return string;
    }

    public long getSize() {
        return size;
    }

    public byte[] getData() {
        return data;
    }
}
