package swim.model.hbm;

import swim.model.Identifiable;

import java.io.Serializable;
import java.util.Objects;

public class DeskUser implements Identifiable<Integer>, Serializable {
    private Integer ID;
    private String email;
    private String password;

    public DeskUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public DeskUser() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeskUser deskUser = (DeskUser) o;
        return ID == deskUser.ID && Objects.equals(email, deskUser.email) && Objects.equals(password, deskUser.password);
    }

    @Override
    public String toString() {
        return "DeskUser{" +
                "id=" + ID +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, email, password);
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public void setID(Integer id) {
        this.ID = id;
    }
}
