package Model.DAOs;

import Model.Beans.AssignmentDispatcher;
import Model.Beans.State;
import Model.Beans.Updates;
import Model.Beans.User;
import Model.Exceptions.AlreadyFoundUnivocalIdentifierException;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AssignmentDispatcherDAO {
    private DataSource ds;

    public AssignmentDispatcherDAO(DataSource ds) {
        this.ds = ds;
    }


    /*public AssignmentDispatcher access(String email, String password) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ASSIGNMENTDISPATCHER AS a WHERE a.email = ? AND a.password = ?");
            pS.setString(1,email);
            pS.setString(2,password);

            ResultSet rS = pS.executeQuery();

            if(rS.next()){
                String name = rS.getString(1);

                AssignmentDispatcher assignmentDispatcher = new AssignmentDispatcher(name, email);
                return assignmentDispatcher;
            }
            else{
                throw new NotFoundException("Access not possible: Wrong email or password...");
            }

        }catch (SQLException e){
            throw new SQLException("Problem with an access in AssignmentDispatcherDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }*/

    public AssignmentDispatcher retrieve(String email) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ASSIGNMENTDISPATCHER AS a WHERE a.email = ? ;");
            pS.setString(1,email);

            ResultSet rs = pS.executeQuery();

            if(rs.next()) {
                String assName = rs.getString(1);

                AssignmentDispatcher aD = new AssignmentDispatcher(assName, email);
                return aD;
            }else{
                throw new NotFoundException("AssignmentDispatcher with email '"+email+"' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving an AssignmentDispatcher in AssignmentDispatcherDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public AssignmentDispatcher retrieveByName(String name) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ASSIGNMENTDISPATCHER AS a WHERE a.name = ? ;");
            pS.setString(1,name);

            ResultSet rs = pS.executeQuery();

            if(rs.next()) {
                String email = rs.getString(2);

                AssignmentDispatcher aD = new AssignmentDispatcher(name, email);
                return aD;
            }else{
                throw new NotFoundException("AssignmentDispatcher with name '"+name+"' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving an AssignmentDispatcher in AssignmentDispatcherDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public AssignmentDispatcher create(String name, String email) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("INSERT INTO ASSIGNMENTDISPATCHER VALUES (?, ?);");
            pS.setString(1,name);
            pS.setString(2,email);

            int inserted = pS.executeUpdate();

            if(inserted == 0) {
                throw new AlreadyFoundUnivocalIdentifierException("Already found an Assignment Dispatcher with name '" + name + "'");
            }else {
                return new AssignmentDispatcher(name, email);
            }

        }catch (SQLException e){
            throw new Exception("Problem with creating an Assignment Dispatcher in AssignmentDispatcherDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public void modifyEmail(String name, String email) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("UPDATE ASSIGNMENTDISPATCHER AS a SET a.email = ? WHERE a.name = ? ;");

            pS.setString(1,email);
            pS.setString(2,name);

            int updated = pS.executeUpdate();

            if(updated == 0) {
                throw new AlreadyFoundUnivocalIdentifierException("Already found an Assignment Dispatcher with name '" + name + "'");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a shippingProperties related to a contract in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public void modifyPassword(String name, String password) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("UPDATE ASSIGNMENTDISPATCHER AS a SET a.password = ? WHERE a.name = ? ;");

            pS.setString(1,password);
            pS.setString(2,name);

            int updated = pS.executeUpdate();

            if(updated == 0) {
                throw new AlreadyFoundUnivocalIdentifierException("Already found an Assignment Dispatcher with name '" + name + "'");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a shippingProperties related to a contract in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }
}
