package kubackip.github.io.LetsDoSettlement;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class mainController {

    // Value that comes from text field
    private String paymentDescriptionValue;

    // formatter for DatePicker, to set proper format value from MM/dd/yyyy to
    // dd/MM/yyyy

    @FXML
    private ListView<String> paymentList;

    @FXML
    private TextField paymentDescription;

    @FXML
    private TextField moneyValue;

    @FXML
    private Button buttonAddToList;

    @FXML
    private DatePicker dateOfPayment;

    @FXML
    private ChoiceBox<String> payer;

    @FXML
    private TextArea longPaymentDescription;

    // Needed for adding values to ListView
    private ObservableList<String> observablePaymentList;

    @FXML
    public void initialize() {
	// returns new ArrayList
	observablePaymentList = FXCollections.observableArrayList();

	paymentList.setItems(observablePaymentList);
    }

    @FXML
    private void addToList(ActionEvent event) {
	String valueFromTextField = paymentDescription.getText();
	setPaymentDescriptionValue(valueFromTextField);

	paymentDescription.clear();
	moneyValue.clear();
	longPaymentDescription.clear();

	if (valueFromTextField.length() > 0) {
	    updateList();
	}
    }

    private void updateList() {
	observablePaymentList.add(getPaymentDescriptionValue());
	paymentList.setItems(observablePaymentList);
    }

    /*
     * Converting data format to dd-MM-yyyy
     * 
     * Code of converter taken from
     * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.
     * html
     */
    @FXML
    private void formatDatePicker(ActionEvent event) {
	String pattern = "dd-MM-yyyy";

	dateOfPayment.setPromptText(pattern.toLowerCase());
	dateOfPayment.setConverter(new StringConverter<LocalDate>() {
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

	    @Override
	    public String toString(LocalDate date) {
		if (date != null) {
		    return dateFormatter.format(date);
		} else {
		    return "";
		}
	    }

	    @Override
	    public LocalDate fromString(String string) {
		if (string != null && !string.isEmpty()) {
		    return LocalDate.parse(string, dateFormatter);
		} else {
		    return null;
		}
	    }
	});
    }

    @FXML
    void formatMoneyValue(ActionEvent event) {
	String moneyValueGetText = moneyValue.getText();

	if (moneyValueGetText.contains(",")) {
	    System.out.println("Comma detected!");
	    moneyValueGetText = moneyValueGetText.replace(",", ".");
	}
	if (!moneyValueGetText.isEmpty()) {
	    Double moneyValueDouble = Double.parseDouble(moneyValueGetText);
	    NumberFormat formatter = new DecimalFormat("#0.00");

	    moneyValue.setText(formatter.format(moneyValueDouble).toString());
	}
    }

    // Getters & Setters
    public String getPaymentDescriptionValue() {
	return paymentDescriptionValue;
    }

    public void setPaymentDescriptionValue(String value) {
	this.paymentDescriptionValue = value;
    }

}
