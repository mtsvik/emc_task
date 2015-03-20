import java.io.FileOutputStream;
import java.io.IOException;
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

    private void fillData() {
        byte[] buffer;
        int bufferSize = difs.get(0).getLength();
        for (int i = 0; i < meta.size(); i++) {
            if (meta.getMetaData().get(i).getDifs() == -1) {
                buffer = uniq.get(meta.getMetaData().get(i).getUniq()).getByteArray();
                for (int j = (bufferSize * i), k = 0; j < buffer.length + (bufferSize * i); j++, k++) {
                    data[j] = buffer[k];
                }
            } else {
                buffer = difs.get(meta.getMetaData().get(i).getDifs()).getByteArray();
                for (int j = (bufferSize * i), k = 0; j < buffer.length + (bufferSize * i); j++, k++) {
                    data[j] = buffer[k];
                }
            }
        }
    }


    public void start() throws IOException {
        fillData();
        FileOutputStream out = new FileOutputStream("/Users/mtsvik/new_file2.vmdk");
        out.write(data);
        out.close();
    }
}
