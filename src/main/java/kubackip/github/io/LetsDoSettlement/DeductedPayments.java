package kubackip.github.io.LetsDoSettlement;

public class DeductedPayments {

    private String name;
    private float value;
    private int paymentID;
    private int payerID;
    private int deductPayerID;

    public DeductedPayments(String name, float value, int paymentID, int payerID, int deductPayerID) {
        this.name = name;
        this.value = value;
        this.paymentID = paymentID;
        this.payerID = payerID;
        this.deductPayerID = deductPayerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getPayerID() {
        return payerID;
    }

    public void setPayerID(int payerID) {
        this.payerID = payerID;
    }

    public int getDeductPayerID() {
        return deductPayerID;
    }

    public void setDeductPayerID(int deductPayerID) {
        this.deductPayerID = deductPayerID;
    }

    @Override
    public String toString() {
        return "DeductedPayments{" + "name=" + name + ", value=" + value
                + ", paymentID=" + paymentID + ", payerID=" + payerID + ", deductPayerID= "
                + deductPayerID + '}';
    }
}
