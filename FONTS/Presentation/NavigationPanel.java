package Presentation;

import java.awt.BorderLayout;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Font;

public class NavigationPanel extends JPanel {
    private String currentPath;
    private String[] FileNames = new String[0];
    private String[] FolderNames = new String[0];
    private static final String returnSymbol = String.valueOf("\u2b9c") + " ..";
    private static String folderSymbol;
    private DefaultListModel<String> model = new DefaultListModel<>();
    private Vector<NavigationClickObserver> singleClickFileSubscribers = new Vector<NavigationClickObserver>();
    private Vector<NavigationClickObserver> singleClickFolderSubscribers = new Vector<NavigationClickObserver>();

    public NavigationPanel() {
        JList<String> jlist = new JList<>(model);
        folderSymbol = getCompatibleFolderSymbol(jlist.getFont()) + " ";
        setLayout(new BorderLayout());
        setVisible(true);
        add(new JScrollPane(jlist), BorderLayout.CENTER);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList<String> theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() % 2 == 0) { // double click
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        if (Arrays.asList(FolderNames).contains(o.toString())) {
                            try {
                                refresh(pathTraverse(o.toString().substring(folderSymbol.length())));
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
                        if (Arrays.asList(FileNames).contains(o.toString())) {
                            for (NavigationClickObserver si : singleClickFileSubscribers) {
                                si.SingleClick_File(pathTraverse(o.toString()));
                            }
                        } else {
                            for (NavigationClickObserver si : singleClickFolderSubscribers) {
                                si.SingleClick_Folder(pathTraverse(o.toString()));
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
            FolderNames[i] = folderSymbol + FolderNames[i];
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
            result = result + steps[i] + System.getProperty("file.separator");
        }
        return result + steps[steps.length - 2];
    }

    private String pathTraverse(String Folder) {
        if (currentPath.length() == 0)
            return Folder;
        return currentPath + System.getProperty("file.separator") + Folder;
    }

    private void clean() {
        model.removeAllElements();
    }

    private String getCompatibleFolderSymbol(Font f) {
        int[] symbols = { 0x1F5C1, 0x1F5C0, 0x1F5BF, 0x1F4C1, 0x1F4C2 };
        for (int sym : symbols) {
            if (f.canDisplay(sym)) {
                return String.valueOf(Character.toChars(sym));
            }
        }
        return "[FOLDER]";
    }

    public void subscribeClickFile(NavigationClickObserver si) {
        singleClickFileSubscribers.add(si);
    }

    public void subscribeClickFolder(NavigationClickObserver si) {
        singleClickFolderSubscribers.add(si);
    }
}