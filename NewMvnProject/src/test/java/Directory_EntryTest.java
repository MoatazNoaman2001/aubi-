import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Directory_EntryTest {

    @Test
    void getBytes() {
        Directory_Entry entry = new Directory_Entry(new Directory_Entry("newFilekjafshdkfh.txt", (byte) 0x01, 4, 45)
                .getBytes());
        System.out.println(entry);

    }
}