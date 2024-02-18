package Model.Beans;

public class State {

    private String phase;

    private String description;

    public State(String phase, String description) {
        this.phase = phase;
        this.description = description;
    }

    public State(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
