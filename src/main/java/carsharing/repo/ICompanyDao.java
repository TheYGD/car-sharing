package carsharing.repo;

import carsharing.domain.Company;

import java.util.List;

public interface ICompanyDao {
    Company getCompanyById(int companyId);
    List<Company> getAllCompanies();
    void addCompany(String name);
}
