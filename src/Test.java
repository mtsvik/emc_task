import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Test {

    public static void main(String[] args) throws IOException {
//        test();

        File file = new File("/Users/mtsvik/file1.vmdk");
        int byteSegment = 64;
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
        System.out.println("Data deduplicated: " + result + " sec");
    }

//    public static void test() {
//        int byteSegment = 3;
//        int similarity = 70;
//        int shingleLength = 4;
////        Entity segment = new Segment(DataGenerator.generateSegment(8*1024));
//        BloomFilterManager bloomFilterManager = new MyBloomFilterManager(shingleLength);
//        DistanceCalculator calculator = new HashDistance(bloomFilterManager);
//        Data data = new Data(new byte[] {98, 'S', 'a', 98, 's', 'b', 96, 'T', 'a', 98, 'S', 'a', 98, 21, 12, 98, 20, 13, 98, 21, 11, 98, 'S', 'a', 98, 'S', 'a'});
//        Deduplication deduplication = new Deduplication(data, calculator, byteSegment, similarity);
//        ArrayList<Entity> array = deduplication.cutData();
//        deduplication.deduplication(bloomFilterManager);
//    }
}
