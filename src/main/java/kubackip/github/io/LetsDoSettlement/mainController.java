package kubackip.github.io.LetsDoSettlement;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class mainController {

    private String value;

    @FXML
    private ListView<String> testList;

    @FXML
    private TextField textFieldAddToList;

    private ObservableList<String> observableList1;

    @FXML
    public void initialize() {
	observableList1 = FXCollections.observableArrayList();

	char c = 'a';

	for (int i = 0; i < 10; i++) {
	    observableList1.add(String.valueOf(c));
	    c++;
	}

	testList.setItems(observableList1);
    }

    @FXML
    private void addToList(ActionEvent event) {
	String valueFromTextField = textFieldAddToList.getText();
	setValue(valueFromTextField);

	updateList();
    }

    private void updateList() {
	observableList1.add(getValue());
	testList.setItems(observableList1);
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }
}
