import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Класс представляющий сервер
class Server {
    private int id;
    private String name;
    private String ipAddress;
    private int consecutiveFailures;

    // Конструктор, геттеры и сеттеры

    public boolean isAvailable(Server server) {
        try {
            URL url = new URL("http://" + server.getIpAddress()); // Формируем URL для HTTP-запроса
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Проверяем код состояния HTTP-ответа
            int responseCode = connection.getResponseCode();

            // Закрываем соединение
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
            // Установите соединение с базой данных (здесь предполагается использование MySQL)
            String jdbcURL = "jdbc:mysql://localhost:3306/yourdb"; // Замените на вашу базу данных
            String dbUsername = "yourusername"; // Замените на ваше имя пользователя
            String dbPassword = "yourpassword"; // Замените на ваш пароль
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
        // Реализация метода для уведомления пользователя об ошибке
        // Здесь может быть отправка уведомления по электронной почте, SMS и т. д.
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
}

// Класс представляющий ошибки
class Error {
    private int id;
    private long timestamp;
    private String errorText;
    private Server server;

    // Конструктор, геттеры и сеттеры
}

public class HealthCheckApp {
    private List<Server> servers = new ArrayList<Server>();
    private List<Error> errors = new ArrayList<Error>();

    public void addServer(Server server) {
        // Логика добавления сервера в базу данных
    }

    public void removeServer(Server server) {
        // Логика удаления сервера из базы данных
    }

    public void editServer(Server server) {
        // Логика редактирования сервера в базе данных
    }

    public List<Error> getErrors(Server server) {
        // Логика получения ошибок для определенного сервера
    }

    public void notifyUser(Server server, String errorMessage) {
        // Логика уведомления пользователя об ошибке
    }


    public Server createServerFromDatabase(int serverId) {
        Server server = null;
        try {
            // Установите соединение с базой данных
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return server;
    }


    public static void main(String[] args) {
        // Создаем экземпляр HealthCheckApp и добавляем серверы
        HealthCheckApp app = new HealthCheckApp();
        Server server1 = new Server(1, "Server1", "192.168.1.1");
        Server server2 = new Server(2, "Server2", "192.168.1.2");
        app.addServer(server1);
        app.addServer(server2);

        // Вызываем метод для создания объекта Server из базы данных
        int serverIdFromDatabase = 1; // Замените на фактический идентификатор сервера
        Server serverFromDatabase = app.createServerFromDatabase(serverIdFromDatabase);

        // Проверяем доступность сервера
        if (serverFromDatabase != null) {
            if (app.isAvailable(serverFromDatabase)) {
                // Сервер доступен
            } else {
                // Сервер недоступен, записываем ошибку
                app.logError(serverFromDatabase);
            }
        }
    }

}
