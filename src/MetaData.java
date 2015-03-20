import java.util.HashMap;
import java.util.Map;

/**
 * Author: Mikhail Tsvik (tsvik@me.com)
 * Date: 11.03.15
 */

public class MetaData {
    private Map<Integer, Entry> data;

    public MetaData() {
        this.data = new HashMap<>();
    }

    public Entry add(int originalIndex, int uniqIndex, int difsIndex) {
        return data.put(originalIndex, new Entry(uniqIndex, difsIndex));
    }

    public Map<Integer, Entry> getMetaData() {
        return data;
    }

    public int size() {
        return data.size();
    }

    class Entry {
        private int u;
        private int d;

        public Entry(int uniq, int difs) {
            this.u = uniq;
            this.d = difs;
        }

        public int getUniq() {
            return u;
        }

        public int getDifs() {
            return d;
        }

        public String toString() {
            return "uniq -> " + u + ", difs -> " + d;
        }
    }
}
