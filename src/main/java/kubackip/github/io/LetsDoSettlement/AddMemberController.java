package kubackip.github.io.LetsDoSettlement;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddMemberController implements Initializable {

    private int id = 0;
    private static List<Members> membersList = new ArrayList<>();

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField secondNameTextField;

    @FXML
    private Button addMemberButton;

    @FXML
    private void addMember(ActionEvent event) {
        String name = nameTextField.getText();
        String secondName = secondNameTextField.getText();

        if (!name.isEmpty() && !secondName.isEmpty()) {
            Members member = new Members(name, secondName, id);
            membersList.add(member);
        }

        clearAllTheTextFields();
        id++;
    }

    @FXML
    private void backToMain(ActionEvent event) throws IOException {
        App.setRoot("unauthorised");
    }

    @FXML
    private void clearAllTheTextFields() {
        nameTextField.clear();
        secondNameTextField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        membersList = new ArrayList<>();
    }

    public static List<Members> getMemberList() {
        if (membersList != null) {
            return membersList;
        } else {
            return null;
        }
    }
}
