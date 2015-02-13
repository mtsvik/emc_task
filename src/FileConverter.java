import sun.security.util.BitArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 02.02.15
 */

public class FileConverter {

    public static BitArray fileToBits(File file) throws IOException {
//        StringBuilder sb = new StringBuilder();
        BitArray bitArray = new BitArray((int) file.length() * 8);
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file))) {
            for (int b, i = 0; (b = is.read()) != -1; i = i + 8) {
                String s = "0000000" + Integer.toBinaryString(b);
                s = s.substring(s.length() - 8);
                for (int k = i, j = 0; k < i + 8; k++, j++) {
                    bitArray.set(k, s.charAt(j) != '0');
                }
//                sb.append(s);
            }
            return bitArray;
        }
    }

//    public static byte[] fileToByteArray(File file) throws IOException {
//        String str = fileToBits(file);
//        byte[] array = new byte[str.length()];
//        for (int i = 0; i < str.length(); i++) {
//            array[i] = (byte) str.charAt(i);
//        }
//        return array;
//    }
//
//    public static BitArray fileToBitArray(File file) throws IOException {
//        String str = fileToBits(file);
//        boolean bool;
//        BitArray array = new BitArray(str.length());
//        for (int i = 0; i < str.length(); i++) {
//            bool = (str.charAt(i) != '0');
//            array.set(i, bool);
//        }
//        return array;
//    }

}
