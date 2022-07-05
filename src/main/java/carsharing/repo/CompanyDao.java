package carsharing.repo;

import carsharing.domain.Company;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CompanyDao implements ICompanyDao {

    private DbInfo dbInfo;

    public CompanyDao(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    @Override
    public Company getCompanyById(int companyId) {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "SELECT * FROM COMPANY " +
                    "WHERE ID = " + companyId + ";";

            ResultSet results = st.executeQuery(sql);

            // unique company
            results.next();
            Company company = new Company(
                    results.getInt(1),
                    results.getString(2)
            );

            return company;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "SELECT * FROM COMPANY " +
                    "ORDER BY ID;";

            ResultSet results = st.executeQuery(sql);

            List<Company> companies = new LinkedList<>();
            while (results.next()) {
                Company company = new Company(
                        results.getInt(1),
                        results.getString(2)
                );
                companies.add(company);
            }

            return companies;

        } catch (SQLException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    @Override
    public void addCompany(String name) {
        try (Connection conn = DriverManager.getConnection(dbInfo.getDbPath())) {
            conn.setAutoCommit(true);

            Statement st = conn.createStatement();
            String sql =
                    "INSERT INTO COMPANY (NAME) " +
                    "VALUES ('" + name + "');";

            st.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}