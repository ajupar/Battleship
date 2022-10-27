package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import kayttoliittymat.laivanupotus.game.Fleet;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Game.GameState;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.playfield.GameSquare;
import kayttoliittymat.laivanupotus.playfield.Playfield;
import kayttoliittymat.laivanupotus.playfield.Playfield.Orientation;
import kayttoliittymat.laivanupotus.ships.Battleship;
import kayttoliittymat.laivanupotus.ships.Carrier;
import kayttoliittymat.laivanupotus.ships.Cruiser;
import kayttoliittymat.laivanupotus.ships.Destroyer;
import kayttoliittymat.laivanupotus.ships.Ship;
import kayttoliittymat.laivanupotus.ships.Submarine;

/**
 * Raahaustilan kontrolleri.
 *
 */
public class GameUIdragAndDropController {

	// Sijoittamatta olevat laivaoliot tyypin mukaan
	private ArrayList<Carrier> boat5sPx = new ArrayList<>();
	private ArrayList<Battleship> boat4sPx = new ArrayList<>();
	private ArrayList<Cruiser> boat3sPx = new ArrayList<>();
	private ArrayList<Submarine> boat2sPx = new ArrayList<>();
	private ArrayList<Destroyer> boat1sPx = new ArrayList<>();

	// määrät
	private SimpleDoubleProperty boat5amountX = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat4amountX = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat3amountX = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat2amountX = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat1amountX = new SimpleDoubleProperty(0);
	
	// kaikki laivat asetettu?
	private SimpleBooleanProperty allShipsSetOnGameArea = new SimpleBooleanProperty();
	
	// luokkamuuttujia
	private GameUIController guiController;
	private SimpleStringProperty informationText = new SimpleStringProperty();
	private SimpleStringProperty informationTextColor = new SimpleStringProperty();
	private Game currentGame;  // käynnissä oleva pli
	private MasterController masterController;  // yhteys yläkontrolleriin
	private Player playerX;
	private GridPane playerXPlayfieldGridPane;  // pelaajan peliruudukko
	private GameSquare[] playerXPlayfieldGameSquaresArray;
	private Playfield playerXPlayfield;  // pelaajan Playfield-olio
	private Fleet playerXFleet;

	private boolean currentDropSuccessful;  // saadaan syötteenä pelaajan Playfield-oliolta
	private Orientation deployOrientation = Orientation.HORIZONTAL;  // oletusasettelu on vaakasuora

	@FXML
	private VBox left_settings;

	@FXML
	private Label playerName;

	@FXML
	private TextField boat5amount;

	@FXML
	private Button boat5Button;

	@FXML
	private TextField boat4amount;

	@FXML
	private Button boat4Button;

	@FXML
	private TextField boat3amount;

	@FXML
	private Button boat3Button;

	@FXML
	private TextField boat2amount;

	@FXML
	private Button boat2Button;

	@FXML
	private TextField boat1amount;

	@FXML
	private Button boat1Button;

	@FXML
	private Button relocateButton;

	@FXML
	private Button setupApproved;

	// dragDetected: raahauksen aloitusmetodit	
	@FXML
	void boat5ButtonOnDragDetected(MouseEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					//boat5button drag detected.");
			dragDetected(event, boat5Button, ShipType.CARRIER);
	}

	@FXML
	void boat4ButtonOnDragDetected(MouseEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat4ButtonOnDragDetected.");
			dragDetected(event, boat4Button, ShipType.BATTLESHIP);
	}

	@FXML
	void boat3ButtonOnDragDetected(MouseEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat3ButtonOnDragDetected.");
			dragDetected(event, boat3Button, ShipType.CRUISER);
	}

	@FXML
	void boat2ButtonOnDragDetected(MouseEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat2ButtonOnDragDetected.");
			dragDetected(event, boat2Button, ShipType.SUBMARINE);
	}

	@FXML
	void boat1ButtonOnDragDetected(MouseEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat1ButtonOnDragDetected.");
		dragDetected(event, boat1Button, ShipType.DESTROYER);
	}


	// dragDone: Reagoi, kun raahaustoiminto on saatu valmiiksi (targetin päässä onDropped).

	@FXML
	void boat5ButtonOnDragDone(DragEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat5ButtonOnDragDone.");
		dragDone(event, boat5Button, ShipType.CARRIER);
	}

	@FXML
	void boat4ButtonOnDragDone(DragEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat4ButtonOnDragDone.");
		dragDone(event, boat4Button, ShipType.BATTLESHIP);
	}

	@FXML
	void boat3ButtonOnDragDone(DragEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat3ButtonOnDragDone.");
		dragDone(event, boat3Button, ShipType.CRUISER);
	}

	@FXML
	void boat2ButtonOnDragDone(DragEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat2ButtonOnDragDone.");
		dragDone(event, boat2Button, ShipType.SUBMARINE);
	}

	@FXML
	void boat1ButtonOnDragDone(DragEvent event) {
		// System.out.println("debug (GameUIdragAndDropController)
					// boat1ButtonOnDragDone.");
		dragDone(event, boat1Button, ShipType.DESTROYER);
	}

	public void setCurrentDropSuccessful(boolean dropSucc) {
		this.currentDropSuccessful = dropSucc;
	}

	public Orientation getDeployOrientation() {
		return deployOrientation;
	}

	public void setDeployOrientation(Orientation o) {
		this.deployOrientation = o;
	}
	
	public SimpleDoubleProperty getBoat5amountX() {
		return boat5amountX;
	}

	public SimpleDoubleProperty getBoat4amountX() {
		return boat4amountX;
	}

	public SimpleDoubleProperty getBoat3amountX() {
		return boat3amountX;
	}

	public SimpleDoubleProperty getBoat2amountX() {
		return boat2amountX;
	}

	public SimpleDoubleProperty getBoat1amountX() {
		return boat1amountX;
	}
	
	// PELKÄT ALUKSEN OSAT HORISONTAALISESTI ELI MUUTTUVANA ARVONA CoordinateX
	// TÄSSÄ COORDINATEX KERTOO RIVIN NUMERON ELI KUN X MUUTTUU --> LIIKUTAAN PYSTYSUUNNASSA YLÖS-ALAS
	// KUN Y MUUTTUU --> LIIKUTAAN VAAKASUUNNASSA VASEN-OIKEA
	// VAIKUTTEITA R-KIELESTÄ
	
	// addBoat: ajetaan alussa, kun asetuksissa määritetyt alusmäärät asetetaan pelaajalle
	public void addBoat5sPx(int x, int y, ArrayList<GameSquare> shipBody) {
		boat5sPx.add(new Carrier(x, y, shipBody, playerX));
		for(Carrier boat: boat5sPx) {
			System.out.print("debug (GameUIdragAndDropController) addBoat5sPx: ");
			System.out.println(boat); //Ship luokassa toString metodi
//			for (GameSquare g : boat.getShipBody() ) {
//				System.out.println("	" + g.toString() + "RowIndex: " + GridPane.getRowIndex(g) + ", ColumnIndex: " + GridPane.getColumnIndex(g));
//			}
		}
	}
	
	public void addBoat4sPx(int x, int y, ArrayList<GameSquare> shipBody) {
		boat4sPx.add(new Battleship(x, y, shipBody, playerX));
		for(Battleship boat: boat4sPx) {
			System.out.print("debug (GameUIdragAndDropController) addBoat4sPx: ");
			System.out.println(boat); //Ship luokassa toString metodi
		}
	}
	
	public void addBoat3sPx(int x, int y, ArrayList<GameSquare> shipBody) {
		boat3sPx.add(new Cruiser(x, y, shipBody, playerX));
		for(Cruiser boat: boat3sPx) {
			System.out.print("debug (GameUIdragAndDropController) addBoat3sPx: ");
			System.out.println(boat); //Ship luokassa toString metodi
		}
	}
	
	public void addBoat2sPx(int x, int y, ArrayList<GameSquare> shipBody) {
		boat2sPx.add(new Submarine(x, y, shipBody, playerX));
		for(Submarine boat: boat2sPx) {
			System.out.print("debug (GameUIdragAndDropController) addBoat2sPx: ");
			System.out.println(boat); //Ship luokassa toString metodi
		}
	}
	
	public void addBoat1sPx(int x, int y, ArrayList<GameSquare> shipBody) {
		boat1sPx.add(new Destroyer(x, y, shipBody, playerX));
		for(Destroyer boat: boat1sPx) {
			System.out.print("debug (GameUIdragAndDropController) addBoat1sPx: ");
			System.out.println(boat); //Ship luokassa toString metodi
		}
	}

	/**
	 * Raahauksen päättyminen, toiminnot kontrollerin päässä
	 * 
	 * @param event
	 * @param sourceButton
	 * @param shipTypeDragDone
	 */
	private void dragDone(DragEvent event, Button sourceButton, ShipType shipTypeDragDone) {

		TransferMode modeUsed = event.getTransferMode();

		Dragboard dragboard = event.getDragboard();

		if (modeUsed == TransferMode.MOVE) {
			// sourceButton.setStyle("-fx-color: red");
			//System.out.println("debug (GUIdraganddropController) dragDone: no color change");
			System.out.println("debug (GUIdraganddropController) dragDone: " + dragboard.getContent(DataFormat.PLAIN_TEXT));

			// raahauksen onnistuttua vähennetään sijoitettu alus listasta
			if (dragboard.getContent(DataFormat.PLAIN_TEXT) == ShipType.CARRIER) {
				//System.out.println("debug (GameUIdragAndDropController) dragDone: shiptype: Carrier");
				if (currentDropSuccessful == true) {
					boat5amountX.setValue(boat5amountX.getValue() - 1);
				}
			}
			if (dragboard.getContent(DataFormat.PLAIN_TEXT) == ShipType.BATTLESHIP) {
				//System.out.println("debug (GameUIdragAndDropController) dragDone: shiptype: Battleship");
				if (currentDropSuccessful == true) {
					boat4amountX.setValue(boat4amountX.getValue() - 1);
				}
			}
			if (dragboard.getContent(DataFormat.PLAIN_TEXT) == ShipType.CRUISER) {
				//System.out.println("debug (GameUIdragAndDropController) dragDone: shiptype: Cruiser");
				if (currentDropSuccessful == true) {
					boat3amountX.setValue(boat3amountX.getValue() - 1);
				}
			}
			if (dragboard.getContent(DataFormat.PLAIN_TEXT) == ShipType.SUBMARINE) {
				//System.out.println("debug (GameUIdragAndDropController) dragDone: shiptype: Submarine");
				if (currentDropSuccessful == true) {
					boat2amountX.setValue(boat2amountX.getValue() - 1);
				}
			}
			if (dragboard.getContent(DataFormat.PLAIN_TEXT) == ShipType.DESTROYER) {
				//System.out.println("debug (GameUIdragAndDropController) dragDone: shiptype: Destroyer");
				if (currentDropSuccessful == true) {
					boat1amountX.setValue(boat1amountX.getValue() - 1);
				}
			}
		}
		event.consume();
	}

	/**
	 * Alusten uudelleensijoittaminen
	 * @param event
	 */
	@FXML
	void relocateButtonAction(ActionEvent event) {

		// masterController hoitaa tämän, koska siivotaan koko käyttöliittymänäkymä
		masterController.relocateButtonResetPlayfield(currentGame);

	}

	/**
	 * Kontrollerin käynnistys
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {

		// System.out.println("debug (GameUIdragAndDropController): one gridpane
		// element: "
		// + playerXPlayfieldGridPane.getChildren().get(0).toString());

		playerName.setText(playerX.getName());
		// Älä siirrä näitä alemmas:
		boat1amountX.setValue(currentGame.getVesselAmounts()[0]);
		boat2amountX.setValue(currentGame.getVesselAmounts()[1]);
		boat3amountX.setValue(currentGame.getVesselAmounts()[2]);
		boat4amountX.setValue(currentGame.getVesselAmounts()[3]);
		boat5amountX.setValue(currentGame.getVesselAmounts()[4]);
		
		setGraphics();

		setBindings(); // Älä siirrä tätä ylemmäs

		System.out.println("debug (GameUIdragAndDropController): " + playerX.getName() +
				" Fleet content: " +boat5amountX.intValue() + "/" + boat4amountX.intValue() + "/" + 
				boat3amountX.intValue() + "/" + boat2amountX.intValue() + "/" + boat1amountX.intValue());
		
//		System.out.println("debug (GameUIdragAndDropController): Game zone dimension "
//				+ currentGame.getGameZoneSizeSetting().getDimension());

	} // initialize() - LOPPU

	/**
	 * Asetukset hyväksytty (käyttäjä painaa OK)
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	public void setupApprovedButtonAction(ActionEvent event) throws Exception {

		// pelaaja 1 asetukset --> pelaaja 2 asetukset
		if (currentGame.getGameState().equals(GameState.PLR1SETUP)) {			

			GridPane player1playFieldGridPane = playerXPlayfield.getPlayfieldGridPane();

			masterController.savePlayer1Setup(player1playFieldGridPane, currentGame);
			
			masterController.loadPlayer2Setup(currentGame);
			
			masterController.setPlayer1Fleet(setShipsToFleet());						

		// pelaaja 2 asetukset --> pelin käynnistys
		} else if (currentGame.getGameState().equals(GameState.PLR2SETUP)) {
			
			GridPane player2playFieldGridPane = playerXPlayfield.getPlayfieldGridPane();
			
			masterController.savePlayer2Setup(player2playFieldGridPane, currentGame);
			
			masterController.setPlayer2Fleet(setShipsToFleet());

			//System.out.println("debug (GameUIdragAndDropController) setupApprovedButtonAction: GameState.PLR2SETUP xxxx.unbind()");
			//informationText.unbind();
			//informationTextColor.unbind();

		} else {
			throw new Exception(
					"(GameUIdragAndDropController) Invalid GameState of currentGame. Must be PLR1SETUP or PLR2SETUP.");
		}
	}
	
	/**
	 * Asettaa alukset Fleet-olion sisälle
	 * 
	 * @return
	 */
	public Fleet setShipsToFleet() {
		
		playerXFleet.setCarrierList(boat5sPx);
		playerXFleet.setBattleshipList(boat4sPx);						
		playerXFleet.setCruiserList(boat3sPx);			
		playerXFleet.setSubmarineList(boat2sPx);
		playerXFleet.setDestroyerList(boat1sPx);
		
		System.out.println("debug (GameUIdragAndDropController) setShipsToFleet:");
		System.out.println(playerXFleet);

		return playerXFleet;
	}
	
	@SuppressWarnings("exports")
	public GameUIdragAndDropController(GameUIController controller, SimpleStringProperty informationTextIN,
			SimpleStringProperty informationTextColorIN, Game currentGameIN, MasterController masterControllerIN,
			Player playerIN, GridPane playerXPlayfieldGridPaneIN, Playfield playerXPlayfieldIN, Fleet playerXFleetIN) {
		guiController = controller;
		informationText = informationTextIN;
		informationTextColor = informationTextColorIN;
		currentGame = currentGameIN;
		masterController = masterControllerIN;
		playerX = playerIN;
		playerXPlayfieldGridPane = playerXPlayfieldGridPaneIN;
		playerXPlayfield = playerXPlayfieldIN;
		playerXFleet = playerXFleetIN;

		playerXPlayfield.setDragAndDropController(this);

		playerXPlayfield.setDeployOrientation(this.deployOrientation);
	}


	/**
	 * Reaktiiviset sidokset
	 */
	private void setBindings() {

		// Converter
		// https://gist.github.com/jundl77/c4b2be5286c58d4f102b
		StringConverter<Number> converter = new NumberStringConverter();
		// Converter related Bindings
		Bindings.bindBidirectional(boat5amount.textProperty(), boat5amountX, converter);
		Bindings.bindBidirectional(boat4amount.textProperty(), boat4amountX, converter);
		Bindings.bindBidirectional(boat3amount.textProperty(), boat3amountX, converter);
		Bindings.bindBidirectional(boat2amount.textProperty(), boat2amountX, converter);
		Bindings.bindBidirectional(boat1amount.textProperty(), boat1amountX, converter);
		
		//Disablointi myös Playfield.java: setButtonXrelatedItems -metodissa 
		//Olisi ehkä parempi toteuttaa kuitenkin tässä.
		//button.disableProperty().bind(Bindings.size(items).greaterThan(2));
		boat5Button.disableProperty().bind(Bindings.when(boat5amountX.isEqualTo(0)).then(true).otherwise(false));
		boat4Button.disableProperty().bind(Bindings.when(boat4amountX.isEqualTo(0)).then(true).otherwise(false));
		boat3Button.disableProperty().bind(Bindings.when(boat3amountX.isEqualTo(0)).then(true).otherwise(false));
		boat2Button.disableProperty().bind(Bindings.when(boat2amountX.isEqualTo(0)).then(true).otherwise(false));
		boat1Button.disableProperty().bind(Bindings.when(boat1amountX.isEqualTo(0)).then(true).otherwise(false));

		// Kaikki alukset sijoitettu pelialueelle
		allShipsSetOnGameArea
				.bind(Bindings
						.when(boat5amountX.isEqualTo(0).and(boat4amountX.isEqualTo(0)).and(boat3amountX.isEqualTo(0))
								.and(boat2amountX.isEqualTo(0)).and(boat1amountX.isEqualTo(0)))
						.then(true).otherwise(false));
		
		// System.out.println("debug (GameUIdragAndDropController)
		// allShipsSetOnGameArea-status: "
		// + allShipsSetOnGameArea.getValue());
		
		relocateButton.disableProperty().bind(Bindings
				.when(boat1amountX.isEqualTo(currentGame.getVesselAmounts()[0]).and(
						boat2amountX.isEqualTo(currentGame.getVesselAmounts()[1])).and(
								boat3amountX.isEqualTo(currentGame.getVesselAmounts()[2])).and(
										boat4amountX.isEqualTo(currentGame.getVesselAmounts()[3])).and(
												boat5amountX.isEqualTo(currentGame.getVesselAmounts()[4]))).then(true).otherwise(false));

		// Ylimmän tason sidonta, joka määrittää, voiko start-nappia painaa
		setupApproved.disableProperty().bind(allShipsSetOnGameArea.not());

		informationText.bind(Bindings.when(allShipsSetOnGameArea.not()).then("Drag & Drop all vessels to game area. Hold Control button for vertical orientation.")
				.otherwise("All vessels have been set to the game area."));

		informationTextColor.bind(Bindings.when(allShipsSetOnGameArea.not()).then("red").otherwise("black"));
	}

	/**
	 * Graafisia yksityiskohtia
	 */
	private void setGraphics() {
		
		boat5amount.setCursor(Cursor.DEFAULT);
		boat4amount.setCursor(Cursor.DEFAULT);
		boat3amount.setCursor(Cursor.DEFAULT);
		boat2amount.setCursor(Cursor.DEFAULT);
		boat1amount.setCursor(Cursor.DEFAULT);
		
		Tooltip boatXbuttonTT = new Tooltip("Start drag&drop from here (drop the vessel to game area).\nHold Control button for vertical orientation.");
		boat5Button.setTooltip(boatXbuttonTT);
		boat4Button.setTooltip(boatXbuttonTT);
		boat3Button.setTooltip(boatXbuttonTT);
		boat2Button.setTooltip(boatXbuttonTT);
		boat1Button.setTooltip(boatXbuttonTT);

		Image boat5Image = new Image(
				getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Carrier_button.png"));
		ImageView boat5ImageView = new ImageView(boat5Image);
		boat5ImageView.setFitWidth(86.0);
		boat5ImageView.setFitHeight(24.0);
		boat5Button.setGraphic(boat5ImageView);
		boat5Button.setCursor(Cursor.OPEN_HAND);

		Image boat4Image = new Image(
				getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Battleship_button3.png"));
		ImageView boat4ImageView = new ImageView(boat4Image);
		boat4ImageView.setFitWidth(76.0);
		boat4ImageView.setFitHeight(18.0);
		boat4Button.setGraphic(boat4ImageView);
		boat4Button.setCursor(Cursor.OPEN_HAND);

		Image boat3Image = new Image(
				getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Cruiser-Admiral-Makarov.png"));
		ImageView boat3ImageView = new ImageView(boat3Image);
		boat3ImageView.setFitWidth(55.0);
		boat3ImageView.setFitHeight(18.0);
		boat3Button.setGraphic(boat3ImageView);
		boat3Button.setCursor(Cursor.OPEN_HAND);

		Image boat2Image = new Image(
				getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Submarine_button.png"));
		ImageView boat2ImageView = new ImageView(boat2Image);
		boat2ImageView.setFitWidth(50.0);
		boat2ImageView.setFitHeight(18.0);
		boat2Button.setGraphic(boat2ImageView);
		boat2Button.setCursor(Cursor.OPEN_HAND);

		Image boat1Image = new Image(
				getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Destroyer_button.png"));
		ImageView boatImageView = new ImageView(boat1Image);
		boatImageView.setFitWidth(40.0);
		boatImageView.setFitHeight(18.0);
		boat1Button.setGraphic(boatImageView);
		boat1Button.setCursor(Cursor.OPEN_HAND);
	}

	/**
	 * Raahauksen käynnistyminen
	 * 
	 * @param event
	 * @param dragStartButton
	 * @param shipTypeToMove
	 */
	private void dragDetected(MouseEvent event, Button dragStartButton, ShipType shipTypeToMove) {

		if (shipTypeToMove == null) {
			event.consume();
			return;
		}

		Image vesselImage = new Image(getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Carrier_dd.png")); // Alustus

		if (shipTypeToMove.equals(ShipType.CARRIER)) {
			vesselImage = new Image(getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Carrier_dd.png"));
		}
		if (shipTypeToMove.equals(ShipType.BATTLESHIP)) {
			vesselImage = new Image(getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Battleship_dd3.png"));
		}
		if (shipTypeToMove.equals(ShipType.CRUISER)) {
			vesselImage = new Image(getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Cruiser-Admiral-Makarov-dd.png"));
		}
		if (shipTypeToMove.equals(ShipType.SUBMARINE)) {
			vesselImage = new Image(getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Submarine_dd.png"));
		}
		if (shipTypeToMove.equals(ShipType.DESTROYER)) {
			vesselImage = new Image(getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/Destroyer_dd.png"));
		}

		Dragboard dragboard = dragStartButton.startDragAndDrop(TransferMode.MOVE);

		// https://stackoverflow.com/questions/52623505/javafx-keyevents-during-drag-drop-operation
		if (!event.isControlDown()) {
			deployOrientation = Orientation.HORIZONTAL;
		} else {
			deployOrientation = Orientation.VERTICAL;
		}

		dragboard.setDragView(vesselImage);

		ClipboardContent content = new ClipboardContent();

		// siirretään DragEventille kuljetettava sisältö (alustyyppi)
		// pelikenttä tunnistaa tämän sisällön ja reagoi siihen
		content.put(DataFormat.PLAIN_TEXT, shipTypeToMove);

		dragboard.setContent(content);

		// System.out.println("debug (GameUIdragAndDropController) dragDetected saved
		// clipboard content " + dragboard.getContent(DataFormat.PLAIN_TEXT));
		
		//System.out.println("debug (GameUIdragAndDropController) dragDetected: "
		//		+ dragboard.getContent(DataFormat.PLAIN_TEXT) + " - " + deployOrientation);

		event.consume();
	}
}
