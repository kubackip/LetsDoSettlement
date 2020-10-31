package kubackip.github.io.LetsDoSettlement;

import java.time.LocalDate;

public class Payment {

    private String name;
    private String description;
    private float value;
    private LocalDate date;
    private int paymentID;
    private int payerID;

    public Payment(String name, String description, float value, LocalDate date,
            int paymentID, int payerID) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.date = date;
        this.paymentID = paymentID;
        this.payerID = payerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return paymentID;
    }

    public void setId(int id) {
        this.paymentID = id;
    }

    @Override
    public String toString() {
        return "Payment{" + "name=" + name + ", description=" + description
                + ", value=" + value + ", date=" + date + ", paymentID=" + paymentID
                + ", payerID: " + payerID + '}';
    }
}
