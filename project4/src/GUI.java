import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {

    private JButton loadFilesButton;
    private JButton createWordCloudButton;
    private JPanel buttonPanel;
    private WordCloudGenerator wordCloudGenerator;

    public GUI() {
        super("Gutenberg Word Cloud");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        wordCloudGenerator = new WordCloudGenerator();

        buttonPanel = new JPanel();
        loadFilesButton = new JButton("Load Files from Gutenberg Library");
        createWordCloudButton = new JButton("Generate Word Cloud");

        loadFilesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //will contain method from FileReader class that reads the selected files
            }
        });

        createWordCloudButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //will contain method from WordCloudGenerator that creates the word cloud
            }
        });

        buttonPanel.add(loadFilesButton);
        buttonPanel.add(createWordCloudButton);

        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);
    }
}