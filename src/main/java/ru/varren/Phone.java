package ru.varren;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        resolver = MyObjectIdResolver.class,
        property = "id", scope = Phone.class)
public class Phone {
    @JsonProperty("id")
    private int id;

    @JsonProperty("n")
    private String n;

    public Phone() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", n='" + n + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        if (id != phone.id) return false;
        return n != null ? n.equals(phone.n) : phone.n == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (n != null ? n.hashCode() : 0);
        return result;
    }
}
