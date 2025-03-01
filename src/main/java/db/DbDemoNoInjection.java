package db;

import java.sql.*;
import java.util.Scanner;

/**
 * Этот класс показывает, как НУЖНО писать код, чтобы не допустить SQL инъекций
 */
public class DbDemoNoInjection {
    public static void main(String[] args) throws SQLException {


        // ПОДКЛЮЧЕНИЕ К БД
        String connectionString = "jdbc:postgresql://dpg-cuofqqt2ng1s73e8pm2g-a.frankfurt-postgres.render.com/x_clients_ehy7";
        String login = "x_user";
        String password = "Mi4j6vZGytGHHMHhmHw86Q4MJ0YSLr1R";
        Connection connection = DriverManager.getConnection(connectionString, login, password);

        // ПОЛУЧЕНИЕ ЭЛЕМЕНТА ПО ИМЕНИ
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя:");
        String name = scanner.nextLine();
        int id = 843;// Важно написать id компании, кооторая есть в БД
        ResultSet resultSet1 = getCompanyInfoByNameAndId(connection, name, id);
        while (resultSet1.next()) {
            System.out.println("Элемент по имени: " + name);
            System.out.println(resultSet1.getInt("id"));
            System.out.println(resultSet1.getString("name"));
            System.out.println(resultSet1.getBoolean("is_active"));
        }
    }

    private static ResultSet getCompanyInfoByNameAndId(Connection connection, String name, int id) throws SQLException {
        String GET_COMPANY = "select * from company where name = ? and id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(GET_COMPANY);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, id);

        return preparedStatement.executeQuery();
    }
}