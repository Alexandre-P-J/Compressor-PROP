package Presentation;

import java.awt.*;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

    private final NavigationPanel navigation;
    private final Toolbar toolbar;
    private FormPanel formPanel;

    public MainFrame() {
        super("Compressor-PROP");

        setLayout(new BorderLayout());

        toolbar = new Toolbar();
        navigation = new NavigationPanel();
        formPanel = new FormPanel();
        PresentationController.setNavigator(navigation);
        navigation.subscribeSingleClick(formPanel); // receive file selection signals

        add(formPanel, BorderLayout.EAST);
        add(toolbar, BorderLayout.NORTH);
        add(navigation, BorderLayout.CENTER);

        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}