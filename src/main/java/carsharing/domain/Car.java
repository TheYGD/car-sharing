package carsharing.domain;

import java.util.Objects;

public class Car {

    private Integer id;
    private String name;
    private int companyId;

    public Car(Integer id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;

        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, companyId);
    }
}