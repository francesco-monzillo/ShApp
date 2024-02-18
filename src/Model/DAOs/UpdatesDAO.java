package Model.DAOs;

import Model.Beans.Courier;
import Model.Beans.Orders;
import Model.Beans.State;
import Model.Beans.Updates;
import Model.Exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class UpdatesDAO {

    private DataSource ds;

    public UpdatesDAO(DataSource ds) {
        this.ds = ds;
    }

    public ArrayList<Updates> retrieveByOrderId(int orderId) throws Exception{

        Connection conn = ds.getConnection();

        try{

            PreparedStatement pS = conn.prepareStatement("SELECT * FROM UPDATES AS u WHERE u.order_id = ?  ORDER BY u.timeStamp ASC;");
            pS.setInt(1, orderId);

            ResultSet rs = pS.executeQuery();

            ArrayList<Updates> updates = new ArrayList<Updates>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String message = rs.getString(2);

                String statePhase = rs.getString(4);
                Date timeStamp = rs.getDate(5);

                StateDAO sDAO = new StateDAO(ds);

                State s = null;

                try {
                    s = sDAO.retrieve(statePhase);
                } catch (NotFoundException e) {
                    throw new Exception("State '" + statePhase + "' not found...");
                }

                Updates u = new Updates(id, message, new Orders(orderId), s, timeStamp);

                updates.add(u);
            }

            if(updates.size()==0)
                throw new NotFoundException("No updates found for Order with orderId'" + orderId + "'");

            else {
                return updates;
            }

        }catch (SQLException e){
            throw new Exception("Problem with retrieving Updates for an Order in UpdatesDAO:\n"+e.getMessage()+"\n"+e.getSQLState());
        }finally{
            conn.close();
        }
    }


    public Updates createUpdate(int orderId, String statePhase, String message, Date timeStamp) throws Exception{
        Connection conn = ds.getConnection();

        try{
            PreparedStatement pS = conn.prepareStatement("INSERT INTO UPDATES(message, order_id, state, timeStamp) VALUES(?, ?, ?, ?);");
            pS.setString(1, message);
            pS.setInt(2, orderId);
            pS.setString(3, statePhase);
            pS.setDate(4, timeStamp);

            int insertedRows = pS.executeUpdate();

            if(insertedRows == 0) {
                throw new Exception("Can't insert an update to Order with id '" + orderId + "' due to a probable violation of constraints");
            }else{
                return new Updates(0, message, new Orders(orderId), new State(statePhase), timeStamp);
            }

        }catch (SQLException e){
            throw new Exception("Problem with inserting an Update in UpdatesDAO:\n" + e.getMessage() + "\n" + e.getSQLState());
        }finally {
            conn.close();
        }
    }

}
