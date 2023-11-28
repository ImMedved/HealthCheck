package ServerCheck;

import org.springframework.stereotype.Service;
// Я временно сделал вывод в консоль ide, все это можно стереть и писать новый вывод,
// но для примера, пока нет вывода из тз, усть будет так. Поменяю в самом конце разработки.
@Service
public class ErrorNotificationService {
    public void notifyError(ServerEntity server) {
        System.out.println("Ошибка на сервере: " + server.getName() + " (" + server.getAddress() + ")");
        server.setErrorCounter(server.getErrorCounter() + 1);
        System.out.println("Счетчик ошибок для сервера " + server.getName() + ": " + server.getErrorCounter());

        if (server.getErrorCounter() >= 3) {
            System.out.println("Счетчик ошибок для сервера " + server.getName() + " превысил порог");
            // Тут пишем логику для запуска уведомления о достижении порога ошибок
            // Куда она должна отправляться и как?
        }
    }
}