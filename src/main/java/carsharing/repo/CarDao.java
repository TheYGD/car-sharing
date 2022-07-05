package carsharing.repo;

import carsharing.domain.Car;
import carsharing.domain.Company;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CarDao implements ICarDao {

    private DbInfo dbInfo;

    public CarDao(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    @Override
    public List<Car> getAllCompanysCars(Company company) {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "SELECT * FROM CAR " +
                            "WHERE COMPANY_ID = " + company.getId() + " " +
                            "ORDER BY ID;";

            ResultSet results = st.executeQuery(sql);

            List<Car> cars = new LinkedList<>();
            while (results.next()) {
                Car car = new Car(
                        results.getInt(1),
                        results.getString(2),
                        results.getInt(3)
                );
                cars.add(car);
            }

            return cars;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addCompanysCar(String carsName, Company company) {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "INSERT INTO CAR (NAME, COMPANY_ID) " +
                            "VALUES ('" + carsName + "', " + company.getId() + ");";

            st.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
