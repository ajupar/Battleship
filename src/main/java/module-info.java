module kayttoliittymat.laivanupotus {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;

    opens kayttoliittymat.laivanupotus to javafx.fxml;
    opens kayttoliittymat.laivanupotus.ui to javafx.fxml; // antaa oikeudet javafx:lle! puretaan moduulisuojaus
    exports kayttoliittymat.laivanupotus;
    exports kayttoliittymat.laivanupotus.ui;  // viedään javafx:n käyttöön
}