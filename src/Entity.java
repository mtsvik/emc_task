import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public interface Entity {

    public BitArray getByteArray();
    public String getString();
    public int getLength();
    public void clear();

}
