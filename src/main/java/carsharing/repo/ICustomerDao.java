package carsharing.repo;

import carsharing.domain.Car;
import carsharing.domain.Customer;

import java.util.List;
import java.util.Set;

public interface ICustomerDao {
    List<Customer> getAllCustomers();
    void createCustomer(String customersName);
    Car getCustomersCar(Customer customer);
    void updateCustomersCar(Customer customer, Car car);
    Set<Integer> getRentedCarsIds();
}
