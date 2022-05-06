import java.io.IOException;
import java.util.*;

public class Directory extends Directory_Entry {
    private ArrayList<Directory_Entry> entries;
    private final Directory_Entry parent;

    public Directory(String fileName, byte fileAttr, int fileSize, int fileCluster, ArrayList<Directory_Entry> entries, Directory_Entry parent) throws IOException {
        super(fileName, fileAttr, fileSize, fileCluster);
        this.entries = entries;
        this.parent = parent;
    }

    public Directory(ArrayList<Directory_Entry> entries, Directory_Entry parent) throws IOException {
        super(parent.getFileName(), parent.getFileAttr(), parent.getFileSize(), parent.getFileCluster());
        this.entries = entries;
        this.parent = parent;
    }

    public ArrayList<Directory_Entry> getEntries() {
        return entries;
    }

    void WriteDirectory(Fat fat) throws IOException {
        VirtualDiskImpl vvd = fat.getDisk();
        List<Byte> bytes = new LinkedList<>();
        if (fat.getAvailableBlocks() == 0) {
            System.out.println("no enough space");
            return;
        }
        if (this.getFileName().equals("root")) {
            byte[] bs = getBytes();
            vvd.setBytes(0, bs);
            vvd.SetNext(0, 1);
            fat.setAvailableBlock(0 , 1);
        } else {
            this.setFileSize(getBytes().length + parent.getBytes().length);
            int count = 0;
            for (byte aByte : vvd.getBytes(fat.getAvailableBlock() - 1)) {
                if (aByte == 0) count++;
            }
            if (getFileSize() < 1024) {
                if (count >= getFileSize()) {
                    setFileCluster(fat.getAvailableBlock() -1);
                } else {
                    setFileCluster(fat.getAvailableBlock());
                }
            } else {
                if (count >= getFileSize() % 1024) {
                    setFileCluster(fat.getAvailableBlock() - 1);
                } else {
                    setFileCluster(fat.getAvailableBlock());
                }
            }
            this.setFileCluster(fat.getAvailableBlock());
            for (byte aByte : getBytes()) {
                bytes.add(aByte);
            }
            for (byte b : "Parent: ".getBytes()) {
                bytes.add(b);
            }
            for (byte aByte : parent.getBytes()) {
                bytes.add(aByte);
            }
            for (byte b : " :p>".getBytes()) {
                bytes.add(b);
            }
            if (bytes.size() < 1024) {
                byte[] arr = new byte[1024];
                for (Byte aByte : bytes) {
                    arr[bytes.indexOf(aByte)] = aByte;
                }
                vvd.setBytes(getFileCluster(), arr);
                vvd.SetNext(getFileCluster() , getFileCluster() +1);
            } else {
                if (fat.getAvailableBlock() - 1 == getFileCluster()) {
                    int numOfBlocks = (bytes.size() / 1024) + (bytes.size() % 1024);
                    if (fat.getAvailableBlocks() > numOfBlocks) {
                        for (int i = 0; i < bytes.size(); i += 1024) {
                            byte[] bb = new byte[1024];
                            if (bytes.size() < i + 1024) {
                                for (int j = (bytes.size() / 1024) * 1024, o = 0; j < bytes.size()
                                        ; j++ , o++) {
                                    bb[o] = bytes.subList( j , bytes.size()).get(j);
                                }
                                vvd.setBytes(getFileCluster(), bb);
                                vvd.SetNext(getFileCluster(), vvd.getAvailableBlock() + 1);
                                fat.setAvailableBlock(getFileCluster() , -1);
                            } else {
                                for (int i1 = 0; i1 < bytes.subList(i, i + 1024).size(); i1++) {
                                    bb[i1] = bytes.subList(i, i + 1024).get(i1);
                                }
                                vvd.setBytes(vvd.getAvailableBlock(), bb);
                                vvd.SetNext(vvd.getAvailableBlock(), vvd.getAvailableBlock() + 1);
                                fat.setAvailableBlock(vvd.getAvailableBlock(), vvd.getAvailableBlock() +1);
                            }
                        }
                    } else {
                        System.out.println("no enough space for " + numOfBlocks + " of blocks");
                    }
                }
            }
        }
    }

    void ReadDirectory(VirtualDiskImpl vvd) throws IOException {
        byte[] allbytes = vvd.getBytes();
        Directory_Entry par = null;
        byte[] parr = null;
        ArrayList<Directory_Entry> arrayList = new ArrayList<>();
        Directory_Entry entry = new Directory_Entry(Arrays.copyOfRange(allbytes , 0 , 32));
        if (Arrays.equals(Arrays.copyOfRange(allbytes, 32, "Parent: ".getBytes().length), "Parent: ".getBytes())){
            parr = Arrays.copyOfRange(allbytes , 32 + "Parent: ".getBytes().length
            , 32 * 2 + "Parent: ".getBytes().length );
            par = new Directory_Entry(parr);
        }
        int start = 32 * 2 + "Parent: ".getBytes().length + " :p>".getBytes().length;
        if (Arrays.equals(Arrays.copyOfRange(allbytes, start, "list: ".getBytes().length), "list: ".getBytes())){
            parr = Arrays.copyOfRange(allbytes , start, start + " :l>".getBytes().length);
            for (int i = 0; i < parr.length; i+=32) {
                arrayList.add(new Directory_Entry(Arrays.copyOfRange(parr , i , i +32)));
            }
            par = new Directory_Entry(parr);
        }
        entries = arrayList;
        Directory directory = new Directory(entry.getFileName() , entry.getFileAttr() , entry.getFileSize() , entry.getFileCluster()
         , arrayList,par);
    }

    int SearchDirectory(Directory_Entry entry) throws IOException {
        ReadDirectory(new VirtualDiskImpl());
        return entries.stream().filter(entry1 -> entry1.equals(entry)).findFirst().map(entries::indexOf).orElse(-1);
    }

    boolean updateDirectory() throws IOException {
//        WriteDirectory(fat);
        return false;
    }
}
