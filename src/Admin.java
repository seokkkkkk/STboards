import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {

    public static boolean AdminMenu(Connection connection, User user) throws SQLException {
        System.out.println("-----------------------------------------");
        System.out.println("Admin page");
        System.out.println("-----------------------------------------");
        System.out.println("1. User management");
        System.out.println("2. Place management");
        System.out.println("3. Board management");
        System.out.println("4. Post management");
        System.out.println("5. Department management");
        System.out.println("6. Logout");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                userManagementProcess(connection);
                return false;
            case 2:
                placeManagementProcess(connection);
                return false;
            case 3:
                boardManagementProcess(connection, user);
                return false;
            case 4:
                postManagementProcess(connection, user);
                return false;
            case 5:
                departmentManagementProcess(connection);
                return false;
            case 6:
                System.out.println("-----------------------------------------");
                System.out.println("Logout");
                user.logout();
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    public static void departmentManagementProcess(Connection connection){
        System.out.println("-----------------------------------------");
        System.out.println("Department management");
        System.out.println("-----------------------------------------");
        System.out.println("1. Create a department");
        System.out.println("2. Delete a department");
        System.out.println("3. View a department");
        System.out.println("4. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                CreateDepart(connection);
                break;
            case 2:
                DeleteDepart(connection);
                break;
            case 3:
                ViewDepart(connection);
                break;
            case 4:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    public static void postManagementProcess(Connection connection, User user) {
        System.out.println("-----------------------------------------");
        UpdatePost(connection);
        System.out.println("-----------------------------------------");
        System.out.println("Post management");
        System.out.println("-----------------------------------------");
        System.out.println("1. Request confirmation");
        System.out.println("2. View posts");
        System.out.println("3. Cancel posts");
        System.out.println("4. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                RequestConfirm(connection, user);
                break;
            case 2:
                ViewPost(connection);
                break;
            case 3:
                CancelPost(connection, user);
                break;
            case 4:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    public static void userManagementProcess(Connection connection) {
        System.out.println("-----------------------------------------");
        System.out.println("User management");
        System.out.println("-----------------------------------------");
        System.out.println("1. Modify a user");
        System.out.println("2. View users");
        System.out.println("3. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                modifyUser(connection);
                break;
            case 2:
                userList(connection);
                break;
            case 3:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    public static void placeManagementProcess(Connection connection) {
        System.out.println("-----------------------------------------");
        System.out.println("Place management");
        System.out.println("-----------------------------------------");
        System.out.println("1. Create a place");
        System.out.println("2. Delete a place");
        System.out.println("3. View all place");
        System.out.println("4. View a place");
        System.out.println("5. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                CreatePlace(connection);
                break;
            case 2:
                DeletePlace(connection);
                break;
            case 3:
                ViewAllPlace(connection);
                break;
            case 4:
                ViewAPlace(connection);
                break;
            case 5:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }

    public static void boardManagementProcess(Connection connection, User user) throws SQLException {
        System.out.println("-----------------------------------------");
        System.out.println("Board management");
        System.out.println("-----------------------------------------");
        System.out.println("1. Create a board");
        System.out.println("2. Delete a board");
        System.out.println("3. View a board");
        System.out.println("4. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");

        Scanner sc = new Scanner(System.in);
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                CreateBoard(connection, user);
                break;
            case 2:
                DeleteBoard(connection, user);
                break;
            case 3:
                ViewBoard(connection);
                break;
            case 4:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }


    private static void ViewBoard(Connection connection){
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("View a board");
        System.out.println("-----------------------------------------");
        String board_sql2 = "SELECT * FROM BOARD ORDER BY place_id, floor";
        try (PreparedStatement statement = connection.prepareStatement(board_sql2)) {
            BoardView(statement);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert board number: ");
        int board2 = sc.nextInt();
        System.out.println("-----------------------------------------");
        String board_sql3 = "SELECT BOARD.place_id, BOARD.board_id, BOARD.board_name, BOARD.slot, BOARD.floor, COALESCE(SUM(CASE WHEN POST.status = 'approved' THEN POST.size ELSE 0 END), 0) AS TotalPostSize FROM BOARD LEFT OUTER JOIN POST ON BOARD.board_id = POST.board_id WHERE BOARD.board_id = ? GROUP BY BOARD.place_id, BOARD.board_id, BOARD.board_name, BOARD.slot, BOARD.floor";
        try (PreparedStatement statement = connection.prepareStatement(board_sql3)) {
            statement.setInt(1, board2);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Board Found");
                }
                do {
                    System.out.println("Place ID: " + resultSet.getInt(1));
                    System.out.println("Board ID: " + resultSet.getInt(2));
                    System.out.println("Board name: " + resultSet.getString(3));
                    System.out.println("Board slot: " + resultSet.getInt(6) + "/" + resultSet.getInt(4));
                    System.out.println("Board floor: " + resultSet.getInt(5));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void BoardView(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                throw new SQLException("No Board Found");
            }
            do {
                System.out.println("PLACE:" + resultSet.getInt(2) + " FLOOR:" + resultSet.getInt(5) + " BOARD:" + resultSet.getInt(1)+ ". " + resultSet.getString(3));
            } while (resultSet.next());
        }
    }

    private static void CreateBoard(Connection connection, User user) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Create a board");
        System.out.println("-----------------------------------------");
        String place_sql = "SELECT * FROM PLACE INNER JOIN USER_DEPART ON PLACE.depart_id = USER_DEPART.depart_id WHERE USER_DEPART.user_id = ? ORDER BY place_id, floor";
        try (PreparedStatement statement = connection.prepareStatement(place_sql)) {
            statement.setInt(1, user.user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Place Found");
                }
                do {
                    System.out.println(resultSet.getInt(2) + ". " + resultSet.getString(3) + " number of floors: " + resultSet.getInt(4) + " ");
                } while (resultSet.next());
            }
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert place number: ");
        int place = sc.nextInt();
        int maxfloor;
        String floor_sql = "SELECT floor FROM PLACE INNER JOIN USER_DEPART ON PLACE.depart_id = USER_DEPART.depart_id WHERE USER_DEPART.user_id = ? AND place_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(floor_sql)) {
            statement.setInt(1, user.user_id);
            statement.setInt(2, place);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Place Found");
                }
                do {
                    maxfloor = resultSet.getInt(1);
                } while (resultSet.next());
            }
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert located floor(MAX:" + maxfloor + "): ");
        int floor = sc.nextInt();
        if (floor > maxfloor){
            throw new SQLException("Invalid floor");
        }
        System.out.print("Insert board size: ");
        int slot = sc.nextInt();
        System.out.println("-----------------------------------------");
        System.out.print("Insert board name: ");
        sc.nextLine(); // 개행 문자 처리
        String board_name = sc.nextLine();
        System.out.println("-----------------------------------------");
        String query = "INSERT INTO BOARD (place_id, board_name, slot, floor) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, place);
            statement.setString(2, board_name);
            statement.setInt(3, slot);
            statement.setInt(4, floor);
            statement.executeUpdate();
            System.out.println("Successfully created " + board_name);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void DeleteBoard(Connection connection, User user) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Delete a board");
        System.out.println("-----------------------------------------");

        // BOARD 테이블 출력
        String board_sql = "SELECT * FROM BOARD INNER JOIN PLACE ON BOARD.place_id = PLACE.place_id INNER USER_DEPART ON PLACE.depart_id = USER_DEPART.depart_id WHERE USER_DEPART.user_id = ? ORDER BY place_id, floor";
        try (PreparedStatement statement = connection.prepareStatement(board_sql)) {
            statement.setInt(1, user.user_id);
            BoardView(statement);
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }

        System.out.println("-----------------------------------------");
        System.out.print("Insert board number: ");
        int boardId = sc.nextInt();
        System.out.println("-----------------------------------------");

        // POST 삭제
        String deletePosts = "DELETE FROM POST WHERE board_id = ?";
        // BOARD 삭제
        String deleteBoard = "DELETE FROM BOARD WHERE board_id = ?";

        try {
            // 트랜잭션 시작
            connection.setAutoCommit(false);

            // POST 삭제
            try (PreparedStatement statement = connection.prepareStatement(deletePosts)) {
                statement.setInt(1, boardId);
                statement.executeUpdate();
            }

            // BOARD 삭제
            try (PreparedStatement statement = connection.prepareStatement(deleteBoard)) {
                statement.setInt(1, boardId);
                statement.executeUpdate();
            }

            // 트랜잭션 커밋
            connection.commit();
            System.out.println("Successfully deleted board with ID " + boardId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                // 롤백 수행
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                // 자동 커밋 설정 복원
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static void ViewAPlace(Connection connection){
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("View a place");
        System.out.println("-----------------------------------------");
        String place_sql3 = "SELECT * FROM PLACE";
        try (PreparedStatement statement = connection.prepareStatement(place_sql3)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Place Found");
                }
                do {
                    System.out.print(resultSet.getInt(2) + ". " + resultSet.getString(3) + " ");
                } while (resultSet.next());
            }
            System.out.println();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.print("Insert place number: ");
        String place2 = sc.nextLine();
        System.out.println("-----------------------------------------");
        String place_sql4 = "SELECT P.place_id, P.place_name, P.floor, COUNT(B.board_id) AS NumberOfBoards, SUM(B.slot) AS TotalSlots, SUM(B.slot) - IFNULL(SUM(PS.TotalPostSize),0) AS SlotPostSizeDifference FROM PLACE P LEFT JOIN BOARD B ON P.place_id = B.place_id LEFT JOIN (SELECT board_id, SUM(size) AS TotalPostSize FROM POST WHERE status = 'approved' GROUP BY board_id) PS ON B.board_id = PS.board_id WHERE P.place_id = ? GROUP BY P.place_id, P.place_name, P.floor";
        try (PreparedStatement statement = connection.prepareStatement(place_sql4)) {
            statement.setString(1, place2);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Place Found");
                }
                do {
                    System.out.println("Place ID: " + resultSet.getInt(1));
                    System.out.println("Place name: " + resultSet.getString(2));
                    System.out.println("Number of floors: " + resultSet.getString(3));
                    System.out.println("Number of boards: " + resultSet.getString(4));
                    System.out.println("Total slots: " + resultSet.getString(5));
                    System.out.println("Remaining Slots: " + resultSet.getString(6));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void ViewAllPlace(Connection connection) {
        System.out.println("-----------------------------------------");
        System.out.println("View all place");
        AllPlaceTable(connection);
    }
    private static void AllPlaceTable(Connection connection){
        System.out.println("-----------------------------------------");
        String place_sql2 = "SELECT * FROM PLACE";
        try (PreparedStatement statement = connection.prepareStatement(place_sql2)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Place Found");
                }
                do {
                    System.out.println(resultSet.getInt(2) + ". " + resultSet.getString(3) + " " + resultSet.getString(4) + "층");
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void DeletePlace(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Delete a place");
        System.out.println("-----------------------------------------");

        // PLACE 테이블 출력
        String place_sql = "SELECT * FROM PLACE";
        try (PreparedStatement statement = connection.prepareStatement(place_sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Place Found");
                }
                do {
                    System.out.print(resultSet.getInt("place_id") + ". " + resultSet.getString("place_name") + " ");
                } while (resultSet.next());
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("-----------------------------------------");
        System.out.print("Insert place number: ");
        int placeId = sc.nextInt();
        System.out.println("-----------------------------------------");

        // POST 삭제
        String deletePosts = "DELETE FROM POST WHERE board_id IN (SELECT board_id FROM BOARD WHERE place_id = ?)";
        // BOARD 삭제
        String deleteBoards = "DELETE FROM BOARD WHERE place_id = ?";
        // PLACE 삭제
        String deletePlace = "DELETE FROM PLACE WHERE place_id = ?";

        try {
            // 트랜잭션 시작
            connection.setAutoCommit(false);

            // POST 삭제
            try (PreparedStatement statement = connection.prepareStatement(deletePosts)) {
                statement.setInt(1, placeId);
                statement.executeUpdate();
            }

            // BOARD 삭제
            try (PreparedStatement statement = connection.prepareStatement(deleteBoards)) {
                statement.setInt(1, placeId);
                statement.executeUpdate();
            }

            // PLACE 삭제
            try (PreparedStatement statement = connection.prepareStatement(deletePlace)) {
                statement.setInt(1, placeId);
                statement.executeUpdate();
            }

            // 트랜잭션 커밋
            connection.commit();
            System.out.println("Successfully deleted place with ID " + placeId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                // 롤백 수행
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                // 자동 커밋 설정 복원
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    private static void CreatePlace(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Create a place");
        System.out.println("-----------------------------------------");
        System.out.print("Insert place name: ");
        String name = sc.nextLine();
        System.out.println("-----------------------------------------");
        String depart_sql = "SELECT * FROM DEPARTMENT";
        try (PreparedStatement statement = connection.prepareStatement(depart_sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Department");
                }
                do {
                    System.out.print(resultSet.getInt(1) + ". " + resultSet.getString(2) + " ");
                } while (resultSet.next());
            }
            System.out.println();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert place department ID: ");
        int depart = sc.nextInt();
        System.out.println("-----------------------------------------");
        System.out.print("Insert number of floors: ");
        sc.nextLine(); // 개행 문자 처리
        String floor = sc.nextLine();
        System.out.println("-----------------------------------------");
        String query = "INSERT INTO PLACE (place_name, depart_id, floor) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, depart);
            statement.setString(3, floor);
            statement.executeUpdate();
            System.out.println("Successfully created " + name);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void userList(Connection connection) {
        System.out.println("-----------------------------------------");
        System.out.println("user list");
        System.out.println("-----------------------------------------");
        String query = "SELECT USER.user_id, USER.name, USER.role, GROUP_CONCAT(DEPARTMENT.depart_name SEPARATOR ', ') AS departments FROM USER LEFT JOIN USER_DEPART ON USER.user_id = USER_DEPART.user_id LEFT JOIN DEPARTMENT ON USER_DEPART.depart_id = DEPARTMENT.depart_id GROUP BY USER.user_id, USER.name, USER.role";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No User");
                }
                do {
                    System.out.println(resultSet.getInt(1) + ". " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void modifyUser(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Modify a user");
        userList(connection);
        System.out.print("Insert user number: ");
        String user = sc.nextLine();
        System.out.println("-----------------------------------------");
        System.out.println("1. Modify role");
        System.out.println("2. Add department");
        System.out.println("3. Remove department");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");
        int menu2 = sc.nextInt();
        switch (menu2) {
            case 1:
                System.out.println("-----------------------------------------");
                System.out.println("Modify role");
                System.out.println("-----------------------------------------");
                System.out.print("Insert role: ");
                sc.nextLine(); // 개행 문자 처리
                String role = sc.nextLine();
                System.out.println("-----------------------------------------");
                String query = "UPDATE USER SET role = ? WHERE user_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, role);
                    statement.setString(2, user);
                    statement.executeUpdate();
                    System.out.println("Successfully modified " + user);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                System.out.println("-----------------------------------------");
                System.out.println("Add department");
                System.out.println("-----------------------------------------");
                String depart_sql = "SELECT * FROM DEPARTMENT";
                try (PreparedStatement statement = connection.prepareStatement(depart_sql)) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            System.out.println(resultSet.getInt(1) + ". " + resultSet.getString(2));
                        }
                    }
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                sc.nextLine(); // 개행 문자 처리
                System.out.print("Insert depart_id: ");
                String depart = sc.nextLine();
                System.out.println("-----------------------------------------");
                String query2 = "INSERT INTO USER_DEPART (user_id, depart_id) VALUES (?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query2)) {
                    statement.setString(1, user);
                    statement.setString(2, depart);
                    statement.executeUpdate();
                    System.out.println("Successfully add " + depart + " to " + user);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                System.out.println("-----------------------------------------");
                System.out.println("Remove department");
                System.out.println("-----------------------------------------");
                String depart_sql2 = "SELECT * FROM USER_DEPART INNER JOIN DEPARTMENT ON USER_DEPART.depart_id = DEPARTMENT.depart_id WHERE user_id = ? )";
                try (PreparedStatement statement = connection.prepareStatement(depart_sql2)) {
                    statement.setString(1, user);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            System.out.println(resultSet.getString(2) + ". " + resultSet.getString(4));
                        }
                    }
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                sc.nextLine(); // 개행 문자 처리
                System.out.print("Insert depart_id: ");
                String depart2 = sc.nextLine();
                System.out.println("-----------------------------------------");
                String query3 = "DELETE FROM USER_DEPART WHERE user_id = ? AND depart_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query3)) {
                    statement.setString(1, user);
                    statement.setString(2, depart2);
                    statement.executeUpdate();
                    System.out.println("Successfully remove " + depart2 + " from " + user);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu2);
        }
    }

    private static void CreateDepart(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Create a department");
        System.out.println("-----------------------------------------");
        System.out.print("Department name: ");
        String name = sc.nextLine();
        String query = "INSERT INTO DEPARTMENT (depart_name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void DeleteDepart(Connection connection) {
        System.out.println("-----------------------------------------");
        System.out.println("Delete a department");
        System.out.println("-----------------------------------------");
        System.out.print("Insert department number: ");
        Scanner sc = new Scanner(System.in);
        String depart = sc.nextLine();
        System.out.println("-----------------------------------------");
        String query = "DELETE FROM DEPARTMENT WHERE depart_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, depart);
            statement.executeUpdate();
            System.out.println("Successfully deleted " + depart);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void ViewDepart(Connection connection) {
        System.out.println("-----------------------------------------");
        System.out.println("View a department");
        System.out.println("-----------------------------------------");
        System.out.println("department list");
        System.out.println("-----------------------------------------");
        String query = "SELECT * FROM DEPARTMENT";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Department");
                }
                do {
                    System.out.println(resultSet.getInt(1) + ". " + resultSet.getString(2));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void ManageablePlace(Connection connection, User user){
        System.out.println("-----------------------------------------");
        System.out.println("Manageable place list");
        System.out.println("-----------------------------------------");
        String place_sql = "SELECT * FROM PLACE INNER JOIN USER_DEPART ON PLACE.depart_id = USER_DEPART.depart_id WHERE USER_DEPART.user_id = ? ORDER BY place_id";
        try (PreparedStatement statement = connection.prepareStatement(place_sql)) {
            statement.setInt(1, user.user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Manageable Place");
                }
                do {
                    System.out.println(resultSet.getInt(2) + ". " + resultSet.getString(3) + " number of floors: " + resultSet.getInt(4) + " ");
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void PlaceValidationCheck(Connection connection, User user, int place){
        //place가 속한 department와 user가 속한 user_depart의 depart_id가 같은지 확인
        String place_depart_sql = "SELECT count(*) FROM PLACE INNER JOIN USER_DEPART ON PLACE.depart_id = USER_DEPART.depart_id WHERE place_id = ? AND USER_DEPART.user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(place_depart_sql)) {
            statement.setInt(1, place);
            statement.setInt(2, user.user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt(1) == 0) {
                        throw new SQLException("You cannot manage this place.");
                    }
                }
            }
        } catch (SQLException e){
            throw new IllegalStateException("Unexpected value: " + e.getMessage());
        }
    }

    public static void CancelPost(Connection connection, User user){
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Cancel posts");
        ManageablePlace(connection, user);
        System.out.println("-----------------------------------------");
        System.out.print("Insert place number: ");
        int place = sc.nextInt();
        System.out.println("-----------------------------------------");
        System.out.println("Approved Board list");
        System.out.println("-----------------------------------------");
        PlaceValidationCheck(connection, user, place);
        String place_board_sql = "SELECT BOARD.*, COUNT(POST.post_id) AS NumOfRequestPosts FROM BOARD INNER JOIN POST ON BOARD.board_id = POST.board_id WHERE BOARD.place_id = ? AND POST.status = 'approved' GROUP BY BOARD.board_id ORDER BY BOARD.floor";
        try (PreparedStatement statement = connection.prepareStatement(place_board_sql)) {
            statement.setInt(1, place);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Post Request");
                }
                do {
                    System.out.println(resultSet.getInt(1) + ". " + resultSet.getString(3) + " FLOOR:" + resultSet.getInt(5) + " SLOT:" + resultSet.getInt(4) + " REQUESTS:" + resultSet.getInt(6));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert board number: ");
        int board = sc.nextInt();
        System.out.println("-----------------------------------------");
        String post_sql = "SELECT * FROM POST WHERE board_id = ? AND status = 'approved' ORDER BY request_time";
        try (PreparedStatement statement = connection.prepareStatement(post_sql)) {
            statement.setInt(1, board);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Post Request");
                }
                do {
                    System.out.println("POST NO." + resultSet.getInt(1) +" USER: " + resultSet.getInt(2) +  " POST: " + resultSet.getString(4) + " DESCRIPTION: " + resultSet.getString(7) + " SIZE: " + resultSet.getInt(6) + " TIMESTAMP: " + resultSet.getTimestamp(9));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert post number: ");
        int post = sc.nextInt();
        System.out.println("-----------------------------------------");
        String post_sql2 = "SELECT * FROM POST WHERE post_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(post_sql2)) {
            statement.setInt(1, post);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Post ID: " + resultSet.getInt(1));
                    System.out.println("Post title: " + resultSet.getString(4));
                    System.out.println("Post content: " + resultSet.getString(7));
                    System.out.println("Post size: " + resultSet.getInt(6));
                    System.out.println("Post user ID: " + resultSet.getInt(2));
                    System.out.println("Post board ID: " + resultSet.getInt(3));
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.println("1. Cancel Post");
        System.out.println("2. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                //post가 속한 board의 place_id를 placeid 변수에 저장
                int placeid = 0;
                String place_sql2 = "SELECT place_id FROM BOARD WHERE board_id = (SELECT board_id FROM POST WHERE post_id = ?)";
                try (PreparedStatement statement = connection.prepareStatement(place_sql2)) {
                    statement.setInt(1, post);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            placeid = resultSet.getInt(1);
                        }
                    }
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                PlaceValidationCheck(connection, user, placeid);
                String query = "UPDATE POST SET status = 'cancelled' WHERE post_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, post);
                    statement.executeUpdate();
                    System.out.println("Successfully cancelled " + post);
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

    private static void RequestConfirm(Connection connection, User user){
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("Request confirmation");
        ManageablePlace(connection, user);
        System.out.println("-----------------------------------------");
        System.out.print("Insert place number: ");
        int place = sc.nextInt();
        System.out.println("-----------------------------------------");
        System.out.println("Request Board list");
        PlaceValidationCheck(connection, user, place);
        String place_board_sql = "SELECT BOARD.*, COUNT(POST.post_id) AS NumOfRequestPosts FROM BOARD INNER JOIN POST ON BOARD.board_id = POST.board_id WHERE BOARD.place_id = ? AND POST.status = 'pending' GROUP BY BOARD.board_id ORDER BY BOARD.floor";
        try (PreparedStatement statement = connection.prepareStatement(place_board_sql)) {
            statement.setInt(1, place);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Post Request");
                }
                do {
                    System.out.println(resultSet.getInt(1) + ". " + resultSet.getString(3) + " FLOOR:" + resultSet.getInt(5) + " SLOT:" + resultSet.getInt(4) + " REQUESTS:" + resultSet.getInt(6));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert board number: ");
        int board = sc.nextInt();
        System.out.println("-----------------------------------------");
        String post_sql = "SELECT * FROM POST WHERE board_id = ? AND status = 'pending' ORDER BY request_time";
        try (PreparedStatement statement = connection.prepareStatement(post_sql)) {
            statement.setInt(1, board);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Post Request");
                }
                do {
                    System.out.println("POST NO." + resultSet.getInt(1) +" USER: " + resultSet.getInt(2) +  " POST: " + resultSet.getString(4) + " DESCRIPTION: " + resultSet.getString(7) + " SIZE: " + resultSet.getInt(6) + " TIMESTAMP: " + resultSet.getTimestamp(9));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.print("Insert post number: ");
        int post = sc.nextInt();
        System.out.println("-----------------------------------------");
        // 선택한 POST의 size 확인
        int postSize = 0;
        String postSizeSql = "SELECT size FROM POST WHERE post_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(postSizeSql)) {
            statement.setInt(1, post);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    postSize = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // BOARD의 사용 가능한 slot 크기 계산
        int availableSlots = 0;
        String boardSlotSql = "SELECT slot, (SUM(size)) AS availableSlots FROM BOARD LEFT OUTER JOIN POST ON BOARD.board_id = POST.board_id AND POST.status = 'approved' WHERE BOARD.board_id = ? GROUP BY BOARD.board_id";
        try (PreparedStatement statement = connection.prepareStatement(boardSlotSql)) {
            statement.setInt(1, board);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int slot1 = resultSet.getInt(1);
                    int slot2 = resultSet.getInt(2);

                    if (resultSet.wasNull()) {
                        slot2 = 0;
                    }

                    availableSlots = slot1 - slot2;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (postSize > availableSlots) {
            System.out.println("Not enough available slots for this post.");
            return; // 함수 종료
        }

        String post_sql2 = "SELECT * FROM POST WHERE post_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(post_sql2)) {
            statement.setInt(1, post);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Post ID: " + resultSet.getInt(1));
                    System.out.println("Post title: " + resultSet.getString(4));
                    System.out.println("Post content: " + resultSet.getString(7));
                    System.out.println("Post size: " + resultSet.getInt(6));
                    System.out.println("Post user ID: " + resultSet.getInt(2));
                    System.out.println("Post board ID: " + resultSet.getInt(3));
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        System.out.println("-----------------------------------------");
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.println("3. Back");
        System.out.println("-----------------------------------------");
        System.out.print("Select menu: ");
        int menu = sc.nextInt();

        switch (menu) {
            case 1:
                String query = "UPDATE POST SET status = 'approved', period = CURRENT_TIMESTAMP + INTERVAL 7 DAY WHERE post_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, post);
                    statement.executeUpdate();
                    System.out.println("Successfully approved " + post);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                String query2 = "UPDATE POST SET status = 'rejected' WHERE post_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query2)) {
                    statement.setInt(1, post);
                    statement.executeUpdate();
                    System.out.println("Successfully rejected " + post);
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + menu);
        }
    }
    private static void UpdatePost(Connection connection){
        String update_post = "UPDATE POST SET status = 'expired' WHERE period < CURRENT_TIMESTAMP";
        try (PreparedStatement statement = connection.prepareStatement(update_post)) {
            statement.executeUpdate();
            System.out.println("Post Data Updated");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void ViewPost(Connection connection){
        Scanner sc = new Scanner(System.in);
        System.out.println("-----------------------------------------");
        System.out.println("View posts");
        AllPlaceTable(connection);
        System.out.println("-----------------------------------------");
        System.out.print("Insert post location: ");
        int place_id = sc.nextInt();
        System.out.println("-----------------------------------------");
        String posts = "SELECT POST.* FROM POST INNER JOIN BOARD ON POST.board_id = BOARD.board_id WHERE BOARD.place_id = ? ORDER BY POST.status, POST.request_time ASC";
        try (PreparedStatement statement = connection.prepareStatement(posts)) {
            statement.setInt(1, place_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("No Post located in this place");
                }
                do {
                    System.out.println("[STATUS]" + resultSet.getString(8) + "| " + resultSet.getInt(1) + ". BOARD: " + resultSet.getString(3) + " " + resultSet.getString(4) + " SIZE: " + resultSet.getString(6) + " REQUEST TIME: " + resultSet.getTimestamp(9) + " EXPIRATION TIME: " + resultSet.getTimestamp(5));
                } while (resultSet.next());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
