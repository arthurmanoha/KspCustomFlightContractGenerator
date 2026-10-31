package kspcustomflightcontractgenerator;

import java.awt.BorderLayout;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author arthu
 */
public class GUI extends JFrame {

    String categories[] = {"4-8p", "10-50p", "100-300p"};
    String airfields[] = {"KSC", "Abandoned Island", "North-East Island", "Japan", "Dessert Airfield"};
    ArrayList<JComponent> routeButtonList;
    int nbColumns = 4; // Title of the airfield (start, intermediate or end); name; addbutton; removebutton

    public GUI(FlightGenerator generator) {

        super();

        //type (x01: 4-8p, x02: 10-50p, x03: 100-300p)
        //or
        //nbPaxMin, nbPaxMax
        //Steps (start, intermediary, end)
        //nbPilots
        //nbEngineers
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        this.setContentPane(mainPanel);

        routeButtonList = new ArrayList<>();

        JPanel passengerPanel = setupPassengerPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        mainPanel.add(passengerPanel, constraints);

        JPanel flightPanel = setupFlightPlanPanel();
        constraints.gridy++;
        mainPanel.add(flightPanel, constraints);

        JFileChooser fileChooser = new JFileChooser();
        constraints.gridy++;
        mainPanel.add(fileChooser, constraints);

        JButton validateButton = new JButton("Create file");
        validateButton.addActionListener((e) -> {
            System.out.println("Save file to disk now.");
        });
        constraints.gridy++;
        mainPanel.add(validateButton, constraints);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pack();

        this.setVisible(true);
    }

    private JPanel setupPassengerPanel() {

        JPanel passengerPanel = new JPanel();

        passengerPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Passengers");
        passengerPanel.add(title, NORTH);

        JPanel centralPanel = new JPanel();

        ButtonGroup group = new ButtonGroup();

        for (String s : categories) {
            JRadioButton radioButton = new JRadioButton(s);
            centralPanel.add(radioButton, CENTER);
            group.add(radioButton);
        }

        JRadioButton customCatButton = new JRadioButton("Custom: ");
        JTextField customCatMinTextField = new JFormattedTextField();
        group.add(customCatButton);
        customCatMinTextField.setPreferredSize(new Dimension(60, 30));
        JLabel customCatSeparator = new JLabel("-");
        JTextField customCatMaxTextField = new JFormattedTextField();
        customCatMaxTextField.setPreferredSize(new Dimension(60, 30));
        centralPanel.add(customCatButton);
        centralPanel.add(customCatMinTextField);
        centralPanel.add(customCatSeparator);
        centralPanel.add(customCatMaxTextField);

        passengerPanel.add(centralPanel, CENTER);
        return passengerPanel;
    }

    private JPanel setupFlightPlanPanel() {

        JPanel flightPlanPanel = new JPanel();

        flightPlanPanel.setLayout(new GridBagLayout());

        // Add the start and destination airfields
        addAirfieldChoice(flightPlanPanel, 0);
        addAirfieldChoice(flightPlanPanel, 1);
        return flightPlanPanel;
    }

    // Take all components from list and arrange them into the panel
    private void computeLayout(JPanel p) {
        int nbElements = routeButtonList.size();

        p.removeAll();
        GridBagLayout layout = new GridBagLayout();
        p.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(1, 1, 1, 1);

        int index = 0;
        for (JComponent component : routeButtonList) {
            if (index % nbColumns == 0) {
                // New row
                constraints.gridx = 0;
                constraints.gridy = constraints.gridy + 1;
            } else {
                constraints.gridx++;
            }
            p.add(component, constraints);
            index++;
        }

        // Set start and end titles
        ((JLabel) routeButtonList.get(0)).setText("start");
        for (int row = 1; row < routeButtonList.size() / nbColumns - 1; row++) {
            System.out.println("step: " + row);
            ((JLabel) routeButtonList.get(row * nbColumns)).setText("row " + row);
        }
        ((JLabel) routeButtonList.get(routeButtonList.size() - 4)).setText("end");

        revalidate();
        pack();
    }

    private void addAirfieldChoice(JPanel p, int initialChoice) {

        JLabel title = new JLabel("");

        JComboBox box = new JComboBox(airfields);
        box.setSelectedIndex(initialChoice);

        JButton addButton = new JButton("+");
        addButton.addActionListener((e) -> {
            addAirfieldChoice(p, initialChoice);
        });

        JButton removeButton = new JButton("-");
        removeButton.addActionListener((e) -> {
            if (routeButtonList.size() > nbColumns) {
                routeButtonList.remove(title);
                routeButtonList.remove(box);
                routeButtonList.remove(addButton);
                routeButtonList.remove(removeButton);
                computeLayout(p);
            }
        });

        routeButtonList.add(title);
        routeButtonList.add(box);
        routeButtonList.add(addButton);
        routeButtonList.add(removeButton);

//        ((JLabel) routeButtonList.get(routeButtonList.size() / nbColumns)).setText("end");
        computeLayout(p);
    }
}
