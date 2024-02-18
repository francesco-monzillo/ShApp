package Model.DAOs;
import Model.Beans.Contract;
import Model.Beans.Orders;
import Model.Beans.ShippingProperty;
import Model.Beans.User;
import Model.Exceptions.AlreadyFoundUnivocalIdentifierException;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShippingPropertyDAO {

    private DataSource ds;

    public ShippingPropertyDAO(DataSource ds) {
        this.ds = ds;
    }


    public ArrayList<ShippingProperty> agreedProperties(Contract agreedContract) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT c.shipprop_id FROM CONTRACTOFFER AS c WHERE c.dispatcher_id = ? AND c.courier_id = ? ;");
            pS.setString(1, agreedContract.getDispatcher().getEmail());
            pS.setString(2, agreedContract.getCourier().getEmail());

            ResultSet rs = pS.executeQuery();

            ArrayList<ShippingProperty> props = new ArrayList<ShippingProperty>();
            while(rs.next()) {
                String shPrName = rs.getString(1);

                ShippingProperty sP = new ShippingProperty(shPrName);
                props.add(sP);
            }

            if(props.size() == 0)
                throw new NotFoundException("No properties found in this contract");
            else{
                return props;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a shippingProperties related to a contract in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }


    public ArrayList<ShippingProperty> getOrderProperties (Orders o) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT o.prop_id FROM ORDERPROPERTIES AS o WHERE o.order_id = ? ;");
            pS.setInt(1, o.getId());

            ResultSet rs = pS.executeQuery();

            ArrayList<ShippingProperty> props = new ArrayList<ShippingProperty>();

            while(rs.next()) {
                String shPrName = rs.getString(1);

                ShippingProperty sP = new ShippingProperty(shPrName);
                props.add(sP);
            }

            if(props.size() == 0)
                throw new NotFoundException("This order has no properties associated");
            else{
                return props;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving shipping properties associated to an order in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }


    public void setOrderProperties (int orderId, ArrayList<ShippingProperty> properties) throws Exception{
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);

        try{

            PreparedStatement pS = null;

            if(properties == null)
                throw new Exception("Not scannable properties array passed...");

            for(int i = 0; i < properties.size(); i++) {

                pS = conn.prepareStatement("INSERT INTO ORDERPROPERTIES VALUES (?, ?);");

                if(properties.get(i) == null)
                    throw new Exception("Trying to associate null Shipping Property to order... rollback");

                pS.setString(1, properties.get(i).getName());
                pS.setInt(2, orderId);


                int result = pS.executeUpdate();

                if(result == 0)
                    throw new Exception("Can't associate Property with name '"+properties.get(i).getName()+"' to an Order... executing rollback");

            }

            conn.commit();


        }catch (SQLException e){
            throw new Exception("Problem with associating a properties to an Order in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }


    public ArrayList<ShippingProperty> retrieveAll() throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM SHIPPINGPROPERTY;");
            ResultSet rs = ps.executeQuery();

            ArrayList<ShippingProperty> properties = new ArrayList<>();

            while(rs.next()){
                String propName = rs.getString(1);
                ShippingProperty currProp = new ShippingProperty(propName);

                properties.add(currProp);
            }

            if(properties.size() == 0)
                throw new NotFoundException("No properties found in db... Impossible... need to explore this problem");

            return properties;

        }catch (SQLException e){
            throw new Exception("Problem with retrieving all the properties in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }

}
