import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Test {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/mtsvik/file2.vmdk");
        int byteSegment = 16;
        int similarity = 75;
        int shingleLength = 512;

        System.out.println("Creating data from file...");
        long time1 = System.currentTimeMillis();
        Data data = new Data(FileConverter.fileToBytes(file));
//        Data data = DataGenerator.generate(100000);
        long time2 = System.currentTimeMillis();
        double result = (time2 - time1) / 1000.0;
        System.out.println("Data created: " + result + " sec");
        System.out.println("Data size: " + data.getSize() / 1024 / 1024 + "MB\n");

//        BloomFilterManager bfm = new GoogleBloomFilterManager(shingleLength);
//        BloomFilterManager bfm = new MyBloomFilterManager(shingleLength);
//        DistanceCalculator calculator = new HashDistance(bfm);
        DistanceCalculator calculator = new HammingDistance();
        Deduplication deduplication = new Deduplication(data, calculator, byteSegment, similarity);

        System.out.println("Cutting data on segments...");
        time1 = System.currentTimeMillis();
        ArrayList<Entity> array = deduplication.cutData();
        time2 = System.currentTimeMillis();
        result = (time2 - time1) / 1000.0;
        System.out.println("Data cutted: " + result + " sec");
        System.out.println("Segment size: " + byteSegment / 1024.0 + "KB");
        System.out.println("Segments number: " + array.size() + "\n");

        System.out.println("Deduplicating data...");
        System.out.println("Similarity: " + similarity + "%");
        time1 = System.currentTimeMillis();
        deduplication.deduplication();
//        deduplication.deduplication(bfm);
        time2 = System.currentTimeMillis();
        result = (time2 - time1) / 1000.0;
        System.out.println("Data deduplicated: " + result + " sec\n");

        System.out.println("Recovery data...");
        Recovery recovery = new Recovery(deduplication.getMeta(), deduplication.getUniq(), deduplication.getDifs(), (int) file.length());
        time1 = System.currentTimeMillis();
        recovery.start();
        time2 = System.currentTimeMillis();
        result = (time2 - time1) / 1000.0;
        System.out.println("Data recovered: " + result + " sec\n");

        System.out.println("Files equals: " + FileConverter.compareFiles("/Users/mtsvik/file2.vmdk","/Users/mtsvik/new_file2.vmdk"));
    }
}
