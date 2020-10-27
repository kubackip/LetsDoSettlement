package kubackip.github.io.LetsDoSettlement;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class MainController {

    // Value that comes from text field
    private String paymentDescriptionValue;
    private String longPaymentDescriptionValue;
    private float moneyValueValue;

    private Payment payment;
    private List<Members> memberList = new ArrayList<Members>();

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
    private ChoiceBox<Members> payer;

    @FXML
    private TextArea longPaymentDescription;

    // Needed for adding values to ListView
    private ObservableList<String> observablePaymentList;

    @FXML
    public void initialize() {
        // returns new ArrayList
        observablePaymentList = FXCollections.observableArrayList();
        paymentList.setItems(observablePaymentList);

        getPayers();
    }

    /**
     *
     *
     * @param event
     */
    @FXML
    private void addToList(ActionEvent event) {
        String valueFromDescriptionTextField = paymentDescription.getText();
        setPaymentDescriptionValue(valueFromDescriptionTextField);
        String valueFromLongDescriptionTextField = longPaymentDescription.getText();
        setLongPaymentDescriptionValue(
                valueFromLongDescriptionTextField);
        float valueFromMoneyValueTextField = getValueFromStringToFloat(
                moneyValue.getText());
        setMoneyValueValue(valueFromMoneyValueTextField);

        clearAllTheTextFields();

        // simple validator, to change in the future
        if (valueFromDescriptionTextField.length() > 0) {
//            if (paymentDescription != null && moneyValue != null
//                    && longPaymentDescription != null) {
//                payment = new Payment(getPaymentDescriptionValue(),
//                        longPaymentDescription.getText(),
//                        getValueFromStringToFloat(moneyValue.getText()),
//                        LocalDate.now());
//            }
            System.out.println(getPaymentDescriptionValue());
            System.out.println(getLongPaymentDescriptionValue());
            System.out.println(getMoneyValueValue());
            System.out.println(LocalDate.now());

            updateList();
        }
    }

    private void updateList() {
        observablePaymentList.add(getPaymentDescriptionValue());
        paymentList.setItems(observablePaymentList);
    }

    /**
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

    /**
     * Formatting input in moneyValue Text Field.
     *
     * @param event
     */
    @FXML
    private void formatMoneyValue(ActionEvent event) {
        String moneyValueGetText = moneyValue.getText();

        if (moneyValueGetText.contains(",")) {
            System.out.println("Comma detected!");
            moneyValueGetText = moneyValueGetText.replace(",", ".");
        }
        if (!moneyValueGetText.isEmpty() && inputIsNumber()) {
            Double moneyValueDouble = Double.parseDouble(moneyValueGetText);
            NumberFormat formatter = new DecimalFormat("#0.00");

            moneyValue.setText(formatter.format(moneyValueDouble));
        } else if (!inputIsNumber()) {
            //shows Alert Box
            showAlertBox();
        }
    }

    @FXML
    private void setAddMemberAsRoot(ActionEvent event) throws IOException {
        App.setRoot("addMember");
    }

    @FXML
    private void clearAllTheTextFields() {
        paymentDescription.clear();
        moneyValue.clear();
        longPaymentDescription.clear();
    }

    /**
     * Checks with regular expression if input value contains numbers and dot or
     * comma signs.
     *
     * @return
     */
    private boolean inputIsNumber() {
        String value = moneyValue.getText();
        String regex = "^[0-9.,]*$";

        return value.matches(regex);
    }

    /**
     * Created simple Alert Box. It informs about possible input value.
     */
    private void showAlertBox() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Input must contain numbers.");
        alert.showAndWait();
    }

    private void getPayers() {
        if (AddMemberController.getMemberList() != null) {
            payer.getItems().addAll(AddMemberController.getMemberList());
            System.out.println(AddMemberController.getMemberList().toString());

            memberList = AddMemberController.getMemberList();
        }
    }

    private float getValueFromStringToFloat(String value) {
        return Float.parseFloat(value);
    }

    /**
     * Getters & Setters
     */
    public String getPaymentDescriptionValue() {
        return paymentDescriptionValue;
    }

    public void setPaymentDescriptionValue(String value) {
        this.paymentDescriptionValue = value;
    }

    public String getLongPaymentDescriptionValue() {
        return longPaymentDescriptionValue;
    }

    public void setLongPaymentDescriptionValue(String longPaymentDescriptionValue) {
        this.longPaymentDescriptionValue = longPaymentDescriptionValue;
    }

    public float getMoneyValueValue() {
        return moneyValueValue;
    }

    public void setMoneyValueValue(float moneyValueValue) {
        this.moneyValueValue = moneyValueValue;
    }
}
