import java.io.File;
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

}
