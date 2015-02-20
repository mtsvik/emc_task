import java.util.ArrayList;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Deduplication {
    private Data srcData;
    private int segmentBytes;
    private ArrayList<Entity> physicalData;
    private ArrayList<Entity> metaData;
    private DistanceCalculator calculator;
    private int similarity;

    public Deduplication(Data srcData, DistanceCalculator calculator, int bytes, int similarity) {
        this.srcData = srcData;
        this.segmentBytes = bytes;
        this.calculator = calculator;
        this.similarity = similarity;
    }

    public ArrayList<Entity> cutData() {
        physicalData = new ArrayList<>();
        byte[] buffData = new byte[segmentBytes];
        for (int i = 0, k = 0; i < srcData.getData().length; i++) {
            buffData[k] = srcData.getData()[i];
            k++;
            if (k == segmentBytes) {
                Entity binaryCode = new Segment(buffData);
                physicalData.add(binaryCode);
                buffData = new byte[segmentBytes];
                k = 0;
            }
        }
        return physicalData;
    }

    /**
     * segmentsNumber - число сегментов, с которыми сравнивается эталонный сегмент.
     * В первом цикле после первой итерации происходит прыжок (выбор следующего эталонного сегмента
     * через 1/30 от общего количества сегментов).
     */

    public void deduplication() {
        metaData = new ArrayList<>();
        double same = 0;
        for (int i = 0; i < physicalData.size(); i += physicalData.size() / 30) {
            if (physicalData.get(i).getByteArray() == null) continue;
            for (int j = i + 1; j < physicalData.size(); j++) {
                if (physicalData.get(j).getByteArray() == null) continue;
                if (calculator.getSimilarity(physicalData.get(i), physicalData.get(j)) >= similarity) {
                    metaData.add(physicalData.get(j));
                    physicalData.get(j).clear();
                    same++;
                }
            }
        }
        double perc = Math.round((same / physicalData.size()) * 100.0);
        System.out.println("Same segments: " + same + "\nPercent: " + perc);
    }

    public void deduplication(BloomFilterManager bfm) {
        metaData = new ArrayList<>();
        double same = 0;
        for (int i = 0; i < physicalData.size(); i+= physicalData.size() / 10) {
            if (physicalData.get(i).getByteArray() == null) continue;
            bfm.fillFilter(physicalData.get(i));
            for (int j = i + 1; j < physicalData.size(); j++) {
                if (physicalData.get(j).getByteArray() == null) continue;
                if (calculator.getSimilarity(physicalData.get(j)) >= similarity) {
                    metaData.add(physicalData.get(j));
                    physicalData.get(j).clear();
                    same++;
                }
            }
        }
        double perc = Math.round((same / physicalData.size()) * 100.0);
        System.out.println("Same segments: " + same + "\nPercent: " + perc);
    }
}
