import sun.security.util.BitArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 02.02.15
 */

public class FileConverter {

    public static byte[] fileToBytes(File file) throws IOException {
        byte[] byteArray = new byte[(int) file.length()];
        Path path = file.toPath();
        byteArray = Files.readAllBytes(path);
            return byteArray;
        }

    public static BitArray fileToBits(File file) throws IOException {
        BitArray bitArray = new BitArray((int) file.length() * 8);
        try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file))) {
            for (int b, i = 0; (b = is.read()) != -1; i = i + 8) {
                String s = "0000000" + Integer.toBinaryString(b);
                s = s.substring(s.length() - 8);
                for (int k = i, j = 0; k < i + 8; k++, j++) {
                    bitArray.set(k, s.charAt(j) != '0');
                }
            }
            return bitArray;
        }
    }

}
