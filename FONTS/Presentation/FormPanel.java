package Presentation;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;


public class FormPanel extends JPanel implements SingleClick {

    private final JLabel TitTam;
    private final JLabel TitVisualizacion;
    private final TextArea TamField;
    private final JComboBox comboBox;
    private StringListener textListener;
    private PanelListener panel;

    public FormPanel() {
        final Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);

        TitTam = new JLabel("Tama\u00f1o: ");
        TamField = new TextArea();
        TamField.setPreferredSize(new Dimension(100,100));
        TitVisualizacion = new JLabel("Previsualizaci\u00f3n: ");
        comboBox = new JComboBox();

        final DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        comboModel.addElement("LZ78");
        comboModel.addElement("LZSS");
        comboModel.addElement("LZW");
        comboModel.addElement("JPEG");
        comboBox.setModel(comboModel);

        final String alg = (String) comboBox.getSelectedItem();

        final Border innerBorder = BorderFactory.createTitledBorder("Informaci\u00f3n del archivo");
        final Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        layoutComponents();
    }
    public void escribirenTam (String tam) {
        System.out.println(tam);
        TamField.append(tam);
    }

        public void layoutComponents() {

            setLayout(new GridBagLayout());

            final GridBagConstraints constr = new GridBagConstraints();

            constr.weightx = 1;
            constr.weighty = 0.1;

            constr.gridx = 0;
            constr.gridy = 0;
            constr.fill = GridBagConstraints.NONE;
            constr.anchor = GridBagConstraints.LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(TitTam, constr);

            constr.gridx = 1;
            constr.gridy = 0;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TamField, constr);

            constr.gridy++;

            constr.weightx = 1;
            constr.weighty = 0.1;

            constr.gridx = 0;
            
            constr.anchor = GridBagConstraints.FIRST_LINE_START;
            constr.insets = new Insets(0,0,0,5);
            add(TitVisualizacion, constr);

            constr.gridy++;

            constr.weightx = 1;
            constr.weighty = 0.1;

            constr.gridx = 0;
            constr.anchor = GridBagConstraints.FIRST_LINE_END;
            constr.insets = new Insets(0,0,0,5);
            add(new JLabel("Algoritmo:"), constr);

            constr.gridx = 1;
            constr.anchor = GridBagConstraints.FIRST_LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(comboBox, constr);
        }

        
        public void SingleClick_Event(String context) {
            System.out.println(context + " [!!!]CON ESTE PATH TIENES MAS QUE SUFICIENTE PARA HACER CONSULTAS Y MODIFICACIONES CON FUNCIONES DE DOMAINCONTROLLER (A TRAVES DE PRESENTATION CONTROLLER)[!!!]\n");
        }
}