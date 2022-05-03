public class File_Entry extends Directory_Entry{
    private Fat fat;
    private Directory parent;

    public File_Entry(String fileName, byte fileAttr, byte[] fileData, int fileSize, int fileCluster, Fat fat, Directory parent) {
        super(fileName, fileAttr, fileSize, fileCluster);
        this.fat = fat;
        this.parent = parent;
    }

    void ReadFile(){}
    void WriteFile(){}
    void DeleteFile(){}
}
