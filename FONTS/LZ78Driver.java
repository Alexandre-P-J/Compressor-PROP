import java.io.* ;
import Compressor.LZ78;

public class LZ78Driver {
    public static void main(String[] args) {
        int DICT_BIT_SIZE = 10;

        if (args.length != 3) {
            printUsage();
            System.exit(1);
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(args[2]));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(args[3]));
            LZ78 file78 = new LZ78();
            if (args[1].equals("compress")) file78.compress(is,os,DICT_BIT_SIZE);
            else if (args[1].equals("decompress")) file78.decompress(is,os);
            else {
                printUsage();
            }
            is.close();
            os.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println(args[2] + " Not Found");
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
