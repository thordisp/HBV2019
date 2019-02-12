package is.hi.hbv.vinnsla;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.JDBCType.INTEGER;

public class HotelsDAO {

    private Connection conn;
    private Statement stmt;
    private ResultSet r;

    public HotelsDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            // db parameters
            String url = "jdbc:sqlite:hotels.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            // System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Hér koma get föll til að ná í hluti úr gagnagrunninum, með queries
    // eða jafnvel til að update-a eitthvað í gagnagrunninum.
    // Getum svo unnið með niðurstöðurnar einhvern veginn til að birta þær.

    public ArrayList<String> getHotelNames() {
        ArrayList<String> hotels = new ArrayList<String>();

        try {
            stmt = conn.createStatement();
            r = stmt.executeQuery("select name from Hotel");

            while (r.next()) {
                hotels.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    public ArrayList<String> getHotelRooms() {
        ArrayList<String> rooms = new ArrayList<String>();

        try {
            stmt = conn.createStatement();
            r = stmt.executeQuery("select * from Room");

            while (r.next()) {
                rooms.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public ArrayList<String> getRoomsbyPrice(int i) {
        ArrayList<String> rooms = new ArrayList<String>();
        // Þessu má breyta í stærri query með líka ? fyrir area og ? fyrir fjölda gesta og kannski ? fyrir date
        // Svo tekur maður bara fleiri parametra inn, einn fyrir hvert leitarskilyrði.
        // Þá er hægt að nota bara þetta fall fyrir leitina
        try {
            stmt = conn.createStatement();
            // Hér má annað hvort breyta yfir í Rate > ? && Rate < ?, eða breyta viðmótinu yfir í alltaf
            // bara minna en 3000, minna en 6000 o.s.frv.
            PreparedStatement p = conn.prepareStatement("SELECT * FROM Room WHERE Rate < ?");
            if (i == 0) {
                p.setInt(1, 3000);
            }
            else if (i == 1) {
                p.setInt(1,6000);
            }
            else if (i == 2) {
                p.setInt(1, 10000);
            }
            else if (i == 3) {
                p.setInt(1, 15000);
            }
            else if (i == 4) {
                p.setInt(1, 20000);
            }
            else {
                p.setInt(1, 1000000000);
            }
            r = p.executeQuery();

            while (r.next()) {
                rooms.add(r.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }


    protected void finalize() {
        try {
            if (r != null) r.close();
            if (stmt != null) stmt.close();
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        HotelsDAO hotel = new HotelsDAO();
    }
}