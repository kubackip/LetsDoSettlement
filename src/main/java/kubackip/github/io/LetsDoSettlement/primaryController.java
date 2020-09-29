package kubackip.github.io.LetsDoSettlement;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class primaryController implements Initializable {

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	loadData();
    }

    private void loadData() {
	list.removeAll(list);

	String a = "a";
	String b = "b";
	String c = "c";

	list.addAll(a, b, c);
	listView.getItems().addAll(list);
    }
}
