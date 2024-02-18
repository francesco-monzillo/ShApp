package Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import Model.Beans.ShippingProperty;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;


public class OrderPublish implements Serializable {

    @JsonProperty("id")
    private int id;
    @JsonProperty("length")
    private int length;
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
    @JsonProperty("location")
    private String location;

    @JsonProperty("properties")
    private ArrayList<ShippingProperty> properties;

    @JsonCreator
    public OrderPublish(@JsonProperty("id") int id, @JsonProperty("length") int length, @JsonProperty("width") int width, @JsonProperty("height") int height, @JsonProperty("weight") int weight, @JsonProperty("creationDate") Date creationDate, @JsonProperty("assignmentDate") Date assignmentDate, @JsonProperty("expectedDeliveryDate") Date expectedDeliveryDate, @JsonProperty("trackingCode") String trackingCode, @JsonProperty("location") String location, @JsonProperty("properties") ArrayList<ShippingProperty> properties) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.creationDate = creationDate;
        this.assignmentDate = assignmentDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.trackingCode = trackingCode;
        this.location = location;
        this.properties = properties;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<ShippingProperty> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<ShippingProperty> properties) {
        this.properties = properties;
    }
}