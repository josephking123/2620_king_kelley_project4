import javax.swing.*;

import com.kennycason.kumo.WordCloud;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {

    private JFileChooser fileChooser;
    private JButton loadButton;
    private JButton generateButton;
    private JPanel infoPanel;
    private JPanel filterPanel;
    private JLabel image;
    private List<Filter> filters;
    private DataHandler handler;
    private WordCloudGenerator cloudGen;
    private WordCloud cloud;

    public GUI() {
        super("Word Cloud Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JScrollPane scrollPane = new JScrollPane(image);
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

        initializeFilters();

        setVisible(true);
    }

    private void initializeFilters() {
        filters = new ArrayList<>();
        filters.add(new Filter("Words ending in 'ing'", "\\b\\w*ing\\b"));
        filters.add(new Filter("Words containing 'ough'", "\\b\\w*ough\\w*\\b"));
        filters.add(new Filter("Words ending in 'ism'", "\\b\\w*ism\\b"));
    
        String[] customFilters = {"Words ending in 'ed'", "Words ending in 'ist'"};
        String[] customRegex = {"\\b\\w*ed\\b", "\\b\\w*ist\\b"};
        for (int i = 0; i < customFilters.length; i++) {
            filters.add(new Filter(customFilters[i], customRegex[i]));
        }

        for (Filter filter : filters) {
            JCheckBox checkBox = new JCheckBox(filter.getName());
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    applyFilters();
                }
            });
            filterPanel.add(checkBox);
        }
    }

    private void selectFolder() {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String folderPath = fileChooser.getSelectedFile().getPath();
            handler = new DataHandler(folderPath);
            Thread thread = new Thread(handler);
            thread.start();
            JOptionPane.showMessageDialog(this, "Folder selected successfully!");
        }
    }

    private void generateWordCloud() {
        // Implement word cloud generation here
        this.cloudGen = new WordCloudGenerator(handler.getWordFrequencyMap(), handler.getEncounteredWords());
        this.cloud = cloudGen.getCloud();
        BufferedImage img = cloud.getBufferedImage();
        image = new JLabel(new ImageIcon(img));
    }

    private void applyFilters() {
        String text = image.getText();
        for (Filter filter : filters) {
            text = applyFilter(text, filter);
        }
        image.setText(text);
    }

    private String applyFilter(String text, Filter filter) {
        StringBuilder filteredText = new StringBuilder();
        for (String word : text.split("\\s+")) {
            if (!filter.applyFilter(word)) {
                filteredText.append(word).append(" ");
            }
        }
        return filteredText.toString().trim();
    }
}
