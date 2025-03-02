package x_clients.rest_assured.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)//аннотация, которая игнорирует неизвестные поля
//Без этой анотации в классе CompanyBusinessTest не будет работать тест deleteCompany
public record Company(int id, String name, String description, Boolean isActive) {

}