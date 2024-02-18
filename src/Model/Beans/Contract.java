package Model.Beans;

import java.sql.Date;
import java.util.ArrayList;

public class Contract {

    private Date start;
    private Date end;
    private AssignmentDispatcher contractDisp;
    private Courier contractCourier;

    private ArrayList<ShippingProperty> shippingProperties;

    public Contract(Date start, Date end, AssignmentDispatcher dispatcher, Courier courier, ArrayList<ShippingProperty> shippingProperties) {
        this.start = start;
        this.end = end;
        this.contractDisp = dispatcher;
        this.contractCourier = courier;
        this.shippingProperties = shippingProperties;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public AssignmentDispatcher getDispatcher() {
        return contractDisp;
    }

    public void setDispatcher(AssignmentDispatcher dispatcher) {
        this.contractDisp = dispatcher;
    }

    public Courier getCourier() {
        return contractCourier;
    }

    public void setCourier(Courier courier) {
        this.contractCourier = courier;
    }

    public ArrayList<ShippingProperty> getShippingProperties() {
        return shippingProperties;
    }

    public void setShippingProperties(ArrayList<ShippingProperty> shippingProperties) {
        this.shippingProperties = shippingProperties;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "start=" + start +
                ", end=" + end +
                ", contractDisp=" + contractDisp +
                ", contractCourier=" + contractCourier +
                ", shippingProperties=" + shippingProperties +
                '}';
    }
}
