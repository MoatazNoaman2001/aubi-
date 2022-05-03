import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Directory_Entry {
    private String fileName;
    private byte fileAttr;
    private byte[] fileData;
    private int fileSize;
    private int fileCluster;

    public Directory_Entry(String fileName, byte fileAttr, int fileSize, int fileCluster) {
        if (fileName.getBytes().length > 11) {
            String s1 = "", NewName = "";
            try {
                s1 = fileName.substring(fileName.lastIndexOf('.'));
            } catch (Exception e) {
                NewName = fileName.substring(0, 11);
            }
            NewName = fileName.substring(0, 11 - s1.length());
            fileName = NewName;
            fileName+= s1;
        }
        this.fileName = fileName;
        this.fileAttr = fileAttr;
        this.fileSize = fileSize;
        this.fileData = new byte[12];
        this.fileCluster = fileCluster;
    }

    public Directory_Entry(byte[] bytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(bytes , 0 ,11));
        this.fileName = new String(Arrays.copyOfRange(bytes , 0 , 11), StandardCharsets.UTF_8);
        this.fileAttr = bytes[12];
        this.fileData = new byte[12];
        this.fileSize = ByteBuffer.wrap(Arrays.copyOfRange(bytes , 24, 28)).getInt();
        this.fileCluster = ByteBuffer.wrap(Arrays.copyOfRange(bytes , 28 , 32)).getInt();
    }


    public byte[] getBytes(){
        Directory_Entry entry = this;
        byte[] totalBytes= new byte[32];
        System.arraycopy(entry.fileName.getBytes(), 0, totalBytes, 0, entry.fileName.getBytes().length);
        totalBytes[12] = entry.fileAttr;
        System.arraycopy(entry.fileData, 0, totalBytes, 12, entry.fileData.length);
        byte[] fileSizeBytes = ByteBuffer.allocate(4).putInt(entry.fileSize).array();
        System.arraycopy(fileSizeBytes , 0 , totalBytes ,24,  fileSizeBytes.length);
        byte[] fileClusterBytes = ByteBuffer.allocate(4).putInt(entry.fileSize).array();
        System.arraycopy(fileClusterBytes , 0 , totalBytes ,24,  fileClusterBytes.length);
        return totalBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte getFileAttr() {
        return fileAttr;
    }

    public void setFileAttr(byte fileAttr) {
        this.fileAttr = fileAttr;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileCluster() {
        return fileCluster;
    }

    public void setFileCluster(int fileCluster) {
        this.fileCluster = fileCluster;
    }

    @Override
    public String toString() {
        return "Directory_Entry{" +
                "fileName='" + fileName + '\'' +
                ", fileAttr=" + fileAttr +
                ", fileData=" + Arrays.toString(fileData) +
                ", fileSize=" + fileSize +
                ", fileCluster=" + fileCluster +
                '}';
    }
}
