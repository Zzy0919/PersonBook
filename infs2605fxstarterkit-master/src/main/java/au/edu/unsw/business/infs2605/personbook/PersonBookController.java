package au.edu.unsw.business.infs2605.personbook;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class PersonBookController {

    @FXML
    ListView<String> personList;

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
    protected void initialize(){

    }

    @FXML
    public void onAddPerson(){

    }


}
