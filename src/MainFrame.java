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
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panelleri sirayla ekliyoruz
        mainPanel.add(createStep1Panel(), "Step 1");
        mainPanel.add(createStep2Panel(), "Step 2");

        this.add(mainPanel);
        this.setVisible(true);
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

        // Step Indicator (Basit versiyon)
        panel.add(new JLabel("Step 1: Profile > Step 2 > Step 3 > Step 4 > Step 5", SwingConstants.CENTER));

        JTextField userField = new JTextField();
        JTextField schoolField = new JTextField();
        JTextField sessionField = new JTextField();
        JButton nextButton = new JButton("Next Step");

        nextButton.addActionListener(e -> {
            if(userField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username to continue.");
            } else if(schoolField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your school to continue.");
            } else if(sessionField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter session name to continue.");
            } else {
                userName = userField.getText();
                schoolName = schoolField.getText();
                sessionName = sessionField.getText();
                cardLayout.show(mainPanel, "Step 2");
            }
        });

        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("School:"));
        panel.add(schoolField);
        panel.add(new JLabel("Session Name:"));
        panel.add(sessionField);
        panel.add(new JLabel(""));
        panel.add(nextButton);

        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        panel.add(new JLabel("Profile ✓ > Step 2: Define > Step 3 > Step 4 > Step 5", SwingConstants.CENTER));

        // Quality Type Selection (Mutual Exclusivity gereği ButtonGroup kullanildi)
        panel.add(new JLabel("Select Quality Type:"));
        JRadioButton productBtn = new JRadioButton("Product Quality", true);
        JRadioButton processBtn = new JRadioButton("Process Quality");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(productBtn); typeGroup.add(processBtn);
        panel.add(productBtn); panel.add(processBtn);

        // Mode Selection
        panel.add(new JLabel("Select Mode:"));
        JRadioButton healthBtn = new JRadioButton("Health", true);
        JRadioButton eduBtn = new JRadioButton("Education");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(healthBtn); modeGroup.add(eduBtn);
        panel.add(healthBtn); panel.add(eduBtn);

        // Scenario Selection
        panel.add(new JLabel("Select Scenario:"));
        String[] scenarios = {"Scenario C - Team Alpha", "Scenario D - Team Beta"};
        JComboBox<String> combo = new JComboBox<>(scenarios);
        panel.add(combo);

        JButton nextButton = new JButton("Next: Plan Measurement");
        nextButton.addActionListener(e -> {
            setupActiveScenerio((String) combo.getSelectedItem());
            mainPanel.add(createStep3Panel(), "Step 3");
            cardLayout.show(mainPanel, "Step 3");
        });

        panel.add(nextButton);
        return panel;
    }

    private void setupActiveScenerio(String scenarioName) {
        activeScenerio = new Scenerio(scenarioName);

        // Odev dokumanindaki örnek veriler (Education Scenario C)
        Dimension usability = new Dimension("Usability", 25);
        usability.addMetric(new Metric("SUS score", "Higher", 50, 0, 100, "points"));
        usability.addMetric(new Metric("Onboarding time", "Lower", 50, 0, 60, "min"));

        Dimension reliability = new Dimension("Reliability", 20);
        reliability.addMetric(new Metric("Uptime", "Higher", 50, 95, 100, "%"));
        reliability.addMetric(new Metric("MTTR", "Lower", 50, 0, 120, "min"));

        activeScenerio.addDimension(usability);
        activeScenerio.addDimension(reliability);
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Profile ✓ > Define ✓ > Step 3: Plan > Step 4 > Step 5", SwingConstants.CENTER), BorderLayout.NORTH);

        String[] columns = {"Dimension", "Metric", "Coeff", "Direction", "Range", "Unit"};
        ArrayList<Object[]> rows = new ArrayList<>();
        for(Dimension d : activeScenerio.getDimensions()) {
            for(Metric m : d.getMetrics()) {
                rows.add(new Object[]{d.getName(), m.getName(), m.getCoefficient(), m.getDirection(), m.getMinRange()+"-"+m.getMaxRange(), m.getUnit()});
            }
        }

        Object[][] data = rows.toArray(new Object[0][]);
        JTable table = new JTable(data, columns);

        JButton nextButton = new JButton("Next: Collect Data");
        nextButton.addActionListener(e -> {
            mainPanel.add(createStep4Panel(), "Step 4");
            cardLayout.show(mainPanel, "Step 4");
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStep4Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        panel.add(new JLabel("Profile ✓ > Define ✓ > Plan ✓ > Step 4: Collect > Step 5", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel inputArea = new JPanel(new GridLayout(0, 2, 5, 5));
        ArrayList<Metric> allMetrics = new ArrayList<>();
        ArrayList<JTextField> fields = new ArrayList<>();

        for(Dimension d : activeScenerio.getDimensions()) {
            for(Metric m : d.getMetrics()) {
                inputArea.add(new JLabel(m.getName() + " (" + m.getUnit() + "):"));
                JTextField f = new JTextField("0");
                fields.add(f);
                inputArea.add(f);
                allMetrics.add(m);
            }
        }

        JButton nextButton = new JButton("Analyze and See Results");
        nextButton.addActionListener(e -> {
            try {
                for (int i = 0; i < allMetrics.size(); i++) {
                    double val = Double.parseDouble(fields.get(i).getText());
                    allMetrics.get(i).setRawValue(val);
                }
                mainPanel.add(createStep5Panel(), "Step 5");
                cardLayout.show(mainPanel, "Step 5");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
            }
        });

        panel.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStep5Panel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        panel.add(new JLabel("Profile ✓ > Define ✓ > Plan ✓ > Collect ✓ > Step 5: Analyse", SwingConstants.CENTER));

        Dimension weakest = null;
        double lowestScore = 6.0;

        for (Dimension d : activeScenerio.getDimensions()) {
            double dScore = d.calculateDimensionScore();

            // JProgressBar kullanimi (Odev gereksinimi)
            JPanel row = new JPanel(new BorderLayout());
            row.add(new JLabel(d.getName() + " Score: "), BorderLayout.WEST);
            JProgressBar pb = new JProgressBar(10, 50); // 1.0-5.0 arasi icin 10-50 yaptim
            pb.setValue((int)(dScore * 10));
            pb.setStringPainted(true);
            pb.setString(String.format("%.1f", dScore));
            row.add(pb, BorderLayout.CENTER);
            panel.add(row);

            if (dScore < lowestScore) {
                lowestScore = dScore;
                weakest = d;
            }
        }

        if (weakest != null) {
            panel.add(new JLabel("--------------------------------------------------"));
            panel.add(new JLabel("GAP ANALYSIS"));
            panel.add(new JLabel("Lowest Dimension: " + weakest.getName() + " (Score: " + String.format("%.1f", lowestScore) + ")"));
            panel.add(new JLabel("Gap Value: " + String.format("%.1f", 5.0 - lowestScore)));

            String label = (lowestScore >= 4.0) ? "Excellent" : (lowestScore >= 3.0) ? "Good" : "Needs Improvement";
            panel.add(new JLabel("Quality Level: " + label));
            panel.add(new JLabel("This dimension has the lowest score and requires the most improvement."));
        }

        return panel;
    }
}