/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 13.02.15
 */

public class MyBitArray {
    private byte[] bytes;
    private int length;

    public MyBitArray(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length for BitArray must be positive");
        } else {
            this.length = length;
            this.bytes = new byte[(length + 7) / 8];
        }
    }

    public MyBitArray(int length, byte[] bytes) {
        if (length < 0) {
            throw new IllegalArgumentException("Length for BitArray must be positive");
        } else if (bytes.length * 8 < length) {
            throw new IllegalArgumentException("Byte array too short to represent bit array of given length");
        } else {
            this.length = length;
            int var3 = (length + 8 - 1) / 8;
            int var4 = var3 * 8 - length;
            byte var5 = (byte) (255 << var4);
            this.bytes = new byte[var3];
            System.arraycopy(bytes, 0, this.bytes, 0, var3);
            if (var3 > 0) {
                this.bytes[var3 - 1] &= var5;
            }
        }
    }

    public MyBitArray(boolean[] booleans) {
        this.length = booleans.length;
        this.bytes = new byte[(this.length + 7) / 8];

        for (int i = 0; i < this.length; ++i) {
            this.set(i, booleans[i]);
        }
    }

    public void set(int i, boolean bool) throws ArrayIndexOutOfBoundsException {
        if (i >= 0 && i < this.length) {
            int var3 = subscript(i);
            int var4 = position(i);
            if (bool) {
                this.bytes[var3] = (byte) (this.bytes[var3] | var4);
            } else {
                this.bytes[var3] = (byte) (this.bytes[var3] & ~var4);
            }

        } else {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(i));
        }
    }

    public boolean get(int i) throws ArrayIndexOutOfBoundsException {
        if (i >= 0 && i < this.length) {
            return (this.bytes[subscript(i)] & position(i)) != 0;
        } else {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(i));
        }
    }

    public boolean[] toBooleanArray() {
        boolean[] var1 = new boolean[this.length];

        for (int var2 = 0; var2 < this.length; ++var2) {
            var1[var2] = this.get(var2);
        }

        return var1;
    }

    public int length() {
        return this.length;
    }


    public byte[] toByteArray() {
        return (byte[]) this.bytes.clone();
    }

    private static int subscript(int var0) {
        return var0 / 8;
    }

    private static int position(int var0) {
        return 1 << 7 - var0 % 8;
    }

    public int hashCode() {
        int var1 = 0;
        for (int var2 = 0; var2 < this.bytes.length; ++var2) {
            var1 = 31 * var1 + this.bytes[var2];
        }
        return var1 ^ this.length;
    }

}
