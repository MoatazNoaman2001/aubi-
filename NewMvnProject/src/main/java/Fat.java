import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Fat {
    private int[] FAT_TABLE =  new int[1024];
    private VirtualDiskImpl disk;


    public Fat() throws IOException {
        disk = new VirtualDiskImpl();
        Arrays.fill(FAT_TABLE , 0 , 4 , -1);
        System.out.println(getNext(9));
    }

    public void Write_Fat_Table() throws IOException {
        disk.WriteFateTable(FAT_TABLE);
    }

    public int getAvailableBlocks(){
        return (int) Arrays.stream(FAT_TABLE).filter(value -> value == 0).count();
    }

    public int[] get_Fat_Table(){
        return FAT_TABLE;
    }


    public byte[] getFatTableBytes() throws IOException {
        return disk.getBytes();
    }

    public int getAvailableBlock() throws IOException {
        return IntStream.range(1 , FAT_TABLE.length)
                .filter(value -> value == 0).findFirst()
                .orElse(-1);
    }

    private int getNext(int index) throws IOException {
        return FAT_TABLE[index];
    }
    private void setNext(int index , int value) throws IOException {
        disk.SetNext(index , value);
        FAT_TABLE[index] = value;
    }
}
