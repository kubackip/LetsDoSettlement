module kubackip.github.io.LetsDoSettlement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.base;

    opens kubackip.github.io.LetsDoSettlement to javafx.fxml;
    exports kubackip.github.io.LetsDoSettlement;
}