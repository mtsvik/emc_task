import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Test {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/mtsvik/file1.vmdk");
        int bitsSegment = 512;
        int similarity = 75;
        int shingleLength = 16;

        System.out.println("Creating data from file...");
        long time1 = System.currentTimeMillis();
        Data data = new Data(FileConverter.fileToBits(file));
//        Data data = DataGenerator.generate(100000);
        long time2 = System.currentTimeMillis();
        double result = (time2 - time1) / 1000.0;
        System.out.println("Data created: " + result + " sec");
        System.out.println("Data size: " + data.getSize() / 8.0 / 1024.0 / 1024.0 + "MB\n");

        BloomFilterManager bfm = new MyBloomFilterManager(shingleLength);
        DistanceCalculator calculator = new HashDistance(bfm);
//        DistanceCalculator calculator = new HammingDistance();
//        DistanceCalculator calculator = new EuclideanDistance();
        Deduplication deduplication = new Deduplication(data, calculator, bitsSegment, similarity);

        System.out.println("Cutting data on segments...");
        time1 = System.currentTimeMillis();
        ArrayList<Entity> array = deduplication.cut();
        time2 = System.currentTimeMillis();
        result = (time2 - time1) / 1000.0;
        System.out.println("Data cutted: " + result + " sec");
        System.out.println("Segment size: " + bitsSegment / 8.0 / 1024.0 + "KB");
        System.out.println("Segments number: " + array.size() + "\n");

        System.out.println("Deduplicating data...");
        System.out.println("Similarity: " + similarity + "%");
        time1 = System.currentTimeMillis();
//        deduplication.deduplication();
        deduplication.smartDeduplication(bfm);
        time2 = System.currentTimeMillis();
        result = (time2 - time1) / 1000.0;
        System.out.println("Data deduplicated: " + result + " sec");

    }

}
