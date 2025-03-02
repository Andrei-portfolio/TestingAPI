package x_clients.rest_assured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import x_clients.rest_assured.db.CompanyRepository;
import x_clients.rest_assured.entity.Company;
import x_clients.rest_assured.entity.CompanyDB;
import x_clients.rest_assured.entity.CreateCompanyResponse;
import x_clients.rest_assured.ext.ApiCompanyHelperResolver;
import x_clients.rest_assured.ext.CreateCompanyRepositoryResolver;
import x_clients.rest_assured.ext.CreateCompanyResponseResolver;
import x_clients.rest_assured.helpers.ApiCompanyHelper;
import x_clients.rest_assured.helpers.AuthHelper;

import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static x_clients.rest_assured.CompanyContractTest.apiCompanyHelper;

@ExtendWith({CreateCompanyRepositoryResolver.class, ApiCompanyHelperResolver.class, CreateCompanyResponseResolver.class})
public class CompanyBusinessTest2 {

    private final static String URL = "https://x-clients-be.onrender.com/";

    private static AuthHelper authHelper;

    //ПОДКЛЮЧЕНИЕ к БД
    private final static String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
    private final static String login = "x_user";
    private final static String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";
    private final static String name = "тест с бд";
    private final static String description = "описание с бд";

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
    public void deleteCompany(ApiCompanyHelper apiCompanyHelper, CreateCompanyResponse createCompanyResponse, CompanyRepository companyRepository)
                throws InterruptedException, SQLException {

            int companyId = createCompanyResponse.id();
            apiCompanyHelper.deleteCompany(companyId);//Удалить компанию
            Thread.sleep(2000);//Так как тест не стабильный (удаление происх. не сразу а с запозд. на неск сек),
            // данная строка кода позволяет нам отсрочить время после удаления на 3 сек. Можно пост и больше
            CompanyDB companyDB = companyRepository.selectById(companyId);
            assertNotNull(companyDB.deletedAt());
            }

            /*ВАЖНО!!! Пока в классе Company не поставим аннотацию @JsonIgnoreProperties, которая игнорирует неизвестные поля
 тест deleteCompany не будет работать*/

    @Test
    @DisplayName("Удаление несуществующей компании")
    @Disabled
    public void deleteNotExistentCompany(ApiCompanyHelper apiCompanyHelper) throws InterruptedException {

        apiCompanyHelper.deleteCompany(666666);
        Thread.sleep(3000);
        Optional<Company> company = apiCompanyHelper.getCompany(666666);
        System.out.println(company);
        // Проверить через GET, что компании больше нет
    }

    @Test//Создадим компанию и проверим через БД, что она там есть
    @DisplayName("Создание компании")
    public void createCompanyTest (ApiCompanyHelper apiCompanyHelper, CompanyRepository companyRepository) throws SQLException {

        //Сам тест
        CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany(
              name,
              description
      );
      int companyId = createCompanyResponse.id();

        CompanyDB result = companyRepository.selectById(companyId);//При insert пишем в коде executeUpdate, при select пишем executeQuery
        assertEquals(name,result.name());
        assertEquals(description,result.description());
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("Получение информации о компании")
    public void readCompanyInfo (
            ApiCompanyHelper apiCompanyHelper,
                CompanyRepository companyRepository) throws SQLException {

        //Сам тест
        int createdId = companyRepository.createCompany(name,description);

        Optional<Company> companyResponse = apiCompanyHelper.getCompany(createdId);
        Company company = companyResponse.get();

        assertEquals(createdId, company.id());
        assertEquals(name, company.name());
        assertEquals(description, company.description());
    }
}



