/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 01.02.15
 */

public class HammingDistance implements DistanceCalculator {
    @Override
    public double getDistance(Entity e1, Entity e2) {
        double summ = 0;
        for (int i = 0; i < e1.getLength(); i++) {
            if (e1.getBitArray().get(i) != e2.getBitArray().get(i)) summ++;
        }
        return summ;
    }

    public double getSimilarity(Entity e1, Entity e2) {
        double length = e1.getLength();
        double distance = getDistance(e1, e2);
        return Math.round(((length - distance) / length) * 100);
    }

    @Override
    public double getSimilarity(Entity e1) {
        return 0;
    }
}
