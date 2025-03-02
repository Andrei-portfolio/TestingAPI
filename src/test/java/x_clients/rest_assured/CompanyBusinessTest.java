package x_clients.rest_assured;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static x_clients.rest_assured.CompanyContractTest.apiCompanyHelper;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import java.sql.*;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import x_clients.rest_assured.entity.Company;
import x_clients.rest_assured.entity.CreateCompanyResponse;
import x_clients.rest_assured.ext.ApiCompanyHelperResolver;
import x_clients.rest_assured.ext.CreateCompanyResponseResolver;
import x_clients.rest_assured.helpers.ApiCompanyHelper;
import x_clients.rest_assured.helpers.AuthHelper;

@ExtendWith({CreateCompanyResponseResolver.class, ApiCompanyHelperResolver.class})
public class CompanyBusinessTest {

    private final static String URL = "https://x-clients-be.onrender.com/";
    private static AuthHelper authHelper;

        @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = URL;
        apiCompanyHelper = new ApiCompanyHelper();
        authHelper = new AuthHelper();
        String userToken = authHelper.authAndGetToken("leonardo", "leads");
        RestAssured.requestSpecification = new RequestSpecBuilder().build().header("x-client-token", userToken);
    }

        @Test
        @DisplayName("Удаление компании")
    public void deleteCompany(ApiCompanyHelper apiCompanyHelper, CreateCompanyResponse createCompanyResponse)
                throws InterruptedException {

            //CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany(); //Создать компанию (Предусловие). Закомитил, т.к. вынесли выше в скобках
            apiCompanyHelper.deleteCompany(createCompanyResponse.id());//Удалить компанию
            Thread.sleep(3000);//Так как тест не стабильный (удаление происх. не сразу а с запозд. на неск сек),
            // данная строка кода позволяет нам отсрочить время после удаления на 3 сек. Можно пост и больше
            Optional<Company> company = apiCompanyHelper.getCompany(createCompanyResponse.id());//Проверить через GET, что компании больше нет
            assertTrue(company.isEmpty());// проверь, что компании нет
        }
            /*ВАЖНО!!! Пока в классе Company не поставим аннотацию @JsonIgnoreProperties, которая игнорирует неизвестные поля
 тест deleteCompany не будет работать*/

    @Test
    @DisplayName("Удаление несуществующей компании")
    @Disabled
    public void deleteNotExistentCompany(ApiCompanyHelper apiCompanyHelper) throws InterruptedException {

        apiCompanyHelper.deleteCompany(666666);
        Thread.sleep(3000);
        //Optional<Company> company = apiCompanyHelper.getCompany(666666);
        Optional<Company> company = apiCompanyHelper.getCompany(666666);
        System.out.println(company);
        // Проверить через GET, что компании больше нет
    }

    @Test//Создадим компанию и проверим через БД, что она там есть
    @DisplayName("Создание компании")
    public void createCompany (ApiCompanyHelper apiCompanyHelper) throws SQLException {
        //ПОДКЛЮЧЕНИЕ к БД
        String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
        String login = "x_user";
        String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";
        Connection connection = DriverManager.getConnection(connectionString, login, password);

        //Сам тест
        String name = "тест с бд";
        String description = "описание с бд";
        CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany(
              name,
              description
      );
      int companyId = createCompanyResponse.id();

        String GET_COMPANY = "select * from company where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPANY);
        preparedStatement.setInt(1, companyId);
        ResultSet result = preparedStatement.executeQuery();//При insert пишем в коде executeUpdate, при select пишем executeQuery
        assertTrue(result.next());// проверяем, что какой то элемент найден
        assertEquals(name,result.getString("name"));
        assertEquals(description,result.getString("description"));
        assertTrue(result.getBoolean("is_active"));
    }

    @Test
    @DisplayName("Получение информации о компании")
    public void readCompanyInfo (ApiCompanyHelper apiCompanyHelper) throws SQLException {

        //ПОДКЛЮЧЕНИЕ к БД
        String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
        String login = "x_user";
        String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";
        Connection connection = DriverManager.getConnection(connectionString, login, password);

        //Сам тест
        String name = "тест с бд";
        String description = "описание с бд";

        String CREATE_COMPANY = "insert into company (name, description) values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(
                CREATE_COMPANY,
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.executeUpdate();// При insert пишем в коде executeUpdate, при select пишем executeQuery
        ResultSet result = preparedStatement.getGeneratedKeys();
        result.next();
        int createdId = result.getInt("id");

        Optional<Company> companyResponse = apiCompanyHelper.getCompany(createdId);
        Company company = companyResponse.get();

        assertEquals(createdId, company.id());
        assertEquals(name, company.name());
        assertEquals(description, company.description());


        //int createdId = companyRepository.createCompany(name, description);
    }
}



