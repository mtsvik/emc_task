import sun.security.util.BitArray;

import java.util.ArrayList;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Deduplication {
    private Data srcData;
    private int bits;
    private ArrayList<Entity> physicalData;
    private ArrayList<Entity> metaData;
    private DistanceCalculator calculator;
    private double[] distanceMatrix;
    private int similarity;

    public Deduplication(Data srcData, DistanceCalculator calculator, int bits, int similarity) {
        this.srcData = srcData;
        this.bits = bits;
        this.calculator = calculator;
        this.similarity = similarity;
    }

    public ArrayList<Entity> cut() {
        physicalData = new ArrayList<>();
        BitArray allData = srcData.getData();
        BitArray buffData = new BitArray(bits);
        int k = 0;
        for (int i = 0; i < allData.length(); i++) {
            buffData.set(k, allData.get(i));
            k++;
            if (k == bits) {
                Entity binaryCode = new Segment(buffData);
                physicalData.add(binaryCode);
                buffData = new BitArray(bits);
                k = 0;
            }
        }
        return physicalData;
    }

    /**
     * segmentsNumber - число сегментов, с которыми сравнивается эталонный сегмент.
     * В первом цикле после первой итерации происходит прыжок (случайный выбор следующего эталонного сегмента, из
     * промежутка от 1/60 до 1/30 от общего количества сегментов).
     */

    public void deduplication() {
        metaData = new ArrayList<>();
        double same = 0;
        for (int i = 0; i < physicalData.size(); i += physicalData.size() / 60 + Math.random() * physicalData.size() / 30) {
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

    public void smartDeduplication(BloomFilterManager bfm) {
        metaData = new ArrayList<>();
        double same = 0;
        for (int i = 0; i < physicalData.size();  i += physicalData.size() / 30 + Math.random() * physicalData.size() / 10) {
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
