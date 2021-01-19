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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 *
 * @author samson
 */
public class UnauthorisedController {

    // Value that comes from text field
    private String paymentDescriptionValue;
    private String paymentDescriptionValueDeduct;
    private String longPaymentDescriptionValue;
    private float moneyValueValue;
    private float moneyValueValueDeduct;
    private int paymentID = 0;

    private Payment payment;
    private DeductedPayments deductedPayment;

    private static List<Members> memberList = new ArrayList<>();
    private static List<Payment> paymentList = new ArrayList<>();
    private static List<DeductedPayments> deductedPaymentList = new ArrayList<>();
    private static Map<Integer, String> assignIdToPayment = new HashMap<>();
    private static Map<Integer, String> pairsOfPayers = new HashMap<>();
    private ObservableList<String> observablePaymentList;
    private ObservableList<String> observableDeductList;

    private int pairId = 0;
    private static float[] pairsSettlement;
    private static float[] pairsDeductedSettlement;
    private boolean pairMapCreated = false;
    private boolean popupWindowOpened = false;
    StringBuilder pairOfObjects = new StringBuilder();

    // Deducted payments
    private float sumOfDeductedPayments;

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
    private ChoiceBox<Members> payerDeduct;

    @FXML
    private TextArea longPaymentDescription;

    @FXML
    private TextField paymentDescriptionDeduct;

    @FXML
    private TextField moneyValueDeduct;

    @FXML
    private ListView<String> deductListView;

    @FXML
    private Label showPayer;

    @FXML
    private Label showPaymentName;

    @FXML
    private Label showPaymentValue;

    @FXML
    private Label showPaymentDate;

    @FXML
    private Label showSettlementMember;

    @FXML
    private Label showSettlementValue;

    /**
     * Create observable lists for payment and deducted payment
     */
    @FXML
    public void initialize() {
        observablePaymentList = FXCollections.observableArrayList();
        observableDeductList = FXCollections.observableArrayList();

        paymentListView.setItems(observablePaymentList);
        deductListView.setItems(observableDeductList);

        getPayers();
        // Map can be created only once
        if (!pairMapCreated && AddMemberController.getMemberList() != null) {
            boolean[] usedBoolean = new boolean[memberList.size()];

            System.out.println("memberList size: " + memberList.size());
            pairsSettlement = new float[numberOfCombinations(memberList.size(), 2)];
            System.out.println("pairsSettlement length: " + pairsSettlement.length);
            
            pairsDeductedSettlement = new float[numberOfCombinations(memberList.size(), 2)];
            System.out.println("pairsDeductedSettlement length: " + pairsDeductedSettlement.length);
            subset(memberList, 2, 0, 0, usedBoolean);

            pairMapCreated = true;
        }
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
        setLongPaymentDescriptionValue(valueFromLongDescriptionTextField);
        float valueFromMoneyValueTextField = getValueFromStringToFloat(moneyValue.getText());
        setMoneyValueValue(valueFromMoneyValueTextField);

        String valueName = getPaymentDescriptionValue() + " - " + getDateOfPayment();

        if (valueFromDescriptionTextField.length() > 0 && valueFromMoneyValueTextField > 0.00) {
            if (observablePaymentList.contains(valueName)) {
                showAlertBox("Płatność o takiej nazwie już istanieje, " + "proszę wybrac inną nazwę!");
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

                updatePairsSettlement(payment);
//                showPairsSettlement();

                sumOfDeductedPayments = 0;
                paymentID++;
            }
        }
    }

    @FXML
    private void addDeductedPayment(ActionEvent event) {
        String valueFromDescriptionTextFieldDeduct = paymentDescriptionDeduct.getText();
        setPaymentDescriptionValueDeduct(valueFromDescriptionTextFieldDeduct);
        float valueFromMoneyValueDeductTextField = getValueFromStringToFloat(
                moneyValueDeduct.getText());

        sumOfDeductedPayments += valueFromMoneyValueDeductTextField;

        setMoneyValueValueDeduct(valueFromMoneyValueDeductTextField);
        setSumOfDeductedPayments(sumOfDeductedPayments);

        if (getSumOfDeductedPayments() > getValueFromStringToFloat(moneyValue.getText())) {
            System.out.println("Błąd: Suma odliczonych płatności przewyższa wartość zakupów");
        }

        deductedPayment = new DeductedPayments(
                getPaymentDescriptionValueDeduct(),
                getMoneyValueValueDeduct(),
                paymentID,
                payer.getValue().getId(),
                payerDeduct.getValue().getId());

        deductedPaymentList.add(deductedPayment);
        System.out.println(deductedPaymentList);

        paymentDescriptionDeduct.clear();
        moneyValueDeduct.clear();

        moneyToDeduct();
        updateDeductList();
    }

    private void moneyToDeduct() {
        int personWhoIsPayer = payer.getValue().getId();
        int personWhoShouldDeductMoney = payerDeduct.getValue().getId();
        int key = -1;
        String members = "";

        if (personWhoIsPayer < personWhoShouldDeductMoney) {
            members = String.valueOf(personWhoIsPayer) + String.valueOf(personWhoShouldDeductMoney);
            key = getKeyFromValue(pairsOfPayers, members);
            System.out.println("key: " + key);
            pairsDeductedSettlement[key] += getMoneyValueValueDeduct();
        } else if (personWhoIsPayer > personWhoShouldDeductMoney) {
            members = String.valueOf(personWhoShouldDeductMoney) + String.valueOf(personWhoIsPayer);
            key = getKeyFromValue(pairsOfPayers, members);
            System.out.println("key: " + key);
            pairsDeductedSettlement[key] -= getMoneyValueValueDeduct();
        }

        for (float f : pairsDeductedSettlement) {
            System.out.println("f: " + f);
        }
    }

    /**
     * Allow to change root fxml, that shows on the screen.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void setAddMemberAsRoot(ActionEvent event) throws IOException {
        App.setRoot("addMember", 320, 640);
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
    private void showPaymentDetails(MouseEvent event) {

        String specificPayment = paymentListView.getSelectionModel().getSelectedItem();
        int mapKeyValue;

        if (assignIdToPayment.containsValue(specificPayment)) {
            mapKeyValue = getKeyFromValue(assignIdToPayment, specificPayment);
            System.out.println(paymentList.get(mapKeyValue));

            int payerId = paymentList.get(mapKeyValue).getPayerID();
            String paymentValue = String.valueOf(paymentList.get(mapKeyValue).getValue());

            showPayer.setText(memberList.get(payerId).getName() + " " + memberList.get(mapKeyValue).getSecondName());
            showPaymentName.setText(paymentList.get(mapKeyValue).getName());
            showPaymentValue.setText(paymentValue);
            showPaymentDate.setText(paymentList.get(mapKeyValue).getDate().format(DateTimeFormatter.ISO_DATE));

            StringBuilder deductPayer = new StringBuilder("");
            StringBuilder deductValue = new StringBuilder("");

            for (int i = 0; i < deductedPaymentList.size(); i++) {
                if (deductedPaymentList.get(i).getPaymentID() == mapKeyValue) {
                    deductPayer.append(memberList.get(deductedPaymentList.get(i).getDeductPayerID()).getName()
                            + " " + memberList.get(deductedPaymentList.get(i).getDeductPayerID()).getSecondName() + "\n");
                    deductValue.append(String.valueOf(deductedPaymentList.get(i).getValue()) + "\n");
                }
            }
            showSettlementMember.setText(deductPayer.toString());
            showSettlementValue.setText(deductValue.toString());
        }
    }

    /**
     * Convert data format to day-month-year
     *
     * Code of converter taken from
     * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html
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

    // Formatter do dokończenia - mozna zrobić formatowanie po zakończeniu śledzenia pola
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

    /**
     * The example of how to get key from value using Map is taken from
     * https://www.javacodeexamples.com/java-hashmap-get-key-from-value-example/2318
     *
     * @param map
     * @param value
     * @return key value
     */
    public static int getKeyFromValue(Map<Integer, String> map, String value) {
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
        observableDeductList.add(getPaymentDescriptionValueDeduct() + " - "
                + payerDeduct.getValue().getName());
        deductListView.setItems(observableDeductList);
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
        }
    }

    private float getValueFromStringToFloat(String value) {
        return Float.parseFloat(value);
    }

    private void subset(List<Members> data, int k, int start, int currentLength, boolean[] used) {
        if (currentLength == k) {
            for (int i = 0; i < data.size(); i++) {
                if (used[i] == true) {
                    //Display members
                    pairOfObjects.append(data.get(i).getId());
                }
            }
            pairsOfPayers.put(pairId, pairOfObjects.toString());
            pairId++;
            pairOfObjects.delete(0, 2);
            return;
        }
        if (start == data.size()) {
            return;
        }

        //For every index we have two options,
        //1... Either we select it, means put true in used[] and make currentLength+1
        used[start] = true;
        subset(data, k, start + 1, currentLength + 1, used);
        //2... or we dont select it, means put false in used[] and dont increase currentLength
        used[start] = false;
        subset(data, k, start + 1, currentLength, used);
    }

    private void updatePairsSettlement(Payment p) {
        // find payer
        System.out.println("ID Osoby płacącej: " + p.getPayerID());

        //find all payment pairs 
        whoHasToMakeSettlement(p.getPayerID());
    }

    /**
     * payerID is an int, so we have to convert String to int, using
     * Integer.parseInt(String s).
     */
    private void whoHasToMakeSettlement(int payerID) {
        String payer = String.valueOf(payerID);

        for (Map.Entry<Integer, String> entry : pairsOfPayers.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            if (value.contains(payer)) {
                // Updating pairsSettlement[] table                
                if (value.startsWith(payer)) {
                    pairsSettlement[key] += getMoneyValueValue() - getSumOfDeductedPayments();
                } else if (value.endsWith(payer)) {
                    pairsSettlement[key] -= getMoneyValueValue() - getSumOfDeductedPayments();
                }
            }
        }
    }

    public int numberOfCombinations(int n, int k) {
        return (calculateFactorial(n) / (calculateFactorial(k) * calculateFactorial(n - k)));
    }

    /**
     *
     * @param number
     * @return factorial
     */
    public int calculateFactorial(int number) {
        int factorial = 1;
        for (int i = 1; i <= number; i++) {
            factorial *= i;
        }
        return factorial;
    }

    private void showPairsSettlement() {
        System.out.println("\nJak się rozkładają płatności na poszczególne pary:");

        for (int i = 0; i < pairsSettlement.length; i++) {
            System.out.println("pairsSettlement[" + i + "]: " + pairsSettlement[i]);
            System.out.println("pairsSettlement[" + i + "] / memberList.size():" + (pairsSettlement[i] / memberList.size()));
            System.out.println("pairsDeductedSettlement[" + i + "]: " + pairsDeductedSettlement[i]);

            if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] >= 0) {
                System.out.println("Osoba o ID = " + pairsOfPayers.get(i).charAt(1) + " musi oddać osobie o ID = "
                        + pairsOfPayers.get(i).charAt(0) + " [" + ((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + "] PLN");
            } else if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] < 0 && (pairsSettlement[i] / memberList.size()) >= (-pairsDeductedSettlement[i])) {
                System.out.println("Osoba o ID = " + pairsOfPayers.get(i).charAt(1) + " musi oddać osobie o ID = "
                        + pairsOfPayers.get(i).charAt(0) + " [" + ((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + "] PLN");
            } else if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] < 0 && (pairsSettlement[i] / memberList.size()) < (-pairsDeductedSettlement[i])) {
                System.out.println("Osoba o ID = " + pairsOfPayers.get(i).charAt(0) + " musi oddać osobie o ID = "
                        + pairsOfPayers.get(i).charAt(1) + " [" + -((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + "] PLN");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] <= 0) {
                System.out.println("Osoba o ID = " + pairsOfPayers.get(i).charAt(0) + " musi oddać osobie o ID = "
                        + pairsOfPayers.get(i).charAt(1) + " [" + -((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + "] PLN");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] > 0 && -(pairsSettlement[i] / memberList.size()) >= pairsDeductedSettlement[i]) {
                System.out.println("Osoba o ID = " + pairsOfPayers.get(i).charAt(0) + " musi oddać osobie o ID = "
                        + pairsOfPayers.get(i).charAt(1) + " [" + -((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + "] PLN");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] > 0 && -(pairsSettlement[i] / memberList.size()) < pairsDeductedSettlement[i]) {
                System.out.println("Osoba o ID = " + pairsOfPayers.get(i).charAt(1) + " musi oddać osobie o ID = "
                        + pairsOfPayers.get(i).charAt(0) + " [" + ((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + "] PLN");
            } else {
                System.out.println("Nikt nikomy nie jest nic winny!");
            }
        }
    }

    /**
     * showAndWait() method shows the Stage object and then blocks (stays inside
     * the showAndWait() method) until the Stage is closed.
     *
     * @param event
     */
    @FXML
    private void showSettlementWindow(ActionEvent event) {
        try {
            if (!popupWindowOpened) {
                popupWindowOpened = true;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("popupwindow.fxml"));
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/kubackip/github/io/LetsDoSettlement/popupwindow.fxml"));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setWidth(600);
                stage.setHeight(580);
                stage.setMaxHeight(620);
                stage.setMaxWidth(650);
                stage.setTitle("Rozliczenie");
                stage.showAndWait();

                if (!stage.isShowing()) {
                    popupWindowOpened = false;
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.err.println("Can't load new window");
        }
    }

    /**
     * Getters & Setters
     *
     * @return
     */
    public String getPaymentDescriptionValue() {
        return paymentDescriptionValue;
    }

    /**
     *
     * @return
     */
    public String getDateOfPayment() {
        return dateOfPayment.getValue().toString();
    }

    /**
     *
     * @param value
     */
    public void setPaymentDescriptionValue(String value) {
        this.paymentDescriptionValue = value;
    }

    /**
     *
     * @return
     */
    public String getLongPaymentDescriptionValue() {
        return longPaymentDescriptionValue;
    }

    /**
     *
     * @return
     */
    public String getPaymentDescriptionValueDeduct() {
        return paymentDescriptionValueDeduct;
    }

    /**
     *
     * @param paymentDescriptionValueDeduct
     */
    public void setPaymentDescriptionValueDeduct(String paymentDescriptionValueDeduct) {
        this.paymentDescriptionValueDeduct = paymentDescriptionValueDeduct;
    }

    /**
     *
     * @param longPaymentDescriptionValue
     */
    public void setLongPaymentDescriptionValue(String longPaymentDescriptionValue) {
        this.longPaymentDescriptionValue = longPaymentDescriptionValue;
    }

    /**
     *
     * @return
     */
    public float getMoneyValueValue() {
        return moneyValueValue;
    }

    /**
     *
     * @param moneyValueValue
     */
    public void setMoneyValueValue(float moneyValueValue) {
        this.moneyValueValue = moneyValueValue;
    }

    /**
     *
     * @return
     */
    public float getMoneyValueValueDeduct() {
        return moneyValueValueDeduct;
    }

    /**
     *
     * @param moneyValueValueDeduct
     */
    public void setMoneyValueValueDeduct(float moneyValueValueDeduct) {
        this.moneyValueValueDeduct = moneyValueValueDeduct;
    }

    private void setSumOfDeductedPayments(float sumOfDeductedPayments) {
        this.sumOfDeductedPayments = sumOfDeductedPayments;
    }

    public float getSumOfDeductedPayments() {
        return sumOfDeductedPayments;
    }

    public static List<Members> getMemberList() {
        return memberList;
    }

    public static List<Payment> getPaymentList() {
        return paymentList;
    }

    public static List<DeductedPayments> getDeductedPaymentList() {
        return deductedPaymentList;
    }

    public static Map<Integer, String> getPairsOfPayers() {
        return pairsOfPayers;
    }

    public static float[] getPairsSettlement() {
        return pairsSettlement;
    }

    public static float[] getPairsDeductedSettlement() {
        return pairsDeductedSettlement;
    }

}
