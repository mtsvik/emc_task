import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Data {
    private BitArray data;
    private long size;

    public Data(BitArray data) {
        this.data = data;
        this.size = data.length();
    }

    public String toString() {
        String string = "";
        for (int i = 0; i < size; i++) {
            string += data.get(i);
        }
        return string;
    }

    public long getSize() {
        return size;
    }

    public BitArray getData() {
        return data;
    }
}
