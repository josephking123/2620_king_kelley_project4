import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.kennycason.kumo.WordCloud;

public class GUI extends JFrame {

    private JFileChooser fileChooser;
    private JButton loadButton;
    private JButton generateButton;
    private JButton resetButton; 
    private JPanel infoPanel;
    private JPanel filterPanel;
    private JLabel image;
    private List<Filter> filters;
    private DataHandler handler;
    private WordCloudGenerator cloudGen;
    private WordCloud cloud;
    private JProgressBar progressBar;

    public GUI() {
        super("Word Cloud Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        image = new JLabel();
        JScrollPane scrollPane = new JScrollPane(image);
        add(scrollPane, BorderLayout.CENTER);

        filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        generateButton = new JButton("Generate Word Cloud");
        loadButton = new JButton("Select Folder");
        resetButton = new JButton("Reset"); // Initialize the reset button

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

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

        resetButton.addActionListener(new ActionListener() { // Action listener for reset button
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(loadButton, BorderLayout.WEST);
        infoPanel.add(generateButton, BorderLayout.CENTER);
        infoPanel.add(resetButton, BorderLayout.EAST); // Add reset button to info panel
        infoPanel.add(progressBar, BorderLayout.SOUTH);

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

        String[] customFilters = { "Words ending in 'ed'", "Words ending in 'ist'" };
        String[] customRegex = { "\\b\\w*ed\\b", "\\b\\w*ist\\b" };
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
            System.out.println("Selected folder path: " + folderPath); // Debug statement
            progressBar.setValue(0);
            handler = new DataHandler(folderPath, progressBar);
            Thread thread = new Thread(handler);
            thread.start();
            JOptionPane.showMessageDialog(this, "Folder selected successfully!");
        }
    }

    private void generateWordCloud() {
        this.cloudGen = new WordCloudGenerator(handler.getWordFrequencyMap(), handler.getEncounteredWords());
        this.cloud = cloudGen.getCloud();
        BufferedImage img = cloud.getBufferedImage();
        if (img == null) {
            System.out.println("WordCloud image is null!");
        } else {
            boolean hasWords = hasWordsInImage(img);
            if (hasWords) {
                System.out.println("WordCloud contains words!");
            } else {
                System.out.println("WordCloud is empty!");
            }
            ImageIcon icon = new ImageIcon(img);
            image.setIcon(icon);
            image.repaint(); // Repaint the JLabel to reflect changes
        }
    }
    
    private boolean hasWordsInImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = img.getRGB(x, y);
                if (pixel != Color.WHITE.getRGB()) { // Assuming the background is white
                    return true;
                }
            }
        }
        return false;
    }
    

    private void applyFilters() {
        Map<String, Integer> filteredWordFrequencyMap = new HashMap<>(handler.getWordFrequencyMap());
        for (Filter filter : filters) {
            filteredWordFrequencyMap = applyFilter(filteredWordFrequencyMap, filter);
        }
        this.cloudGen = new WordCloudGenerator(filteredWordFrequencyMap, handler.getEncounteredWords());
        this.cloud = cloudGen.getCloud();
        BufferedImage img = cloud.getBufferedImage();
        ImageIcon icon = new ImageIcon(img);
        image.setIcon(icon);
    }
    
    private Map<String, Integer> applyFilter(Map<String, Integer> wordFrequencyMap, Filter filter) {
        Map<String, Integer> filteredWordFrequencyMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            String word = entry.getKey();
            if (!filter.applyFilter(word)) {
                filteredWordFrequencyMap.put(word, entry.getValue());
            }
        }
        return filteredWordFrequencyMap;
    }

    private void reset() {
        progressBar.setValue(0);
        image.setIcon(null);
        image.setText(""); // Clear text
        for (Component component : filterPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                ((JCheckBox) component).setSelected(false); // Uncheck all checkboxes
            }
        }
    }
}
