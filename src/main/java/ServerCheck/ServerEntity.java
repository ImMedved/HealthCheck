package ServerCheck;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class ServerEntity {
    @Id
    private Long id;
    private String name;
    private String address;
    private int errorCounter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getErrorCounter() {
        return errorCounter;
    }

    public void setErrorCounter(int errorCounter) {
        this.errorCounter = errorCounter;
    }
}
