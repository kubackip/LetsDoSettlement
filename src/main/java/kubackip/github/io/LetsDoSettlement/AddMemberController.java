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

    private int id;
    private static List<Members> membersList;

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

        System.out.println(membersList.get(id).toString());

        clearAllTheTextFields();
        id++;
    }

    @FXML
    private void backToMain(ActionEvent event) throws IOException {
        App.setRoot("main");
    }

    @FXML
    private void clearAllTheTextFields() {
        nameTextField.clear();
        secondNameTextField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        membersList = new ArrayList<>();
        this.id = 0;
    }

    public static List getMemberList() {
        if (membersList != null) {
            return membersList;
        } else {
            return null;
        }
    }

}
