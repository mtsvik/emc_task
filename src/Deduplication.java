import java.util.ArrayList;
import java.util.List;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Deduplication {
    private Data srcData;
    private int segmentBytes;
    private DistanceCalculator calculator;
    private int similarity;
    private ArrayList<Entity> cuttedData;
    private List<Entity> uniq;
    private List<Entity> difs;
    private List<Entity> buffer;
    private int bufferSize = 6000;
    private MetaData meta;

    public Deduplication(Data srcData, DistanceCalculator calculator, int bytes, int similarity) {
        this.srcData = srcData;
        this.segmentBytes = bytes;
        this.calculator = calculator;
        this.similarity = similarity;
    }

    public ArrayList<Entity> cutData() {
        cuttedData = new ArrayList<>();
        byte[] buffData = new byte[segmentBytes];
        for (int i = 0, k = 0; i < srcData.getData().length; i++) {
            buffData[k] = srcData.getData()[i];
            k++;
            if (k == segmentBytes) {
                Entity binaryCode = new Segment(buffData);
                cuttedData.add(binaryCode);
                buffData = new byte[segmentBytes];
                k = 0;
            }
        }
        return cuttedData;
    }


    /**
     * segmentsNumber - число сегментов, с которыми сравнивается эталонный сегмент.
     * В первом цикле после первой итерации происходит прыжок (выбор следующего эталонного сегмента
     * через 1/30 от общего количества сегментов).
     */

    public void deduplication() {
        double same = 0;
        int counter = 0;
        meta = new MetaData();
        buffer = new ArrayList<>(bufferSize);
        uniq = new ArrayList<>();
        difs = new ArrayList<>();
        while (counter != cuttedData.size()) {
            if (counter == 0) {
                buffer = cuttedData.subList(counter, bufferSize);
                for (int i = 0; i < buffer.size(); i++) {
                    counter++;
                    if (buffer.get(i).getByteArray() == null) continue;
                    uniq.add(buffer.get(i));
                    meta.add(i, uniq.size() - 1, -1);
                    for (int j = i + 1; j < buffer.size(); j++) {
                        if (buffer.get(j).getByteArray() == null) continue;
                        if (calculator.getSimilarity(buffer.get(i), buffer.get(j)) >= similarity) {
                            difs.add(buffer.get(j));
                            meta.add(j, uniq.size() - 1, difs.size() - 1);
                            buffer.get(j).clear();
                            same++;
                        }
                    }
                }
            } else {
                if ((cuttedData.size() - counter) < bufferSize) {
                    bufferSize = cuttedData.size() - counter;
                }
                buffer = cuttedData.subList(counter, bufferSize + counter);
                System.out.println(uniq.size());
                System.out.println(difs.size());
                System.out.println(meta.getMetaData().size());
                System.out.println();
                for (int i = 0; i < buffer.size(); i++) {
                    counter++;
                    for (int j = 0; j < uniq.size(); j++) {
                        if (calculator.getSimilarity(buffer.get(i), uniq.get(j)) >= similarity) {
                            difs.add(buffer.get(i));
                            meta.add(counter + i, j, difs.size() - 1);
                            same++;
                            break;
                        }
                        if (j == (uniq.size() - 1)) {
                            uniq.add(buffer.get(i));
                            meta.add(counter + i, uniq.size() - 1, -1);
                            break;
                        }
                    }
                }
            }
        }
        double perc = Math.round((same / cuttedData.size()) * 100.0);
        System.out.println("Same segments: " + same + "\nPercent: " + perc);
    }

    public List<Entity> getUniq() {
        return uniq;
    }

    public List<Entity> getDifs() {
        return difs;
    }

    public MetaData getMeta() {
        return meta;
    }

    //    public void deduplication(BloomFilterManager bfm) {
//        metaData = new ArrayList<>();
//        double same = 0;
//        for (int i = 0; i < cuttedData.size(); i+= cuttedData.size() / 10) {
//            if (cuttedData.get(i).getByteArray() == null) continue;
//            bfm.fillFilter(cuttedData.get(i));
//            for (int j = i + 1; j < cuttedData.size(); j++) {
//                if (cuttedData.get(j).getByteArray() == null) continue;
//                if (calculator.getSimilarity(cuttedData.get(j)) >= similarity) {
//                    metaData.add(cuttedData.get(j));
//                    cuttedData.get(j).clear();
//                    same++;
//                }
//            }
//        }
//        double perc = Math.round((same / cuttedData.size()) * 100.0);
//        System.out.println("Same segments: " + same + "\nPercent: " + perc);
//    }
}
