package Model.DAOs;
import Model.Beans.*;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class StateDAO {

    private DataSource ds;

    public StateDAO(DataSource ds) {
        this.ds = ds;
    }


    public State retrieve(String ph) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM STATE AS s WHERE s.phase = ? ;");
            pS.setString(1, ph);

            ResultSet rs = pS.executeQuery();

            if(rs.next()) {
                String description = rs.getString(2);

                State s = new State(ph,description);

                return s;
            }else{
                throw new NotFoundException("State with name '" + ph+ "' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a State in StateDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<State> retrieveAll() throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM STATE;");

            ResultSet rs = pS.executeQuery();

            ArrayList<State> states = new ArrayList<>();

            while(rs.next()) {
                String phase = rs.getString(1);
                String description = rs.getString(2);

                State s = new State(phase,description);

                states.add(s);
            }

            if(states.size() == 0){
                throw new NotFoundException("No Shipment States found... please insert them");
            }else{
                return states;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a State in StateDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<Updates> retrieveUpdatesByState(State s) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM UPDATES AS u WHERE u.state = ? ;");
            pS.setString(1, s.getPhase());

            ResultSet rs = pS.executeQuery();

            ArrayList<Updates> updates = new ArrayList<Updates>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String message = rs.getString(2);
                int orderId = rs.getInt(3);
                String statePhase = rs.getString(4);

                Date timeStamp = rs.getDate(5);

                Updates u = new Updates(id, message, new Orders(orderId), new State(statePhase,""), timeStamp);
                updates.add(u);
            }

            if(updates.size() == 0)
                throw new NotFoundException("No properties found in this contract");
            else{
                return updates;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a shippingProperties related to a contract in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<Orders> retrieveOrdersByState(State s) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT o.id, o.length, o.width, o.height, o.weight, o.creationDate, o.assignmentDate, o.expectedDeliveryDate, o.trackingCode, o.location, o.dispatcher_id, o.courier_id, o.user_id FROM ORDERS AS o, UPDATES AS u WHERE o.id = u.order_id AND u.id = ( SELECT MAX(us.id) FROM UPDATES AS us WHERE o.id = us.order_id ) AND u.state = ? ;");
            pS.setString(1, s.getPhase());

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int id = rs.getInt(1);
                int length = rs.getInt(2);
                int width = rs.getInt(3);
                int height = rs.getInt(4);
                int weight = rs.getInt(5);
                Date creationDate = rs.getDate(6);
                Date assignmentDate = rs.getDate(7);
                Date expectedDeliveryDate = rs.getDate(8);
                String trackingCode = rs.getString(9);
                String state = rs.getString(10);
                String country = rs.getString(11);
                String district = rs.getString(12);
                String zipCode = rs.getString(13);
                String street = rs.getString(14);
                int streetNumber = rs.getInt(15);

                String dispatcherId = rs.getString(16);
                String courierId = rs.getString(17);
                String userId = rs.getString(18);

                AssignmentDispatcher disp = null; //Chiamare la retrieve dell'AssignmentDispatcherDAO sul dispatcherId trovato
                Courier courier = null; //Chiamare la retrieve del CourierDAO sul courierId trovato (solo se tale ordine è gia stato assegnato)

                UserDAO uD = new UserDAO(ds);

                User user = null;
                try{
                    uD.retrieve(userId);
                }catch (NotFoundException nf){

                }

                ShippingPropertyDAO spD = new ShippingPropertyDAO(ds);
                ArrayList<ShippingProperty> orderProps = null;
                try{
                    spD.getOrderProperties(new Orders(id)); //Properties legate ad un Ordine
                }catch (NotFoundException e){

                }

                //Gli updates verranno presi solo all'occorrenza (Dato variabile)

                Orders o = new Orders(id, length, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, disp, courier, user, orderProps, null);

                orders.add(o);
            }

            if(orders.size() == 0)
                throw new NotFoundException("There are no orders with '"+s.getPhase()+"' as current state");
            else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a shippingProperties related to a contract in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    public ArrayList<Orders> retrieveOrdersByStateNotAssociated(State s) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT o.id, o.length, o.width, o.height, o.weight, o.creationDate, o.assignmentDate, o.expectedDeliveryDate, o.trackingCode, o.location, o.dispatcher_id, o.courier_id, o.user_id FROM ORDERS AS o, UPDATES AS u WHERE o.id = u.order_id AND u.id = ( SELECT MAX(us.id) FROM UPDATES AS us WHERE o.id = us.order_id ) AND u.state != ? ;");
            pS.setString(1, s.getPhase());

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int id = rs.getInt(1);
                int length = rs.getInt(2);
                int width = rs.getInt(3);
                int height = rs.getInt(4);
                int weight = rs.getInt(5);
                Date creationDate = rs.getDate(6);
                Date assignmentDate = rs.getDate(7);
                Date expectedDeliveryDate = rs.getDate(8);
                String trackingCode = rs.getString(9);
                String state = rs.getString(10);
                String country = rs.getString(11);
                String district = rs.getString(12);
                String zipCode = rs.getString(13);
                String street = rs.getString(14);
                int streetNumber = rs.getInt(15);

                String dispatcherId = rs.getString(16);
                String courierId = rs.getString(17);
                String userId = rs.getString(18);

                AssignmentDispatcher disp = null; //Chiamare la retrieve dell'AssignmentDispatcherDAO sul dispatcherId trovato
                Courier courier = null; //Chiamare la retrieve del CourierDAO sul courierId trovato (solo se tale ordine è gia stato assegnato)

                UserDAO uD = new UserDAO(ds);

                User user = null;
                try{
                    uD.retrieve(userId);
                }catch (NotFoundException nf){

                }

                ShippingPropertyDAO spD = new ShippingPropertyDAO(ds);
                ArrayList<ShippingProperty> orderProps = null;
                try{
                    spD.getOrderProperties(new Orders(id)); //Properties legate ad un Ordine
                }catch (NotFoundException e){

                }

                //Gli updates verranno presi solo all'occorrenza (Dato variabile)

                Orders o = new Orders(id, length, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, disp, courier, user, orderProps, null);

                orders.add(o);
            }

            if(orders.size() == 0)
                throw new NotFoundException("There are no orders with '"+s.getPhase()+"' as current state");
            else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving a shippingProperties related to a contract in ShippingPropertyDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

    public Updates retrieveLastUpdateByOrder(Orders o) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT u.* FROM UPDATES AS u WHERE u.order_id = ? AND u.id = ( SELECT MAX(us.id) FROM UPDATES AS us WHERE us.order_id = ?);");
            pS.setInt(1,o.getId());
            pS.setInt(2,o.getId());
            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            if(rs.next()) {
                int id = rs.getInt(1);
                String message = rs.getString(2);
                String order_id = rs.getString(3);

                String statePhase = rs.getString(4);

                StateDAO sDAO = new StateDAO(ds);

                State s = null;

                try{
                    s = sDAO.retrieve(statePhase);
                }catch (NotFoundException e){
                    throw new NotFoundException("State '"+ statePhase +"' not found, while assigning to the last update for Order with id '"+ o.getId() +"'");
                }

                Date timestamp = rs.getDate(5);

                Updates u = new Updates(id, message, o, s, timestamp);

                return u;

            }else{
                throw new NotFoundException("No updates found for Order with id '"+o.getId()+"'");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving the last Update for an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }

    }

}
