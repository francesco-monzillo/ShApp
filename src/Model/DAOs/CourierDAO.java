package Model.DAOs;
import Model.Beans.AssignmentDispatcher;
import Model.Beans.Contract;
import Model.Beans.Courier;
import Model.Beans.ShippingProperty;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class CourierDAO {

    private DataSource ds;

    public CourierDAO(DataSource ds) {
        this.ds = ds;
    }


    /*public Courier access(String email, String password) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("SELECT * FROM COURIER AS c WHERE c.email = ? AND c.password = ?");
            pS.setString(1,email);
            pS.setString(2,password);

            ResultSet rS = pS.executeQuery();

            if(rS.next()){
                String name = rS.getString(1);

                Courier courier = new Courier(name, email, password);
                return courier;
            }
            else{
                throw new NotFoundException("Access not possible: Wrong email or password...");
            }

        }catch (SQLException e){
            throw new SQLException("Problem with an access in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }*/

    public Courier retrieve(String email) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM COURIER AS c WHERE c.email = ? ;");
            pS.setString(1, email);

            ResultSet rs = pS.executeQuery();

            if(rs.next()) {
                String name = rs.getString(1);

                Courier  cou = new Courier(name, email);

                return cou;
            }else{
                throw new NotFoundException("Courier with email '" + email + "' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a Courier in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    public Courier retrieveByName(String name) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM COURIER AS c WHERE c.name = ? ;");
            pS.setString(1, name);

            ResultSet rs = pS.executeQuery();

            if(rs.next()) {
                String email = rs.getString(2);

                Courier  cou = new Courier(name, email);

                return cou;
            }else{
                throw new NotFoundException("Courier with name '" + name + "' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a Courier in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }



    public ArrayList<Courier> retrieveAll() throws Exception {

        Connection conn = ds.getConnection();

        try {

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM COURIER;");

            ResultSet rs = pS.executeQuery();

            ArrayList<Courier> couriers = new ArrayList<>();

            while (rs.next()) {

                String name = rs.getString(1);
                String email = rs.getString(2);

                Courier cou = new Courier(name, email);

                couriers.add(cou);
            }
            if (couriers.size() == 0) {
                throw new NotFoundException("No couriers found in DB");
            } else {
                return couriers;
            }

        } catch (SQLException e) {
            throw new Exception("Problem with retrieving a Courier in CourierDAO:\n" + e.getMessage() + "\n" + e.getSQLState());
        } finally {
            conn.close();
        }
    }

    public Courier create(String name, String email) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("INSERT INTO COURIER VALUES (?, ?);");
            pS.setString(1, name);
            pS.setString(2, email);

            int result = pS.executeUpdate();

            if(result > 0) {

                Courier  cou = new Courier(name, email);

                return cou;
            }else{
                throw new NotFoundException("Courier with name '" + name+ "' already exists");
            }

        }catch (SQLException e){
            throw new Exception("Problem with creating a Courier in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    public void delete(String name) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("DELETE FROM COURIER AS c WHERE c.name = ? ;");
            pS.setString(1, name);

            int result = pS.executeUpdate();

            if(result == 0) {
                throw new NotFoundException("Courier with name '" + name+ "' already exists");
            }

        }catch (SQLException e){
            throw new Exception("Problem with creating a Courier in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    public void modifyEmail(String name, String email) throws Exception{

        Connection conn = ds.getConnection();

        try{

            try{
                retrieve(name);
            }catch (NotFoundException e){
                throw new NotFoundException("Can't modify the email of Courier '"+name+"', courier not found");
            }

            PreparedStatement pS = conn.prepareStatement("UPDATE COURIER AS c SET c.email = ? WHERE c.name = ? ;");
            pS.setString(1, email);
            pS.setString(2, name);

            int result = pS.executeUpdate();

            if(result == 0){
                throw new NotFoundException("Courier with name '" + name+ "' not found");
            }

        }catch (SQLException e){
            throw new Exception("Problem with modifying a Courier email in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    /*public void modifyPassword(String name, String oldPassword, String newPassword) throws Exception{

        Connection conn = ds.getConnection();

        try{

            try{
                Courier c = retrieve(name);
                if(! (c.getPassword().equals(oldPassword)) )
                    throw new Exception("Can't modify the password of Courier '"+name+"' , wrong password");
            }catch (NotFoundException e){
                throw new NotFoundException("Can't modify the password of Courier '"+name+"' , courier not found");
            }

            PreparedStatement pS = conn.prepareStatement("UPDATE COURIER AS c SET c.password = ? WHERE c.name = ? ;");
            pS.setString(1, newPassword);
            pS.setString(2, name);

            int result = pS.executeUpdate();

            if(result == 0){
                throw new NotFoundException("Courier with name '" + name+ "' not found");
            }

        }catch (SQLException e){
            throw new Exception("Problem with modifying a Courier email in CourierDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }*/


}
