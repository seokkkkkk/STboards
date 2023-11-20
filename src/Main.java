import java.sql.*;
import java.util.Scanner;
public class Main {
    static String userName = "seok";
    static String password = "119dbstjr";
    static String url = "jdbc:mysql://25.8.212.104:3306/stboard";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        User user = new User(0, null, null, null, null, null);

        while(true){
            try {
                Connection connection = DriverManager.getConnection(url, userName, password);
                if (user.user_id != 0){
                    Process.UserMenuProcess(connection, user);
                } else{
                    Process.start();
                    int menu = sc.nextInt();
                    if(Process.menu(connection, menu, user)){
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("-----------------------------------------");
                System.out.println("Error: " + e.getMessage());
                sc.nextLine();
            }
        }

    }
}