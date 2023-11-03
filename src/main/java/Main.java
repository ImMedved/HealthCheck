import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import javax.swing.*;
import java.awt.*;


class HealthCheckApp {
    private final JTextArea serverListTextArea;
    private final JTextField nameField;
    private final JTextField urlField;

    public HealthCheckApp() {

        HealthCheckApp app = new HealthCheckApp();
        Server server1 = new Server(1, "Server1", "192.168.1.1");
        Server server2 = new Server(2, "Server2", "192.168.1.2");
        app.addServer(server1);
        app.addServer(server2);

        //ниже — создание интерфейса
        JFrame frame = new JFrame("Health Check Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        serverListTextArea = new JTextArea();
        serverListTextArea.setEditable(false);
        panel.add(new JScrollPane(serverListTextArea));

        JPanel addServerPanel = new JPanel();
        nameField = new JTextField(15);
        urlField = new JTextField(15);
        JButton addButton = new JButton("Добавить сервер");

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String url = urlField.getText();
            serverListTextArea.append(name + " - " + url + "\n");
            nameField.setText("");
            urlField.setText("");
        });

        addServerPanel.add(new JLabel("Имя сервера:"));
        addServerPanel.add(nameField);
        addServerPanel.add(new JLabel("URL сервера:"));
        addServerPanel.add(urlField);
        addServerPanel.add(addButton);
        panel.add(addServerPanel);

        JButton startCheckButton = new JButton("Запустить проверку");
        panel.add(startCheckButton);

        String name = nameField.toString();
        String url = urlField.toString();

        startCheckButton.addActionListener(e -> {
            int serverIdFromDatabase = 1; // Как заменить на фактический идентификатор сервера?
            Server serverFromDatabase = app.createServerFromDatabase(serverIdFromDatabase);

            if (name != null && url != null) {
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        String url = urlField.getText();
                        serverListTextArea.append(name + " - " + url + "\n");


                        // Создаем объект Server
                        Server newServer = new Server(serverIdFromDatabase ,name, url);
                        newServer.setIpAddress(url);

                        app.addServer(newServer);

                        nameField.setText("");
                        urlField.setText("");
                    }
                });
            }

            // ExecutorService с фиксированным количеством потоков
            // Если сделать неограниченное кол-во потоков, все ведь просто упадет на каком-то из вызовов?
            ExecutorService executor = Executors.newFixedThreadPool(2); // Настройка количество потоков

            // Зацикливаем проверку всех серверов в базе данных
            while (true) {
                for (Server server : app.servers) {
                    // Выполняем проверку каждого сервера в отдельном потоке
                    executor.execute(() -> {
                        // Вызываем logError, если сервер недоступен
                        if (!app.isAvailable(server)) {
                            app.logError(server);
                        }
                    });
                }

                try {
                    // Пауза между проверками (по идее, нужно сделать отсчет от старта потока,
                    // тк поверка тоже занимает время, но по мне это не так важно, учитывая,
                    // что потоков все равно ограниченное кол-во.
                    Thread.sleep(60000); // 60 секунд (1 минута)
                } catch (InterruptedException exept) {
                    exept.printStackTrace();
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }



    private final List<Server> servers = new ArrayList<Server>();
    private final List<Error> errors = new ArrayList<Error>();

    public void addServer(Server server) {
        try {
            // Установите соединение с базой данных
            String jdbcURL = "jdbc:mysql://localhost:3306/yourdb";
            String dbUsername = "yourusername";
            String dbPassword = "yourpassword";
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);

            // Подготовьте SQL-запрос для вставки нового сервера
            String insertQuery = "INSERT INTO server (url, error_count, last_error_date) VALUES (?, 0, null)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, server.getIpAddress());
            insertStatement.executeUpdate();

            // Закрываем ресурсы
            insertStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    public void removeServer(Server server) {
        // Логика удаления сервера из базы данных
    }

    public void editServer(Server server) {
        // Логика редактирования сервера в базе данных
    }

    public List<Error> getErrors(Server server) {
        // Логика получения ошибок для определенного сервера (устарело)
        return(null);
    }

    public void notifyUser(Server server, String errorMessage) {
        // Логика уведомления пользователя об ошибке (класс alert)
    }

    // метод нужно переработать, так он работать не будет
    public Server createServerFromDatabase(int serverId) {
        Server server = null;
        try {
            // Устанавливаем соединение с базой данных

            String jdbcURL = "jdbc:mysql://localhost:3306/yourdb";
            String dbUsername = "yourusername";
            String dbPassword = "yourpassword";
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);

            // Запрос для извлечения данных сервера по его идентификатору
            String selectQuery = "SELECT * FROM server WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, serverId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Извлекаем данные из результата запроса и создаем объект Server
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String ipAddress = resultSet.getString("ip_address");
                int consecutiveFailures = resultSet.getInt("consecutive_failures");

                server = new Server(id, name, ipAddress);
                server.setConsecutiveFailures(consecutiveFailures);
            }

            // Закрываем ресурсы
            resultSet.close();
            selectStatement.close();
            connection.close();
        } catch (Exception e) { // а что будет, если запрос close не пройдет? Эксепшн тут только для красоты?
            e.printStackTrace();
        }
        return server;
    }



    public boolean isAvailable(Server server) {
        try {
            URL url = new URL("http://" + server.getIpAddress()); // Формируем URL для HTTP-запроса
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Проверяем код состояния HTTP-ответа
            int responseCode = connection.getResponseCode();

            connection.disconnect();

            // Если код состояния равен 200, сервер доступен (вернем false), иначе вернем true
            return responseCode != 200;
        } catch (Exception e) {
            // Обработка исключения, если что-то пошло не так
            e.printStackTrace();
            return true; // Считаем сервер недоступным в случае ошибки
        }
    }

    public void logError(Server server) {
        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/yourdb";
            String dbUsername = "yourusername";
            String dbPassword = "yourpassword";
            Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);

            // Проверяем, есть ли уже запись для этого сервера в таблице "server"
            String selectQuery = "SELECT * FROM server WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, server.getId());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int errorsInARow = resultSet.getInt("errors_in_a_row");

                // Если код состояния вернул 1 (ошибка), увеличиваем счетчик ошибок
                if (isAvailable(server)) {
                    // Если последний запрос был успешным, сбрасываем счетчик ошибок
                    errorsInARow = 0;
                } else {
                    errorsInARow++;
                }

                // Если счетчик ошибок достиг 3, вызываем метод алерта
                if (errorsInARow == 3) {
                    alert(server);
                }

                // Обновляем значение атрибута "errors_in_a_row" в базе данных
                String updateQuery = "UPDATE server SET errors_in_a_row = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setInt(1, errorsInARow);
                updateStatement.setInt(2, server.getId());
                updateStatement.executeUpdate();
            }

            // Закрываем ресурсы
            resultSet.close();
            selectStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для вызова алерта
    public void alert(Server server) {
        // Реализация метода для уведомления пользователя об ошибке (почта, смс, whatever)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HealthCheckApp::new);
    }
}


// Класс представляющий сервер
class Server {
    private int id;
    private String name;
    private String ipAddress;
    private int consecutiveFailures;

    public Server(int id, String name, String ipAddress) {
        
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConsecutiveFailures(int consecutiveFailures) {
        this.consecutiveFailures = consecutiveFailures;
    }

    public int getConsecutiveFailures() {
        return consecutiveFailures;
    }
}

// Класс представляющий ошибки
class Error {
    private int id;
    private long timestamp;
    private String errorText;
    private Server server;

    // Конструктор, геттеры и сеттеры
}