package Model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.xml.crypto.Data;

public class DatabaseConnection {
    private ArrayList<Connection> connections;
    private MysqlDataSource ms;

    public DatabaseConnection(){
        ms = new MysqlDataSource();
        ms.setPort(3306);
        ms.setDatabaseName("shappdb");
        ms.setUser("user");
        ms.setPassword("Giukatfra.0202");
        ms.setUrl("jdbc:mysql://localhost:3306/shappdb");
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            ms.setConnectTimeout(15);
            ms.setAutoReconnect(false);
            connection = ms.getConnection();

            System.out.println("Connected to the database!");
        } catch (SQLException e) {

            System.err.println("Connection error: " + e.getMessage());

        }
        connections.add(connection);

        return connection;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public MysqlDataSource getMs() {
        return ms;
    }

    public void setMs(MysqlDataSource ms) {
        this.ms = ms;
    }

    public void releaseDatabaseConnection(){
        Connection conn = connections.get(connections.size()-1);
        try{
            conn.close();
        }catch (SQLException e) {
            System.out.println("Problem closing last connection..." + "\n" + e.getErrorCode() + " " + e.getMessage());
            e.printStackTrace();
        }
    }
}