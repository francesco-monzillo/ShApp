package Model.Beans;

public class Courier {

    private String name;
    private String email;

    public Courier(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Courier(String email){
        this.email=email;
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
        return "Courier{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
