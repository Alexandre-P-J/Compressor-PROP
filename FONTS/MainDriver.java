//import java.io.*;
//import Domain.DomainController;
import Presentation.PresentationController;
public class MainDriver {
    public static void main(String[] args) {
        try {
            PresentationController.DisplayUI();
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
