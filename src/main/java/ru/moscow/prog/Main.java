package ru.moscow.prog;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.sql.DriverManager;


import com.mysql.jdbc.Connection;

public class Main {
   // static final String DB_CONNECTION = "com.mysql.jdbc.Driver";//
     static final String DB_CONNECTION ="jdbc:mysql://localhost:3306/shop";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "mashenika17122009M";
    static java.sql.Connection conn;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        try {
            try {
                conn= DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);

                initDB();

            while (true){
                System.out.printf("1: add client");
                System.out.printf("2: add random clients");
                System.out.printf("3: delete client");
                System.out.printf("4: change client");
                System.out.printf("5: view client");
                System.out.printf("->");

                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        addClient(sc);
                        break;

                    case "2":
                        insertRandomClients(sc);
                        break;

                    case "3":
                        deleteClient(sc);
                        break;

                    case "4":
                        changeClient(sc);
                        break;

                    case "5":
                        viewClients();
                        break;
                    default:
                        return;
                }
                }
            }catch(SQLException ex) {
                ex.printStackTrace();
                return;
            }/*finally {
            sc.close();
            if (conn!=null) conn.close();
        }*/
    }catch(Exception ex1) {
            ex1.printStackTrace();
            return;
        }
}
private static void initDB () throws SQLException {
    Statement st = conn.createStatement();
    try {
        st.execute("DROP TABLE IF EXISTS Clients");
        st.execute("CREATE TABLE Clients (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY , name VARCHAR (20) NOT NULL , age INT)");
    } finally {
        st.close();
    }
}
private static void addClient(Scanner sc) throws SQLException {
    System.out.println("Enter client name: ");
    String name = sc.nextLine();
    System.out.println("Enter client age: ");
    String sAge = sc.nextLine();
    int age = Integer.parseInt(sAge);
    PreparedStatement ps = conn.prepareStatement("INSERT INTO Clients (name, age) VALUES (?,?)");
    try {
        ps.setString(1, name);
        ps.setInt(2, age);
        ps.executeUpdate();
    } finally {
        ps.close();
    }
}
 private static void deleteClient(Scanner sc) throws SQLException {
     System.out.println("Enter client name: ");
     String name = sc.nextLine();

     PreparedStatement ps = conn.prepareStatement("DELETE FROM Clients WHERE name = ?");
     try {
         ps.setString(1, name);
         ps.executeUpdate();
     } finally {
         ps.close();
     }
 }
 private static void changeClient(Scanner sc) throws SQLException {
     System.out.println("Enter client name: ");
     String name = sc.nextLine();
     System.out.println("Enter client age: ");
     String sAge= sc.nextLine();
     int age = Integer.parseInt(sAge);

     PreparedStatement ps = conn.prepareStatement(" UPDATE CLients SET age = ? WHERE name = ?");
     try {
         ps.setInt(1, age);
         ps.setString(2, name);
         ps.executeUpdate();
     }finally {
         ps.close();
     }
    }

    public static void insertRandomClients(Scanner sc) throws SQLException {
        System.out.println("Enter clients count: ");
        String sCount = sc.nextLine();
        int count = Integer.parseInt(sCount);
        Random rnd = new Random();

        conn.setAutoCommit(false);
        try {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT  INTO Clients (name, age) VALUES (?, ?)");
                try {
                    for (int i = 0; i < count; i++) {
                        ps.setString(1, "Name" + i);
                        ps.setInt(2, rnd.nextInt(100));
                        ps.executeUpdate();
                    }
                    conn.commit();
                } finally {
                    ps.close();
                }
            } catch (Exception ex) {
                conn.rollback();
            }
        } finally {
            conn.setAutoCommit(true);
        }
    }
    private static void viewClients() throws SQLException{
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clients");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.println(md.getColumnName(i) + "\t\t");
                    System.out.println();
                }
                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.println(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close();
            }
        } finally {
            ps.close();
        }
    }
  /*  public static void connect() throws Exception {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:main.db");
        Statement stmt = conn.createStatement();
    }

    public static void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}

