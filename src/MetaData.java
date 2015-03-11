import java.util.Hashtable;
import java.util.Map;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 11.03.15
 */

public class MetaData {
    private Map<Integer, Meta> data;

    public MetaData() {
        this.data = new Hashtable<>();
    }

    public void add(int originalIndex, int uniqIndex, int difsIndex) {
        data.put(originalIndex, new Meta(uniqIndex, difsIndex));
    }

    public Map<Integer, Meta> getMetaData() {
        return data;
    }

    static class Meta {
        static int u;
        static int d;

        public Meta(int uniq, int difs) {
            u = uniq;
            d = difs;
        }
    }
}
