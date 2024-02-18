package Model.DAOs;
import Model.Beans.User;
import Model.Exceptions.AlreadyFoundUnivocalIdentifierException;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private DataSource ds;

    public UserDAO(DataSource ds) {
        this.ds = ds;
    }

    public User create(String email, String name, String surname, String phoneNumber) throws AlreadyFoundUnivocalIdentifierException, Exception{

        Connection conn = ds.getConnection();

        try{

            /*if(retrieve(email) != null)
                throw new AlreadyFoundUnivocalIdentifierException("Email already taken, maybe you forgot your password...");*/

            PreparedStatement pS = conn.prepareStatement("INSERT INTO USER(email, name, surname, phoneNumber) VALUES(?, ?, ?, ?)");
            pS.setString(1, email);
            pS.setString(2, name);
            pS.setString(3, surname);
            pS.setString(4, phoneNumber);

            int res = pS.executeUpdate();

            if(res == 1) {
                User u = new User(email, name, surname, phoneNumber);
                return u;
            }else {
                throw new Exception("Something wrong with user insertion");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving an user in UserDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    public User retrieve(String email) throws NotFoundException, Exception {
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("SELECT * FROM user AS u WHERE u.email = ?");
            pS.setString(1, email);
            ResultSet rS = pS.executeQuery();

            if(rS.next()){
                String name = rS.getString(2);
                String surname = rS.getString(3);
                String phoneNumber = rS.getString(4);
                User u = new User(email,name,surname,phoneNumber);

                return u;
            }else{
                throw new NotFoundException("User with email: " +email+" not found");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving an user in UserDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public void delete(String email) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("DELETE FROM USER AS u WHERE u.email = ?");
            pS.setString(1, email);

            int update = pS.executeUpdate();

            if (update == 1);
            else throw new Exception("Impossible user removing: Wrong email or password given ");

        }catch (SQLException e){
            throw new SQLException("Problem with removing an user in UserDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }

    public void modifyEmail(String oldEmail, String newEmail) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("UPDATE USER AS u SET u.email=? WHERE u.email=?");
            pS.setString(1,newEmail);
            pS.setString(2,oldEmail);

            int numOfRes = pS.executeUpdate();

            if(numOfRes == 0)
                throw new NotFoundException("Impossible email update... oldEmail not found");


        }catch (SQLException e){
            throw new SQLException("Problem with the edit of an email in UserDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }

    public void modifyName(String email,String newName) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("UPDATE USER AS u SET u.name=? WHERE u.email=?");
            pS.setString(1,newName);
            pS.setString(2,email);

            int numOfRes = pS.executeUpdate();

            if(numOfRes == 0)
                throw new NotFoundException("Impossible name update... email not found");

            return;

        }catch (SQLException e){
            throw new SQLException("Problem with the edit of an email in UserDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }

    public void modifySurname(String email,String newSurname) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("UPDATE USER AS u SET u.surname=? WHERE u.email=?");
            pS.setString(1,newSurname);
            pS.setString(2,email);

            int numOfRes = pS.executeUpdate();

            if(numOfRes == 0)
                throw new NotFoundException("Impossible email update... oldEmail not found");

            return;

        }catch (SQLException e){
            throw new SQLException("Problem with the edit of an email in UserDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }



    public DataSource getDs() {
        return ds;
    }

    public void setDs(DataSource ds) {
        this.ds = ds;
    }
}
