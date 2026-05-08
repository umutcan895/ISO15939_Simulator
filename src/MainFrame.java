import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private Scenerio activeScenerio;

    // Step 1 verileri
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
            if(userField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lutfen kullanici adini giriniz!");
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
            String selected = (String) combo.getSelectedItem();
            setupActiveScenerio(selected); // Senaryoyu hazirla
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

        // Örnek boyut ve metrik ekleme (Senin kodundaki mantikla)
        Dimension usability = new Dimension("Usability", 1.0);
        Metric susScore = new Metric("SUS Score", "Higher", 1.0, 0.0, 100.0, "Points");
        usability.addMetric(susScore);

        activeScenerio.addDimension(usability);
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Step 3: Measurement Plan", SwingConstants.CENTER);

        // Metrikleri tabloya dokme
        String[] columns = {"Metric", "Coefficient", "Direction", "Unit"};
        ArrayList<Metric> allMetrics = activeScenerio.getDimensions().get(0).getMetrics();
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

        ArrayList<Metric> metrics = activeScenerio.getDimensions().get(0).getMetrics();
        ArrayList<JTextField> fields = new ArrayList<>();

        for (Metric m : metrics) {
            panel.add(new JLabel(m.getName() + " (" + m.getUnit() + "):"));
            JTextField f = new JTextField();
            fields.add(f);
            panel.add(f);
        }

        JButton nextButton = new JButton("Analyse Results");
        nextButton.addActionListener(e -> {
            try {
                for (int i = 0; i < metrics.size(); i++) {
                    double val = Double.parseDouble(fields.get(i).getText());
                    metrics.get(i).setRawValue(val);
                }
                mainPanel.add(createStep5Panel(), "Step 5");
                cardLayout.show(mainPanel, "Step 5");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lutfen gecerli sayisal degerler giriniz!");
            }
        });

        panel.add(new JLabel(""));
        panel.add(nextButton);

        return panel;
    }

    private JPanel createStep5Panel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        double finalScore = activeScenerio.calculateFinalQualityScore();

        panel.add(new JLabel("Step 5: Final Analysis", SwingConstants.CENTER));
        panel.add(new JLabel("User: " + userName));
        panel.add(new JLabel("Overall Quality Score: " + String.format("%.2f", finalScore)));
        panel.add(new JLabel("Gap Analysis (Goal 5.0): " + String.format("%.2f", 5.0 - finalScore)));

        return panel;
    }
}