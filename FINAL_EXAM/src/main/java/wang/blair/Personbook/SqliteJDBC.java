package wang.blair.Personbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SqliteJDBC {

    public Connection conn = null;
    public String PERSON_TABLE = "Person";
    public String CASE_NOTE_TABLE = "CaseNote";

    public SqliteJDBC() {
        String dbName = "person.db";
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            this.conn = c;
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        this.createPersonTable();
        this.createCaseNoteTable();
    }

    private void createPersonTable() {
        Connection c = this.conn;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + this.PERSON_TABLE+ " " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " full_name           CHAR(100)    NOT NULL, " +
                    " important_personal            TINYINT     NOT NULL, " +
                    " important_bussiness           TINYINT     NOT NULL, " +
                    " new_contact_not_yet_saved     TINYINT     NOT NULL, " +
                    " birthday                      CHAR(50)    NOT NULL" +
                    " )";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Person Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    private void createCaseNoteTable() {
        Connection c = this.conn;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE " + this.CASE_NOTE_TABLE + " " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " person_id           INT    NOT NULL, " +
                    " case_note           TEXT    NOT NULL, " +
                    " create_time                      CHAR(100)    NOT NULL" +
                    " )";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("CaseNote Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

}
