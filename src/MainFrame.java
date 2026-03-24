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


        JPanel step1 = createStep1Panel();
        JPanel step2 = new JPanel();
        step2.add(new JLabel("Tebrik ederim, 2. adima gectiniz ."));

mainPanel.add(step1,"Step 1");
mainPanel.add(step2,"Step 2");
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

}
