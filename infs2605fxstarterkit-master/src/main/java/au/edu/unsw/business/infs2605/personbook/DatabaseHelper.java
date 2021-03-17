package au.edu.unsw.business.infs2605.personbook;

import java.util.ArrayList;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    public void initDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:personbook.db");
        Statement st = conn.createStatement();
        String sql = "CREATE TABLE PersonBook(id int, full_name varchar(64), birth_day_month varchar(16), " +
                "birth_year varchar(4), important_personal CHAR(1), important_business char(1))";
        st.execute(sql);
        sql = "CRETE TABLE Note(id int, person_id int, create_time datetime, content varchar(512)";
        st.execute(sql);
        st.close();
        conn.close();
    }

    public ArrayList<PersonBook> getAllPersons() throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:personbook.db");
        Statement st = conn.createStatement();
        String sql = "SELECT id, full_name, birth_day, birth_year, important_personal, important_business " +
                "from PersonBook";
        ResultSet rs = st.executeQuery(sql);
        ArrayList<PersonBook> books = new ArrayList<>();
        while(rs.next()){
            books.add(new PersonBook(rs.getInt("id"), rs.getString("full_name"),
                    rs.getString("birth_day"), rs.getString("birth_year"),
                    rs.getString("important_personal"), rs.getString("important_business")));
        }
        st.close();
        conn.close();
        return books;
    }

    public ArrayList<Note> getNoteByPersonId(int personId) throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:personbook.db");
        String sql = "SELECT id, person_id, create_time, content from Note where person_id = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, personId);
        ResultSet rs = st.executeQuery();
        ArrayList<Note> notes = new ArrayList<>();
        while(rs.next()){
            notes.add(new Note(rs.getInt("id"), rs.getInt("person_id"),
                    rs.getString("create_time"), rs.getString("content")));
        }
        st.close();
        conn.close();
        return notes;
    }

    public void insertPerson(PersonBook book) throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:personbook.db");
        String sql = "insert into PersonBook values(?, ?, ?, ?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, book.getId());
        st.setString(2, book.getFullName());
        st.setString(3, book.getBirthDay());
        st.setString(4, book.getBirthYear());
        st.setString(5, book.getIsImportantPersonal());
        st.setString(6, book.getIsImportantBusiness());
        st.execute();
        st.close();
        conn.close();
    }

    public void insertNote(Note note) throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:personbook.db");
        String sql = "insert into Note values(?, ?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setInt(1, note.getId());
        st.setInt(2, note.getPersonId());
        st.setString(3, note.getCreateTime());
        st.setString(4, note.getContent());
        st.execute();
        st.close();
        conn.close();

    }

    public void updatePerson(PersonBook book) throws SQLException{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:personbook.db");
        String sql = "update PersonBook set full_name = ?, birth_day = ?, birth_year = ?, " +
                "important_personal = ?, important_business = ? where id = ?";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, book.getFullName());
        st.setString(2, book.getBirthDay());
        st.setString(3, book.getBirthYear());
        st.setString(4, book.getIsImportantPersonal());
        st.setString(5, book.getIsImportantBusiness());
        st.setInt(6, book.getId());
        st.execute();
        st.close();
        conn.close();
    }

}
