package ServerCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ServerService {
    private final ServerRepository serverRepository;
    private final ErrorNotificationService errorNotificationService;
    private final RestTemplate restTemplate;

    @Autowired
    public ServerService(
            ServerRepository serverRepository,
            ErrorNotificationService errorNotificationService,
            RestTemplate restTemplate
    ) {
        this.serverRepository = serverRepository;
        this.errorNotificationService = errorNotificationService;
        this.restTemplate = restTemplate;
    }

    public List<ServerEntity> getAllServers() {
        return serverRepository.findAll();
    }

    public void checkServers() {
        List<ServerEntity> servers = serverRepository.findAll();

        for (ServerEntity server : servers) {
            String url = server.getAddress();

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode() != HttpStatus.OK) {
                    errorNotificationService.notifyError(server);
                }
            } catch (RestClientException e) {
                errorNotificationService.notifyError(server);
            }
        }
    }
}
