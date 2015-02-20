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

    public int getBit(int var1) throws ArrayIndexOutOfBoundsException {
        return ((this.bytesArray[subscript(var1)] & position(var1)) != 0) ? 1 : 0;
    }

    private static int subscript(int var0) {
        return var0 / 8;
    }

    private static int position(int var0) {
        return 1 << 7 - var0 % 8;
    }

    @Override
    public void clear() {
        bytesArray = null;
        length = 0;
    }

    @Override
    public long[][] getMatrix(int[] bases, int numOfHashfunctions, int shingleLength) {
        long[][] matrix = new long[numOfHashfunctions][length * 8 - shingleLength + 1];
        long[] basePow = new long[numOfHashfunctions];

        for (int k = 0; k < numOfHashfunctions; k++)
            basePow[k] = (long) (Math.pow(bases[k], shingleLength) % Util.MODULE);

        for (int i = 0; i < numOfHashfunctions; i++) {
            for (int j = 0; j < length * 8 - shingleLength + 1; j++) {
                matrix[i][j] = basePow[i] * getBit(j);
            }
        }
        return matrix;
    }


}
