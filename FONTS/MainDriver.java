import java.io.*;
import Domain.DomainController;
public class MainDriver {
    public static void main(String[] args) {
        try {
            // EJEMPLO DE USO DEL DOMAIN CONTROLLER PARA SACAR DATOS PARA VISUALIZAR EN LA INTERFAZ (esto deberia estar en PresentationController u otra clase de esa capa)
            /*
            DomainController dc = DomainController.getInstance();
            dc.openNotCompressed("FONTS"); // initialize the file tree (selecciona un archivo/carpeta no comprimido)

            System.out.println("\nFiles:");
            String[] f = dc.getFileNames("FONTS/Testing");
            for (int i = 0; i < f.length; ++i)
                System.out.println(f[i]);

            System.out.println("\nFolders:");
            String[] f1 = dc.getFolderNames("FONTS/Testing");
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
