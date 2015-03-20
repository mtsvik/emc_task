import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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

    public static boolean compareFiles(String f1, String f2) throws IOException {
        byte[] file1 = fileToBytes(new File(f1));
        byte[] file2 = fileToBytes(new File(f2));
        return Arrays.equals(file1, file2);
    }

}
