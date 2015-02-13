/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 02.02.15
 */

public class EuclideanDistance implements DistanceCalculator {

    @Override
    public double getDistance(Entity e1, Entity e2) {
        double summ = 0;
        for (int i = 0; i < e1.getByteArray().length(); i++) {
            summ += Math.pow((e1.getByteArray().get(i)==e2.getByteArray().get(i)?0:1), 2);
        }
        return Math.sqrt(summ);
    }

    @Override
    public double getSimilarity(Entity e1, Entity e2) {
        double max = Math.sqrt(e1.getLength());
        double dist = getDistance(e1, e2);
        return Math.round(((1 - dist / max) * 100)*(5.0/3.0));
    }

    @Override
    public double getSimilarity(Entity e1) {
        return 0;
    }
}
