package Presentation;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.BorderFactory;

public class ShowFrame extends JFrame {

    private ShowDocument showDocument;
    private ShowImage showImage;

    public ShowFrame(String path) throws Exception {
        if(PresentationController.isFileImage(path)) {
            setTitle("Image Viewer");
            showImage = new ShowImage(path, this);
            add(showImage);
        }
        else {
            setTitle("Text Viewer");
            showDocument = new ShowDocument(path);
            add(showDocument);
            setSize(600, 850);
        }
        setLocationRelativeTo(null);    //Centering frame
        setVisible(true);
    }
}