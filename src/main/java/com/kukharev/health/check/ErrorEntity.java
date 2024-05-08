package com.kukharev.health.check;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "errors")
public class ErrorEntity {
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;

    @ManyToOne
    @JoinColumn(name = "server_id", referencedColumnName = "id")
    private ServerEntity server;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "error_code")
    private int errorCode;

    // Getters and setters
    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setErrorCode(int errorCode){this.errorCode = errorCode; }

    public Long getId() {
        return Id;
    }
}


