public class File_Entry extends Directory_Entry{
    private Directory parent;
    private String content;

    public File_Entry(String fileName, byte fileAttr, byte[] fileData, int fileSize, int fileCluster, Directory parent) {
        super(fileName, fileAttr, fileSize, fileCluster);
        this.parent = parent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    void ReadFile(){}
    void WriteFile(){}
    void DeleteFile(){}
}
