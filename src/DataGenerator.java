/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class DataGenerator {

    public static byte[] generateSegment(int bytes) {
        byte[] arr = new byte[bytes];
        for (int i = 0; i < bytes; i++) {
            arr[i] = (byte) (Math.random() * 127);
        }
        return arr;
    }

}
