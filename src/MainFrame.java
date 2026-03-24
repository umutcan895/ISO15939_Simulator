import javax.swing.*;
import java.awt.*;
public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel= new JPanel(cardLayout);



    public MainFrame() {

        setTitle("ISO 15939 SImulator");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createStep1Panel(), "Step 1");
        mainPanel.add(createStep2Panel("Education LMS"), "Step 2" );
        mainPanel.add(createStep3Panel(),"Step 3");



this.add(mainPanel);
this.setVisible(true);


    }


    private JPanel createStep1Panel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JLabel step1Label = new JLabel("Lutfen bir Seneryo Secini: ");

        String[] scenarios = {"Education LMS", "Health INFO System"};
        JComboBox<String> combo = new JComboBox<>(scenarios);
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            cardLayout.show(mainPanel,"Step 2");

        });

        panel.add(step1Label);
        panel.add(combo);
        panel.add(nextButton);

        return panel;

    }
    private JPanel createStep2Panel(String scenarioName ) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Scenario; " + scenarioName + " - Seletc Dimensions");
        String[] dimensions = {"usability ", "Reliability ", "Maintainability"};
        JList<String> list = new JList<>(dimensions);

        JButton nextButton = new JButton("Next: Enter THe Values");
        nextButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Step 3");
        });
            panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel(new GridLayout(4,1,10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        JLabel label = new JLabel("Metrik Degerini Griniz (0_100): " + SwingConstants.CENTER);
        JTextField inputField = new JTextField();

        JButton calculationButton = new JButton("Hesepla");
        calculationButton.addActionListener(e -> {
            try {
                double rawData = Double.parseDouble(inputField.getText());
                double normalizedSCore = (rawData / 100.0) * 5.0;
                String resultMessage = String.format("Ham Data: %.2f\nNormalize Edilmis Skor (1_5): %.2f", rawData, normalizedSCore);
                JOptionPane.showMessageDialog(this, resultMessage, "Hesaplama Sonucu ", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lutfen Gecerli Deger Girin ","Hata", JOptionPane.ERROR_MESSAGE);

            }

        });
        panel.add(label);
        panel.add(inputField);
        panel.add(new JLabel(""));
        panel.add(calculationButton);
        return panel;

    }



}
