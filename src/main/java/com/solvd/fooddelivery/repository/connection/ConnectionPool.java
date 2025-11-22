package com.solvd.fooddelivery.repository.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ConnectionPool {

    private static ConnectionPool instance;

    private final List<Connection> connections;

    private ConnectionPool(){
        try{
            Class.forName(Config.DRIVER.getValue());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        int connectionPoolSize = Integer.parseInt(Config.POOL_SIZE.getValue());
        this.connections = new ArrayList<>(connectionPoolSize);
        IntStream.range(0, connectionPoolSize)
                .boxed().forEach(index -> connections.add(createConnection()));
    }

    private Connection createConnection() {

        Connection connection;
        try {

            connection = DriverManager.getConnection(
                    Config.URL.getValue(),
                    Config.USERNAME.getValue(),
                    Config.PASSWORD.getValue());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public Connection getConnection() {
        synchronized (connections){
            if (connections.isEmpty()){
                try {
                    while (connections.isEmpty()){
                        connections.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return connections.removeLast();
    }

    public static synchronized ConnectionPool getInstance() {

        if (instance == null){
            instance = new ConnectionPool();
        }
        return instance;
    }

    public void releaseConnection(Connection connection){
        if (connection == null){
            throw new RuntimeException("Connection is null");
        }
        synchronized (connections){
            connections.add(connection);
            connections.notifyAll();
        }
    }
}
