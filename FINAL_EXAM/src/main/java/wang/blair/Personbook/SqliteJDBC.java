package wang.blair.Personbook;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;


public class SqliteJDBC {

    public String PERSON_TABLE = "Person";
    public String CASE_NOTE_TABLE = "CaseNote";
    public String DATE_FMT = "dd/MM/yyyy";

    public Connection conn = null;

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
                    "(" +
                    " full_name           CHAR(100)    NOT NULL, " +
                    " important_personal            TINYINT     NOT NULL, " +
                    " important_business           TINYINT     NOT NULL, " +
                    " new_contact_not_yet_saved     TINYINT     NOT NULL, " +
                    " day INT NOT NULL, " +
                    " month INT NOT NULL, " +
                    " year INT NOT NULL " +
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
                    "(" +
                    " person_full_name           INT    NOT NULL, " +
                    " case_note           TEXT    NOT NULL, " +
                    " create_time                      INT NOT NULL" +
                    " )";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("CaseNote Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        String sql = "select * from " + this.PERSON_TABLE;
        Connection c = this.conn;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                Person person = new Person();

                String  fullName = rs.getString("full_name");
                person.setFullName(fullName);

                boolean importantPersonal  = (rs.getInt("important_personal") == 1);
                person.setImportantPersonal(importantPersonal);

                boolean importantBusiness = (rs.getInt("important_business") == 1);
                person.setImportantBusiness(importantBusiness);

                boolean newContactNotYetSaved = (rs.getInt("new_contact_not_yet_saved") == 1);
                person.setNewContactNotYetSaved(newContactNotYetSaved);

                int day = rs.getInt("day");
                int month = rs.getInt("month");
                if(day > 0 || month > 0) {
                    MonthDay bday = MonthDay.of(month, day);
                    person.setBdayMonthDay(bday);
                }
                int year = rs.getInt("year");
                if (year > 0) {
                    person.setBirthdayYear(Year.of(year));
                }

                people.add(person);
            }
            System.out.println("Get all person done successfully");
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        for (int i=0; i<people.size(); i++) {
            Person person = people.get(i);
            getCaseNotes(person);
        }

        return people;
    }

    public List<CaseNote> getCaseNotes(Person person) {
        List<CaseNote> notes = new ArrayList<>();
        String sql = "select * from " + this.CASE_NOTE_TABLE + " where person_full_name = '" + person.getFullName() + "' order by create_time";
        Connection c = this.conn;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                CaseNote caseNote = new CaseNote();

                String  fullName = rs.getString("person_full_name");
                person.setFullName(fullName);

                Date ts = rs.getDate("create_time");
                LocalDateTime createTime = ts.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                caseNote.setCreateTime(createTime);

                notes.add(caseNote);
                person.addCaseNote(caseNote);
            }
            System.out.println("Get all case notes of " + person.getFullName() + " done successfully");
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println("failed to load case notes of " + person.getFullName() + e.getClass().getName() + ": " + e.getMessage() );
        }

        return notes;
    }

    public void savePeople(List<Person> people) {
        for (int i=0; i<people.size(); i++) {
            Person person = people.get(i);
            savePerson(person);
        }
    }

    public void savePerson(Person person) {
        Connection c = this.conn;
        Statement stmt = null;
        boolean hasOld = (findPerson(person.getFullName()) != null);

        try {
            stmt = c.createStatement();
            String sql = "";
            if (hasOld == false) {
                sql = String.format("insert into %s (full_name, important_personal, important_business, new_contact_not_yet_saved, day, month, year) values ('%s', %d, %d, %d, %d, %d, %d)",
                        this.PERSON_TABLE,
                        person.getFullName(),
                        person.isImportantPersonal() ? 1: 0,
                        person.isImportantBusiness() ? 1: 0,
                        person.isNewContactNotYetSaved() ? 1: 0,
                        person.getBdayMonthDay() != null ? person.getBdayMonthDay().getDayOfMonth(): 0,
                        person.getBdayMonthDay() != null ? person.getBdayMonthDay().getMonth(): 0,
                        person.getBirthdayYear() != null ? person.getBirthdayYear().getValue(): 0
                        );
            } else {
                sql = String.format("update %s set full_name='%s', important_personal=%d, important_business=%d, new_contact_not_yet_saved=%d, day=%d, month=%d, year=%d where full_name = '%s'",
                        this.PERSON_TABLE,
                        person.getFullName(),
                        person.isImportantPersonal() ? 1: 0,
                        person.isImportantBusiness() ? 1: 0,
                        person.isNewContactNotYetSaved() ? 1: 0,
                        person.getBdayMonthDay() != null ? person.getBdayMonthDay().getDayOfMonth(): 0,
                        person.getBdayMonthDay() != null ? person.getBdayMonthDay().getMonth(): 0,
                        person.getBirthdayYear() != null ? person.getBirthdayYear().getValue(): 0,
                        person.getFullName());
            }
            System.out.println(sql);

            stmt.execute(sql);

            System.out.println("Save person " + person.getFullName() + " done successfully");
            stmt.close();

            saveCaseNote(person);
        } catch ( Exception e ) {
            System.err.println( "Save person " + person.getFullName() + ": " + e.getClass().getName() + ": " + e.getMessage() );
        }

    }

    public Person findPerson(String fullName) {
        Person person = null;
        Connection c = this.conn;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = String.format("select * from %s where full_name = '%s'", fullName);
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                person = parsePersonRow(rs);
            }
            stmt.close();
            System.out.println("CaseNote Table created successfully");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return person;
    }

    public Person parsePersonRow(ResultSet rs) {
        Person person = new Person();

        try {
            String  fullName = rs.getString("full_name");
            person.setFullName(fullName);

            boolean importantPersonal  = (rs.getInt("important_personal") == 1);
            person.setImportantPersonal(importantPersonal);

            boolean importantBusiness = (rs.getInt("important_business") == 1);
            person.setImportantBusiness(importantBusiness);

            boolean newContactNotYetSaved = (rs.getInt("new_contact_not_yet_saved") == 1);
            person.setNewContactNotYetSaved(newContactNotYetSaved);

            int day = rs.getInt("day");
            int month = rs.getInt("month");
            if(day > 0 || month > 0) {
                MonthDay bday = MonthDay.of(month, day);
                person.setBdayMonthDay(bday);
            }
            int year = rs.getInt("year");
            if (year > 0) {
                person.setBirthdayYear(Year.of(year));
            }
        } catch (Exception e) {
            System.out.println("failed to parse row to Person: " + e.toString());
        }

        return person;
    }

    public void saveCaseNote(Person person) {
        Connection c = this.conn;
        Statement stmt = null;

        // remove old notes
        try {
            stmt = c.createStatement();
            stmt.execute(String.format("delete from %s where person_full_name = '%s'", this.CASE_NOTE_TABLE, person.getFullName()));
            stmt.close();
            System.out.println("Remove old case notes");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        // begin to insert
        for(int i=0; i<person.getCaseNotes().size(); i++) {
            CaseNote note = person.getCaseNotes().get(i);

            try {
                stmt = c.createStatement();
                String sql = "insert into " + this.CASE_NOTE_TABLE + " (person_full_name, case_note, create_time) values (";

                Timestamp ts = Timestamp.valueOf(note.getCreateTime());
                sql += String.format("'%s', '%s', %d)", person.getFullName(), note.getCaseNoteText(), ts.getTime());

                stmt.execute(sql);
                stmt.close();

                System.out.println(String.format("Save case note %s done", note.getCaseNoteText()));
            } catch (Exception e) {
                System.err.println( "Save case note " + note.getCaseNoteText() + ", " + note.getCreateTime().toString() + ", " + e.getClass().getName() + ": " + e.getMessage() );
            }
        }

    }

}
