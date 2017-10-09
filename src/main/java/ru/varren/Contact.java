package ru.varren;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Contact {

    @JsonProperty("id")
    private int id;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("phones")
    private List<Phone> phones;

    public Contact() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", phones=" + phones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact that = (Contact) o;

        if (id != that.id) return false;
        return phones != null ? phones.equals(that.phones) : that.phones == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (phones != null ? phones.hashCode() : 0);
        return result;
    }
}
