package com.kukharev.health.check;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servers")
public class ServerController {
    private final ServerService serverService;
    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }
}

