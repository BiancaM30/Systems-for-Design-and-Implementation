package swim.client.gui.dto;

import swim.model.SwimmingEvent;

import java.util.List;

public class ContestantDTO {
    private int id;
    private String name;
    private int age;
    private List<SwimmingEvent> registrations;

    public ContestantDTO(int id, String name, int age, List<SwimmingEvent> registrations) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.registrations = registrations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<SwimmingEvent> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<SwimmingEvent> registrations) {
        this.registrations = registrations;
    }

}
