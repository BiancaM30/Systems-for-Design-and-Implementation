package swim.client.gui.dto;
import swim.model.enums.Style;
import swim.model.enums.Distance;

public class SwimmingEventDTO {
    private int id;
    private Distance distance;
    private Style style;
    private int noContestants;

    public SwimmingEventDTO(int id, Distance distance, Style style, int noContestants) {
        this.id = id;
        this.distance = distance;
        this.style = style;
        this.noContestants = noContestants;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDistance() {
        return distance.toString();
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public String getStyle() {
        return style.toString();
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public int getNoContestants() {
        return noContestants;
    }

    public void setNoContestants(int noContestants) {
        this.noContestants = noContestants;
    }
}
