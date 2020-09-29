package kubackip.github.io.LetsDoSettlement;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class mainController {

    @FXML
    private ListView<String> testList;

    private ObservableList<String> observableList1;

    @FXML
    public void initialize() {
	observableList1 = FXCollections.observableArrayList();

	observableList1.add("Hello!");
	testList.setItems(observableList1);
    }
}
