package x_clients.rest_assured.db;

import x_clients.rest_assured.entity.CompanyDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface CompanyRepository {

    int createCompany(String name, String description) throws SQLException;

    CompanyDB selectById(int companyId) throws SQLException;
}

