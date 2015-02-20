/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 20.02.15
 */

public class Util {
    public static final int MODULE = 1073741824;

    public static long mod(long a, long b) {
        long result = a % b;
        if (result < 0) { result += b; }
        return result;
    }
}
