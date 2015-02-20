import sun.security.util.BitArray;

import java.io.IOException;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Test {

    public static void main(String[] args) throws IOException {
        test();

//        File file = new File("/Users/mtsvik/file1.vmdk");
//        int byteSegment = 64;
//        int similarity = 75;
//        int shingleLength = 16;
//
//        System.out.println("Creating data from file...");
//        long time1 = System.currentTimeMillis();
//        Data data = new Data(FileConverter.fileToBytes(file));
////        Data data = DataGenerator.generate(100000);
//        long time2 = System.currentTimeMillis();
//        double result = (time2 - time1) / 1000.0;
//        System.out.println("Data created: " + result + " sec");
//        System.out.println("Data size: " + data.getSize() / 1024 / 1024 + "MB\n");
//
//        BloomFilterManager bfm = new GoogleBloomFilterManager(shingleLength);
//        DistanceCalculator calculator = new HashDistance(bfm);
////        DistanceCalculator calculator = new HammingDistance();
////        DistanceCalculator calculator = new EuclideanDistance();
//        Deduplication deduplication = new Deduplication(data, calculator, byteSegment, similarity);
//
//        System.out.println("Cutting data on segments...");
//        time1 = System.currentTimeMillis();
//        ArrayList<Entity> array = deduplication.cutData();
//        time2 = System.currentTimeMillis();
//        result = (time2 - time1) / 1000.0;
//        System.out.println("Data cutted: " + result + " sec");
//        System.out.println("Segment size: " + byteSegment / 1024.0 + "KB");
//        System.out.println("Segments number: " + array.size() + "\n");
//
//        System.out.println("Deduplicating data...");
//        System.out.println("Similarity: " + similarity + "%");
//        time1 = System.currentTimeMillis();
////        deduplication.deduplication();
//        deduplication.deduplication(bfm);
//        time2 = System.currentTimeMillis();
//        result = (time2 - time1) / 1000.0;
//        System.out.println("Data deduplicated: " + result + " sec");
    }

    public static void test() {
        int shingleLength = 64*8;
        Entity segment = new Segment(DataGenerator.generateSegment(8*1024));
//        Entity segment = new Segment(new byte[] {127, 'S'});
        MyBloomFilter<Shingle> bf = new MyBloomFilter<>(segment.getLength() * 8 - shingleLength + 1, 0.01);
        fillFilter(segment, bf, shingleLength);
    }

    public static void fillFilter(Entity standart, MyBloomFilter<Shingle> bloomFilter, int shingleLength) {
        BitArray buffer = new BitArray(shingleLength);
        BitArray standartSegment = new BitArray(standart.getLength() * 8, standart.getByteArray());
        for (int i = 0; i < standartSegment.length() - shingleLength + 1; i++) {
            for (int k = 0, counter = k + i; k < shingleLength; k++) {
                buffer.set(k, standartSegment.get(counter));
                counter++;
            }
            Shingle shingle = new Shingle(buffer, shingleLength, i);
            bloomFilter.put(shingle, standart);
            buffer = new BitArray(shingleLength);
        }
    }

}
