import sun.security.util.BitArray;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 02.02.15
 */

public class EuclideanDistance implements DistanceCalculator {

    @Override
    public double getDistance(Entity e1, Entity e2) {
        double summ = 0;
        BitArray segment1 = new BitArray(e1.getLength() * 8, e1.getByteArray());
        BitArray segment2 = new BitArray(e2.getLength() * 8, e2.getByteArray());
        for (int i = 0; i < segment1.length(); i++) {
            summ += Math.pow((segment1.get(i) == segment2.get(i) ? 0 : 1), 2);
        }
        return Math.sqrt(summ);
    }

    @Override
    public double getSimilarity(Entity e1, Entity e2) {
        double max = Math.sqrt(e1.getLength() * 8);
        double dist = getDistance(e1, e2);
        return Math.round((1 - (dist / max) * (0.5)) * 100);
    }

    @Override
    public double getSimilarity(Entity e1) {
        return 0;
    }
}
