package entrypoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorldApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);

        HelloWorldController controller = new HelloWorldController();

        String result = controller.sayHello();

        System.out.println(result);
    }
}
