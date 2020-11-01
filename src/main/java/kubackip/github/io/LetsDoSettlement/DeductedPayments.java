package kubackip.github.io.LetsDoSettlement;

public class DeductedPayments {

    private String name;
    private float value;
    private int paymentID;
    private int payerID;

    public DeductedPayments(String name, float value, int paymentID, int payerID) {
        this.name = name;
        this.value = value;
        this.paymentID = paymentID;
        this.payerID = payerID;
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

    @Override
    public String toString() {
        return "DeductedPayments{" + "name=" + name + ", value=" + value
                + ", paymentID=" + paymentID + ", payerID=" + payerID + '}';
    }
}
