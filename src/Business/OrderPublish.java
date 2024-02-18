package Business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

public class OrderPublish implements Serializable {

    //OrderPublish(insertedOrderId, lenght, width, height, weight, Date(),null,null,null, state, country, district,zipCode, street, streetNumber,checkedProperties)
    @JsonProperty("id")
    private int id;
    @JsonProperty("lenght")
    private int lenght;
    @JsonProperty("width")
    private int width;
    @JsonProperty("height")
    private int height;
    @JsonProperty("weight")
    private int weight;
    @JsonProperty("creationDate")
    private Date creationDate;
    @JsonProperty("assignmentDate")
    private Date assignmentDate;
    @JsonProperty("expectedDeliveryDate")
    private Date expectedDeliveryDate;
    @JsonProperty("trackingCode")
    private String trackingCode;
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private String country;
    @JsonProperty("district")
    private String district;
    @JsonProperty("zipCode")
    private String zipCode;
    @JsonProperty("street")
    private String street;
    @JsonProperty("streetNumber")
    private int streetNumber;
    @JsonProperty("properties")
    private String[] properties;

    @JsonCreator
    public OrderPublish(@JsonProperty("id") int id, @JsonProperty("length") int lenght, @JsonProperty("width") int width, @JsonProperty("height") int height, @JsonProperty("weight") int weight, @JsonProperty("creationDate") Date creationDate, @JsonProperty("assignmentDate") Date assignmentDate, @JsonProperty("expectedDeliveryDate") Date expectedDeliveryDate, @JsonProperty("trackingCode") String trackingCode, @JsonProperty("state") String state, @JsonProperty("country") String country, @JsonProperty("district") String district, @JsonProperty("zipCode") String zipCode, @JsonProperty("street") String street, @JsonProperty("streetNumber") int streetNumber , @JsonProperty("properties") String[] properties) {
        this.id = id;
        this.lenght = lenght;
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
        this.properties = properties;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return lenght;
    }

    public void setLength(int length) {
        this.lenght = length;
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

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }
}
