package Model.Beans;

import java.sql.Date;
import java.util.ArrayList;

public class Orders {

    private int id;
    private int length;
    private int width;
    private int height;
    private int weight;
    private Date creationDate;
    private Date assignmentDate;
    private Date expectedDeliveryDate;
    private String trackingCode;
    private String state;
    private String country;
    private String district;
    private String zipCode;
    private String street;
    private int streetNumber;
    private AssignmentDispatcher orderDisp;
    private Courier orderCourier;
    private User orderFinalUser;
    private ArrayList<ShippingProperty> orderProperties;
    private ArrayList<Updates> updates;


    public Orders(int id, int length, int width, int height, int weight, Date creationDate, Date assignmentDate, Date expectedDeliveryDate, String trackingCode, String state, String country, String district, String zipCode, String street, int streetNumber, AssignmentDispatcher orderDisp, Courier orderCourier, User orderFinalUser, ArrayList<ShippingProperty> orderProperties, ArrayList<Updates> updates) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.creationDate = creationDate;
        this.assignmentDate = assignmentDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.trackingCode = trackingCode;
        this.state = state;
        this.country = country;
        this.district = district;
        this.zipCode = zipCode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.orderDisp = orderDisp;
        this.orderCourier = orderCourier;
        this.orderFinalUser = orderFinalUser;
        this.orderProperties = orderProperties;
        this.updates = updates;
    }

    public Orders(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(Date assignmentDate) {
        this.assignmentDate = assignmentDate;
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

    public AssignmentDispatcher getOrderDisp() {
        return orderDisp;
    }

    public void setOrderDisp(AssignmentDispatcher orderDisp) {
        this.orderDisp = orderDisp;
    }

    public Courier getOrderCourier() {
        return orderCourier;
    }

    public void setOrderCourier(Courier orderCourier) {
        this.orderCourier = orderCourier;
    }

    public User getOrderFinalUser() {
        return orderFinalUser;
    }

    public void setOrderFinalUser(User orderFinalUser) {
        this.orderFinalUser = orderFinalUser;
    }

    public ArrayList<ShippingProperty> getOrderProperties() {
        return orderProperties;
    }

    public void setOrderProperties(ArrayList<ShippingProperty> orderProperties) {
        this.orderProperties = orderProperties;
    }

    public ArrayList<Updates> getUpdates() {
        return updates;
    }

    public void setUpdates(ArrayList<Updates> updates) {
        this.updates = updates;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", creationDate=" + creationDate +
                ", assignmentDate=" + assignmentDate +
                ", expectedDeliveryDate=" + expectedDeliveryDate +
                ", trackingCode='" + trackingCode + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", district='" + district + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber=" + streetNumber +
                ", orderDisp=" + orderDisp +
                ", orderCourier=" + orderCourier +
                ", orderFinalUser=" + orderFinalUser +
                ", orderProperties=" + orderProperties +
                ", updates=" + updates +
                '}';
    }
}
