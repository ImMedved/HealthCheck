package com.kukharev.health.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServerService {

    private static final Logger logger = LoggerFactory.getLogger(ServerService.class);

    private ErrorRepository errorRepository;
    private final ServerRepository serverRepository;
    private LocalDateTime timestamp;

    @Autowired
    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Autowired
    public void setErrorRepository(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    public void invokeServerService(){
        List<ServerEntity> servers = serverRepository.findAll();
        for (ServerEntity server : servers) {
            checkServer(server.getAddress());
        }

    }

    public void checkServer(String serverUrl) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                logError(serverUrl, responseCode);
            }
        } catch (IOException e) {
            logger.error("Error occurred while checking server {}: {}", serverUrl, e.getMessage());
        }
    }

    public void logError(String serverUrl, int responseCode) {
        ServerEntity serverEntity = serverRepository.findByUrl(serverUrl);
        if (serverEntity != null) {
            ErrorEntity errorEntity = new ErrorEntity();
            errorEntity.setServer(serverEntity);
            errorEntity.setTimestamp(getTimestamp());
            errorEntity.setErrorCode(responseCode);
            errorRepository.save(errorEntity);
            serverEntity.setErrorCounter(serverEntity.getErrorCounter() + 1);
            serverRepository.save(serverEntity);
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}


