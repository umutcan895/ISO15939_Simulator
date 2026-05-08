import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private JPanel stepIndicatorPanel;
    private Scenerio activeScenerio;
    private String userName, schoolName, sessionName;

    public MainFrame() {
        setTitle("ISO 15939 Measurement Process Simulator");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        stepIndicatorPanel = createStepIndicator("Profile");
        add(stepIndicatorPanel, BorderLayout.NORTH);

        mainPanel.add(createStep1Panel(), "Profile");
        mainPanel.add(createStep2Panel(), "Define");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createStepIndicator(String activeStep) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        String[] steps = {"Profile", "Define", "Plan", "Collect", "Analyse"};
        boolean foundActive = false;

        for (String step : steps) {
            JLabel label = new JLabel(step);
            if (step.equals(activeStep)) {
                label.setFont(new Font("Arial", Font.BOLD, 14));
                label.setForeground(Color.BLUE);
                foundActive = true;
            } else if (!foundActive) {
                label.setText(step + " ✓");
                label.setForeground(new Color(0, 128, 0));
            }
            panel.add(label);
        }
        return panel;
    }

    private void updateStepIndicator(String stepName) {
        remove(stepIndicatorPanel);
        stepIndicatorPanel = createStepIndicator(stepName);
        add(stepIndicatorPanel, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 200));

        JTextField userField = new JTextField();
        JTextField schoolField = new JTextField();
        JTextField sessionField = new JTextField();
        JButton nextButton = new JButton("Next");

        nextButton.addActionListener(e -> {
            if (userField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your username to continue.");
            } else if (schoolField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your school name to continue.");
            } else if (sessionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a session name to continue.");
            } else {
                userName = userField.getText();
                schoolName = schoolField.getText();
                sessionName = sessionField.getText();
                updateStepIndicator("Define");
                cardLayout.show(mainPanel, "Define");
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

        JRadioButton productRadio = new JRadioButton("Product Quality", true);
        JRadioButton processRadio = new JRadioButton("Process Quality");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(productRadio);
        typeGroup.add(processRadio);

        JRadioButton healthRadio = new JRadioButton("Health Mode", true);
        JRadioButton eduRadio = new JRadioButton("Education Mode");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(healthRadio);
        modeGroup.add(eduRadio);

        String[] eduScenarios = {"Scenario C - Team Alpha", "Scenario D - Team Beta"};
        JComboBox<String> scenarioCombo = new JComboBox<>(eduScenarios);

        JButton nextButton = new JButton("Next: Plan");
        nextButton.addActionListener(e -> {
            setupScenario((String) scenarioCombo.getSelectedItem());
            mainPanel.add(createStep3Panel(), "Plan");
            updateStepIndicator("Plan");
            cardLayout.show(mainPanel, "Plan");
        });

        panel.add(new JLabel("Select Quality Type:"));
        panel.add(productRadio); panel.add(processRadio);
        panel.add(new JLabel("Select Mode:"));
        panel.add(healthRadio); panel.add(eduRadio);
        panel.add(new JLabel("Select Scenario:"));
        panel.add(scenarioCombo);
        panel.add(nextButton);

        return panel;
    }

    private void setupScenario(String name) {
        activeScenerio = new Scenerio(name);
        Dimension usability = new Dimension("Usability", 25);
        usability.addMetric(new Metric("SUS score", "Higher", 50, 0, 100, "points"));
        usability.addMetric(new Metric("Onboarding time", "Lower", 50, 0, 60, "min"));

        Dimension perf = new Dimension("Perf. Efficiency", 20);
        perf.addMetric(new Metric("Video start time", "Lower", 50, 0, 15, "sec"));
        perf.addMetric(new Metric("Concurrent exams", "Higher", 50, 0, 600, "users"));

        activeScenerio.addDimension(usability);
        activeScenerio.addDimension(perf);
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Dimension", "Metric", "Coeff", "Direction", "Range", "Unit"};
        ArrayList<Object[]> rowList = new ArrayList<>();
        for (Dimension d : activeScenerio.getDimensions()) {
            for (Metric m : d.getMetrics()) {
                rowList.add(new Object[]{d.getName(), m.getName(), m.getCoefficient(), m.getDirection(), m.getMinRange()+"-"+m.getMaxRange(), m.getUnit()});
            }
        }

        Object[][] data = rowList.toArray(new Object[0][]);
        JTable table = new JTable(data, columns);
        JButton nextButton = new JButton("Next: Collect");

        nextButton.addActionListener(e -> {
            mainPanel.add(createStep4Panel(), "Collect");
            updateStepIndicator("Collect");
            cardLayout.show(mainPanel, "Collect");
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStep4Panel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        ArrayList<Metric> metrics = new ArrayList<>();
        ArrayList<JTextField> fields = new ArrayList<>();

        for (Dimension d : activeScenerio.getDimensions()) {
            for (Metric m : d.getMetrics()) {
                inputPanel.add(new JLabel(m.getName() + " (" + m.getUnit() + "):"));
                JTextField f = new JTextField("0");
                fields.add(f);
                inputPanel.add(f);
                metrics.add(m);
            }
        }

        JButton nextButton = new JButton("Analyse Results");
        nextButton.addActionListener(e -> {
            try {
                for (int i = 0; i < metrics.size(); i++) {
                    metrics.get(i).setRawValue(Double.parseDouble(fields.get(i).getText()));
                }
                mainPanel.add(createStep5Panel(), "Analyse");
                updateStepIndicator("Analyse");
                cardLayout.show(mainPanel, "Analyse");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter numeric values only.");
            }
        });

        panel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStep5Panel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension weakest = null;
        double minScore = 6.0;

        for (Dimension d : activeScenerio.getDimensions()) {
            double score = d.calculateDimensionScore();
            JPanel dPanel = new JPanel(new BorderLayout());
            dPanel.add(new JLabel(d.getName() + ": " + String.format("%.1f", score)), BorderLayout.WEST);
            JProgressBar bar = new JProgressBar(1, 5);
            bar.setValue((int) score);
            bar.setStringPainted(true);
            dPanel.add(bar, BorderLayout.CENTER);
            panel.add(dPanel);

            if (score < minScore) {
                minScore = score;
                weakest = d;
            }
        }

        if (weakest != null) {
            String label = minScore >= 4.5 ? "Excellent" : minScore >= 3.5 ? "Good" : minScore >= 2.5 ? "Needs Improvement" : "Poor";
            panel.add(new JLabel("Weakest: " + weakest.getName() + " (Score: " + String.format("%.1f", minScore) + ")"));
            panel.add(new JLabel("Gap: " + String.format("%.1f", 5.0 - minScore)));
            panel.add(new JLabel("Level: " + label));
            panel.add(new JLabel("This dimension has the lowest score and requires the most improvement."));
        }

        return panel;
    }
}