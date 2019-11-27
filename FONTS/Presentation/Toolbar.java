package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Toolbar extends JPanel implements ActionListener {

    private final JButton fileButton;
    private final JButton compressButton;
    private final JFileChooser fileChooser;
    private String s;
    private File f;

    private StringListener textListener;

    public Toolbar() {
        setBorder(BorderFactory.createEtchedBorder());
        fileButton = new JButton("Abrir archivo");
        compressButton = new JButton("Comprimir/Descomprimir");
        fileChooser = new JFileChooser();
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
                    System.out.println(fileChooser.getSelectedFile());
                    f = fileChooser.getSelectedFile();
                    s = f.getName();
                    textListener.textEmitted("Has seleccionado " + s);
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
