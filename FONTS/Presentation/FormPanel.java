package Presentation;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;


public class FormPanel extends JPanel {

    private final JLabel TitTamaño;
    private final JLabel TitVisualizacion;
    private final JTextField TamañoField;
    private final JComboBox comboBox;

    public FormPanel() {
        final Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);

        TitTamaño = new JLabel("Tamaño: ");
        TamañoField = new JTextField(10);
        TitVisualizacion = new JLabel("Previsualización: ");
        comboBox = new JComboBox();

        final DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        comboModel.addElement("LZ78");
        comboModel.addElement("LZSS");
        comboModel.addElement("LZW");
        comboModel.addElement("JPEG");
        comboBox.setModel(comboModel);

        final String alg = (String) comboBox.getSelectedItem();

        final Border innerBorder = BorderFactory.createTitledBorder("Información del archivo");
        final Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        layoutComponents();
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
            add(TitTamaño, constr);

            constr.gridx = 1;
            constr.gridy = 0;
            constr.anchor = GridBagConstraints.LINE_START;
            constr.insets = new Insets(0,0,0,0);
            add(TamañoField, constr);

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

}