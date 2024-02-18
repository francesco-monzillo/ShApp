package Model.Beans;

import java.sql.Connection;
import java.sql.Date;

import com.mysql.cj.jdbc.MysqlDataSource;
import Model.DAOs.UserDAO;
import Model.DatabaseConnection;

public class Main {

    public static void main (String[] args){
        DatabaseConnection dbC = new DatabaseConnection();
        UserDAO uD = new UserDAO(dbC.getMs());

        //Scrivere istruzioni per testare le operazioni CRUD di base del DAO...

    }

}
