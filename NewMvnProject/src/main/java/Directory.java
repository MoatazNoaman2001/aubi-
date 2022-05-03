import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Directory extends Directory_Entry {
    private ArrayList<Directory_Entry> entries;
    private Directory_Entry parent;

    public Directory(String fileName, byte fileAttr, byte[] fileData, int fileSize, int fileCluster, ArrayList<Directory_Entry> entries, Directory_Entry parent) {
        super(fileName, fileAttr, fileSize, fileCluster);
        this.entries = entries;
        this.parent = parent;
    }

    boolean WriteDirectory() throws IOException {
        List<Byte> bytes = new LinkedList<>();
        for (Directory_Entry entry : entries) {
            for (byte aByte : entry.getBytes()) {
                bytes.add(aByte);
            }
        }
//        new VirtualDiskImpl().WriteFateTable();
        VirtualDiskImpl vvd = new VirtualDiskImpl();
        for (int i = 0; i < bytes.size(); i+= 1024) {
            byte[] bb = new byte[1024];
            for (int i1 = 0; i1 < bytes.subList(i, i + 1024).size(); i1++) {
                bb[i1] = bytes.subList(i, i + 1024).get(i1);
            }
            vvd.setBytes(i / 1024 ,bb);
        }
        return false;
    }

    void ReadDirectory() throws IOException {
        byte [] allbytes = new VirtualDiskImpl().getBytes();
        for (int i = 0; i < allbytes.length; i+= 32) {
            Directory_Entry entry = new Directory_Entry(Arrays.copyOfRange(allbytes , i , i +32));
            if (!entries.contains(entry))entries.add(entry);
        }
    }

    int SearchDirectory() {
        return -1;
    }

    boolean updateDirectory() {
        return false;
    }
}
