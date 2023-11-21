import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Scanner;
import java.sql.*;

public class User {
    int user_id;
    String id;
    String password;
    String name;
    String role;
    Timestamp created_date;

    User(int user_id, String id, String password, String name, String role, Timestamp created_date){
        this.user_id = user_id;
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
        this.created_date = created_date;
    }

    public void setUser(int user_id, String id, String password, String name, String role, Timestamp created_date){
        this.user_id = user_id;
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
        this.created_date = created_date;
    }

    public void logout(){
        this.user_id = 0;
        this.id = "";
        this.password = "";
        this.name = "";
        this.role = "";
        this.created_date = null;
    }

    public static boolean UserMenu(Connection connection, User user) {
        System.out.println("-----------------------------------------");
        System.out.println("User page");
        System.out.println("-----------------------------------------");
        System.out.println("1. Apply for a post");
        System.out.println("2. View my posts");
        System.out.println("3. Logout");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                ApplyPost(connection, user);
                return false;
            case 2:
                ViewMyPosts(connection, user);
                return false;
            case 3:
                System.out.println("-----------------------------------------");
                System.out.println("Logout");
                user.logout();
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    private static void ApplyPost(Connection connection, User user) {
        System.out.println("-----------------------------------------");
        System.out.println("Apply post");
        System.out.println("-----------------------------------------");
        String place = "SELECT * FROM place";
        try (PreparedStatement statement = connection.prepareStatement(place)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getInt(2) + ". " + resultSet.getString(3));
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Select place: ");
        Scanner sc = new Scanner(System.in);
        int place_id = sc.nextInt();
        String selected_place = "SELECT * FROM place INNER JOIN BOARD ON place.place_id = board.place_id WHERE place.place_id = ? ORDER BY board.floor";
        try (PreparedStatement statement = connection.prepareStatement(selected_place)) {
            statement.setInt(1, place_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Invalid place");
                }
                do {
                    System.out.println(resultSet.getInt(5) + ". " + resultSet.getString(3) + " " + resultSet.getString(9) + "층 " + resultSet.getString(7));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }
        System.out.print("Choose Board: ");
        int board_id = sc.nextInt();
        String board_check = "SELECT * FROM board WHERE board_id = ? AND place_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(board_check)) {
            statement.setInt(1, board_id);
            statement.setInt(2, place_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Invalid board");
                }
            }
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert post name: ");
        sc.nextLine();
        String post_name = sc.nextLine();
        System.out.print("Insert post content: ");
        String post_content = sc.nextLine();
        System.out.print("Insert require slot size: ");
        int require_slot = sc.nextInt();
        String insert_post = "INSERT INTO post (user_id, board_id, post_name, size, description, request_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insert_post)) {
            statement.setInt(1, user.user_id);
            statement.setInt(2, board_id);
            statement.setString(3, post_name);
            statement.setInt(4, require_slot);
            statement.setString(5, post_content);
            statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            System.out.println("-----------------------------------------");
            System.out.println("Post applied successfully");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void ViewMyPosts(Connection connection, User user) {
        System.out.println("-----------------------------------------");
        System.out.println("View my posts");
        System.out.println("-----------------------------------------");
        String my_posts = "SELECT * FROM post INNER JOIN board ON post.board_id = board.board_id INNER JOIN place ON board.place_id = place.place_id WHERE user_id = ? order by status";
        try (PreparedStatement statement = connection.prepareStatement(my_posts)) {
            statement.setInt(1, user.user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No post found");
                }
                do {
                    System.out.println("["+resultSet.getString(8) + "] " + resultSet.getInt(1) + ". " + resultSet.getString(17) + " " +resultSet.getString(14) + "층 "  + resultSet.getString(4));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            throw new IllegalStateException(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Select post: ");
        Scanner sc = new Scanner(System.in);
        int post_id = sc.nextInt();
        System.out.println("-----------------------------------------");
        String selected_post = "SELECT * FROM post INNER JOIN board ON post.board_id = board.board_id INNER JOIN place ON board.place_id = place.place_id WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selected_post)) {
            statement.setInt(1, post_id);
            statement.setInt(2, user.user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Invalid post");
                }
                do {
                    System.out.println("Post name: " + resultSet.getString(4));
                    System.out.println("Post content: " + resultSet.getString(7));
                    System.out.println("Require slot size: " + resultSet.getInt(6));
                    System.out.println("Request time: " + resultSet.getTimestamp(9));
                    System.out.println("Expired time: " + resultSet.getTimestamp(5));
                    System.out.println("Status: " + resultSet.getString(8));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.println("1. Cancel post");
        System.out.println("2. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");
        int menu = sc.nextInt();
        switch (menu) {
            case 1:
                String post_status = "SELECT * FROM post WHERE post_id = ? AND status = 'pending'";
                try (PreparedStatement statement = connection.prepareStatement(post_status)) {
                    statement.setInt(1, post_id);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()) {
                            throw new SQLException("This post is not pending");
                        }
                    }
                } catch (SQLException e){
                    throw new IllegalStateException("Unexpected value: " + e.getMessage());
                }
                String cancel_post = "UPDATE post SET status = 'cancel' WHERE post_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(cancel_post)) {
                    statement.setInt(1, post_id);
                    statement.executeUpdate();
                    System.out.println("-----------------------------------------");
                    System.out.println("Post canceled successfully");
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }
}
