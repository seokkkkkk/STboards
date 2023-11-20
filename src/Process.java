import java.sql.Connection;
import java.sql.SQLException;

public class Process {
    public static void start() {
        System.out.println("-----------------------------------------");
        System.out.println("SeoulTech Board Management System");
        System.out.println("-----------------------------------------");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");
    }

    public static boolean menu(Connection connection, int menu, User user){
        switch (menu) {
            case 1:
                try{
                    Account.login(connection, user);
                    return false;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            case 2:
                try{
                    Account.register(connection);
                    return false;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            case 3:
                System.out.println("-----------------------------------------");
                System.out.println("Exit SeoulTech Board Management System");
                System.out.println("-----------------------------------------");
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    public static void UserMenuProcess(Connection connection, User user) throws SQLException {
        while(true){
            if(user.role.equals("admin")){
                if (Admin.AdminMenu(connection, user)){
                    break;
                }
            } else {
                if (User.UserMenu(connection, user)){
                    break;
                }
            }
        }
    }
}
