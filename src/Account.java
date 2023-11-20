import java.sql.*;
import java.util.Scanner;

public class Account {
    public static void login(Connection connection, User user) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Login page");
        System.out.println("-----------------------------------------");
        System.out.print("ID: ");
        String id = sc.nextLine();
        System.out.print("Password: ");
        String pw = sc.nextLine();
        System.out.println("-----------------------------------------");
        String query = "SELECT * FROM USER WHERE id = ? AND pw = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            statement.setString(2, pw);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) > 0) {
                        user.setUser(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getTimestamp(6));
                        System.out.println(user.name + "님이 SeoulTech Board Management System에 접속하셨습니다.");
                        return;
                    }
                }
            }
        }
        throw new SQLException("Invalid ID or password");
    }

    public static void register(Connection connection) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Register page");
        System.out.println("-----------------------------------------");
        System.out.print("ID: ");
        String id = sc.nextLine();
        System.out.print("Password: ");
        String pw = sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.println("-----------------------------------------");
        String query = "INSERT INTO USER (id, pw, name, role, created_date) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, id);
            statement.setString(2, pw);
            statement.setString(3, name);
            statement.setString(4, "user");
            statement.executeUpdate();

            System.out.println("Successfully registered " + name);
        } catch (SQLException e){
            throw new SQLException(e);
        }
    }
}
