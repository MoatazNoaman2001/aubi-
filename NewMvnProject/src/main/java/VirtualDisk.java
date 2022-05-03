import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.stream.IntStream;

interface VirtualDisk {
    void create() throws IOException;

    byte[] getBytes() throws IOException;

    int getAvailableBlock() throws IOException;

    int getNext(int index) throws IOException;

    boolean SetNext(int index, int value) throws IOException;

    boolean setBytes(int index, byte[] bytes) throws IOException;

    byte[] getBytes(int index) throws IOException;
}

class VirtualDiskImpl implements VirtualDisk {
    FileOutputStream outputStreamOUT;
    FileInputStream InputStream;

    public VirtualDiskImpl() throws FileNotFoundException {
        outputStreamOUT = new FileOutputStream("C:\\Users\\Mo3taz kayad\\Desktop\\fatTable.txt");
        InputStream = new FileInputStream("C:\\Users\\Mo3taz kayad\\Desktop\\fatTable.txt");
    }

    @Override
    public void create() throws IOException {
        byte[][] bytes = new byte[1024][1024];
        outputStreamOUT.write(bytes[0]);
        int[] n2 = new int[1024];
        Arrays.fill(n2, 0, n2.length, '*');
        for (int j : n2) {
            outputStreamOUT.write(ByteBuffer.allocate(4).putInt(j).array());
        }
        for (int i = 5; i < 1024; i++) {
            outputStreamOUT.write(bytes[i]);
        }
        outputStreamOUT.close();

    }

    @Override
    public byte[] getBytes() throws IOException {
        return Arrays.copyOfRange(InputStream.readAllBytes(), 1024 * 5, InputStream.readAllBytes().length);
    }

    @Override
    public int getAvailableBlock() throws IOException {
        byte[] bytes = InputStream.readAllBytes();
        byte[] FatTable = Arrays.copyOfRange(bytes, 1024, 1024 * 5);
        int[] FatTableIntArr = new int[FatTable.length / 4];
        int i = 0;
        int j = 0;
        while (i <= FatTable.length - 1) {
            FatTableIntArr[j] = ConvertToInt(FatTable[i], FatTable[i + 1], FatTable[i + 2], FatTable[i + 3]);
            j++;
            i += 4;
        }
        return IntStream.range(0, FatTableIntArr.length).filter(k -> FatTableIntArr[k] == 42).findFirst().orElse(-1);
    }

    @Override
    public int getNext(int index) throws IOException {
        if (index > 1024)
            return -1;
        byte[] bytes = InputStream.readAllBytes();
        byte[] FatTable = Arrays.copyOfRange(bytes, 1024, 1024 * 5);
        int[] FatTableIntArr = new int[FatTable.length / 4];
        int i = 0;
        int j = 0;
        while (i <= FatTable.length - 1) {
            FatTableIntArr[j] = ConvertToInt(FatTable[i], FatTable[i + 1], FatTable[i + 2], FatTable[i + 3]);
            j++;
            i += 4;
        }
        return FatTableIntArr[index];
    }

    @Override
    public boolean SetNext(int index, int value) throws IOException {
        if (index >= 1024)
            return false;
        byte[] bytes = InputStream.readAllBytes();
        byte[] FatTableIN = Arrays.copyOfRange(bytes, 1024, 1024 * 5),
                FatTableOUT = new byte[FatTableIN.length];
        int[] FatTableIntArr = new int[FatTableIN.length / 4];
        int i = 0;
        int j = 0;
        while (i <= FatTableIN.length - 1) {
            FatTableIntArr[j] = ConvertToInt(FatTableIN[i], FatTableIN[i + 1], FatTableIN[i + 2], FatTableIN[i + 3]);
            j++;
            i += 4;
        }
        FatTableIntArr[index] = value;
        int s = 0, l = 0;
        while (s <= FatTableIntArr.length - 1) {
            byte[] bytes1 = ByteBuffer.allocate(4).putInt(FatTableIntArr[s]).array();
            FatTableOUT[l] = bytes1[0];
            FatTableOUT[l + 1] = bytes1[1];
            FatTableOUT[l + 2] = bytes1[2];
            FatTableOUT[l + 3] = bytes1[3];
            l += 4;
            s++;
        }
        System.arraycopy(FatTableOUT, 0, bytes, 1024, 1024 * 5 - 1024);
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Mo3taz kayad\\Desktop\\fatTable.txt");
        outputStream.write(bytes);
        return true;
    }

    @Override
    public boolean setBytes(int index, byte[] bytes) throws IOException {
        if (index > 1024) {
            System.out.println("stop shitting ");
            return false;
        }
        byte[] ab = InputStream.readAllBytes();
        byte[] nb = new byte[1024 * 1024 - 1024 * 5];
        if (ab.length - 5120 >= 0) System.arraycopy(ab, 1024 * 5, nb, 0, ab.length - 5120);
        byte[][] blocks = new byte[1024][1024];
        for (int i = 0, j = 0; i < nb.length; i += 1024, j++) {
            blocks[j] = Arrays.copyOfRange(nb, i, i + 1024);
        }
        blocks[index] = bytes;
        System.arraycopy(blocks[index], 0, nb, index * 1024, (index + 1) * 1024);
        System.arraycopy(nb, 0, ab, 1024 * 5, ab.length - 5120);
        outputStreamOUT.write(ab);
        return true;
    }

    @Override
    public byte[] getBytes(int index) throws IOException {
        if (index > 1024) {
            System.out.println("stop shitting ");
            return new byte[0];
        }
        byte[] ab = InputStream.readAllBytes();
        byte[] nb = new byte[1024 * 1024 - 1024 * 5];
        if (ab.length - 5120 >= 0) System.arraycopy(ab, 1024 * 5, nb, 0, ab.length - 5120);
        byte[][] blocks = new byte[1024][1024];
        for (int i = 0, j = 0; i < nb.length; i += 1024, j++) {
            blocks[j] = Arrays.copyOfRange(nb, i, i + 1024);
        }
        return blocks[index];
    }

    private int ConvertToInt(byte b, byte b1, byte b2, byte b3) {
        byte[] bytes = {b, b1, b2, b3};
        return ByteBuffer.wrap(bytes).getInt();
    }

    public void WriteFateTable(int[] fat_table) throws IOException {
        byte[] bytes = InputStream.readAllBytes();
        byte[] FatTableIN = Arrays.copyOfRange(bytes, 1024, 1024 * 5),
                FatTableOUT = new byte[FatTableIN.length];
        int[] FatTableIntArr = new int[FatTableIN.length / 4];

        int s = 0, l = 0;
        while (s <= FatTableIntArr.length - 1) {
            byte[] bytes1 = ByteBuffer.allocate(4).putInt(FatTableIntArr[s]).array();
            FatTableOUT[l] = bytes1[0];
            FatTableOUT[l + 1] = bytes1[1];
            FatTableOUT[l + 2] = bytes1[2];
            FatTableOUT[l + 3] = bytes1[3];
            l += 4;
            s++;
        }
        System.arraycopy(FatTableOUT, 0, bytes, 1024, 1024 * 5 - 1024);
        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Mo3taz kayad\\Desktop\\fatTable.txt");
        outputStream.write(bytes);
    }
}
