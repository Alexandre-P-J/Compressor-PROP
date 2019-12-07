//import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import Domain.*;
import Presentation.PresentationController;
public class MainDriver {
    public static void main(String[] args) {
        try {
            DomainController.readFileTree("TESTING");
            DomainController.compressTo("Out");
            DomainController.readFileTree("Out");
            //System.out.println(DomainController.getDocument("TESTING/MainDriver.java"));
            DomainController.decompressTo("zzz");

            /*
            
            JPEG inst0 = new JPEG(new Huffman());
            inst0.compress(is0, os);
            InputStream is1 = new FileInputStream("Original1.ppm");
            JPEG inst1 = new JPEG(new Huffman());
            inst1.compress(is1, os);
            os.close();

            InputStream is2 = new FileInputStream("COM");
            OutputStream os2 = new FileOutputStream("DE0");
            JPEG inst2 = new JPEG(new Huffman());
            inst2.decompress(is2, os2);
            OutputStream os3 = new FileOutputStream("DE1");
            JPEG inst3 = new JPEG(new Huffman());
            inst3.decompress(is2, os3);
            os2.close();
            os3.close();
            */
            
            //PresentationController.DisplayUI();
            // EJEMPLO DE USO DEL DOMAIN CONTROLLER PARA SACAR DATOS PARA VISUALIZAR EN LA INTERFAZ (esto deberia estar en PresentationController u otra clase de esa capa)
            
            /*
            DomainController dc = DomainController.getInstance();
            dc.readFileTree("FONTS"); // initialize the file tree (selecciona un archivo/carpeta no importa si esta comprimido o no)

            System.out.println("\nFiles in FONTS/Testing:");
            String[] f = dc.getFileNames("FONTS/Testing"); // si hubieramos usado readFileTree con el path a un archivo aqui solo tendia sentido usar "/" (lee la docu...)
            for (int i = 0; i < f.length; ++i)
                System.out.println(f[i]);

            System.out.println("\nFolders in FONTS:");
            String[] f1 = dc.getFolderNames("FONTS");
            for (int i = 0; i < f1.length; ++i)
                System.out.println(f1[i]);
            */
        
        // acordaos de gestionar excepciones, comentado por ahora
        /*} catch (FileNotFoundException fnfe) {
            System.out.println("File Not Found");
			System.exit(1);
        } catch (IOException ioe) {
			System.out.println("IO Error: " + ioe.getMessage());
            System.exit(1); */
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
