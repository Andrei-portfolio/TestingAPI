package x_clients.rest_assured;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static x_clients.rest_assured.CompanyContractTest.apiCompanyHelper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import x_clients.rest_assured.entity.Company;
import x_clients.rest_assured.entity.Company;
import x_clients.rest_assured.entity.CreateCompanyResponse;
//import x_clients.rest_assured.ext.ApiCompanyHelperResolver;
//import x_clients.rest_assured.ext.CreateCompanyResponseResolver;
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
    public void deleteCompany(CreateCompanyResponse createCompanyResponse) throws InterruptedException {

            //CreateCompanyResponse createCompanyResponse = apiCompanyHelper.createCompany(); //Создать компанию (Предусловие). Закомитил, т.к. вынесли выше в скобках
            apiCompanyHelper.deleteCompany(createCompanyResponse.id());//Удалить компанию
            Thread.sleep(3000);//Так как тест не стабильный (удаление происх. не сразу а с запозд. на неск сек),
            // данная строка кода позволяет нам отсрочить время после удаления на 3 сек. Можно пост и больше
            Company company = apiCompanyHelper.getCompany(createCompanyResponse.id());//Проверить через GET, что компании больше нет
            System.out.println(company);
        }
            /*ВАЖНО!!! Пока в классе Company не поставим аннотацию @JsonIgnoreProperties, которая игнорирует неизвестные поля
 тест deleteCompany не будет работать*/

//
//        Optional<Company> company = apiCompanyHelper.getCompany(createCompanyResponse.id());
//        assertTrue(company.isEmpty());
//        // Проверить через GET, что компании больше нет
//    }
//
    public void deleteNotExistentCompany() throws InterruptedException {
        apiCompanyHelper.deleteCompany(666666);
        Thread.sleep(3000);
        //Optional<Company> company = apiCompanyHelper.getCompany(666666);
                Company company = apiCompanyHelper.getCompany(666666);
        System.out.println(company);
        // Проверить через GET, что компании больше нет
    }

}