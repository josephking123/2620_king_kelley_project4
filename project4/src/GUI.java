import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;

    private WordCloudGenerator wordCloudGenerator;

    public GUI(WordCloudGenerator wordCloudGenerator) {
        this.wordCloudGenerator = wordCloudGenerator;
       
        frame = new JFrame("Word Cloud Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);


        JTextArea wordCloudArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(wordCloudArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        JButton generateButton = new JButton("Generate Word Cloud");
        generateButton.addActionListener(e -> generateWordCloud());
        frame.add(generateButton, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private void applyFilters() {
       //will contain methods to add filters whenever filters are added
    }

    private void generateWordCloud() {

    }
}
