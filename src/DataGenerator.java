/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class DataGenerator {

    public static byte[] generateBits(int length) {
        byte[] data = new byte[(length + 7) / 8];
        for (int i = 0; i < length; i++) {
            data[i] = (byte) ((Math.random() > 0.5) ? 1 : 0);
        }
        return data;
    }

    public static Data generate(int length) {
        return new Data(generateBits(length));
    }

}
