/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 10.02.15
 */

public interface BloomFilterManager {

    public void fillFilter(Entity standart);
    public double getSimilarity(Entity e1);
}
