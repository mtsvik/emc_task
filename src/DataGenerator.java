import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class DataGenerator {

    public static BitArray generateBits(int length) {
        BitArray data = new BitArray(length);
        for (int i = 0; i < length; i++) {
            data.set(i, (Math.random() > 0.5));
        }
        return data;
    }

    public static Data generate(int bits) {
        return new Data(generateBits(bits));
    }

}
