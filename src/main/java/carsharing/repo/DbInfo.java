package carsharing.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbInfo {
    private String dbPath;

    public DbInfo(String dbPath) {
        this.dbPath = dbPath;

        try (Connection conn = DriverManager.getConnection (dbPath)) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();

            String sqlCompanyies =
                    "CREATE TABLE if not exists COMPANY " +
                            "   (ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "   NAME VARCHAR UNIQUE NOT NULL" +
                            ")";
            st.executeUpdate(sqlCompanyies);

            String sqlCars =
                    "CREATE TABLE if not exists CAR " +
                            "   (ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "   NAME VARCHAR UNIQUE NOT NULL, " +
                            "   COMPANY_ID INT NOT NULL, " +
                            "   CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID)" +
                            "   REFERENCES COMPANY(ID)" +
                            ");";
            st.executeUpdate(sqlCars);

            String sqlUsers =
                    "CREATE TABLE if not exists CUSTOMER " +
                            "   (ID INT PRIMARY KEY AUTO_INCREMENT, " +
                            "   NAME VARCHAR UNIQUE NOT NULL, " +
                            "   RENTED_CAR_ID INT DEFAULT NULL, " +
                            "   CONSTRAINT fk_rented_car FOREIGN KEY (RENTED_CAR_ID)" +
                            "   REFERENCES CAR(ID)" +
                            ");";
            st.executeUpdate(sqlUsers);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDbPath() {
        return dbPath;
    }
}
