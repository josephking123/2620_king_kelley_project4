import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String folderPath = "lib\\gutenberg-data";

            DataHandler dataHandler = new DataHandler(folderPath);
            WordCloudGenerator wordCloudGenerator = new WordCloudGenerator();

            GUI gui = new GUI(wordCloudGenerator);

            Thread dataThread = new Thread(dataHandler);
            dataThread.start();
        });
    }
}

