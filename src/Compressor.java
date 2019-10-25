import java.io.* ;

public class Compressor {
    public static void main(String[] args) {
        int DICT_BIT_SIZE = 10;

        if (args.length != 4) {
            printUsage();
            System.exit(1);
        }
            
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(args[2]));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(args[3]));
           
            if (args[0].equals("LZW")) {
                LZW fileW = new LZW(DICT_BIT_SIZE);
                if (args[1].equals("compress")) fileW.compress(is,os);
                else if (args[1].equals("decompress")) fileW.decompress(is,os);
                else {
                    printUsage();
                }
            }
            else if (args[0].equals("LZ78")) {
                LZ78 file78 = new LZ78(DICT_BIT_SIZE);
                if (args[1].equals("compress")) file78.compress(is,os);
                else if (args[1].equals("decompress")) file78.decompress(is,os);
                else {
                    printUsage();
                }
            }
            else if (args[0].equals("JPEG")) {
                JPEG filePPM = new JPEG();
                if (args[1].equals("compress")) filePPM.compress(is,os);
                //else if (args[1].equals("decompress")) file78.decompress(is,os);
                else {
                    printUsage();
                }
            }
            else {
                printUsage();
            }
            is.close();
            os.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println("El archivo " + args[4] + " no se puede abrir");
			System.exit(1);
        } catch (IOException ioe) {
			System.out.println("Error en leer el archivo " +args[2]+ " o en escribir el archivo " + args[3]);
            System.exit(1);
        }
    }
    
    public static void printUsage() {
        System.out.println("Arguments: LZW/LZ78 compress/decompress FromFile ToFile");
    }
}