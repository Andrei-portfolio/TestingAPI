package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Чтобы подключиться к БД нужно в мэйвэн репозиториии скачать драйвер https://mvnrepository.com/artifact/org.postgresql/postgresql/42.7.4
и сохранить в pom файле
 */

public class DbDemo {
    public static void main(String[] args) throws SQLException {
        // ПОДКЛЮЧЕНИЕ К БД
        String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
        String login = "x_user";
        String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";
        Connection connection = DriverManager.getConnection(connectionString, login, password);

        // ЗАПРОС НА ПОЛУЧЕНИЕ ДАННЫХ (три колонки)
        String SELECT_ID_NAME = "select id, name, is_active from company;";
        ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ID_NAME);

        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));// Можно поставить цифрой - номер колонки (1), либо название
            System.out.println(resultSet.getString("name"));//Можно поставить цифрой - номер колонки (2), либо название
            System.out.println(resultSet.getBoolean("is_active"));
        }
        System.out.println("Конец");

        // ЗАПРОС НА ПОЛУЧЕНИЕ КОЛИЧЕСТВА ЗАПИСЕЙ В ТАБЛИЦЕ
        String SELECT_COUNT = "select count(*) from company;";
        resultSet = connection.createStatement().executeQuery(SELECT_COUNT);
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("count"));
        }

        // ПОЛУЧЕНИЕ ЭЛЕМЕНТА ПО АЙДИ
        int id = 838;//Важно поставить актуальный id, существующей компании. Иначе в терминале будет пусто
        ResultSet resultSet1 = getCompanyInfoById(connection, id);
        while (resultSet1.next()) {
            System.out.println("Элемент по id = " + id);
            System.out.println(resultSet1.getInt("id"));
            System.out.println(resultSet1.getString("name"));
            System.out.println(resultSet1.getBoolean("is_active"));
        }
    }

    private static ResultSet getCompanyInfoById(Connection connection, int id) throws SQLException {
        String GET_COMPANY = "select * from company where id = " + id;

        return connection.createStatement().executeQuery(GET_COMPANY);
    }
}
        /* */
