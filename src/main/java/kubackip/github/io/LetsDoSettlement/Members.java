package kubackip.github.io.LetsDoSettlement;

public class Members {

    private String name;
    private String secondName;
    private int id;

    public Members(String name, String secondName, int id) {
        this.name = name;
        this.secondName = secondName;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
