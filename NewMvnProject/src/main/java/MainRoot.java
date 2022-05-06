import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiPredicate;

public class MainRoot {

    public static Directory CurrentDir;
    public static String path = System.getProperty("user.dir");
    ;
    private static Fat fat;

    static {
        try {
            fat = new Fat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Directory_Entry> files = new ArrayList<>();
    private static ArrayList<Directory_Entry> dirs = new ArrayList<>();

    public MainRoot() throws IOException {
        Directory root = new Directory("root", Byte.parseByte(Integer.toHexString(0x01)), 1, 0, new ArrayList<>(), null);
        root.WriteDirectory(fat);
        Directory user = new Directory("user", Byte.parseByte(Integer.toHexString(0x01)), 1, 0, new ArrayList<>(), root);
        user.WriteDirectory(fat);
        Directory user2 = new Directory("user2", Byte.parseByte(Integer.toHexString(0x01)), 1, 0, new ArrayList<>(), root);
        user2.WriteDirectory(fat);
    }

    public static void main(String[] args) throws IOException {
        MainRoot root = new MainRoot();
        Scanner scanner = new Scanner(System.in);
        String command = "";
        do {
            System.out.print(path + "> ");
            command = scanner.nextLine();
            if (!CMD.test(command, path)) System.out.println("an error happen unknown reason");
        } while (!command.trim().equalsIgnoreCase("quit"));

//        TreeMap leagues= getMyRequest("https://app.sportdataapi.com/api/v1/soccer/leagues?country_id=3");

//        FileOutputStream stream = new FileOutputStream("C:\\Users\\Mo3taz kayad\\Desktop\\fatTable.txt");
    }


    public static BiPredicate<String, Directory_Entry> MD = (s , d) -> {
        Directory dir = null;
        try {
            dir = new Directory(s, Byte.parseByte(Integer.toHexString(0x01)), 1, 0, new ArrayList<>(), d);
            dir.WriteDirectory(fat);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    };
    private final static BiPredicate<String, String> CMD = (command, path) -> {
        if (command.isBlank()) {
            System.out.println("write some thing");
        } else if (command.equalsIgnoreCase("cls")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        } else if (command.equals("help")) {

        } else if (command.equals("dir")) {
            System.out.println("\t" + "Directory of " + path.lastIndexOf("\\") + ": ");
            System.out.println("\t\t\t" + files.size() + " File(s) " + "\t" + files.size() * 32);
            System.out.println("\t\t\t" + dirs.size() + " dir(s)" + "\t" + fat.getAvailableBlocks() * 1024
                    + " free bytes");

        } else if (command.equals("quit")) {
            System.out.println("thanks for using win cmd");
            try {
                Runtime.getRuntime().exec("taskkill /f /im cmd.exe");
            } catch (IOException ignored) {
            }
        } else if (command.trim().startsWith("md")) {
            if (command.trim().equals("md")) {
                System.out.println("write name of directory");
                return true;
            }
            if (command.split(" ")[0].equals("md")) {
                MD.test(command.trim().split(" ")[1] , new Directory_Entry("root" ,
                        Byte.parseByte(Integer.toHexString(0x01)) , 1  , 1));
            }
        } else {
            System.out.println("unknown command");
        }
        return true;
    };

    private static boolean getAllFiles() {
        return false;
    }

    private static TreeMap getMyRequest(String url) throws IOException {
        TreeMap tree = new TreeMap<>();
        String res = getJson(url);
        Gson gson = new Gson();
        if (!res.isEmpty()) {
            tree = gson.fromJson(res, TreeMap.class);
        }
        return tree;
    }


    private static String getJson(String url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setUseCaches(true);
        connection.setRequestProperty("apikey", "f1e21bd0-cc90-11ec-89f2-edc9b68fdf57");
//        connection.setRequestProperty("country_id" , "48");
        connection.connect();
        String res = "";
        int Status = connection.getResponseCode();

        if (Status == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder b = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                b.append(line + "\n");
            }
            res = b.toString();
        }
        return res;
    }
}
