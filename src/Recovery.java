import java.util.ArrayList;

/**
* Author: Mikhail Tsvik (tsvik@me.com)
* Date: 11.03.15
*/

public class Recovery {
    private MetaData meta;
    private ArrayList<Entity> uniq;
    private ArrayList<Entity> difs;
    private byte[] data;

    public Recovery(MetaData meta, ArrayList<Entity> uniq, ArrayList<Entity> difs, int fileSizeBytes) {
        this.meta = meta;
        this.uniq = uniq;
        this.difs = difs;
        data = new byte[fileSizeBytes];
    }




}
