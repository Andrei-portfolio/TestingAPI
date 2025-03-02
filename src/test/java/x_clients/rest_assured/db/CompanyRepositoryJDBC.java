package x_clients.rest_assured.db;

import x_clients.rest_assured.entity.CompanyDB;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyRepositoryJDBC implements CompanyRepository {

    private final Connection connection;

    public CompanyRepositoryJDBC(Connection connection) {
       this.connection = connection;
    }

    @Override
    public int createCompany(String name, String description) throws SQLException {
        String CREATE_COMPANY = "insert into company (name, description) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(
                CREATE_COMPANY,
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.executeUpdate();// При insert пишем в коде executeUpdate, при select пишем executeQuery
        ResultSet result = preparedStatement.getGeneratedKeys();
        result.next();
        return result.getInt("id");
    }

    @Override
    public CompanyDB selectById(int companyId) throws SQLException {
        String GET_COMPANY = "select * from company where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPANY);
        preparedStatement.setInt(1, companyId);

        ResultSet result = preparedStatement.executeQuery();
       result.next();// проверяем, что какой то элемент найден
        return new CompanyDB(
        result.getInt("id"),
        result.getString("name"),
        result.getString("description"),
        result.getBoolean("is_active"),
        result.getString("deleted_at")
        );
    }
}