import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Отдельный класс для взаимодействия с базой данных
class DatabaseManager {
    // Методы для добавления, удаления и редактирования серверов в базе данных
    // Методы для получения данных о серверах
}

// Отдельный класс для проверки доступности серверов
class ServerChecker extends Thread implements ServerCheckerInterface {

}

// Отдельный класс для уведомлений
class Notifier {
    // Методы для отправки уведомлений (почта, SMS и т.д.)
}

// Класс для управления интерфейсом
class HealthCheckUI extends Thread {
    private final BlockingQueue<String> dataQueueName;
    private final BlockingQueue<String> dataQueueUrl;
    private JTextField nameField;
    private JTextField urlField;

    public HealthCheckUI(BlockingQueue<String> dataQueueName, BlockingQueue<String> dataQueueUrl) {
        this.dataQueueName = dataQueueName;
        this.dataQueueUrl = dataQueueUrl;

    }

    @Override
    public void run() {

        //ниже — создание интерфейса
        JFrame frame = new JFrame("Health Check Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JTextArea serverListTextArea = new JTextArea();
        serverListTextArea.setEditable(false);
        panel.add(new JScrollPane(serverListTextArea));

        JPanel addServerPanel = new JPanel();
        nameField = new JTextField(15);
        urlField = new JTextField(15);
        JButton addButton = new JButton("Добавить сервер");

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String url = urlField.getText();
            nameField.setText("");
            urlField.setText("");
            try {
                dataQueueName.put(name);
            } catch (InterruptedException f) {
                f.printStackTrace();
            }
            try {
                dataQueueUrl.put(url);
            } catch (InterruptedException g) {
                g.printStackTrace();
            }

        });

        addServerPanel.add(new JLabel("Имя сервера:"));
        addServerPanel.add(nameField);
        addServerPanel.add(new JLabel("URL сервера:"));
        addServerPanel.add(urlField);
        addServerPanel.add(addButton);
        panel.add(addServerPanel);
    }
}



public class HealthCheckAppTemp {
    public static void main(String[] args) {
        BlockingQueue<String> dataQueueName = new ArrayBlockingQueue<>(10);
        BlockingQueue<String> dataQueueUrl = new ArrayBlockingQueue<>(10);


        Thread UITThread = new HealthCheckUI(dataQueueName, dataQueueUrl);

        //база серверов для примера
        String[] serverUrls = {
                "https://example.com",
                "https://another-example.com"
        };

        UITThread.start();

        // Создаем отдельный поток для каждой проверки сервера
        for (String serverUrl : serverUrls) {
            Thread serverThread = new Thread(() -> {
                ServerChecker serverChecker = new ServerChecker();
                boolean isAvailable = serverChecker.isServerAvailable(serverUrl);

                if (isAvailable) {
                    System.out.println("Сервер " + serverUrl + " недоступен.");
                } else {
                    System.out.println("Сервер " + serverUrl + " доступен.");
                }
            });

            // Запускаем потоки
            serverThread.start();
        }

    }
}



