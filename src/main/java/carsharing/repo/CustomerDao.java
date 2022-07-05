package carsharing.repo;

import carsharing.domain.Car;
import carsharing.domain.Customer;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CustomerDao implements ICustomerDao {

    private DbInfo dbInfo;

    public CustomerDao(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    @Override
    public void createCustomer(String customersName) {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "INSERT INTO CUSTOMER (NAME) " +
                            "VALUES ('" + customersName + "');";

            st.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "SELECT * FROM CUSTOMER " +
                            "ORDER BY ID;";

            ResultSet results = st.executeQuery(sql);

            List<Customer> customers = new LinkedList<>();
            while (results.next()) {
                Customer customer = new Customer(
                        results.getInt(1),
                        results.getString(2),
                        (Integer) results.getObject(3)
                );
                customers.add(customer);
            }

            return customers;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateCustomersCar(Customer customer, Car car) {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "UPDATE CUSTOMER " +
                            "SET RENTED_CAR_ID = " + car.getId() + " " +
                            "WHERE ID = " + customer.getId() + ";";

            st.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Car getCustomersCar(Customer customer) {
        if (customer.getRented_car_id() == null) {
            return null;
        }

        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "SELECT * FROM CAR " +
                            "WHERE ID = " + customer.getRented_car_id() + ";";

            ResultSet results = st.executeQuery(sql);

            // unique car
            results.next();
            Car car = new Car(
                    results.getInt(1),
                    results.getString(2),
                    results.getInt(3)
            );

            return car;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<Integer> getRentedCarsIds() {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER;";

            ResultSet results = st.executeQuery(sql);
            Set<Integer> rentedCarsIds = new HashSet<>();

            while (results.next()) {
                rentedCarsIds.add(results.getInt(1));
            }

            return rentedCarsIds;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
