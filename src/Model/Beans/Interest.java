package Model.Beans;

import java.sql.Date;

public class Interest {

    private Courier courier;
    private Date expectedDeliveryDate;
    private String trackingCode;


    public Interest(Courier courier, Date expectedDeliveryDate, String trackingCode) {
        this.courier = courier;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.trackingCode = trackingCode;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    @Override
    public String toString() {
        return "Interest{" +
                "courier=" + courier +
                ", expectedDeliveryDate=" + expectedDeliveryDate +
                ", trackingCode='" + trackingCode + '\'' +
                '}';
    }
}
