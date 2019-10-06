import java.io.* ;

public class Main {
    public static void main(String[] args) 
    {
        if (args.length < 4)
            System.out.println("Usage: java LZW/LZ78 compress/decompress FromFile ToFile");
            
        try {
            InputStream is = new FileInputStream(args[2]);
            OutputStream os = new FileOutputStream(args[3]);
           
            if (args[0].equals("LZW")) {
                LZW fileW = new LZW();
                if (args[1].equals("compress")) fileW.compress(is,os);
                else if (args[1].equals("decompress")) fileW.decompress(is,os);
            }
            else if (args[0].equals("LZ78")) {
                LZ78 file78 = new LZ78();
                if (args[1].equals("compress")) file78.compress(is,os);
                else if (args[1].equals("decompress")) file78.decompress(is,os);    
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
}