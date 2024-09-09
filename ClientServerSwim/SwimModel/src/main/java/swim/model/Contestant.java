package swim.model;

import java.io.Serializable;
import java.util.Objects;

public class Contestant implements Identifiable<Integer>, Serializable {

    private int id;
    private String name;
    private int age;

    public Contestant(String name, int age) {
        this.name = name;
        this.age = age;
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

    @Override
    public String toString() {
        return "Contestant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contestant that = (Contestant) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    public int getId(){return this.id;}
}
