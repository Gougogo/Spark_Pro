import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class bigFileGenarator {
    public static void main(String[] args) throws IOException {
        String destFile = "in/luci1.txt";
        String lineSeparator = System.getProperty("line.separator");

        String line1 = "test";
        FileOutputStream fos = new FileOutputStream(destFile);
        for (int i = 0; i < 50000; i++) {
                fos.write(line1.getBytes());
                fos.write(lineSeparator.getBytes());

                // Flush the written bytes to the file
                fos.flush();

                System.out.println("Text has  been  written to "
                        + (new File(destFile)).getAbsolutePath());
        }

        String str = "12";
        System.out.println(Double.parseDouble(str));
    }
}
