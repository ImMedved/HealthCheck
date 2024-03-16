CREATE TABLE server (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    error_counter INT
);

INSERT INTO server (name, address, error_counter) VALUES ('Server 1', 'http://fake-server-1.com', 0);
INSERT INTO server (name, address, error_counter) VALUES ('Server 2', 'http://fake-server-2.com', 3);
