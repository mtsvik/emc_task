import java.security.NoSuchAlgorithmException;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public class HashDistance implements DistanceCalculator {
    private BloomFilterManager bloomFilterManager;

    public HashDistance(BloomFilterManager bloomFilterManager) {
        this.bloomFilterManager = bloomFilterManager;
    }

    @Override
    public double getDistance(Entity e1, Entity e2) {
        return 0;
    }

    @Override
    public double getSimilarity(Entity e1, Entity e2) {
        return 0;
    }

    @Override
    public double getSimilarity(Entity e1) throws NoSuchAlgorithmException {
        return bloomFilterManager.getSimilarity(e1);
    }
}
