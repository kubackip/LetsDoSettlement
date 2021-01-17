package kubackip.github.io.LetsDoSettlement;

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

        showIfItWorks();
        showSettlement();
        showSettlement.setText(settlement.toString());
    }

    private void showIfItWorks() {
        System.out.println("Members:");
        System.out.println(memberList.isEmpty());
        for (Members m : memberList) {
            System.out.println(m);
        }

        System.out.println("Deducted Payments:");
        System.out.println(deductedPaymentList.isEmpty());
        for (DeductedPayments p : deductedPaymentList) {
            System.out.println(p);
        }

        System.out.println("Payment List");
        System.out.println(paymentList.isEmpty());
        for (Payment payment : paymentList) {
            System.out.println(payment);
        }

        System.out.println("pairsSettlement:");
        for (float f : pairsSettlement) {
            System.out.println(f);
        }

        System.out.println("pairsDeductedSettlement:");
        for (float f1 : pairsDeductedSettlement) {
            System.out.println(f1);
        }
        System.out.println("pairsOfPayers");
        for (Map.Entry<Integer, String> entry : pairsOfPayers.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key: " + key + ", value: " + value);
        }
    }

    private void showSettlement() {
        for (int i = 0; i < pairsSettlement.length; i++) {
            System.out.println("pairsOfPayers get(i): " + pairsOfPayers.get(i));
            System.out.println("pairsOfPayers charAt(1)" + pairsOfPayers.get(i).charAt(1));
            System.out.println("pairsOfPayers charAt(0)" + pairsOfPayers.get(i).charAt(0));

            System.out.println("i: " + i);

            if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] >= 0) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " musi oddać: "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " " + ((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] < 0 && (pairsSettlement[i] / memberList.size()) >= (-pairsDeductedSettlement[i])) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " musi oddać: "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " " + ((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] > 0 && pairsDeductedSettlement[i] < 0 && (pairsSettlement[i] / memberList.size()) < (-pairsDeductedSettlement[i])) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " musi oddać: "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " " + -((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] <= 0) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " musi oddać: "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " " + -((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] > 0 && -(pairsSettlement[i] / memberList.size()) >= pairsDeductedSettlement[i]) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " musi oddać: "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " " + -((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            } else if (pairsSettlement[i] < 0 && pairsDeductedSettlement[i] > 0 && -(pairsSettlement[i] / memberList.size()) < pairsDeductedSettlement[i]) {
                settlement.append(memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(1))).toString() + " musi oddać: "
                        + memberList.get(Character.getNumericValue(pairsOfPayers.get(i).charAt(0))).toString() + " " + ((pairsSettlement[i] / memberList.size()) + pairsDeductedSettlement[i]) + " złotych" + "\n");
            }
        }
    }
    
    private float formatFloat(float toFormat) {
        return 0f;
    }
}
