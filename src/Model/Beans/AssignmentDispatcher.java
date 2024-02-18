package Model.Beans;

import java.util.ArrayList;

public class AssignmentDispatcher {

    private String name;

    private String email;

    public AssignmentDispatcher(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public AssignmentDispatcher(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "AssignmentDispatcher{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
