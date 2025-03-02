package x_clients.rest_assured.ext;

import org.junit.jupiter.api.extension.*;
import x_clients.rest_assured.db.CompanyRepository;
import x_clients.rest_assured.db.CompanyRepositoryJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateCompanyRepositoryResolver implements ParameterResolver, BeforeAllCallback, AfterAllCallback {

    private Connection connection;

    private final static String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";

    private final static String login = "x_user";

    private final static String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CompanyRepository.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return new CompanyRepositoryJDBC(connection);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
    if (connection !=null)
        connection.close();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        try {
            connection = DriverManager.getConnection(connectionString, login, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}