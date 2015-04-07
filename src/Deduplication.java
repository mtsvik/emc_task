import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class Deduplication {
    private Data srcData;
    private int segmentSizeByte;
    private DistanceCalculator calculator;
    private int similarity;
    private ArrayList<Entity> cuttedData;
    private ArrayList<Entity> uniq;
    private ArrayList<Entity> difs;
    private int bufferSize = 1000;
    private int bufferSizeIndex = bufferSize;
    private MetaData meta;

    public Deduplication(Data srcData, DistanceCalculator calculator, int bytes, int similarity) {
        this.srcData = srcData;
        this.segmentSizeByte = bytes;
        this.calculator = calculator;
        this.similarity = similarity;
    }

    public ArrayList<Entity> cutData() throws IOException {
        cuttedData = new ArrayList<>();
        byte[] buffData = new byte[segmentSizeByte];
        int end = (int) Util.mod(srcData.getData().length, segmentSizeByte);
        for (int i = 0, k = 0; i < srcData.getData().length; i++) {
            buffData[k] = srcData.getData()[i];
            k++;
            if (k == segmentSizeByte) {
                Entity binaryCode = new Segment(buffData);
                cuttedData.add(binaryCode);
                buffData = new byte[segmentSizeByte];
                k = 0;
            }
        }
        if (end != 0) {
            buffData = new byte[end];
            int k = srcData.getData().length - end;
            for (int i = 0; i < end; i++) {
                buffData[i] = srcData.getData()[k];
                k++;
            }
            Entity binaryCode = new Segment(buffData);
            cuttedData.add(binaryCode);
        }
        return cuttedData;
    }

    public void deduplication() {
        double same = 0;
        int counter = 0;
        int bufferCounter = 1;
        meta = new MetaData();
        List<Entity> buffer;
        uniq = new ArrayList<>();
        difs = new ArrayList<>();

        while (counter + 1 != cuttedData.size()) {
            if (counter == 0) {
                buffer = new ArrayList<>(cuttedData.subList(0, bufferSize));
                for (int i = 0; i < buffer.size(); i++) {
                    counter = i;
                    if (buffer.get(i).isClear()) continue;
                    uniq.add(buffer.get(i));
                    meta.add(i, uniq.size() - 1, -1);
                    for (int j = i + 1; j < buffer.size(); j++) {
                        if (buffer.get(j).isClear()) continue;
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
                    bufferSize = cuttedData.size() - counter - 1;
                }
                buffer = new ArrayList<>(cuttedData.subList(counter + 1, bufferSize + counter + 1));
                for (int i = 0; i < buffer.size(); i++) {
                    counter++;
                    for (int j = 0; j < uniq.size(); j++) {
                        if (calculator.getSimilarity(buffer.get(i), uniq.get(j)) >= similarity) {
                            difs.add(buffer.get(i));
                            if (meta.add(bufferSizeIndex * bufferCounter + i, j, difs.size() - 1) != null)
                                System.out.println(counter);
                            same++;
                            break;
                        } else if (j == (uniq.size() - 1)) {
                            uniq.add(buffer.get(i));
                            if (meta.add(bufferSizeIndex * bufferCounter + i, uniq.size() - 1, -1) != null)
                                System.out.println("    " + counter);
                            break;
                        }
                    }
                }
                bufferCounter++;
            }
        }
        double rate = Math.round((same / cuttedData.size()) * 100.0) / 100.0;
        System.out.println("------------------" +
                "\nSame segments: " + difs.size() +
                "\nUnique segments: " + uniq.size() +
                "\nMeta segments: " + meta.size() +
                "\nDeduplication rate: " + rate);
    }

    public ArrayList<Entity> getUniq() {
        return uniq;
    }

    public ArrayList<Entity> getDifs() {
        return difs;
    }

    public MetaData getMeta() {
        return meta;
    }

    public void deduplication(BloomFilterManager bfm) throws NoSuchAlgorithmException {
        double same = 0;
        int counter = 0;
        int bufferCounter = 1;
        meta = new MetaData();
        List<Entity> buffer;
        uniq = new ArrayList<>();
        difs = new ArrayList<>();

        while (counter + 1 != cuttedData.size()) {
            if (counter == 0) {
                buffer = new ArrayList<>(cuttedData.subList(0, bufferSize));
                for (int i = 0; i < buffer.size(); i++) {
                    counter = i;
                    if (buffer.get(i).isClear()) continue;
                    uniq.add(buffer.get(i));
                    meta.add(i, uniq.size() - 1, -1);
                    bfm.fillFilter(buffer.get(i));
                    for (int j = i + 1; j < buffer.size(); j++) {
                        if (buffer.get(j).isClear()) continue;
                        if (calculator.getSimilarity(buffer.get(j)) >= similarity) {
                            difs.add(buffer.get(j));
                            meta.add(j, uniq.size() - 1, difs.size() - 1);
                            buffer.get(j).clear();
                            same++;
                        }
                    }
                }
            } else {
                if ((cuttedData.size() - counter) < bufferSize) {
                    bufferSize = cuttedData.size() - counter - 1;
                }
                buffer = new ArrayList<>(cuttedData.subList(counter + 1, bufferSize + counter + 1));
                for (int i = 0; i < buffer.size(); i++) {
                    bfm.fillFilter(buffer.get(i));
                    counter++;
                    for (int j = 0; j < uniq.size(); j++) {
                        if (calculator.getSimilarity(uniq.get(j)) >= similarity) {
                            difs.add(buffer.get(i));
                            if (meta.add(bufferSizeIndex * bufferCounter + i, j, difs.size() - 1) != null)
                                System.out.println(counter);
                            same++;
                            break;
                        } else if (j == (uniq.size() - 1)) {
                            uniq.add(buffer.get(i));
                            if (meta.add(bufferSizeIndex * bufferCounter + i, uniq.size() - 1, -1) != null)
                                System.out.println("    " + counter);
                            break;
                        }
                    }
                }
                bufferCounter++;
            }
        }
        double rate = Math.round((same / cuttedData.size()) * 100.0) / 100.0;
        System.out.println("------------------" +
                "\nSame segments: " + difs.size() +
                "\nUnique segments: " + uniq.size() +
                "\nMeta segments: " + meta.size() +
                "\nDeduplication rate: " + rate);
    }
}
