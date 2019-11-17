import java.io.*;
import Compressor.JPEG;
import Compressor.Huffman;
import Constants.JPEG_Quality;

public class JPEGDriver {
    public static void main(String[] args) {
        if (args.length != 3) {
            printUsage();
            System.exit(1);
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(args[1]));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(args[2]));
            JPEG filePPM = new JPEG(new Huffman());
            if (args[0].equals("compress")) filePPM.compress(is,os,JPEG_Quality.DEFAULT.getLuminanceTable(), JPEG_Quality.DEFAULT.getChrominanceTable());
            else if (args[0].equals("decompress")) filePPM.decompress(is,os);
            else {
                printUsage();
            }
            is.close();
            os.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println(args[1] + " Not Found");
			System.exit(1);
        } catch (IOException ioe) {
			System.out.println("IO Error: " + ioe.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void printUsage() {
        System.out.println("Arguments: compress/decompress FromFile ToFile");
    }
}
