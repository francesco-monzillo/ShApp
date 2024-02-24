package Model.DAOs;
import Model.Beans.*;
import Model.Exceptions.AlreadyFoundUnivocalIdentifierException;
import Model.Exceptions.NotFoundException;
import com.sun.jdi.StringReference;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class OrdersDAO {
    private DataSource ds;

    public OrdersDAO(DataSource ds) {
        this.ds = ds;
    }


    public Orders retrieve(int orderId) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.id = ? ;");
            pS.setInt(1, orderId);

            ResultSet rs = pS.executeQuery();

            ArrayList<Updates> updates = new ArrayList<Updates>();

            if(rs.next()) {
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherMail = rs.getString(16);
                String courierMail = rs.getString(17);
                String userId = rs.getString(18);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try{
                    aD = aDDAO.retrieve(assignmentDispatcherMail);
                }catch (NotFoundException e){
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }

                try{
                    c = cD.retrieve(courierMail);
                }catch (NotFoundException e){
                }

                try{
                    u = uD.retrieve(userId);
                }catch (NotFoundException e){
                    System.out.println("User not found for Order with id '"+ orderId +"'");
                }


                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                return o;

            }else{
                throw new NotFoundException("Order with id '"+orderId+"' not found...");
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<Orders> retrieveByDispatcher(String dispatcherMail) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.dispatcher_id = ? ORDER BY o.assignmentDate DESC;");
            pS.setString(1, dispatcherMail);

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String courierMail = rs.getString(17);
                String userId = rs.getString(18);

                AssignmentDispatcherDAO assignmentDispatcherDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try{
                    aD = assignmentDispatcherDAO.retrieve(dispatcherMail);
                }catch (NotFoundException e){
                    System.out.println("Ordine associato a dispatcher non valido... indagare");
                }

                try{
                    c = cD.retrieve(courierMail);
                }catch (NotFoundException e){
                    System.out.println("Order associated to dispatcher '"+ dispatcherMail +"' not assigned yet");
                }

                try{
                    u = uD.retrieve(userId);
                }catch (NotFoundException e){
                    System.out.println("User not found for Order with id '"+ orderId +"'");
                }


                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, street, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                orders.add(o);
            }

            if(orders.size() == 0){
                throw new NotFoundException("The Disptacher with mail '"+dispatcherMail+"' didn't submit any order... ");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving Orders associated to a dispatcher in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public ArrayList<Orders> retrieveByEndUser(String userMail) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.user_id = ? ORDER BY o.assignmentDate DESC;");
            pS.setString(1, userMail);

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherName = rs.getString(16);

                String courierName = rs.getString(17);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try{
                    aD = aDDAO.retrieve(assignmentDispatcherName);
                }catch (Exception e){
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }

                try{
                    c = cD.retrieve(courierName);
                }catch (NotFoundException e){
                    System.out.println("Order associated to Courier '"+ courierName +"' not assigned yet");
                }

                u = new User(userMail);

                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                StateDAO sD = new StateDAO(ds);

                orders.add(o);
            }

            if(orders.size() == 0){
                throw new NotFoundException("The User with mail '"+userMail+"' has no delivered orders associated... ");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving Orders associated to an User in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public ArrayList<Orders> retrieveByEndUserNotDelivered(String userMail) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.user_id = ? ORDER BY o.assignmentDate DESC;");
            pS.setString(1, userMail);

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherName = rs.getString(16);

                String courierName = rs.getString(17);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try{
                    aD = new AssignmentDispatcher(assignmentDispatcherName);
                }catch (Exception e){
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }

                try{
                    c = cD.retrieve(courierName);
                }catch (NotFoundException e){
                    System.out.println("Order associated to Courier '"+ courierName +"' not assigned yet");
                }

                u = new User(userMail);

                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                StateDAO sD = new StateDAO(ds);

                Updates update = sD.retrieveLastUpdateByOrder(o);

                if(! (update.getState().getPhase().equals("Consegnato")))
                    orders.add(o);
            }

            if(orders.size() == 0){
                throw new NotFoundException("The User with mail '"+userMail+"' has no active shipments associated... ");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving Orders associated to an User in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public ArrayList<Orders> retrieveByEndUserDelivered(String userMail) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.user_id = ? ORDER BY o.assignmentDate DESC;");
            pS.setString(1, userMail);

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherName = rs.getString(16);

                String courierName = rs.getString(17);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try{
                    aD = new AssignmentDispatcher(assignmentDispatcherName);
                }catch (Exception e){
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }

                try{
                    c = cD.retrieve(courierName);
                }catch (NotFoundException e){
                    System.out.println("Order associated to Courier '"+ courierName +"' not assigned yet");
                }

                u = new User(userMail);

                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                StateDAO sD = new StateDAO(ds);

                Updates update = sD.retrieveLastUpdateByOrder(o);

                if(update.getState().getPhase().equals("Consegnato"))
                    orders.add(o);
            }

            if(orders.size() == 0){
                throw new NotFoundException("The User with mail '"+userMail+"' has no delivered orders associated... ");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving Orders associated to an User in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<Orders> retrieveByCourier(String courierEmail) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.courier_id = ? ORDER BY o.assignmentDate DESC;");
            pS.setString(1, courierEmail);

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherName = rs.getString(16);
                String userId = rs.getString(18);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try{
                    aD = aDDAO.retrieve(assignmentDispatcherName);
                }catch (NotFoundException e){
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }


                c = new Courier(courierEmail);

                try{
                    u = uD.retrieve(userId);
                }catch (NotFoundException e){
                    System.out.println("User not found for Order with id '"+ orderId +"'");
                }


                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, street, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                orders.add(o);
            }

            if(orders.size() == 0){
                throw new NotFoundException("The Courier with name '"+courierEmail+"' has no orders assigned... ");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving Orders associated with couriers in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }

    public ArrayList<Orders> retrieveNotAssigned() throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS AS o WHERE o.courier_id IS NULL ORDER BY o.creationDate ASC;");

            ResultSet rs = pS.executeQuery();

            ArrayList<Orders> orders = new ArrayList<Orders>();

            while(rs.next()) {
                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherName = rs.getString(16);
                String userId = rs.getString(18);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                User u = null;

                try{
                    aD = aDDAO.retrieve(assignmentDispatcherName);
                }catch (NotFoundException e){
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }


                try{
                    u = uD.retrieve(userId);
                }catch (NotFoundException e){
                    System.out.println("User not found for Order with id '"+ orderId +"'");
                }


                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try{
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                }catch (NotFoundException e){
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, street, country, district, zipCode, street, streetNumber, aD, null, u, props, null);

                orders.add(o);
            }

            if(orders.size() == 0){
                throw new NotFoundException("Not existing orders not assigned...");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving not assigned Orders in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public ArrayList<Orders> retrieveAllOrders() throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM ORDERS;");

            ResultSet rs = pS.executeQuery();

            ArrayList<Updates> updates = new ArrayList<Updates>();

            ArrayList<Orders> orders = new ArrayList<>();

            while(rs.next()) {

                int orderId = rs.getInt(1);
                int lenght = rs.getInt(2);
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

                String assignmentDispatcherName = rs.getString(16);
                String courierName = rs.getString(17);
                String userId = rs.getString(18);

                AssignmentDispatcherDAO aDDAO = new AssignmentDispatcherDAO(ds);
                CourierDAO cD = new CourierDAO(ds);
                UserDAO uD = new UserDAO(ds);

                AssignmentDispatcher aD = null;
                Courier c = null;
                User u = null;

                try {
                    aD = aDDAO.retrieve(assignmentDispatcherName);
                } catch (NotFoundException e) {
                    throw new Exception("No assDisp found for this Order... something's wrong");
                }

                try {
                    c = cD.retrieve(courierName);
                } catch (NotFoundException e) {

                }

                try {
                    u = uD.retrieve(userId);
                } catch (NotFoundException e) {
                    System.out.println("User not found for Order with id '" + orderId + "'");
                }


                ShippingPropertyDAO sPDAO = new ShippingPropertyDAO(ds);

                ArrayList<ShippingProperty> props = null;

                try {
                    props = sPDAO.getOrderProperties(new Orders(orderId));
                } catch (NotFoundException e) {
                    System.out.println("No properties found for this Order");
                }

                //L'ordine può essere salvato nella sessione, e gli Updates ad esso relativi verranno aggiunti solo all'occorrenza
                Orders o = new Orders(orderId, lenght, width, height, weight, creationDate, assignmentDate, expectedDeliveryDate, trackingCode, state, country, district, zipCode, street, streetNumber, aD, c, u, props, null);

                orders.add(o);


            }
            if(orders.size() == 0){
                throw new NotFoundException("No orders found in DB...");
            }else{
                return orders;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public Orders create(Orders o) throws Exception{
        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("INSERT INTO ORDERS(lenght,width,height,weight,creationDate,assignmentDate,expectedDeliveryDate,trackingCode,state,country,district,zipCode,street,streetNumber,dispatcher_id,courier_id,user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",Statement.RETURN_GENERATED_KEYS);
            pS.setInt(1, o.getLength());
            pS.setInt(2, o.getWidth());
            pS.setInt(3, o.getHeight());
            pS.setInt(4, o.getWeight());
            pS.setDate(5, o.getCreationDate());
            pS.setDate(6, o.getAssignmentDate());
            pS.setDate(7, o.getExpectedDeliveryDate());
            pS.setString(8, o.getTrackingCode());
            pS.setString(9, o.getState());
            pS.setString(10, o.getCountry());
            pS.setString(11, o.getDistrict());
            pS.setString(12, o.getZipCode());
            pS.setString(13, o.getStreet());
            pS.setInt(14, o.getStreetNumber());
            pS.setString(15, o.getOrderDisp().getEmail());

            Courier c = o.getOrderCourier();
            User u = o.getOrderFinalUser();

            if(c != null)
                pS.setString(16, c.getName());
            else{
                pS.setString(16, null);
            }

            if(u != null){
                pS.setString(17, u.getEmail());
            }else{
                pS.setString(17, null);
            }

            int result = pS.executeUpdate();

            if(result > 0) {
                ResultSet rs = pS.getGeneratedKeys();
                if(rs.next()) {
                    o.setId(rs.getInt(1));
                    return o;
                }else{
                    throw new Exception("PROBLEM WITH RETRIEVING ORDER AUTO GENERATED KEY");
                }
            }else{
                throw new Exception("Problem Inserting Order with id '" + o.getId() + "'");
            }

        }catch (SQLException e){
            throw new Exception("Problem with inserting an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public ArrayList<Interest> retrieveInterestedCouriers(int order_id) throws Exception {
        Connection conn = ds.getConnection();

        try {
            PreparedStatement pS = conn.prepareStatement("SELECT i.courier_id, i.expectedDeliveryDate, i.trackingCode FROM InterestedCouriers AS i WHERE i.order_id = ?  ORDER BY i.timeStamp ASC;");
            pS.setInt(1, order_id);

            ResultSet rs = pS.executeQuery();

            ArrayList<Interest> interests = new ArrayList<>();

            CourierDAO cD = new CourierDAO(ds);

            while (rs.next()) {
                Courier courier = cD.retrieve(rs.getString(1));
                Date expectedDeliveryDate = rs.getDate(2);
                String trackingCode = rs.getString(3);

                Interest interest = new Interest(courier, expectedDeliveryDate, trackingCode);

                interests.add(interest);
            }

            if (interests.size() == 0)
                throw new NotFoundException("No couriers interested in this order, try again 5 minutes");
            else
                return interests;
        }catch (SQLException e){
            throw new Exception("Problem with retrieving Couriers interested in an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }


    public void insertInterestedCourier(String courierName, int orderId, Date timestamp, Date expectedDeliveryDate, String trackingCode) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("INSERT INTO InterestedCouriers() VALUES (?, ?, ?, ? ,?);");
            pS.setString(1, courierName);
            pS.setInt(2, orderId);
            pS.setDate(3, timestamp);
            pS.setDate(4, expectedDeliveryDate);
            pS.setString(5, trackingCode);

            int res = pS.executeUpdate();

            if(res == 0)
                throw new AlreadyFoundUnivocalIdentifierException("I could not insert an interested courier... Probably an already existing row or a violated constraint on a secondary key");

        }catch (SQLException e){
            throw new Exception("Problem with inserting an interested Courier in an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }



    public void assignToCourier(String courierName, int orderId, Date assignmentDate, Date expectedDeliveryDate, String trackingCode) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("UPDATE Orders AS o SET o.courier_id = ?, o.assignmentDate = ?, o.expectedDeliveryDate = ?, o.trackingCode = ? WHERE o.id = ?;");
            pS.setString(1, courierName);
            pS.setDate(2, assignmentDate);
            pS.setDate(3, expectedDeliveryDate);
            pS.setString(4, trackingCode);
            pS.setInt(5, orderId);

            int res = pS.executeUpdate();

            if(res == 0)
                throw new AlreadyFoundUnivocalIdentifierException("I could not assign a Courier to an Order... Probably a violated constraint on a secondary key");

        }catch (SQLException e){
            throw new Exception("Problem assigning an Order to a Courier in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally {
            conn.close();
        }
    }


    public void removeInterestedCouriers(int orderId) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("DELETE FROM InterestedCouriers AS i WHERE i.order_id = ?;");
            pS.setInt(1, orderId);

            int res = pS.executeUpdate();

            if(res == 0)
                throw new NotFoundException("I could not delete InterestedCouriers to an Order... Probably the Order with id '"+orderId+"' not existing");

        }catch (SQLException e){
            throw new Exception("Problem with the removal of interestedCouriers to an Order in OrdersDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
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
