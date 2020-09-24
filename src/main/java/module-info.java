module kubackip.github.io.LetsDoSettlement {
    requires javafx.controls;
    requires javafx.fxml;

    opens kubackip.github.io.LetsDoSettlement to javafx.fxml;
    exports kubackip.github.io.LetsDoSettlement;
}