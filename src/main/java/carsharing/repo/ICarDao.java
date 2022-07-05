package carsharing.repo;

import carsharing.domain.Car;
import carsharing.domain.Company;

import java.util.List;

public interface ICarDao {
    List<Car> getAllCompanysCars(Company company);
    void addCompanysCar(String carsName, Company company);
}
