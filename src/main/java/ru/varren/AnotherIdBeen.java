package ru.varren;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        resolver = MyObjectIdResolver.class,
        property = "anotherId", scope = AnotherIdBeen.class)
public class AnotherIdBeen {

    @JsonProperty("anotherId")
    private int anotherId;

    @JsonProperty("n")
    private String n;

    public AnotherIdBeen() {
    }

    public int getAnotherId() {
        return anotherId;
    }

    public void setAnotherId(int id) {
        this.anotherId = id;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "AnotherIdBeen{" +
                "anotherId=" + anotherId +
                ", n='" + n + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnotherIdBeen that = (AnotherIdBeen) o;

        if (anotherId != that.anotherId) return false;
        return n != null ? n.equals(that.n) : that.n == null;
    }

    @Override
    public int hashCode() {
        int result = anotherId;
        result = 31 * result + (n != null ? n.hashCode() : 0);
        return result;
    }
}
