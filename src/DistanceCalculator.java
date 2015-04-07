import java.security.NoSuchAlgorithmException;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 02.02.15
 */

public interface DistanceCalculator {
    double getDistance(Entity e1, Entity e2);
    double getSimilarity(Entity e1, Entity e2);
    double getSimilarity(Entity e1) throws NoSuchAlgorithmException;
}
