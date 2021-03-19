package au.edu.unsw.business.infs2605.personbook;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class PersonBookController {

    @FXML
    ListView<String> personList;

    @FXML
    TextField fullNameField;

    @FXML
    TextField birthDayField;

    @FXML
    TextField birthYearField;

    @FXML
    TextField importantPersonalField;

    @FXML
    TextField importantBusinessField;

    @FXML
    Label notesLabel;

    @FXML
    TextField addNotesField;

    @FXML
    TextField idInput;

    @FXML
    TextField fullNameInput;

    @FXML
    TextField birthDayInput;

    @FXML
    TextField birthYearInput;

    @FXML
    TextField importantPersonalInput;

    @FXML
    TextField importantBusinessInput;

    @FXML
    public void initialize() throws IOException, SQLException {
        //DatabaseHelper.initDatabase();
        ArrayList<PersonBook> persons = DatabaseHelper.getAllPersons();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(PersonBook person : persons){
            items.add(person.getId() + "|" + person.getFullName());
        }
        personList.setItems(items);

        personList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String item = personList.getSelectionModel().getSelectedItem();
                String[] info = item.split("\\|");
                int id = Integer.parseInt(info[0]);
                try{
                    PersonBook book = DatabaseHelper.getPersonBook(id);
                    fullNameField.setText(book.getFullName());
                    birthDayField.setText(book.getBirthDay());
                    birthYearField.setText(book.getBirthYear());
                    importantPersonalField.setText(book.getIsImportantPersonal());
                    importantBusinessField.setText(book.getIsImportantBusiness());

                    notesLabel.setText("");
                    ArrayList<Note> notes = DatabaseHelper.getNoteByPersonId(id);
                    StringBuilder contents = new StringBuilder();
                    for(Note note : notes){
                        contents.append(note.getCreateTime()).append(": ").append(note.getContent()).append("\n");
                    }
                    notesLabel.setText(contents.toString());
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void onAddPerson(ActionEvent event) throws SQLException{
        PersonBook book = new PersonBook(Integer.parseInt(idInput.getText()), fullNameInput.getText(),
                birthDayInput.getText(), birthYearInput.getText(), importantPersonalInput.getText(),
                importantPersonalInput.getText());
        DatabaseHelper.insertPerson(book);
        personList.getItems().add(book.getId() + "|" + book.getFullName());
    }

    @FXML
    public void onSave(ActionEvent event) throws SQLException{
        String item = personList.getSelectionModel().getSelectedItem();
        String[] info = item.split("\\|");
        int id = Integer.parseInt(info[0]);
        PersonBook book = new PersonBook(id, fullNameField.getText(), birthDayField.getText(),
                birthYearField.getText(), importantPersonalField.getText(),
                importantBusinessField.getText());
        DatabaseHelper.updatePerson(book);
    }

    @FXML
    public void onAddNote(ActionEvent event) throws SQLException{
        String item = personList.getSelectionModel().getSelectedItem();
        String[] info = item.split("\\|");
        int id = Integer.parseInt(info[0]);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String addTimestamp = formatter.format(new Date());
        Note note = new Note(1, id, addTimestamp, addNotesField.getText());
        DatabaseHelper.insertNote(note);
        addNotesField.setText("");

        notesLabel.setText(notesLabel.getText() + "\n" + note.getCreateTime() + ":" + note.getContent());
    }

}
