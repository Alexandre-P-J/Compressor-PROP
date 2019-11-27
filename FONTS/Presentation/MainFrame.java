package Presentation;

import java.awt.*;

import javax.swing.JFrame;

public class MainFrame extends JFrame{

    private final TextPanel textPanel;
    private final Toolbar toolbar;
    private FormPanel formPanel;

    public MainFrame() {
        super("Compressor-PROP");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        textPanel = new TextPanel();
        formPanel = new FormPanel();

        toolbar.setStringListener(new StringListener() {
            @Override
            public void textEmitted(String text) {
                textPanel.appendText(text);
            }
        });

        add(formPanel, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}