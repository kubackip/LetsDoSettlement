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
    private String paymentDescriptionValueDeduct;
    private String longPaymentDescriptionValue;
    private float moneyValueValue;
    private float moneyValueValueDeduct;
    private int paymentID;

    private Payment payment;
    private DeductedPayments deductedPayment;

    private List<Members> memberList = new ArrayList<>();
    private List<Payment> paymentList = new ArrayList<>();
    private List<DeductedPayments> deductedPaymentList = new ArrayList<>();
    private Map<Integer, String> assignIdToPayment = new HashMap<>();
    private ObservableList<String> observablePaymentList;
    private ObservableList<String> observableDeductList;

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

    @FXML
    private TextField paymentDescriptionDeduct;

    @FXML
    private ChoiceBox<Members> payerDeduct;

    @FXML
    private TextField moneyValueDeduct;

    @FXML
    private ListView<String> deductListView;

    // Needed for adding values to ListView
    @FXML
    public void initialize() {
        // returns new ArrayList
        observablePaymentList = FXCollections.observableArrayList();
        observableDeductList = FXCollections.observableArrayList();

        paymentListView.setItems(observablePaymentList);
        deductListView.setItems(observableDeductList);

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

        if (valueFromDescriptionTextField.length() > 0
                && valueFromMoneyValueTextField != 0.00) {
            if (observablePaymentList.contains(valueName)) {
                showAlertBox("Płatność o takiej nazwie już istanieje, "
                        + "proszę wybrac inną nazwę!");
            } else {
                payment = new Payment(
                        getPaymentDescriptionValue(),
                        getLongPaymentDescriptionValue(),
                        getMoneyValueValue(),
                        dateOfPayment.getValue(),
                        paymentID,
                        payer.getValue().getId());

                paymentList.add(payment);

                clearAllTheTextFields();
                updateList();
                paymentID++;
            }
        }
    }

    @FXML
    private void addDeductedPayment(ActionEvent event) {
        String valueFromDescriptionTextFieldDeduct = paymentDescriptionDeduct.getText();
        setPaymentDescriptionValueDeduct(valueFromDescriptionTextFieldDeduct);
        float valueFromMoneyValueTextField = getValueFromStringToFloat(
                moneyValueDeduct.getText());
        setMoneyValueValueDeduct(valueFromMoneyValueTextField);

        deductedPayment = new DeductedPayments(getPaymentDescriptionValueDeduct(),
                getMoneyValueValueDeduct(),
                 paymentID,
                payerDeduct.getValue().getId());

        deductedPaymentList.add(deductedPayment);
        System.out.println(deductedPaymentList);
        
        paymentDescriptionDeduct.clear();
        moneyValueDeduct.clear();
        updateDeductList();
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
        formatTextFieldWithMoneyValue(moneyValue);
    }

    @FXML
    private void formatMoneyValueDeduct(ActionEvent event) {
        formatTextFieldWithMoneyValue(moneyValueDeduct);
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
        paymentDescriptionDeduct.clear();
        moneyValueDeduct.clear();
        deductListView.getItems().clear();
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

    private void formatTextFieldWithMoneyValue(TextField field) {
        String fieldValueGetText = field.getText();

        if (fieldValueGetText.contains(",")) {
            fieldValueGetText = fieldValueGetText.replace(",", ".");
        }
        if (!fieldValueGetText.isEmpty() && inputIsNumber()) {
            Double moneyValueDouble = Double.parseDouble(fieldValueGetText);
            NumberFormat formatter = new DecimalFormat("#0.00");

            field.setText(formatter.format(moneyValueDouble));
        } else if (!inputIsNumber()) {
            showAlertBox("Input must contain numbers.");
        }
    }

    /**
     * The example of how to get key from value using Map is taken from
     * https://www.javacodeexamples.com/java-hashmap-get-key-from-value-example/2318
     *
     * @param map
     * @param value
     * @return
     */
    private static int getKeyFromValue(Map<Integer, String> map, String value) {
        //get all map keys using keySet method
        Set<Integer> keys = map.keySet();

        //iterate all keys
        for (int key : keys) {
            //if maps value for the current key matches, return the key
            if (map.get(key).equals(value)) {
                return key;
            }
        }
        //if no values matches, return -1 -> throws exception
        return -1;
    }

    /**
     * Updates ListView paymentListView with value from paymentDescription and
     * dateOfPayment fields.
     */
    private void updateList() {
        String specificPayment = getPaymentDescriptionValue() + " - "
                + payer.getValue().getName() + ", Data: " + getDateOfPayment();

        if (!observablePaymentList.contains(specificPayment)) {
            observablePaymentList.add(getPaymentDescriptionValue() + " - "
                    + payer.getValue().getName() + ", Data: " + getDateOfPayment());
            paymentListView.setItems(observablePaymentList);
            assignIdToPayment.put(paymentID, specificPayment);
        } else {
            showAlertBox("Nazwa płatności nie może się powtarzać!");
        }
    }

    private void updateDeductList() {
        String specificPayment = getPaymentDescriptionValueDeduct() + " - "
                + payerDeduct.getValue().getName();

        observableDeductList.add(getPaymentDescriptionValueDeduct() + " - "
                + payerDeduct.getValue().getName());
        deductListView.setItems(observableDeductList);
//            assignIdToPayment.put(paymentID, specificPayment);

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
            payerDeduct.getItems().addAll(AddMemberController.getMemberList());
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

    public String getPaymentDescriptionValueDeduct() {
        return paymentDescriptionValueDeduct;
    }

    public void setPaymentDescriptionValueDeduct(String paymentDescriptionValueDeduct) {
        this.paymentDescriptionValueDeduct = paymentDescriptionValueDeduct;
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

    public float getMoneyValueValueDeduct() {
        return moneyValueValueDeduct;
    }

    public void setMoneyValueValueDeduct(float moneyValueValueDeduct) {
        this.moneyValueValueDeduct = moneyValueValueDeduct;
    }
}
