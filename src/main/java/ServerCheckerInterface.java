import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface ServerCheckerInterface {
    public default boolean isServerAvailable(String serverUrl) {
        try {
            // Создаем объект URL для заданного сервера
            URL url = new URL(serverUrl);

            // Открываем HTTP-соединение
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Получаем код состояния HTTP-ответа
            int responseCode = connection.getResponseCode();

            // Закрываем соединение
            connection.disconnect();

            // Если код состояния равен 200, сервер доступен, возвращаем false
            return responseCode != 200;
        } catch (IOException e) {
            // Обработка исключения, если что-то пошло не так (например, сервер недоступен)
            e.printStackTrace();
            return true; // Считаем сервер недоступным в случае ошибки
        }
    }
}

