package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Game.PlayerNumber;
import kayttoliittymat.laivanupotus.game.Player;

/**
 * Runkokäyttöliittymän kontrolleri.
 *
 */
public class GameUIController {

	private Stage rootStage;
	private MasterController masterController;

	public GameUIController(Stage rootStageIN, MasterController mc) {

		this.masterController = mc;
		this.rootStage = rootStageIN;
	}

	@FXML
	private SimpleStringProperty informationText = new SimpleStringProperty();  // alareunan ohjeteksti

	@FXML
	private SimpleStringProperty informationTextColor = new SimpleStringProperty();  // ohjetekstin väri

	@FXML
	private VBox bottom_infoArea;  // ohjetekstin alue

	@FXML
	private Label informativeTextLeftBottom;  // ohjetekstin Label-olio

	@FXML
	private Button exitButton;  // Exit-nappi

	@FXML
	private AnchorPane leftAnchor;  // käyttöliittymän vasen puolisko

	@FXML
	private AnchorPane centerAnchor;  // käyttöliittymän keskiosa (pelikenttä)

	@FXML
	private AnchorPane bottomAnchor;  // käyttöliittymän alaosa (infoteksti ja exit-nappi)

	/**
	 * Exit-nappulan tapahtumankäsittelijä.
	 * 
	 * @param event
	 */
	@FXML
	void exitButtonOnAction(ActionEvent event) {

		final Stage exitPopupStage = new Stage();
		exitPopupStage.initModality(Modality.APPLICATION_MODAL);
//        switchPlayerPopup.initOwner(primaryStage);
		VBox exitVbox = new VBox(20);

		Text exitText = new Text("Do you really want to exit?");
		exitText.setStyle("-fx-font-size: 18px");
		Button exitButton = new Button("Exit");
		Button cancelButton = new Button("Cancel");

		exitButton.setStyle("-fx-font-size: 18px");
		cancelButton.setStyle("-fx-font-size: 18px");
		exitButton.setMinSize(80, 30);
		cancelButton.setMinSize(80, 30);
		
		HBox buttonHBox = new HBox(cancelButton, exitButton);
		buttonHBox.setSpacing(10);

		// EXIT
		exitButton.setOnAction(e -> {

			exitPopupStage.close();
			
			Platform.exit();

		});
		
		// CANCEL
		cancelButton.setOnAction(e -> {
			
			exitPopupStage.close();
			
		});
		
		exitVbox.getChildren().addAll(exitText, buttonHBox);
		
		buttonHBox.setAlignment(Pos.CENTER);

		exitVbox.setAlignment(Pos.CENTER);

		Scene exitPopupScene = new Scene(exitVbox, 300, 200);
		exitPopupStage.setTitle("Quit battleship game");
		exitPopupStage.setScene(exitPopupScene);
		exitPopupStage.show();
	}

	/**
	 * Kontrollerin käynnistys.
	 * 
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {

		setBindings();

		masterController.setInformationText(informationText);
		masterController.setInformationTextColor(informationTextColor);

		masterController.initializeSettings(leftAnchor, centerAnchor, bottomAnchor);

		// System.out.println("debug (GameUIController.java): informationText_2: " +
		// this.informationText.getValue());
		// informationText.set("ARGH!!!");

	} // initialize() - LOPPU

	/**
	 * Reaktiivisten sidosten asettaminen: ohjeteksti ja sen väri.
	 */
	private void setBindings() {
		informativeTextLeftBottom.textProperty().bind(informationText);

		informativeTextLeftBottom.textFillProperty()
				.bind(Bindings.when(informationTextColor.isEqualTo("red")).then(Color.RED).otherwise(
						Bindings.when(informationTextColor.isEqualTo("red")).then(Color.RED).otherwise(Color.BLACK)));
	}
}