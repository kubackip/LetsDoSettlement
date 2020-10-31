package kubackip.github.io.LetsDoSettlement;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private int paymentID;

    private Payment payment;
    private List<Members> memberList = new ArrayList<>();
    private List<Payment> paymentList = new ArrayList<>();
    private Map<Integer, String> assignIdToPayment = new HashMap<>();
    private ObservableList<String> observablePaymentList;

    @FXML
    private ListView<String> paymentListView;

    @FXML
    private TextField paymentDescription;

    @FXML
    private TextField moneyValue;

    @FXML
    private DatePicker dateOfPayment;

    @FXML
    private ChoiceBox<Members> payer;

    @FXML
    private TextArea longPaymentDescription;

    // Needed for adding values to ListView
    @FXML
    public void initialize() {
        // returns new ArrayList
        observablePaymentList = FXCollections.observableArrayList();
        paymentListView.setItems(observablePaymentList);

        this.paymentID = 0;
        getPayers();
    }

    /**
     * Creating new Payment object, with values from text fields and etc.
     *
     * @param event
     */
    @FXML
    private void addToPaymentList(ActionEvent event) {
        String valueFromDescriptionTextField = paymentDescription.getText();
        setPaymentDescriptionValue(valueFromDescriptionTextField);
        String valueFromLongDescriptionTextField = longPaymentDescription.getText();
        setLongPaymentDescriptionValue(
                valueFromLongDescriptionTextField);
        float valueFromMoneyValueTextField = getValueFromStringToFloat(
                moneyValue.getText());
        setMoneyValueValue(valueFromMoneyValueTextField);

        String valueName = getPaymentDescriptionValue() + " - " + getDateOfPayment();

        // simple validator, to change in the future
        if (valueFromDescriptionTextField.length() > 0
                && valueFromMoneyValueTextField != 0.00) {
            if (observablePaymentList.contains(valueName)) {
                showAlertBox("Płatność o takiej nazwie już istanieje, "
                        + "proszę wybrac inną nazwę!");
            } else {
                payment = new Payment(getPaymentDescriptionValue(),
                        getLongPaymentDescriptionValue(),
                        getMoneyValueValue(),
                        dateOfPayment.getValue(),
                        paymentID, payer.getValue().getId());

                System.out.println(payment.toString());
                paymentList.add(payment);

                clearAllTheTextFields();
                updateList();
                paymentID++;
            }
        }
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
            showAlertBox("Input must contain numbers.");
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

    @FXML
    private void showPaymentDetails(ActionEvent event) {
        if (paymentListView.getSelectionModel().getSelectedItem() != null) {
            String specificPayment = paymentListView.getSelectionModel().getSelectedItem();
            int mapKeyValue;

            if (assignIdToPayment.containsValue(specificPayment)) {
                mapKeyValue = getKeyFromValue(assignIdToPayment, specificPayment);
                System.out.println(paymentList.get(mapKeyValue));
            }
        }
    }

    /**
     * The example of how to get key from value using Map is taken from
     * https://www.javacodeexamples.com/java-hashmap-get-key-from-value-example/2318
     *
     * @param <K>
     * @param <V>
     * @param map
     * @param value
     * @return
     */
    private static <K, V> K getKeyFromValue(Map<K, V> map, Object value) {
        //get all map keys using keySet method
        Set<K> keys = map.keySet();

        //iterate all keys
        for (K key : keys) {
            //if maps value for the current key matches, return the key
            if (map.get(key).equals(value)) {
                return key;
            }
        }
        //if no values matches, return null
        return null;
    }

    /**
     * Updates ListView paymentListView with value from paymentDescription and
     * dateOfPayment fields.
     */
    private void updateList() {
        String specificPayment = getPaymentDescriptionValue() + " - " + getDateOfPayment();

        if (!observablePaymentList.contains(specificPayment)) {
            observablePaymentList.add(getPaymentDescriptionValue() + " - " + getDateOfPayment());
            paymentListView.setItems(observablePaymentList);
            assignIdToPayment.put(paymentID, specificPayment);
        } else {
            showAlertBox("Nazwa płatności nie może się powtarzać!");
        }
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

    private void showAlertBox(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    private void getPayers() {
        if (AddMemberController.getMemberList() != null) {
            payer.getItems().addAll(AddMemberController.getMemberList());
            memberList = AddMemberController.getMemberList();

            System.out.println(AddMemberController.getMemberList().toString());
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

    public String getDateOfPayment() {
        return dateOfPayment.getValue().toString();
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
