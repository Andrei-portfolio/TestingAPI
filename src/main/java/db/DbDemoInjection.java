package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Этот класс показывает, как НЕЛЬЗЯ писать код, чтобы не допустить SQL инъекций
 */
public class DbDemoInjection {
    public static void main(String[] args) throws SQLException {


        // ПОДКЛЮЧЕНИЕ К БД
        String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
        String login = "x_user";
        String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";
        Connection connection = DriverManager.getConnection(connectionString, login, password);

        // ПОЛУЧЕНИЕ ЭЛЕМЕНТА ПО ИМЕНИ
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя:");// Ввести имя компании
        String name = scanner.nextLine();

        ResultSet resultSet1 = getCompanyInfoByName(connection, name);
        while (resultSet1.next()) {
            System.out.println("Элемент по имени: " + name);
            System.out.println(resultSet1.getInt("id"));
            System.out.println(resultSet1.getString("name"));
            System.out.println(resultSet1.getBoolean("is_active"));
        }
    }

    private static ResultSet getCompanyInfoByName(Connection connection, String name) throws SQLException {
        String GET_COMPANY = "select * from company where name = '" + name + "'";

        return connection.createStatement().executeQuery(GET_COMPANY);
    }
}