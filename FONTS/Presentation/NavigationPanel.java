package Presentation;

import java.awt.BorderLayout;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;

public class NavigationPanel extends JPanel {
    private String currentPath;
    private String[] FileNames = new String[0];
    private String[] FolderNames = new String[0];
    private static final String returnSymbol = String.valueOf("\u2b9c") + " ..";
    private DefaultListModel<String> model = new DefaultListModel<>();
    private Vector<SingleClick> singleClickSubscribers = new Vector<SingleClick>();

    public NavigationPanel() {
        JList<String> jlist = new JList<>(model);
        setLayout(new BorderLayout());
        setVisible(true);
        add(new JScrollPane(jlist), BorderLayout.CENTER);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList<String> theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) { // double click
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        if (contains(FolderNames, o.toString())) {
                            try {
                                refresh(pathTraverse(o.toString().substring(2)));
                            } catch (Exception e) {
                                System.out.println("Unreachable Folder");
                            }
                        } else if (returnSymbol.equals(o.toString())) {
                            try {
                                refresh(pathReturn(currentPath));
                            } catch (Exception e) {
                                System.out.println("Unreachable Folder");
                            }
                        }
                    }
                } else if (mouseEvent.getClickCount() == 1) { // single click
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        if (contains(FileNames, o.toString())) {
                            for (SingleClick si : singleClickSubscribers) {
                                si.SingleClick_Event(pathTraverse(o.toString()));
                            }
                        }
                    }
                }
            }
        };
        jlist.addMouseListener(mouseListener);
    }

    public void refresh(String Path) throws Exception {
        currentPath = Path;
        FileNames = PresentationController.getFileNames(Path);
        FolderNames = PresentationController.getFolderNames(Path);
        clean();
        if (!pathReturn(Path).equals(Path)) {
            model.addElement(returnSymbol);
        }
        Arrays.sort(FileNames);
        Arrays.sort(FolderNames);
        for (int i = 0; i < FolderNames.length; ++i) {
            FolderNames[i] = String.valueOf("\uF115") + " " + FolderNames[i]; // Unicode magic (show folder character)
            model.addElement(FolderNames[i]);
        }
        for (int i = 0; i < FileNames.length; ++i) {
            model.addElement(FileNames[i]);
        }
    }

    private String pathReturn(String Path) {
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        String steps[] = Path.split(pattern);
        if (steps.length <= 1) {
            return "";
        }
        String result = "";
        for (int i = 0; i < steps.length - 2; ++i) {
            result = result + steps[i] + "/";
        }
        return result + steps[steps.length - 2];
    }

    private String pathTraverse(String Folder) {
        if (currentPath.length() == 0)
            return Folder;
        return currentPath + "/" + Folder;
    }

    private void clean() {
        model.removeAllElements();
    }

    private boolean contains(String[] arr, String e) {
        for (String s : arr) {
            if (s.equals(e))
                return true;
        }
        return false;
    }

    public void subscribeSingleClick(SingleClick si) {
        singleClickSubscribers.add(si);
    }
}