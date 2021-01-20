package kubackip.github.io.LetsDoSettlement;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopUpWindowController {

    private List<Members> memberList;
    private List<DeductedPayments> deductedPaymentList;
    private List<Payment> paymentList;
    private Map<Integer, String> pairsOfPayers;

    private float[] pairsSettlement;
    private float[] pairsDeductedSettlement;

    StringBuilder settlement = new StringBuilder();
    StringBuilder paymentDescription = new StringBuilder();

    private int counter = 1;

    @FXML
    private Label showSettlement;

    @FXML
    private Label getPaymentAndDeductedPayment;

    @FXML

    void initialize() {
        memberList = UnauthorisedController.getMemberList();
        deductedPaymentList = UnauthorisedController.getDeductedPaymentList();
        paymentList = UnauthorisedController.getPaymentList();

        pairsSettlement = UnauthorisedController.getPairsSettlement();
        pairsDeductedSettlement = UnauthorisedController.getPairsDeductedSettlement();

        pairsOfPayers = UnauthorisedController.getPairsOfPayers();

        showSettlement();
        showSettlement.setText(settlement.toString());

        showPaymentDescription();
        getPaymentAndDeductedPayment.setText(paymentDescription.toString());
    }

    private void showSettlement() {
        for (int i = 0; i < pairsSettlement.length; i++) {
//            System.out.println("pairsOfPayers get(i): " + pairsOfPayers.get(i));
//            System.out.println("pairsOfPayers charAt(1)" + pairsOfPayers.get(i).charAt(1));
//            System.out.println("pairsOfPayers charAt(0)" + pairsOfPayers.get(i).charAt(0));

            System.out.println("i: " + i);

            if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] >= 0) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " musi oddać "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString()
                        + ": " + formatFloat((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] < 0 && (pairsSettlement[i] / memberList.size()) >= (-pairsDeductedSettlement[i])) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " musi oddać "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString()
                        + ": " + formatFloat((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] < 0 && (pairsSettlement[i] / memberList.size()) < (-pairsDeductedSettlement[i])) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " musi oddać "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString()
                        + ": " + formatFloat(-((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i])) + " złotych" + "\n");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] <= 0) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " musi oddać "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString()
                        + ": " + formatFloat(-((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i])) + " złotych" + "\n");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] > 0 && -(pairsSettlement[i] / memberList.size()) >= pairsDeductedSettlement[i]) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " musi oddać "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString()
                        + ": " + formatFloat(-((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i])) + " złotych" + "\n");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] > 0 && -(pairsSettlement[i] / memberList.size()) < pairsDeductedSettlement[i]) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " musi oddać "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString()
                        + ": " + formatFloat((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            }
        }
    }

    public String formatFloat(float formatFloat) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        return decimalFormat.format(formatFloat);
    }

    private void showPaymentDescription() {
        for (int i = 0; i < paymentList.size(); i++) {
            paymentDescription.append("Zakupy nr." + counter + "\n\n");
            paymentDescription.append("Kto płacił: " + memberList.get(paymentList.get(i).getPayerID()).toString() + "\n\n");
            paymentDescription.append("Nazwa płatności: " + paymentList.get(i).getName() + "\n\n");
            paymentDescription.append("Ile zapłacił/a: " + paymentList.get(i).getValue() + " PLN\n\n");
            if (!paymentList.get(i).getDescription().isBlank()) {
                paymentDescription.append("Dodatkowe informacje o płatności:" + paymentList.get(i).getDescription() + "\n\n");
            }

            for (int j = 0; j < deductedPaymentList.size(); j++) {
                if (deductedPaymentList.get(j).getPaymentID() == i) {
                    paymentDescription.append("Kto odliczył produkt od zakupów: "
                            + memberList.get(deductedPaymentList.get(j).getDeductPayerID()).toString() + "\n\n");
                    paymentDescription.append("Co odliczył/a: " + deductedPaymentList.get(j).getName() + "\n\n");
                    paymentDescription.append("Ile to kosztowało: " + deductedPaymentList.get(j).getValue() + " PLN\n\n");
                }
            }
            paymentDescription.append("==============================\n\n");
            counter++;
        }
    }
}
