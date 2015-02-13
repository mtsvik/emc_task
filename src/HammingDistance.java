/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class HammingDistance implements DistanceCalculator {
    @Override
    public double getDistance(Entity e1, Entity e2) {
        double summ = 0;
        MyBitArray segment1 = new MyBitArray(e1.getLength() * 8, e1.getByteArray());
        MyBitArray segment2 = new MyBitArray(e2.getLength() * 8, e2.getByteArray());
        for (int i = 0; i < segment1.length(); i++) {
            if (segment1.get(i) != segment2.get(i)) summ++;
        }
        return summ;
    }

    public double getSimilarity(Entity e1, Entity e2) {
        double length = e1.getLength() * 8;
        double distance = getDistance(e1, e2);
        return Math.round(((length - distance) / length) * 100);
    }

    @Override
    public double getSimilarity(Entity e1) {
        return 0;
    }
}
