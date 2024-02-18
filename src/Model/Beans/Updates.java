package Model.Beans;

import java.sql.Date;

public class Updates {

    private int id;
    private String message;
    private Orders order;
    private State state;

    private Date timeStamp;


    public Updates(int id, String message, Orders order, State state, Date timeStamp) {
        this.id = id;
        this.message = message;
        this.order = order;
        this.state = state;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
