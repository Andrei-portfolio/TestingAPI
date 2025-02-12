package x_clients.rest_assured.entity;

public record CreateCompanyRequest(String name, String description) {// Создаём объект
    // этого класса. Напомню, что в данном случае мы создали Record. Т.е. класс, который не требует делать
    // конструктор и геттеры. Так как они автоматически создаются, хоть мы их и не видим

}
