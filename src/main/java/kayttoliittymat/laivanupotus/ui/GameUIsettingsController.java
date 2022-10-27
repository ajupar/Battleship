package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import kayttoliittymat.laivanupotus.game.Fleet;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.playfield.Playfield;
import kayttoliittymat.laivanupotus.game.Game.GameState;
import kayttoliittymat.laivanupotus.game.Game.PlayerNumber;

/**
 * Kontrolleri peliasetusten säätämiselle.
 *
 */
public class GameUIsettingsController {

	private FXMLLoader loader;
	private GameUIController guiController;
	private MasterController masterController;

	private GameUIplayfield player1GameUIplayfield; // käytetään pelaaja 1:n pelikentän luomiseen
	private GameUIplayfield player2GameUIplayfield; // käytetään pelaaja 2:n pelikentän luomiseen

	// tekstit eivät olet FXML-elementteinä, koska niiden viittaukset on tuotu
	// konstruktoreiden kautta GameUIControllerista
	private SimpleStringProperty informationText;
	private SimpleStringProperty informationTextColor;

	private SimpleBooleanProperty plrNamesValid = new SimpleBooleanProperty();

	private SimpleDoubleProperty boatTotalArea = new SimpleDoubleProperty(0);
	private SimpleBooleanProperty atLeastOneBoatSelected = new SimpleBooleanProperty();

	private SimpleDoubleProperty boat5amount = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat4amount = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat3amount = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat2amount = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty boat1amount = new SimpleDoubleProperty(0);

	private SimpleBooleanProperty boatsFitToGameArea = new SimpleBooleanProperty();

	private SimpleDoubleProperty areaSizeProperty = new SimpleDoubleProperty(100); // sama kuin ComboBoxin oletusasetus
																					// 10 x 10 --> pinta-ala 100

	private SimpleBooleanProperty b5chk1 = new SimpleBooleanProperty();  // carrierin valintaruutu

	private SimpleBooleanProperty b4chk1 = new SimpleBooleanProperty();  // battleshipit
	private SimpleBooleanProperty b4chk2 = new SimpleBooleanProperty();

	private SimpleBooleanProperty b3chk1 = new SimpleBooleanProperty();  // cruiserit
	private SimpleBooleanProperty b3chk2 = new SimpleBooleanProperty();
	private SimpleBooleanProperty b3chk3 = new SimpleBooleanProperty();

	private SimpleBooleanProperty b2chk1 = new SimpleBooleanProperty();  // submarinet
	private SimpleBooleanProperty b2chk2 = new SimpleBooleanProperty();
	private SimpleBooleanProperty b2chk3 = new SimpleBooleanProperty();
	private SimpleBooleanProperty b2chk4 = new SimpleBooleanProperty();

	private SimpleBooleanProperty b1chk1 = new SimpleBooleanProperty();  // destroyerit
	private SimpleBooleanProperty b1chk2 = new SimpleBooleanProperty();
	private SimpleBooleanProperty b1chk3 = new SimpleBooleanProperty();
	private SimpleBooleanProperty b1chk4 = new SimpleBooleanProperty();
	private SimpleBooleanProperty b1chk5 = new SimpleBooleanProperty();
	
	private int[] vesselAmounts;

	public GameUIsettingsController(GameUIController controller, SimpleStringProperty informationTextIN,
			SimpleStringProperty informationTextColorIN, MasterController masterControllerIN) {
		guiController = controller;
		informationText = informationTextIN;
		informationTextColor = informationTextColorIN;
		masterController = masterControllerIN;
	}

	/**
	 * Kontrollerin käynnistys.
	 * 
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {

		// Täydennetään ComboBoxiin sisältö
		gameAreaSizeSelection.getItems().setAll(GameZoneSize.values());

		// asetetaan UI:n ehdottama oletuskoko
		gameAreaSizeSelection.setValue(GameZoneSize.size10);

		setEventHandlers();

		setBindings();
		
		setGraphics();

	} // initialize() - LOPPU

	@FXML
	private VBox leftSettings;

	@FXML
	private TextField player1name;

	@FXML
	private TextField player2name;

	@FXML
	private CheckBox boat5chk1;

	@FXML
	private TextField boat5pcs;

	@FXML
	private CheckBox boat4chk1;

	@FXML
	private CheckBox boat4chk2;

	@FXML
	private TextField boat4pcs;

	@FXML
	private CheckBox boat3chk1;

	@FXML
	private CheckBox boat3chk2;

	@FXML
	private CheckBox boat3chk3;

	@FXML
	private TextField boat3pcs;

	@FXML
	private CheckBox boat2chk1;

	@FXML
	private CheckBox boat2chk2;

	@FXML
	private CheckBox boat2chk3;

	@FXML
	private CheckBox boat2chk4;

	@FXML
	private TextField boat2pcs;

	@FXML
	private CheckBox boat1chk1;

	@FXML
	private CheckBox boat1chk2;

	@FXML
	private CheckBox boat1chk3;

	@FXML
	private CheckBox boat1chk4;

	@FXML
	private CheckBox boat1chk5;

	@FXML
	private TextField boat1pcs;

	@FXML
	private ComboBox<GameZoneSize> gameAreaSizeSelection;  // pelikentän koon valinta

	@FXML
	private Button startButton;

	/*
	 * boat1 methods: destroyers checkboxes
	 */

	@FXML
	void player1nameAction(ActionEvent event) {

	}

	@FXML
	void player2nameAction(ActionEvent event) {

	}

	@FXML
	void player1nameChanged(InputMethodEvent event) {

	}

	@FXML
	void player2nameChanged(InputMethodEvent event) {

	}
	
	/*
	 * boat5 methods: carrier checkbox
	 */

	@FXML
	void boat5AddRemove1(ActionEvent event) {

		if (boat5chk1.isSelected()) {
			setBoatAmounts("Boat5", "chk1", "add");
		} else {
			setBoatAmounts("Boat5", "chk1", "remove");
		}
	}
	
	/*
	 * boat4 methods: battleship checkboxes
	 */

	@FXML
	void boat4AddRemove1(ActionEvent event) {

		if (boat4chk1.isSelected()) {
			setBoatAmounts("Boat4", "chk1", "add");
		} else {
			setBoatAmounts("Boat4", "chk1", "remove");
			
			//Mikäli boat4 rivin kaikki alukset ovat valittuina,
			//niin ensimmäisen checkboxin valinnan poisto tyhjentää koko rivin valinnnat
			if (b4chk2.getValue() == true) {
				b4chk2.setValue(false);
				setBoatAmounts("Boat4", "chk1", "remove");
			}
		}
	}

	@FXML
	void boat4AddRemove2(ActionEvent event) {

		if (boat4chk2.isSelected()) {
			setBoatAmounts("Boat4", "chk2", "add");
			
			//Mikäli boat4 riviltä ei ole valittuna yhtään alusta,
			//niin viimeisen checkboxin valinta valitsee kaikki alukset käyttöön
			if (b4chk1.getValue() == false) {
				b4chk1.setValue(true);
				setBoatAmounts("Boat4", "chk2", "add");
			}
		} else {
			setBoatAmounts("Boat4", "chk2", "remove");
		}
	}
	
	/*
	 * boat3 methods: cruiser checkboxes
	 */

	@FXML
	void boat3AddRemove1(ActionEvent event) {

		if (boat3chk1.isSelected()) {
			setBoatAmounts("Boat3", "chk1", "add");
		} else {
			setBoatAmounts("Boat3", "chk1", "remove");
			
			//Mikäli boat3 rivin kaikki alukset ovat valittuina,
			//niin ensimmäisen checkboxin valinnan poisto tyhjentää koko rivin valinnnat
			if (b3chk2.getValue() == true && b3chk3.getValue() == true) {
				b3chk2.setValue(false);
				setBoatAmounts("Boat3", "chk1", "remove");
				b3chk3.setValue(false);
				setBoatAmounts("Boat3", "chk1", "remove");
			}
		}
	}

	@FXML
	void boat3AddRemove2(ActionEvent event) {

		if (boat3chk2.isSelected()) {
			setBoatAmounts("Boat3", "chk2", "add");
		} else {
			setBoatAmounts("Boat3", "chk2", "remove");
		}
	}

	@FXML
	void boat3AddRemove3(ActionEvent event) {

		if (boat3chk3.isSelected()) {
			setBoatAmounts("Boat3", "chk3", "add");
			
			//Mikäli boat3 riviltä ei ole valittuna yhtään alusta,
			//niin viimeisen checkboxin valinta valitsee kaikki alukset käyttöön
			if (b3chk1.getValue() == false && b3chk2.getValue() == false) {
				b3chk1.setValue(true);
				setBoatAmounts("Boat3", "chk3", "add");
				b3chk2.setValue(true);
				setBoatAmounts("Boat3", "chk3", "add");
			}
		} else {
			setBoatAmounts("Boat3", "chk3", "remove");
		}
	}
	
	/*
	 * boat2 methods: submarines checkboxes
	 */

	@FXML
	void boat2AddRemove1(ActionEvent event) {

		if (boat2chk1.isSelected()) {
			setBoatAmounts("Boat2", "chk1", "add");
		} else {
			setBoatAmounts("Boat2", "chk1", "remove");
			
			//Mikäli boat2 rivin kaikki alukset ovat valittuina,
			//niin ensimmäisen checkboxin valinnan poisto tyhjentää koko rivin valinnnat
			if (b2chk2.getValue() == true && b2chk3.getValue() == true
					&& b2chk4.getValue() == true) {
				b2chk2.setValue(false);
				setBoatAmounts("Boat2", "chk1", "remove");
				b2chk3.setValue(false);
				setBoatAmounts("Boat2", "chk1", "remove");
				b2chk4.setValue(false);
				setBoatAmounts("Boat2", "chk1", "remove");
			}
		}
	}

	@FXML
	void boat2AddRemove2(ActionEvent event) {

		if (boat2chk2.isSelected()) {
			setBoatAmounts("Boat2", "chk2", "add");
		} else {
			setBoatAmounts("Boat2", "chk2", "remove");
		}
	}

	@FXML
	void boat2AddRemove3(ActionEvent event) {

		if (boat2chk3.isSelected()) {
			setBoatAmounts("Boat2", "chk3", "add");
		} else {
			setBoatAmounts("Boat2", "chk3", "remove");
		}
	}

	@FXML
	void boat2AddRemove4(ActionEvent event) {

		if (boat2chk4.isSelected()) {
			setBoatAmounts("Boat2", "chk4", "add");
			
			//Mikäli boat2 riviltä ei ole valittuna yhtään alusta,
			//niin viimeisen checkboxin valinta valitsee kaikki alukset käyttöön
			if (b2chk1.getValue() == false && b2chk2.getValue() == false && b2chk3.getValue() == false) {
				b2chk1.setValue(true);
				setBoatAmounts("Boat2", "chk4", "add");
				b2chk2.setValue(true);
				setBoatAmounts("Boat2", "chk4", "add");
				b2chk3.setValue(true);
				setBoatAmounts("Boat2", "chk4", "add");
			}
		} else {
			setBoatAmounts("Boat2", "chk4", "remove");
		}
	}	

	@FXML
	void boat1AddRemove1(ActionEvent event) {

		if (boat1chk1.isSelected()) {
			setBoatAmounts("Boat1", "chk1", "add");
			
		} else {
			setBoatAmounts("Boat1", "chk1", "remove");
			
			//Mikäli boat1 rivin kaikki alukset ovat valittuina,
			//niin ensimmäisen checkboxin valinnan poisto tyhjentää koko rivin valinnnat
			if (b1chk2.getValue() == true && b1chk3.getValue() == true
					&& b1chk4.getValue() == true && b1chk5.getValue() == true ) {
				b1chk2.setValue(false);
				setBoatAmounts("Boat1", "chk1", "remove");
				b1chk3.setValue(false);
				setBoatAmounts("Boat1", "chk1", "remove");
				b1chk4.setValue(false);
				setBoatAmounts("Boat1", "chk1", "remove");
				b1chk5.setValue(false);
				setBoatAmounts("Boat1", "chk1", "remove");
			}
		}
	}

	@FXML
	void boat1AddRemove2(ActionEvent event) {

		if (boat1chk2.isSelected()) {
			setBoatAmounts("Boat1", "chk2", "add");
		} else {
			setBoatAmounts("Boat1", "chk2", "remove");
		}
	}

	@FXML
	void boat1AddRemove3(ActionEvent event) {

		if (boat1chk3.isSelected()) {
			setBoatAmounts("Boat1", "chk3", "add");
		} else {
			setBoatAmounts("Boat1", "chk3", "remove");
		}
	}

	@FXML
	void boat1AddRemove4(ActionEvent event) {

		if (boat1chk4.isSelected()) {
			setBoatAmounts("Boat1", "chk4", "add");
		} else {
			setBoatAmounts("Boat1", "chk4", "remove");
		}
	}

	@FXML
	void boat1AddRemove5(ActionEvent event) {

		if (boat1chk5.isSelected()) {
			setBoatAmounts("Boat1", "chk5", "add");
			
			//Mikäli boat5 riviltä ei ole valittuna yhtään alusta,
			//niin viimeisen checkboxin valinta valitsee kaikki alukset käyttöön
			if (b1chk1.getValue() == false && b1chk2.getValue() == false && b1chk3.getValue() == false
					&& b1chk4.getValue() == false) {
				b1chk1.setValue(true);
				setBoatAmounts("Boat1", "chk5", "add");
				b1chk2.setValue(true);
				setBoatAmounts("Boat1", "chk5", "add");
				b1chk3.setValue(true);
				setBoatAmounts("Boat1", "chk5", "add");
				b1chk4.setValue(true);
				setBoatAmounts("Boat1", "chk5", "add");
			}
		} else {
			setBoatAmounts("Boat1", "chk5", "remove");
		}
	}

	/**
	 * Metodi, joka ajetaan, kun Start-nappia painetaan. Käynnistää pelilogiikan
	 * ajamisen ja siirtymisen seuraavaan pelitilaan.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void startButtonAction(ActionEvent event) throws IOException {

		Player player1 = new Player(player1name.getText(), PlayerNumber.PLAYER1);
		Player player2 = new Player(player2name.getText(), PlayerNumber.PLAYER2);

		GameZoneSize gameZoneSizeSetting = gameAreaSizeSelection.getValue();

		int gameZoneDim = gameZoneSizeSetting.getDimension();

		player1GameUIplayfield = new GameUIplayfield(gameZoneDim);
		player2GameUIplayfield = new GameUIplayfield(gameZoneDim);

		int carrierAmount = boat5amount.intValue();
		int battleShipAmount = boat4amount.intValue();
		int cruiserAmount = boat3amount.intValue();
		int submarineAmount = boat2amount.intValue();
		int destroyerAmount = boat1amount.intValue();

		vesselAmounts = new int[]{ destroyerAmount, submarineAmount, cruiserAmount, battleShipAmount, carrierAmount };

		// Luodaan Game-olio, jolle annetaan konstruktorissa pelaajat ja peliasetukset
		Game currentGame = new Game(player1, player2, gameZoneSizeSetting, player1GameUIplayfield,
				player2GameUIplayfield, vesselAmounts);

		// masterController jatkaa pelin kulkua
		masterController.loadPlayer1Setup(currentGame);
	}

	/**
	 * Päivitetään keskellä oleva peliruudukkomalli, kun pelin kokoasetusta muutellaan
	 * 
	 * @param event
	 */
	@FXML
	void gameAreaBoxOnAction(ActionEvent event) {

		masterController.updateCenterAnchor(gameAreaSizeSelection.getValue().getDimension());

		areaSizeProperty.setValue(gameAreaSizeSelection.getValue().getArea());
		System.out.println("debug (GameUIsettingsController.java): Selected game area size: "
				+ areaSizeProperty.getValue().toString());
	}

	/*
	 * Lisätään/vähennetään syötteenä saadun laivatyypiin määrää yhdellä ja 
	 * kasvatetaan/poistetaan laivojen kokoinaisalaa aluksen pinta-alalla. 
	 * 
	 * @param boatX laivatyyppi
	 * @param chkX checkboksin numero
	 * @param addRemove suoritetaanko lisätys vai poisto
	 */
	public void setBoatAmounts(String boatX, String chkX, String addRemove) {

		switch (boatX) {
		case "Boat5":

			if (addRemove.equals("add")) {
				boat5amount.setValue(boat5amount.getValue() + 1);
				boatTotalArea.setValue(boatTotalArea.getValue() + 5);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") added, " + boatX + " amount (pcs): "
								+ boat5amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			if (addRemove.equals("remove")) {
				boat5amount.setValue(boat5amount.getValue() - 1);
				boatTotalArea.setValue(boatTotalArea.getValue() - 5);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") removed, " + boatX + " amount (pcs): "
								+ boat5amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			break;

		case "Boat4":

			if (addRemove.equals("add")) {
				boat4amount.setValue(boat4amount.getValue() + 1);
				boatTotalArea.setValue(boatTotalArea.getValue() + 4);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") added, " + boatX + " amount (pcs): "
								+ boat4amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			if (addRemove.equals("remove")) {
				boat4amount.setValue(boat4amount.getValue() - 1);
				boatTotalArea.setValue(boatTotalArea.getValue() - 4);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") removed, " + boatX + " amount (pcs): "
								+ boat4amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			break;

		case "Boat3":

			if (addRemove.equals("add")) {
				boat3amount.setValue(boat3amount.getValue() + 1);
				boatTotalArea.setValue(boatTotalArea.getValue() + 3);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") added, " + boatX + " amount (pcs): "
								+ boat3amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			if (addRemove.equals("remove")) {
				boat3amount.setValue(boat3amount.getValue() - 1);
				boatTotalArea.setValue(boatTotalArea.getValue() - 3);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") removed, " + boatX + " amount (pcs): "
								+ boat3amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			break;

		case "Boat2":

			if (addRemove.equals("add")) {
				boat2amount.setValue(boat2amount.getValue() + 1);
				boatTotalArea.setValue(boatTotalArea.getValue() + 3);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") added, " + boatX + " amount (pcs): "
								+ boat2amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			if (addRemove.equals("remove")) {
				boat2amount.setValue(boat2amount.getValue() - 1);
				boatTotalArea.setValue(boatTotalArea.getValue() - 3);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") removed, " + boatX + " amount (pcs): "
								+ boat2amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			break;

		case "Boat1":

			if (addRemove.equals("add")) {
				boat1amount.setValue(boat1amount.getValue() + 1);
				boatTotalArea.setValue(boatTotalArea.getValue() + 2);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") added, " + boatX + " amount (pcs): "
								+ boat1amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			if (addRemove.equals("remove")) {
				boat1amount.setValue(boat1amount.getValue() - 1);
				boatTotalArea.setValue(boatTotalArea.getValue() - 2);
				System.out.println(
						"debug: " + boatX + " (checkbox" + chkX.charAt(3) + ") removed, " + boatX + " amount (pcs): "
								+ boat1amount.getValue() + ", Total boat area: " + boatTotalArea.doubleValue());
			}
			break;

		default:
			System.out.println("no match");
		}

	}
	
	/**
	 * Tehdään tapahtumankäsittelijät
	 */
	private void setEventHandlers() {

		// HANDLERIT CARRIEREILLE boat5

		boat5chk1.setOnAction((event) -> {
			boat5AddRemove1(event);
		});

		// HANDLERIT BATTLESHIPEILLE boat4

		boat4chk1.setOnAction((event) -> {
			boat4AddRemove1(event);
		});

		boat4chk2.setOnAction((event) -> {
			boat4AddRemove2(event);
		});

		// HANDLERIT CRUISEREILLE boat3

		boat3chk1.setOnAction((event) -> {
			boat3AddRemove1(event);
		});

		boat3chk2.setOnAction((event) -> {
			boat3AddRemove2(event);
		});

		boat3chk3.setOnAction((event) -> {
			boat3AddRemove3(event);
		});

		// HANDLERIT SUBMARINEILLE boat2

		boat2chk1.setOnAction((event) -> {
			boat2AddRemove1(event);
		});

		boat2chk2.setOnAction((event) -> {
			boat2AddRemove2(event);
		});

		boat2chk3.setOnAction((event) -> {
			boat2AddRemove3(event);
		});

		boat2chk4.setOnAction((event) -> {
			boat2AddRemove4(event);
		});

		// EVENT HANDLERIT DESTROYEREIDEN CHECKBOXEILLE boat1

		boat1chk1.setOnAction((event) -> {
			boat1AddRemove1(event);
		});

		boat1chk2.setOnAction((event) -> {
			boat1AddRemove2(event);
		});

		boat1chk3.setOnAction((event) -> {
			boat1AddRemove3(event);
		});

		boat1chk4.setOnAction((event) -> {
			boat1AddRemove4(event);
		});

		boat1chk5.setOnAction((event) -> {
			boat1AddRemove5(event);
		});
	}
	
	/**
	 * Reaktiiviset sidonnat
	 */
	private void setBindings() {
		// Muodostetaan sidokset muuttujien välille
		// Converter
		// https://gist.github.com/jundl77/c4b2be5286c58d4f102b
		StringConverter<Number> converter = new NumberStringConverter();
		// Converter related Bindings
		Bindings.bindBidirectional(boat5pcs.textProperty(), boat5amount, converter);
		Bindings.bindBidirectional(boat4pcs.textProperty(), boat4amount, converter);
		Bindings.bindBidirectional(boat3pcs.textProperty(), boat3amount, converter);
		Bindings.bindBidirectional(boat2pcs.textProperty(), boat2amount, converter);
		Bindings.bindBidirectional(boat1pcs.textProperty(), boat1amount, converter);

		// Pelaajien nimikentät eivät saa olla tyhjiä
		plrNamesValid.bind(
				Bindings.when(player1name.textProperty().isNotEmpty().and(player2name.textProperty().isNotEmpty()))
						.then(true).otherwise(false));

		// Tarkistetaan mahtuvatko alukset ruudukkoon. areaSizeProperty päivittyy aina,
		// kun ComboBoxia klikataan ja pinta-ala samalla muuttuu
		boatsFitToGameArea.bind(Bindings.when(boatTotalArea.multiply(2).lessThanOrEqualTo(areaSizeProperty)).then(true)
				.otherwise(false));

		// Peliä ei voi aloittaa ilman aluksia, joten vähintään yhden pitää olla
		// valittuna...
		atLeastOneBoatSelected.bind(Bindings.when(boatTotalArea.greaterThan(1)).then(true).otherwise(false));

		// Ylimmän tason sidonta, joka määrittää, voiko start-nappia painaa
		startButton.disableProperty()
				.bind(plrNamesValid.not().or(boatsFitToGameArea.not()).or(atLeastOneBoatSelected.not()));

		informationText.bind(Bindings.when(plrNamesValid.not().or(atLeastOneBoatSelected.not()))
				.then("Type player names and add at least one vessel to your fleet.")
				.otherwise(Bindings.when(boatsFitToGameArea.not())
						.then("Reduce amount of vessels or select bigger game area size.")
						.otherwise("Game starting is possible.")));

		informationTextColor.bind(Bindings.when(plrNamesValid.not().or(atLeastOneBoatSelected.not())).then("red")
				.otherwise(Bindings.when(boatsFitToGameArea.not()).then("red").otherwise("black")));

		Bindings.bindBidirectional(boat5chk1.selectedProperty(), b5chk1);

		Bindings.bindBidirectional(boat4chk1.selectedProperty(), b4chk1);
		Bindings.bindBidirectional(boat4chk2.selectedProperty(), b4chk2);

		Bindings.bindBidirectional(boat3chk1.selectedProperty(), b3chk1);
		Bindings.bindBidirectional(boat3chk2.selectedProperty(), b3chk2);
		Bindings.bindBidirectional(boat3chk3.selectedProperty(), b3chk3);

		Bindings.bindBidirectional(boat2chk1.selectedProperty(), b2chk1);
		Bindings.bindBidirectional(boat2chk2.selectedProperty(), b2chk2);
		Bindings.bindBidirectional(boat2chk3.selectedProperty(), b2chk3);
		Bindings.bindBidirectional(boat2chk4.selectedProperty(), b2chk4);

		Bindings.bindBidirectional(boat1chk1.selectedProperty(), b1chk1);
		Bindings.bindBidirectional(boat1chk2.selectedProperty(), b1chk2);
		Bindings.bindBidirectional(boat1chk3.selectedProperty(), b1chk3);
		Bindings.bindBidirectional(boat1chk4.selectedProperty(), b1chk4);
		Bindings.bindBidirectional(boat1chk5.selectedProperty(), b1chk5);

	}
	
	/**
	 * Graafisia yksityiskohtia
	 */
	private void setGraphics() {
		boat5pcs.setCursor(Cursor.DEFAULT);
		boat4pcs.setCursor(Cursor.DEFAULT);
		boat3pcs.setCursor(Cursor.DEFAULT);
		boat2pcs.setCursor(Cursor.DEFAULT);
		boat1pcs.setCursor(Cursor.DEFAULT);
		
		boat5chk1.setCursor(Cursor.HAND);
		boat4chk1.setCursor(Cursor.HAND);
		boat4chk2.setCursor(Cursor.HAND);
		boat3chk1.setCursor(Cursor.HAND);
		boat3chk2.setCursor(Cursor.HAND);
		boat3chk3.setCursor(Cursor.HAND);
		boat2chk1.setCursor(Cursor.HAND);
		boat2chk2.setCursor(Cursor.HAND);
		boat2chk3.setCursor(Cursor.HAND);
		boat2chk4.setCursor(Cursor.HAND);
		boat1chk1.setCursor(Cursor.HAND);
		boat1chk2.setCursor(Cursor.HAND);
		boat1chk3.setCursor(Cursor.HAND);
		boat1chk4.setCursor(Cursor.HAND);
		boat1chk5.setCursor(Cursor.HAND);
		
		Tooltip boatXpcsTT = new Tooltip("Select vessels by using check boxes (on the left).");
		boat5pcs.setTooltip(boatXpcsTT);
		boat4pcs.setTooltip(boatXpcsTT);
		boat3pcs.setTooltip(boatXpcsTT);
		boat2pcs.setTooltip(boatXpcsTT);
		boat1pcs.setTooltip(boatXpcsTT);
		
		// Hiiren viereen tulee aputeksti, kun hiirtä pitää valintaruudun päällä
		boat4chk2.setTooltip(new Tooltip("If no battleships selected... Select all battleships with this check box."));
		boat3chk3.setTooltip(new Tooltip("If no cruisers selected... Select all cuisers with this check box."));
		boat2chk4.setTooltip(new Tooltip("If no submarines selected... Select all submarines with this check box."));
		boat1chk5.setTooltip(new Tooltip("If no destoyers selected... Select all destoyers with this check box."));
		
		boat4chk1.setTooltip(new Tooltip("If all battleships are selected... Deselect all battleships with this check box."));
		boat3chk1.setTooltip(new Tooltip("If all cruisers are selected... Deselect all cuisers with this check box."));
		boat2chk1.setTooltip(new Tooltip("If all submarines are selected... Deselect all submarines with this check box."));
		boat1chk1.setTooltip(new Tooltip("If all destoyers selected... Deselect all destoyers with this check box."));
	}
}
