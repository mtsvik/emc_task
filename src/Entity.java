/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public interface Entity {

    public byte[] getByteArray();
    public String getString();
    public int getLength();
    public void clear();
    public long[][] getMatrix(int[] bases, int numOfHashfunctions, int shingleLength);

}
