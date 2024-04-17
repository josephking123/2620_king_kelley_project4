import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    private JFileChooser fileChooser;
    private JButton loadButton;
    private JButton generateButton;
    private JPanel infoPanel;
    private JPanel filterPanel;
    private JTextArea wordCloudArea;

    public GUI() {
        super("Word Cloud Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        wordCloudArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(wordCloudArea);
        add(scrollPane, BorderLayout.CENTER);

        filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        generateButton = new JButton("Generate Word Cloud");
        loadButton = new JButton("Select Folder");

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFolder();
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateWordCloud();
            }
        });

        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(loadButton, BorderLayout.WEST);
        infoPanel.add(generateButton, BorderLayout.EAST);

        add(infoPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void selectFolder() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String folderPath = fileChooser.getSelectedFile().getPath();
            DataHandler dataHandler = new DataHandler(folderPath);
            Thread thread = new Thread(dataHandler);
            thread.start();
            JOptionPane.showMessageDialog(this, "Folder selected successfully!");
        }
    }

    private void generateWordCloud() {
        // Implement word cloud generation here
    }
}