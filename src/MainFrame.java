import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private Scenerio activeScenerio;

    private String userName, schoolName, sessionName;

    public MainFrame() {
        setTitle("ISO 15939 Simulator");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createStep1Panel(), "Step 1");
        mainPanel.add(createStep2Panel(), "Step 2");

        this.add(mainPanel);
        this.setVisible(true);
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JLabel title = new JLabel("Step 1: Profile Information", SwingConstants.CENTER);
        JTextField userField = new JTextField();
        JTextField schoolField = new JTextField();
        JTextField sessionField = new JTextField();
        JButton nextButton = new JButton("Next");

        nextButton.addActionListener(e -> {
            if(userField.getText().isEmpty() || schoolField.getText().isEmpty() || sessionField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lutfen tum alanlari doldurunuz!");
            } else {
                userName = userField.getText();
                schoolName = schoolField.getText();
                sessionName = sessionField.getText();
                cardLayout.show(mainPanel, "Step 2");
            }
        });

        panel.add(title);
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("School:"));
        panel.add(schoolField);
        panel.add(new JLabel("Session Name:"));
        panel.add(sessionField);
        panel.add(nextButton);

        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel step2Label = new JLabel("Step 2: Define Scenario", SwingConstants.CENTER);
        String[] scenarios = {"Education LMS", "Health INFO System"};
        JComboBox<String> combo = new JComboBox<>(scenarios);
        JButton nextButton = new JButton("Next: Plan");

        nextButton.addActionListener(e -> {
            setupActiveScenerio((String) combo.getSelectedItem());
            mainPanel.add(createStep3Panel(), "Step 3");
            cardLayout.show(mainPanel, "Step 3");
        });

        panel.add(step2Label);
        panel.add(new JLabel("Lutfen bir Senaryo Seciniz:"));
        panel.add(combo);
        panel.add(nextButton);

        return panel;
    }

    private void setupActiveScenerio(String scenarioName) {
        activeScenerio = new Scenerio(scenarioName);

        Dimension usability = new Dimension("Usability", 0.6);
        usability.addMetric(new Metric("SUS Score", "Higher", 0.7, 0.0, 100.0, "Points"));
        usability.addMetric(new Metric("Task Time", "Lower", 0.3, 10.0, 60.0, "Seconds"));

        Dimension reliability = new Dimension("Reliability", 0.4);
        reliability.addMetric(new Metric("Uptime", "Higher", 1.0, 95.0, 100.0, "%"));

        activeScenerio.addDimension(usability);
        activeScenerio.addDimension(reliability);
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Step 3: Measurement Plan", SwingConstants.CENTER);

        String[] columns = {"Metric", "Coefficient", "Direction", "Unit"};
        ArrayList<Metric> allMetrics = new ArrayList<>();
        for(Dimension d : activeScenerio.getDimensions()) {
            allMetrics.addAll(d.getMetrics());
        }

        Object[][] data = new Object[allMetrics.size()][4];
        for (int i = 0; i < allMetrics.size(); i++) {
            Metric m = allMetrics.get(i);
            data[i] = new Object[]{m.getName(), m.getCoefficient(), m.getDirection(), m.getUnit()};
        }

        JTable table = new JTable(data, columns);
        JButton nextButton = new JButton("Next: Collect Data");

        nextButton.addActionListener(e -> {
            mainPanel.add(createStep4Panel(), "Step 4");
            cardLayout.show(mainPanel, "Step 4");
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStep4Panel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        ArrayList<Metric> allMetrics = new ArrayList<>();
        for(Dimension d : activeScenerio.getDimensions()) {
            allMetrics.addAll(d.getMetrics());
        }

        ArrayList<JTextField> fields = new ArrayList<>();

        for (Metric m : allMetrics) {
            panel.add(new JLabel(m.getName() + " (" + m.getUnit() + "):"));
            JTextField f = new JTextField("0");
            fields.add(f);
            panel.add(f);
        }

        JButton nextButton = new JButton("Analyse Results");
        nextButton.addActionListener(e -> {
            try {
                for (int i = 0; i < allMetrics.size(); i++) {
                    double val = Double.parseDouble(fields.get(i).getText());
                    allMetrics.get(i).setRawValue(val);
                }
                mainPanel.add(createStep5Panel(), "Step 5");
                cardLayout.show(mainPanel, "Step 5");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lutfen sadece sayisal degerler giriniz!");
            }
        });

        panel.add(new JLabel(""));
        panel.add(nextButton);

        return panel;
    }

    private JPanel createStep5Panel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        panel.add(new JLabel("Step 5: Final Analysis", SwingConstants.CENTER));

        Dimension weakest = null;
        double lowestScore = 6.0;

        for (Dimension d : activeScenerio.getDimensions()) {
            double dScore = d.calculateDimensionScore();
            panel.add(new JLabel(d.getName() + " Score: " + String.format("%.2f", dScore)));

            if (dScore < lowestScore) {
                lowestScore = dScore;
                weakest = d;
            }
        }

        double finalScore = activeScenerio.calculateFinalQualityScore();
        panel.add(new JLabel("Overall Quality Score: " + String.format("%.2f", finalScore)));

        if (weakest != null) {
            panel.add(new JLabel("Weakest Dimension: " + weakest.getName()));
            panel.add(new JLabel("Gap Analysis: " + String.format("%.2f", 5.0 - lowestScore)));
        }

        return panel;
    }
}