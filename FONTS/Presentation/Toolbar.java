package Presentation;

import Presentation.PresentationController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Toolbar extends JPanel implements ActionListener {

    private final JButton fileButton;
    private final JButton compressButton;
    private final JFileChooser fileChooser;
    private String s;
    private static File f;
    private static PresentationController Pc;

    private StringListener textListener;
    
    public Toolbar() {
        setBorder(BorderFactory.createEtchedBorder());
        fileButton = new JButton("Abrir archivo");
        compressButton = new JButton("Comprimir/Descomprimir");
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        s = new String();
        f = new File(s);

        compressButton.addActionListener(this);
        fileButton.addActionListener(this);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(fileButton);
        add(compressButton);
    }

    public void setStringListener(final StringListener listener) {
        this.textListener = listener;
    }

    public void actionPerformed(final ActionEvent e) {
        final JButton clicked = (JButton) e.getSource();

       if (clicked == fileButton) {
           
            if(textListener != null){
                if(fileChooser.showOpenDialog(Toolbar.this) == JFileChooser.APPROVE_OPTION){
                    textListener.limpiarText();
                    System.out.println(fileChooser.getSelectedFile());
                    f = fileChooser.getSelectedFile();
                    s = f.getName();
                    double tam = (double) f.length();
                    String tama = String.valueOf(tam);
                    if (f.isFile()) {
                        textListener.textEmitted("- " + s + "\n");
                    }
                    else if (f.isDirectory()) {
                        String[] archivos = Pc.obtenerArchivos(f, true);
                        String aux;
                        if (archivos != null) {
                            System.out.println(archivos.length);
                            for (int i = 0; i < archivos.length; ++i) {
                                aux = "+ " + archivos[i] + "\n";
                                textListener.textEmitted(aux);
                            }
                        }
                        else {
                            System.out.println("archivos es nulo");
                        }
                        archivos = Pc.obtenerArchivos(f, false);
                        if (archivos != null) {
                            System.out.println(archivos.length);
                            for (int i = 0; i < archivos.length; ++i) {
                                aux = "- " + archivos[i] + "\n";
                                textListener.textEmitted(aux);
                            }
                        }
                        else {
                            System.out.println("archivos es nulo");
                        }
                    }                    
                }
            }
        } 
        else{
            if(textListener != null) {
            textListener.textEmitted("Comprimiendo...\n");
            }
        }
    }
}
