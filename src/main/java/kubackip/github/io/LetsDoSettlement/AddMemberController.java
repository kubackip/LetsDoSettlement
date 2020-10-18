package kubackip.github.io.LetsDoSettlement;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddMemberController implements Initializable {

    private Integer id = 0;

    private Map<Integer, String> member;

    @FXML
    private TextField setName;

    @FXML
    private TextField setSecondName;

    @FXML
    private Button addMemberButton;

    @FXML
    void addMember(ActionEvent event) {
        member = new HashMap<Integer, String>();

        String fullName = setName.getText() + " " + setSecondName.getText();

        member.put(id, fullName);

        // sprawdzenie czy to działa
        System.out.println(member.get(id));
        id++;
    }

    @FXML
    private void backToMain(ActionEvent event) throws IOException {
        App.setRoot("main");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
