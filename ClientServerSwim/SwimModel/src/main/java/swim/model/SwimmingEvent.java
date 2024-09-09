package swim.model;

import swim.model.enums.Style;
import swim.model.enums.Distance;

import java.io.Serializable;
import java.util.Objects;

public class SwimmingEvent implements swim.model.Identifiable<Integer>, Serializable {
    private int id;
    private Distance distance;
    private Style style;


    public SwimmingEvent(Distance distance, Style style) {
        this.distance = distance;
        this.style = style;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    /*@Override
    public String toString() {
        return "SwimmingEvent{" +
                "id=" + id +
                ", distance=" + distance +
                ", style=" + style +
                '}';
    }*/

    public String toString() {
        return "{" +
                distance + ',' +
                style +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SwimmingEvent that = (SwimmingEvent) o;
        return id == that.id && distance == that.distance && style == that.style;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, style);
    }


    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }
}
