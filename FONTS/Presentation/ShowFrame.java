package Presentation;

import java.awt.*;

import javax.swing.JFrame;

public class ShowFrame extends JFrame {

    private ShowDocument showDocument;
    private ShowImage showImage;

    public ShowFrame(String path) throws Exception {
        setTitle("Visual");

        if(PresentationController.isFileImage(path)) {
            showImage = new ShowImage(path);
            getContentPane().add(showImage);
        }
        else {
            showDocument = new ShowDocument(path);
            add(showDocument);
        }
        setSize(600, 850);
        setLocationRelativeTo(null);    //Centering frame
        setVisible(true);
    }
}