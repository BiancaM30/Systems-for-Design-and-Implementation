package swim.model;

import java.io.Serializable;
import java.util.Objects;


public class Registration implements swim.model.Identifiable<Tuple<Contestant, SwimmingEvent>>, Serializable {
    private Contestant contestant;
    private SwimmingEvent event;

    public Registration(Contestant contestant, SwimmingEvent event) {
        this.contestant = contestant;
        this.event = event;
    }


    public Contestant getContestant() {
        return contestant;
    }

    public void setContestant(Contestant contestant) {
        this.contestant = contestant;
    }

    public SwimmingEvent getEvent() {
        return event;
    }

    public void setEvent(SwimmingEvent event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return Objects.equals(contestant, that.contestant) && Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestant, event);
    }

    @Override
    public String toString() {
        return "Registration{" +
                "contestant=" + contestant +
                ", event=" + event +
                '}';
    }

    @Override
    public Tuple<Contestant, SwimmingEvent> getID() {
        return null;
    }

    @Override
    public void setID(Tuple<Contestant, SwimmingEvent> id) {

    }
}
