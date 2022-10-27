package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import kayttoliittymat.laivanupotus.game.Fleet;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Game.GameState;
import kayttoliittymat.laivanupotus.game.Game.PlayerNumber;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;
import kayttoliittymat.laivanupotus.playfield.Playfield;
import kayttoliittymat.laivanupotus.ships.Battleship;
import kayttoliittymat.laivanupotus.ships.Carrier;
import kayttoliittymat.laivanupotus.ships.Cruiser;
import kayttoliittymat.laivanupotus.ships.Destroyer;
import kayttoliittymat.laivanupotus.ships.Ship;
import kayttoliittymat.laivanupotus.ships.Submarine;

/**
 * Pelitilastojen kontrolleri Ajetaan ampumisvaiheessa
 * 
 *
 */
public class GameUIstatisticsController {

	private SimpleStringProperty informationText = new SimpleStringProperty();
	private SimpleStringProperty informationTextColor = new SimpleStringProperty();

	private SimpleStringProperty playerNameSimple = new SimpleStringProperty();
	private SimpleStringProperty opponentNameSimple = new SimpleStringProperty();

	private Game currentGame;
	private PlayerNumber plrNumberX;
	private MasterController masterController;
	private boolean gameContinues = true; // saa arvon false, kun jompikumpi pelaajista voittaa --> lopetusruutu

	private Playfield ownPlayfield;
	private GridPane ownPlayfieldGridPane;
	private Fleet ownFleet;
	private ArrayList<Ship> ownAllShipsList;
	private ArrayList<Carrier> ownBoat5List;
	private ArrayList<Battleship> ownBoat4List;
	private ArrayList<Cruiser> ownBoat3List;
	private ArrayList<Submarine> ownBoat2List;
	private ArrayList<Destroyer> ownBoat1List;
	private SimpleDoubleProperty ownShotHitAmount;
	private SimpleDoubleProperty ownShotTotalAmount;

	private Playfield opponentPlayfield;
	private GridPane opponentPlayfieldGridPane;
	private Fleet opponentFleet;
	private ArrayList<Ship> opponentAllShipsList;
	private ArrayList<Carrier> opponentBoat5List;
	private ArrayList<Battleship> opponentBoat4List;
	private ArrayList<Cruiser> opponentBoat3List;
	private ArrayList<Submarine> opponentBoat2List;
	private ArrayList<Destroyer> opponentBoat1List;
	private SimpleDoubleProperty opponentShotHitAmount;

	private SimpleDoubleProperty opponentShotTotalAmount;

	private SimpleBooleanProperty shotFired = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty shipHitProperty = new SimpleBooleanProperty(false);

	private SimpleDoubleProperty boat5amount = new SimpleDoubleProperty(0); // Carrier
	private SimpleDoubleProperty boat4amount = new SimpleDoubleProperty(0); // Battleship
	private SimpleDoubleProperty boat3amount = new SimpleDoubleProperty(0); // Cruiser
	private SimpleDoubleProperty boat2amount = new SimpleDoubleProperty(0); // Submarine
	private SimpleDoubleProperty boat1amount = new SimpleDoubleProperty(0); // Destroyer

	// BRUTE-FORCELLA HOMMAT HOITUU ...

	private SimpleBooleanProperty boat5down1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5down1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5down1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5down1hit4 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5down1hit5 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down1hit4 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down2hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4down2hit4 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down2hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down3hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down3hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3down3hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down2hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down3hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down3hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down3hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down4hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down4hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2down4hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down3hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down3hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down4hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down4hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down5hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1down5hit2 = new SimpleBooleanProperty(false);

	// NYT ON TOSI BRUTE

	private SimpleBooleanProperty boat5up1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5up1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5up1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5up1hit4 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat5up1hit5 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up1hit4 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up2hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat4up2hit4 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up2hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up3hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up3hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat3up3hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up1hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up2hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up3hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up3hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up3hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up4hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up4hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat2up4hit3 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up1hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up1hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up2hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up2hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up3hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up3hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up4hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up4hit2 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up5hit1 = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty boat1up5hit2 = new SimpleBooleanProperty(false);

	@FXML
	private VBox left_settings;

	@FXML
	private Label playerName;

	@FXML
	private Label playerStatisticsHit;

	@FXML
	private Label playerStatisticsMiss;

	@FXML
	private Label playerStatisticsTotal;

	@FXML
	private Label playerStatisticsAccuracy;

	@FXML
	private Label opponentName;

	@FXML
	private Label opponentStatisticsHit;

	@FXML
	private Label opponentStatisticsMiss;

	@FXML
	private Label opponentStatisticsTotal;

	@FXML
	private Label opponentStatisticsAccuracy;

	@FXML
	private HBox boat5up1;

	@FXML
	private HBox boat4up1;

	@FXML
	private HBox boat4up2;

	@FXML
	private HBox boat3up1;

	@FXML
	private HBox boat3up2;

	@FXML
	private HBox boat3up3;

	@FXML
	private HBox boat2up1;

	@FXML
	private HBox boat2up2;

	@FXML
	private HBox boat2up3;

	@FXML
	private HBox boat2up4;

	@FXML
	private HBox boat1up1;

	@FXML
	private HBox boat1up2;

	@FXML
	private HBox boat1up3;

	@FXML
	private HBox boat1up4;

	@FXML
	private HBox boat1up5;

	@FXML
	private Button boat5up1button1;

	@FXML
	private Button boat5up1button2;

	@FXML
	private Button boat5up1button3;

	@FXML
	private Button boat5up1button4;

	@FXML
	private Button boat5up1button5;

	@FXML
	private Button boat4up1button1;

	@FXML
	private Button boat4up1button2;

	@FXML
	private Button boat4up1button3;

	@FXML
	private Button boat4up1button4;

	@FXML
	private Button boat4up2button1;

	@FXML
	private Button boat4up2button2;

	@FXML
	private Button boat4up2button3;

	@FXML
	private Button boat4up2button4;

	@FXML
	private Button boat3up1button1;

	@FXML
	private Button boat3up1button2;

	@FXML
	private Button boat3up1button3;

	@FXML
	private Button boat3up2button1;

	@FXML
	private Button boat3up2button2;

	@FXML
	private Button boat3up2button3;

	@FXML
	private Button boat3up3button1;

	@FXML
	private Button boat3up3button2;

	@FXML
	private Button boat3up3button3;

	@FXML
	private Button boat2up1button1;

	@FXML
	private Button boat2up1button2;

	@FXML
	private Button boat2up1button3;

	@FXML
	private Button boat2up2button1;

	@FXML
	private Button boat2up2button2;

	@FXML
	private Button boat2up2button3;

	@FXML
	private Button boat2up3button1;

	@FXML
	private Button boat2up3button2;

	@FXML
	private Button boat2up3button3;

	@FXML
	private Button boat2up4button1;

	@FXML
	private Button boat2up4button2;

	@FXML
	private Button boat2up4button3;

	@FXML
	private Button boat1up1button1;

	@FXML
	private Button boat1up1button2;

	@FXML
	private Button boat1up2button1;

	@FXML
	private Button boat1up2button2;

	@FXML
	private Button boat1up3button1;

	@FXML
	private Button boat1up3button2;

	@FXML
	private Button boat1up4button1;

	@FXML
	private Button boat1up4button2;

	@FXML
	private Button boat1up5button1;

	@FXML
	private Button boat1up5button2;

	@FXML
	private HBox boat5down1;

	@FXML
	private HBox boat4down1;

	@FXML
	private HBox boat4down2;

	@FXML
	private HBox boat3down1;

	@FXML
	private HBox boat3down2;

	@FXML
	private HBox boat3down3;

	@FXML
	private HBox boat2down1;

	@FXML
	private HBox boat2down2;

	@FXML
	private HBox boat2down3;

	@FXML
	private HBox boat2down4;

	@FXML
	private HBox boat1down1;

	@FXML
	private HBox boat1down2;

	@FXML
	private HBox boat1down3;

	@FXML
	private HBox boat1down4;

	@FXML
	private HBox boat1down5;

	@FXML
	private Button boat5down1button1;

	@FXML
	private Button boat5down1button2;

	@FXML
	private Button boat5down1button3;

	@FXML
	private Button boat5down1button4;

	@FXML
	private Button boat5down1button5;

	@FXML
	private Button boat4down1button1;

	@FXML
	private Button boat4down1button2;

	@FXML
	private Button boat4down1button3;

	@FXML
	private Button boat4down1button4;

	@FXML
	private Button boat4down2button1;

	@FXML
	private Button boat4down2button2;

	@FXML
	private Button boat4down2button3;

	@FXML
	private Button boat4down2button4;

	@FXML
	private Button boat3down1button1;

	@FXML
	private Button boat3down1button2;

	@FXML
	private Button boat3down1button3;

	@FXML
	private Button boat3down2button1;

	@FXML
	private Button boat3down2button2;

	@FXML
	private Button boat3down2button3;

	@FXML
	private Button boat3down3button1;

	@FXML
	private Button boat3down3button2;

	@FXML
	private Button boat3down3button3;

	@FXML
	private Button boat2down1button1;

	@FXML
	private Button boat2down1button2;

	@FXML
	private Button boat2down1button3;

	@FXML
	private Button boat2down2button1;

	@FXML
	private Button boat2down2button2;

	@FXML
	private Button boat2down2button3;

	@FXML
	private Button boat2down3button1;

	@FXML
	private Button boat2down3button2;

	@FXML
	private Button boat2down3button3;

	@FXML
	private Button boat2down4button1;

	@FXML
	private Button boat2down4button2;

	@FXML
	private Button boat2down4button3;

	@FXML
	private Button boat1down1button1;

	@FXML
	private Button boat1down1button2;

	@FXML
	private Button boat1down2button1;

	@FXML
	private Button boat1down2button2;

	@FXML
	private Button boat1down3button1;

	@FXML
	private Button boat1down3button2;

	@FXML
	private Button boat1down4button1;

	@FXML
	private Button boat1down4button2;

	@FXML
	private Button boat1down5button1;

	@FXML
	private Button boat1down5button2;

	public GameUIstatisticsController(SimpleStringProperty informationTextIN,
			SimpleStringProperty informationTextColorIN, Game currentGameIn, PlayerNumber plrNumberXin,
			MasterController masterControllerIN, Playfield ownPlayfieldIN, Playfield opponentPlayfieldIN) {

		informationText = informationTextIN;
		informationTextColor = informationTextColorIN;
		currentGame = currentGameIn;
		plrNumberX = plrNumberXin;
		masterController = masterControllerIN;
		ownPlayfield = ownPlayfieldIN;
		ownPlayfieldGridPane = ownPlayfieldIN.getPlayfieldGridPane();
		opponentPlayfield = opponentPlayfieldIN;
		opponentPlayfieldGridPane = opponentPlayfieldIN.getOpponentView();

		ownPlayfield.setOwnGameUIStatisticsController(this);
		opponentPlayfield.setOpponentGameUIStatisticsController(this);

	}

	/**
	 * Kontrollerin käynnistys
	 * 
	 * @throws IOException
	 */
	@FXML
	private void initialize() throws IOException {

		System.out.println("debug (GameUIstatisticsController) initialize: " + plrNumberX);

		// Asetetaan pelaajien nimet/tiedot

		// Pelaaja 1
		if (plrNumberX.equals(PlayerNumber.PLAYER1)) {
			masterController.setPlayer1GameUIStatisticsController(this);

			ownFleet = masterController.getPlayer1Fleet();
			opponentFleet = masterController.getPlayer2Fleet();

			playerNameSimple.setValue(currentGame.getPlayer1().getName());
			opponentNameSimple.setValue(currentGame.getPlayer2().getName());

			ownShotHitAmount = masterController.getPlayer1ShotHitAmount();
			ownShotTotalAmount = masterController.getPlayer1ShotTotalAmount();

			opponentShotHitAmount = masterController.getPlayer2ShotHitAmount();
			opponentShotTotalAmount = masterController.getPlayer2ShotTotalAmount();

			// System.out.println("debug (GameUIstatisticsController) initialize: names
			// player/opponent: "
			// + currentGame.getPlayer1().getName() + "/" +
			// currentGame.getPlayer2().getName());

			// Pelaaja 2
		} else if (plrNumberX.equals(PlayerNumber.PLAYER2)) {
			masterController.setPlayer2GameUIStatisticsController(this);

			ownFleet = masterController.getPlayer2Fleet();
			opponentFleet = masterController.getPlayer1Fleet();

			playerNameSimple.setValue(currentGame.getPlayer2().getName());
			opponentNameSimple.setValue(currentGame.getPlayer1().getName());

			ownShotHitAmount = masterController.getPlayer2ShotHitAmount();
			ownShotTotalAmount = masterController.getPlayer2ShotTotalAmount();

			opponentShotHitAmount = masterController.getPlayer1ShotHitAmount();
			opponentShotTotalAmount = masterController.getPlayer1ShotTotalAmount();

			// System.out.println("debug (GameUIstatisticsController) initialize: names
			// player/opponent: "
			// + currentGame.getPlayer2().getName() + "/" +
			// currentGame.getPlayer1().getName());
		} else {
			System.out.println("GUIStatisticsController Constructor: Invalid PlayerNr value");
		}

		ownAllShipsList = ownFleet.getAllShipsList();
		opponentAllShipsList = opponentFleet.getAllShipsList();

		// Älä siirrä näitä alemmas:
		boat1amount.setValue(currentGame.getVesselAmounts()[0]);
		boat2amount.setValue(currentGame.getVesselAmounts()[1]);
		boat3amount.setValue(currentGame.getVesselAmounts()[2]);
		boat4amount.setValue(currentGame.getVesselAmounts()[3]);
		boat5amount.setValue(currentGame.getVesselAmounts()[4]);

		ownBoat5List = ownFleet.getCarrierList();
		ownBoat4List = ownFleet.getBattleshipList();
		ownBoat3List = ownFleet.getCruiserList();
		ownBoat2List = ownFleet.getSubmarineList();
		ownBoat1List = ownFleet.getDestroyerList();

		opponentBoat5List = opponentFleet.getCarrierList();
		opponentBoat4List = opponentFleet.getBattleshipList();
		opponentBoat3List = opponentFleet.getCruiserList();
		opponentBoat2List = opponentFleet.getSubmarineList();
		opponentBoat1List = opponentFleet.getDestroyerList();

		//System.out.println("debug (GameUIstatisticsController) initialize: " + " Fleet content: "
		//		+ boat5amount.intValue() + "/" + boat4amount.intValue() + "/" + boat3amount.intValue() + "/"
		//		+ boat2amount.intValue() + "/" + boat1amount.intValue());

		setBindings(); // reaktiiviset sidonnat

	}

	/**
	 * Onko ammuttu tällä vuorolla?
	 * 
	 * @return
	 */
	public SimpleBooleanProperty getShotFired() {
		return shotFired;
	}

	/**
	 * Asetetaan vastustajan peliruudukko, jota ammutaan
	 * 
	 * @param opponentGridPaneIN
	 */
	public void setOpponentPlayfieldGridPane(GridPane opponentGridPaneIN) {
		opponentPlayfieldGridPane = opponentGridPaneIN;
	}

	/**
	 * Asetetaan ohjetekstin sisältö ja väri
	 * 
	 * @param informationTextIN
	 * @param informationTextColorIN
	 */
	public void setInformationTextAndColor(SimpleStringProperty informationTextIN,
			SimpleStringProperty informationTextColorIN) {
		this.informationText = informationTextIN;
		this.informationTextColor = informationTextColorIN;
	}

	/**
	 * Kontrollerin toiminnot, kun peliruutua on ammuttu Vastustajan Playfield-olio
	 * kutsuu tätä metodia, kun peliruutua ammutaan siellä.
	 * 
	 * @param shipHit
	 */
	public void setOnShotFired(boolean shipHit) {

		if (plrNumberX.equals(PlayerNumber.PLAYER1)) {
			// Lasketaan osumien ja laukausten määrät
			if (shipHit) { // Osuma
				masterController.addPlayer1ShotHitAmount();
				masterController.addPlayer1ShotTotalAmount();

			} else { // Ei osumaa
				masterController.addPlayer1ShotTotalAmount();
			}

			ownShotHitAmount = masterController.getPlayer1ShotHitAmount();
			ownShotTotalAmount = masterController.getPlayer1ShotTotalAmount();

			opponentShotHitAmount = masterController.getPlayer2ShotHitAmount();
			opponentShotTotalAmount = masterController.getPlayer2ShotTotalAmount();

			// System.out.println("debug (GameUIstatisticsController) setOnShotFired - plr1
			// - Shots: " + ownShotTotalAmount.toString());

		}

		if (plrNumberX.equals(PlayerNumber.PLAYER2)) {
			// Lasketaan osumien ja laukausten määrät
			if (shipHit) { // Osuma
				masterController.addPlayer2ShotHitAmount();
				masterController.addPlayer2ShotTotalAmount();
			} else { // Ei osumaa
				masterController.addPlayer2ShotTotalAmount();
			}

			ownShotHitAmount = masterController.getPlayer2ShotHitAmount();
			ownShotTotalAmount = masterController.getPlayer2ShotTotalAmount();

			opponentShotHitAmount = masterController.getPlayer1ShotHitAmount();
			opponentShotTotalAmount = masterController.getPlayer1ShotTotalAmount();

			// System.out.println("debug (GameUIstatisticsController) setOnShotFired - plr2
			// - Shots: " + ownShotTotalAmount.toString());
		}

		// Samalla vuorolla ei voi ampua toista kertaa
		opponentPlayfield.setShootingBlocked(true);

		// ammuttu jo tällä vuorolla
		shotFired.setValue(true);

		System.out.println("debug (GameUIstatisticsController setOnShotFired) " + this.plrNumberX + " shipHit: " + shipHit);
		// System.out.println("debug (GameUIstatisticsController setOnShotFired):
		// ControllerClass opponentPlayfield equals calling Playfield object "
		// + testOpponentPlayfield.equals(opponentPlayfield));

		// informationTextColor.setValue("black");

		// Pelivuoro vaihtuu kolmen sekunnin viiveellä, kun laukaus on ammuttu
		Task<Void> clockTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {

				// System.out.println("debug (GameUIstatisticsController) setOnShotFired: clockTask
				// call() method");

				try {

					boolean allShipsSunk = true;

					opponentPlayfield.setShootingBlocked(false);
					
					//Tarkistetaan onko boat5 aluksia uponnut. Mikäli uponnut -> koko aluksen väri punaiseksi
					if(opponentBoat5List.size() > 0) {
						 //System.out.println("debug (GameUIstatisticsController) setOnShotFired boat5:" + opponentBoat5List.size());
						 for(Ship boat: opponentBoat5List) {
							 colorVesselRedIfSunk(boat.getShipBody()); 
						 }
					}
					
					//Tarkistetaan onko boat4 aluksia uponnut. Mikäli uponnut -> koko aluksen väri punaiseksi
					if(opponentBoat4List.size() > 0) {
						 //System.out.println("debug (GameUIstatisticsController) setOnShotFired boat4:" + opponentBoat4List.size());
						 for(Ship boat: opponentBoat4List) {
							 colorVesselRedIfSunk(boat.getShipBody()); 
						 }
					}
					
					//Tarkistetaan onko boat3 aluksia uponnut. Mikäli uponnut -> koko aluksen väri punaiseksi
					if(opponentBoat3List.size() > 0) {
						 //System.out.println("debug (GameUIstatisticsController) setOnShotFired boat3:" + opponentBoat3List.size());
						 for(Ship boat: opponentBoat3List) {
							 colorVesselRedIfSunk(boat.getShipBody()); 
						 }
					}
					
					//Tarkistetaan onko boat2 aluksia uponnut. Mikäli uponnut -> koko aluksen väri punaiseksi
					if(opponentBoat2List.size() > 0) {
						 //System.out.println("debug (GameUIstatisticsController) setOnShotFired boat2:" + opponentBoat2List.size());
						 for(Ship boat: opponentBoat2List) {
							 colorVesselRedIfSunk(boat.getShipBody()); 
						 }
					}
					
					//Tarkistetaan onko boat1 aluksia uponnut. Mikäli uponnut -> koko aluksen väri punaiseksi
					if(opponentBoat1List.size() > 0) {
						 //System.out.println("debug (GameUIstatisticsController) setOnShotFired boat1:" + opponentBoat1List.size());
						 for(Ship boat: opponentBoat1List) {
							 colorVesselRedIfSunk(boat.getShipBody()); 
						 }
					}
										
					// Tarkistetaan, onko kaikki vastustajan alukset upotettu
					// Pelaaja 1:n vuoro
					if (currentGame.getGameState().equals(GameState.PLR1TURN)) {
				
						for (Ship s : opponentAllShipsList) {
							if (s.isSunk() == false) {
								allShipsSunk = false;
							}
						}
						System.out.println("debug (GameUIstatisticsController) PLR1 opponent allShipsSunk: " + allShipsSunk);

						// jos kaikki vastustajan alukset ei upotettu, peli jatkuu
						if (allShipsSunk == false) {

							Platform.runLater(() -> masterController.nextTurnPlayer2(currentGame));

							// jos kaikki vastustajan alukset on upotettu, peli päättyy tämän pelaajan
							// voittoon
						} else {
							gameContinues = false;

//							System.out.println("(debug) GAME OVER !!!!  Player 1 won");
							
							opponentAllShipsList.clear();
							ownAllShipsList.clear();

							Platform.runLater(() -> masterController.gameOver(currentGame, PlayerNumber.PLAYER1));

							// POPUP-IKKUNA, PELI PÄÄTTYY

						}

						// Pelaaja 2:n vuoro, muuten sama kuin yllä
					} else if (currentGame.getGameState().equals(GameState.PLR2TURN)) {

						for (Ship s : opponentAllShipsList) {
							if (s.isSunk() == false) {
								allShipsSunk = false;
							}
						}
						
						System.out.println("debug (GameUIstatisticsController) PLR2 opponent allShipsSunk: " + allShipsSunk);

						if (allShipsSunk == false) {
							Platform.runLater(() -> masterController.nextTurnPlayer1(currentGame));

						} else {
							gameContinues = false;

//							System.out.println("(debug) GAME OVER !!!!  Player 2 won");
							
							opponentAllShipsList.clear();
							ownAllShipsList.clear();

							Platform.runLater(() -> masterController.gameOver(currentGame, PlayerNumber.PLAYER2));

							// POPUP-IKKUNA, PELI PÄÄTTYY

						}

					} else {
						System.out.println("GUIStatisticsController: Invalid GameState");
					}

//					Platform.runLater(masterController.nextTurnPlayer2(currentGame));				
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

		};

		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

		if (shipHit == false) {
			shipHitProperty.setValue(false);
		} else {
			shipHitProperty.setValue(true);
		}

		// käynnistää 3 sekunnin ajastimen
		exec.schedule(clockTask, 3, TimeUnit.SECONDS);

	}
	
	/**
	 * Värjätään aluksen ruudut pelikentällä punaiseksi, mikäli se on uponnut.
	 * @param shipBodyList
	 */
	private void colorVesselRedIfSunk(ArrayList<GameSquare> shipBody) {
		
		ArrayList<Boolean> gsHits = new ArrayList<Boolean>();
		for(GameSquare gs: shipBody) {
			if(gs.getIsHit() == true) {
				gsHits.add(true);
			} else {
				gsHits.add(false);
			}
		}
		if(!gsHits.contains(false)) {
			for(GameSquare gs: shipBody) {
				gs.setStyle("-fx-color: red");
			} 
		}
	}

	/**
	 * Reaktiiviset sidokset
	 */
	private void setBindings() {

		playerName.textProperty().bind(playerNameSimple);
		opponentName.textProperty().bind(opponentNameSimple);

		// ohjeteksti
		informationText.bind(Bindings.when(shotFired.not()).then("Aim and fire your missile.").otherwise(Bindings
				.when(shipHitProperty.not()).then("You did not hit any vessels.").otherwise("You hit an enemy ship!")));

		// ohjetekstin väri
		informationTextColor.bind(Bindings.when(shotFired.not()).then("red").otherwise("black"));

		StringConverter<Number> converter = new NumberStringConverter();
		// Converter related Bindings

		// ampumistilastojen päivittyminen
		Bindings.bindBidirectional(playerStatisticsHit.textProperty(), ownShotHitAmount, converter);
		Bindings.bindBidirectional(playerStatisticsTotal.textProperty(), ownShotTotalAmount, converter);

		Bindings.bindBidirectional(opponentStatisticsHit.textProperty(), opponentShotHitAmount, converter);
		Bindings.bindBidirectional(opponentStatisticsTotal.textProperty(), opponentShotTotalAmount, converter);

		// Desimaalin poisto:
		// https://stackoverflow.com/questions/31373527/javafx-formatting-doubleproperty
		playerStatisticsMiss.textProperty().bind(ownShotTotalAmount.subtract(ownShotHitAmount).asString("%.0f"));
		opponentStatisticsMiss.textProperty()
				.bind(opponentShotTotalAmount.subtract(opponentShotHitAmount).asString("%.0f"));

		playerStatisticsAccuracy.textProperty().bind(Bindings.when(ownShotTotalAmount.isEqualTo(0)).then("0.0")
				.otherwise(ownShotHitAmount.divide(ownShotTotalAmount).multiply(100.0).asString("%.1f")));
		opponentStatisticsAccuracy.textProperty().bind(Bindings.when(opponentShotTotalAmount.isEqualTo(0)).then("0.0")
				.otherwise(opponentShotHitAmount.divide(opponentShotTotalAmount).multiply(100.0).asString("%.1f")));

		// harmaaksi ne laivat vasemmassa paneelissa, jotka eivät ole mukana pelissä
		// BOAT1
		boat5up1.disableProperty().bind(Bindings.when(boat5amount.isEqualTo(0)).then(true).otherwise(false));
		boat5down1.disableProperty().bind(Bindings.when(boat5amount.isEqualTo(0)).then(true).otherwise(false));

		// BOAT2
		boat4up2.disableProperty()
				.bind(Bindings.when(boat4amount.isEqualTo(0).or(boat4amount.isEqualTo(1))).then(true).otherwise(false));
		boat4down2.disableProperty()
				.bind(Bindings.when(boat4amount.isEqualTo(0).or(boat4amount.isEqualTo(1))).then(true).otherwise(false));

		boat4up1.disableProperty().bind(Bindings.when(boat4amount.isEqualTo(0)).then(true).otherwise(false));
		boat4down1.disableProperty().bind(Bindings.when(boat4amount.isEqualTo(0)).then(true).otherwise(false));

		// BOAT3
		// Rivin viimeinen alus, jos määrä on 0,1,2 -> disabled
		boat3up3.disableProperty()
				.bind(Bindings.when(boat3amount.isEqualTo(0).or(boat3amount.isEqualTo(1)).or(boat3amount.isEqualTo(2)))
						.then(true).otherwise(false));
		boat3down3.disableProperty()
				.bind(Bindings.when(boat3amount.isEqualTo(0).or(boat3amount.isEqualTo(1)).or(boat3amount.isEqualTo(2)))
						.then(true).otherwise(false));
		// Rivin keskimmäinen alus, jos määrä on 0,1 -> disabled
		boat3up2.disableProperty()
				.bind(Bindings.when(boat3amount.isEqualTo(0).or(boat3amount.isEqualTo(1))).then(true).otherwise(false));
		boat3down2.disableProperty()
				.bind(Bindings.when(boat3amount.isEqualTo(0).or(boat3amount.isEqualTo(1))).then(true).otherwise(false));
		// Rivin ensimmäinen alus, jos määrä on 0 -> disabled
		boat3up1.disableProperty().bind(Bindings.when(boat3amount.isEqualTo(0)).then(true).otherwise(false));
		boat3down1.disableProperty().bind(Bindings.when(boat3amount.isEqualTo(0)).then(true).otherwise(false));

		// BOAT4
		// Rivin viimeinen alus, jos määrä on 0,1,2,3 -> disabled
		boat2up4.disableProperty().bind(Bindings.when(boat2amount.isEqualTo(0).or(boat2amount.isEqualTo(1))
				.or(boat2amount.isEqualTo(2)).or(boat2amount.isEqualTo(3))).then(true).otherwise(false));
		boat2down4.disableProperty().bind(Bindings.when(boat2amount.isEqualTo(0).or(boat2amount.isEqualTo(1))
				.or(boat2amount.isEqualTo(2)).or(boat2amount.isEqualTo(3))).then(true).otherwise(false));

		boat2up3.disableProperty()
				.bind(Bindings.when(boat2amount.isEqualTo(0).or(boat2amount.isEqualTo(1)).or(boat2amount.isEqualTo(2)))
						.then(true).otherwise(false));
		boat2down3.disableProperty()
				.bind(Bindings.when(boat2amount.isEqualTo(0).or(boat2amount.isEqualTo(1)).or(boat2amount.isEqualTo(2)))
						.then(true).otherwise(false));

		boat2up2.disableProperty()
				.bind(Bindings.when(boat2amount.isEqualTo(0).or(boat2amount.isEqualTo(1))).then(true).otherwise(false));
		boat2down2.disableProperty()
				.bind(Bindings.when(boat2amount.isEqualTo(0).or(boat2amount.isEqualTo(1))).then(true).otherwise(false));
		// Rivin ensimmäinen alus, jos määrä on 0 -> disabled
		boat2up1.disableProperty().bind(Bindings.when(boat2amount.isEqualTo(0)).then(true).otherwise(false));
		boat2down1.disableProperty().bind(Bindings.when(boat2amount.isEqualTo(0)).then(true).otherwise(false));

		// BOAT5
		// Rivin viimeinen alus, jos määrä on 0,1,2,3,5 -> disabled
		boat1up5.disableProperty()
				.bind(Bindings
						.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1)).or(boat1amount.isEqualTo(2))
								.or(boat1amount.isEqualTo(3)).or(boat1amount.isEqualTo(4)))
						.then(true).otherwise(false));
		boat1down5.disableProperty()
				.bind(Bindings
						.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1)).or(boat1amount.isEqualTo(2))
								.or(boat1amount.isEqualTo(3)).or(boat1amount.isEqualTo(4)))
						.then(true).otherwise(false));

		boat1up4.disableProperty().bind(Bindings.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1))
				.or(boat1amount.isEqualTo(2)).or(boat1amount.isEqualTo(3))).then(true).otherwise(false));
		boat1down4.disableProperty().bind(Bindings.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1))
				.or(boat1amount.isEqualTo(2)).or(boat1amount.isEqualTo(3))).then(true).otherwise(false));
		// Rivin keskimmäinen alus, jos määrä on 0,1,2 -> disabled
		boat1up3.disableProperty()
				.bind(Bindings.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1)).or(boat1amount.isEqualTo(2)))
						.then(true).otherwise(false));
		boat1down3.disableProperty()
				.bind(Bindings.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1)).or(boat1amount.isEqualTo(2)))
						.then(true).otherwise(false));

		boat1up2.disableProperty()
				.bind(Bindings.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1))).then(true).otherwise(false));
		boat1down2.disableProperty()
				.bind(Bindings.when(boat1amount.isEqualTo(0).or(boat1amount.isEqualTo(1))).then(true).otherwise(false));
		// Rivin ensimmäinen alus, jos määrä on 0 -> disabled
		boat1up1.disableProperty().bind(Bindings.when(boat1amount.isEqualTo(0)).then(true).otherwise(false));
		boat1down1.disableProperty().bind(Bindings.when(boat1amount.isEqualTo(0)).then(true).otherwise(false));

		// omat alukset
		ownBoat5List = ownFleet.getCarrierList();
		ownBoat4List = ownFleet.getBattleshipList();
		ownBoat3List = ownFleet.getCruiserList();
		ownBoat2List = ownFleet.getSubmarineList();
		ownBoat1List = ownFleet.getDestroyerList();

		/*
		 * 
		 * JA LOPPU ONKIN SITTEN BRUTE-FORCEA !!!
		 * 
		 */

		// Boat5 - Carrier - Button oranssiksi, kun siihen on osuttu. Muutetaan
		// binäärimuuttujan tilaa osuma merkiksi.
		if (boat5amount.intValue() == 1) {
			for (int k = 0; k < boat5amount.intValue(); k++) {
				for (int i = 0; i < 5; i++) {
					if (i == 0 && ownBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5down1button1.setStyle("-fx-color: orange");
						boat5down1hit1.setValue(true);
					}
					if (i == 1 && ownBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5down1button2.setStyle("-fx-color: orange");
						boat5down1hit2.setValue(true);
					}
					if (i == 2 && ownBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5down1button3.setStyle("-fx-color: orange");
						boat5down1hit3.setValue(true);
					}
					if (i == 3 && ownBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5down1button4.setStyle("-fx-color: orange");
						boat5down1hit4.setValue(true);
					}
					if (i == 4 && ownBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5down1button5.setStyle("-fx-color: orange");
						boat5down1hit5.setValue(true);
					}

					if (i == 0 && opponentBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5up1hit1.setValue(true);
					}
					if (i == 1 && opponentBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5up1hit2.setValue(true);
					}
					if (i == 2 && opponentBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5up1hit3.setValue(true);
					}
					if (i == 3 && opponentBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5up1hit4.setValue(true);
					}
					if (i == 4 && opponentBoat5List.get(k).getShipBody().get(i).getIsHit()) {
						boat5up1hit5.setValue(true);
					}
				}

			}
		}

		// Boat5 - Carrier - Buttonit punaisiksi, kun niihin kaikkiin on osuttu
		if (boat5down1hit1.getValue() && boat5down1hit2.getValue() && boat5down1hit3.getValue()
				&& boat5down1hit4.getValue() && boat5down1hit5.getValue()) {
			boat5down1button1.setStyle("-fx-color: red");
			boat5down1button2.setStyle("-fx-color: red");
			boat5down1button3.setStyle("-fx-color: red");
			boat5down1button4.setStyle("-fx-color: red");
			boat5down1button5.setStyle("-fx-color: red");
		}

		if (boat5up1hit1.getValue() && boat5up1hit2.getValue() && boat5up1hit3.getValue() && boat5up1hit4.getValue()
				&& boat5up1hit5.getValue()) {
			boat5up1button1.setStyle("-fx-color: red");
			boat5up1button2.setStyle("-fx-color: red");
			boat5up1button3.setStyle("-fx-color: red");
			boat5up1button4.setStyle("-fx-color: red");
			boat5up1button5.setStyle("-fx-color: red");
		}

		// Boat4 - Battleship - Button oranssiksi, kun siihen on osuttu. Muutetaan
		// binäärimuuttujan tilaa osuma merkiksi.
		if (boat4amount.intValue() == 1) {
			for (int k = 0; k < boat4amount.intValue(); k++) {
				for (int i = 0; i < 4; i++) {
					if (i == 0 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4down1button1.setStyle("-fx-color: orange");
						boat4down1hit1.setValue(true);
					}
					if (i == 1 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4down1button2.setStyle("-fx-color: orange");
						boat4down1hit2.setValue(true);
					}
					if (i == 2 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4down1button3.setStyle("-fx-color: orange");
						boat4down1hit3.setValue(true);
					}
					if (i == 3 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4down1button4.setStyle("-fx-color: orange");
						boat4down1hit4.setValue(true);
					}

					if (i == 0 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4up1hit1.setValue(true);
					}
					if (i == 1 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4up1hit2.setValue(true);
					}
					if (i == 2 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4up1hit3.setValue(true);
					}
					if (i == 3 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
						boat4up1hit4.setValue(true);
					}
				}
			}
		}

		if (boat4amount.intValue() == 2) {
			for (int k = 0; k < boat4amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 4; i++) {
						if (i == 0 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down1button1.setStyle("-fx-color: orange");
							boat4down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down1button2.setStyle("-fx-color: orange");
							boat4down1hit2.setValue(true);
						}
						if (i == 2 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down1button3.setStyle("-fx-color: orange");
							boat4down1hit3.setValue(true);
						}
						if (i == 3 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down1button4.setStyle("-fx-color: orange");
							boat4down1hit4.setValue(true);
						}

						if (i == 0 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up1hit2.setValue(true);
						}
						if (i == 2 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up1hit3.setValue(true);
						}
						if (i == 3 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up1hit4.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 4; i++) {
						if (i == 0 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down2button1.setStyle("-fx-color: orange");
							boat4down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down2button2.setStyle("-fx-color: orange");
							boat4down2hit2.setValue(true);
						}
						if (i == 2 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down2button3.setStyle("-fx-color: orange");
							boat4down2hit3.setValue(true);
						}
						if (i == 3 && ownBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4down2button4.setStyle("-fx-color: orange");
							boat4down2hit4.setValue(true);
						}

						if (i == 0 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up2hit2.setValue(true);
						}
						if (i == 2 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up2hit3.setValue(true);
						}
						if (i == 3 && opponentBoat4List.get(k).getShipBody().get(i).getIsHit()) {
							boat4up2hit4.setValue(true);
						}
					}

				}
			}
		}

		// Boat4 - Battleship - Buttonit punaisiksi, kun niihin kaikkiin on osuttu
		if (boat4down1hit1.getValue() && boat4down1hit2.getValue() && boat4down1hit3.getValue()
				&& boat4down1hit4.getValue()) {
			boat4down1button1.setStyle("-fx-color: red");
			boat4down1button2.setStyle("-fx-color: red");
			boat4down1button3.setStyle("-fx-color: red");
			boat4down1button4.setStyle("-fx-color: red");
		}

		if (boat4up1hit1.getValue() && boat4up1hit2.getValue() && boat4up1hit3.getValue() && boat4up1hit4.getValue()) {
			boat4up1button1.setStyle("-fx-color: red");
			boat4up1button2.setStyle("-fx-color: red");
			boat4up1button3.setStyle("-fx-color: red");
			boat4up1button4.setStyle("-fx-color: red");
		}

		if (boat4down2hit1.getValue() && boat4down2hit2.getValue() && boat4down2hit3.getValue()
				&& boat4down2hit4.getValue()) {
			boat4down2button1.setStyle("-fx-color: red");
			boat4down2button2.setStyle("-fx-color: red");
			boat4down2button3.setStyle("-fx-color: red");
			boat4down2button4.setStyle("-fx-color: red");
		}

		if (boat4up2hit1.getValue() && boat4up2hit2.getValue() && boat4up2hit3.getValue() && boat4up2hit4.getValue()) {
			boat4up2button1.setStyle("-fx-color: red");
			boat4up2button2.setStyle("-fx-color: red");
			boat4up2button3.setStyle("-fx-color: red");
			boat4up2button4.setStyle("-fx-color: red");
		}

		// Boat3 - Cruiser - Button oranssiksi, kun siihen on osuttu. Muutetaan
		// binäärimuuttujan tilaa osuma merkiksi.
		if (boat3amount.intValue() == 1) {
			for (int k = 0; k < boat3amount.intValue(); k++) {
				for (int i = 0; i < 3; i++) {
					if (i == 0 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
						boat3down1button1.setStyle("-fx-color: orange");
						boat3down1hit1.setValue(true);
					}
					if (i == 1 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
						boat3down1button2.setStyle("-fx-color: orange");
						boat3down1hit2.setValue(true);
					}
					if (i == 2 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
						boat3down1button3.setStyle("-fx-color: orange");
						boat3down1hit3.setValue(true);
					}

					if (i == 0 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
						boat3up1hit1.setValue(true);
					}
					if (i == 1 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
						boat3up1hit2.setValue(true);
					}
					if (i == 2 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
						boat3up1hit3.setValue(true);
					}
				}
			}
		}

		if (boat3amount.intValue() == 2) {
			for (int k = 0; k < boat3amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down1button1.setStyle("-fx-color: orange");
							boat3down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down1button2.setStyle("-fx-color: orange");
							boat3down1hit2.setValue(true);
						}
						if (i == 2 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down1button3.setStyle("-fx-color: orange");
							boat3down1hit3.setValue(true);
						}

						if (i == 0 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up1hit2.setValue(true);
						}
						if (i == 2 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up1hit3.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down2button1.setStyle("-fx-color: orange");
							boat3down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down2button2.setStyle("-fx-color: orange");
							boat3down2hit2.setValue(true);
						}
						if (i == 2 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down2button3.setStyle("-fx-color: orange");
							boat3down2hit3.setValue(true);
						}

						if (i == 0 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up2hit2.setValue(true);
						}
						if (i == 2 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up2hit3.setValue(true);
						}
					}
				}
			}
		}

		if (boat3amount.intValue() == 3) {
			for (int k = 0; k < boat3amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down1button1.setStyle("-fx-color: orange");
							boat3down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down1button2.setStyle("-fx-color: orange");
							boat3down1hit2.setValue(true);
						}
						if (i == 2 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down1button3.setStyle("-fx-color: orange");
							boat3down1hit3.setValue(true);
						}

						if (i == 0 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up1hit2.setValue(true);
						}
						if (i == 2 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up1hit3.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down2button1.setStyle("-fx-color: orange");
							boat3down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down2button2.setStyle("-fx-color: orange");
							boat3down2hit2.setValue(true);
						}
						if (i == 2 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down2button3.setStyle("-fx-color: orange");
							boat3down2hit3.setValue(true);
						}

						if (i == 0 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up2hit2.setValue(true);
						}
						if (i == 2 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up2hit3.setValue(true);
						}
					}

				} else if (k == 2) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down3button1.setStyle("-fx-color: orange");
							boat3down3hit1.setValue(true);
						}
						if (i == 1 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down3button2.setStyle("-fx-color: orange");
							boat3down3hit2.setValue(true);
						}
						if (i == 2 && ownBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3down3button3.setStyle("-fx-color: orange");
							boat3down3hit3.setValue(true);
						}

						if (i == 0 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up3hit1.setValue(true);
						}
						if (i == 1 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up3hit2.setValue(true);
						}
						if (i == 2 && opponentBoat3List.get(k).getShipBody().get(i).getIsHit()) {
							boat3up3hit3.setValue(true);
						}
					}
				}
			}
		}

		// Boat3 - Cruiser - Buttonit punaisiksi, kun niihin kaikkiin on osuttu
		if (boat3down1hit1.getValue() && boat3down1hit2.getValue() && boat3down1hit3.getValue()) {
			boat3down1button1.setStyle("-fx-color: red");
			boat3down1button2.setStyle("-fx-color: red");
			boat3down1button3.setStyle("-fx-color: red");
		}

		if (boat3up1hit1.getValue() && boat3up1hit2.getValue() && boat3up1hit3.getValue()) {
			boat3up1button1.setStyle("-fx-color: red");
			boat3up1button2.setStyle("-fx-color: red");
			boat3up1button3.setStyle("-fx-color: red");
		}

		if (boat3down2hit1.getValue() && boat3down2hit2.getValue() && boat3down2hit3.getValue()) {
			boat3down2button1.setStyle("-fx-color: red");
			boat3down2button2.setStyle("-fx-color: red");
			boat3down2button3.setStyle("-fx-color: red");
		}

		if (boat3up2hit1.getValue() && boat3up2hit2.getValue() && boat3up2hit3.getValue()) {
			boat3up2button1.setStyle("-fx-color: red");
			boat3up2button2.setStyle("-fx-color: red");
			boat3up2button3.setStyle("-fx-color: red");
		}

		if (boat3down3hit1.getValue() && boat3down3hit2.getValue() && boat3down3hit3.getValue()) {
			boat3down3button1.setStyle("-fx-color: red");
			boat3down3button2.setStyle("-fx-color: red");
			boat3down3button3.setStyle("-fx-color: red");
		}

		if (boat3up3hit1.getValue() && boat3up3hit2.getValue() && boat3up3hit3.getValue()) {
			boat3up3button1.setStyle("-fx-color: red");
			boat3up3button2.setStyle("-fx-color: red");
			boat3up3button3.setStyle("-fx-color: red");
		}

		// Boat2 - Submarine - Button oranssiksi, kun siihen on osuttu. Muutetaan
		// binäärimuuttujan tilaa osuma merkiksi.
		if (boat2amount.intValue() == 1) {
			for (int k = 0; k < boat2amount.intValue(); k++) {
				for (int i = 0; i < 3; i++) {
					if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
						boat2down1button1.setStyle("-fx-color: orange");
						boat2down1hit1.setValue(true);
					}
					if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
						boat2down1button2.setStyle("-fx-color: orange");
						boat2down1hit2.setValue(true);
					}
					if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
						boat2down1button3.setStyle("-fx-color: orange");
						boat2down1hit3.setValue(true);
					}

					if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
						boat2up1hit1.setValue(true);
					}
					if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
						boat2up1hit2.setValue(true);
					}
					if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
						boat2up1hit3.setValue(true);
					}
				}
			}
		}

		if (boat2amount.intValue() == 2) {
			for (int k = 0; k < boat2amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button1.setStyle("-fx-color: orange");
							boat2down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button2.setStyle("-fx-color: orange");
							boat2down1hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button3.setStyle("-fx-color: orange");
							boat2down1hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit3.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button1.setStyle("-fx-color: orange");
							boat2down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button2.setStyle("-fx-color: orange");
							boat2down2hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button3.setStyle("-fx-color: orange");
							boat2down2hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit3.setValue(true);
						}
					}
				}
			}
		}

		if (boat2amount.intValue() == 3) {
			for (int k = 0; k < boat2amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button1.setStyle("-fx-color: orange");
							boat2down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button2.setStyle("-fx-color: orange");
							boat2down1hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button3.setStyle("-fx-color: orange");
							boat2down1hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit3.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button1.setStyle("-fx-color: orange");
							boat2down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button2.setStyle("-fx-color: orange");
							boat2down2hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button3.setStyle("-fx-color: orange");
							boat2down2hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit3.setValue(true);
						}
					}

				} else if (k == 2) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down3button1.setStyle("-fx-color: orange");
							boat2down3hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down3button2.setStyle("-fx-color: orange");
							boat2down3hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down3button3.setStyle("-fx-color: orange");
							boat2down3hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up3hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up3hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up3hit3.setValue(true);
						}
					}
				}
			}
		}

		if (boat2amount.intValue() == 4) {
			for (int k = 0; k < boat2amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button1.setStyle("-fx-color: orange");
							boat2down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button2.setStyle("-fx-color: orange");
							boat2down1hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down1button3.setStyle("-fx-color: orange");
							boat2down1hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up1hit3.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button1.setStyle("-fx-color: orange");
							boat2down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button2.setStyle("-fx-color: orange");
							boat2down2hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down2button3.setStyle("-fx-color: orange");
							boat2down2hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up2hit3.setValue(true);
						}
					}

				} else if (k == 2) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down3button1.setStyle("-fx-color: orange");
							boat2down3hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down3button2.setStyle("-fx-color: orange");
							boat2down3hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down3button3.setStyle("-fx-color: orange");
							boat2down3hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up3hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up3hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up3hit3.setValue(true);
						}
					}

				} else if (k == 3) {
					for (int i = 0; i < 3; i++) {
						if (i == 0 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down4button1.setStyle("-fx-color: orange");
							boat2down4hit1.setValue(true);
						}
						if (i == 1 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down4button2.setStyle("-fx-color: orange");
							boat2down4hit2.setValue(true);
						}
						if (i == 2 && ownBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2down4button3.setStyle("-fx-color: orange");
							boat2down4hit3.setValue(true);
						}

						if (i == 0 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up4hit1.setValue(true);
						}
						if (i == 1 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up4hit2.setValue(true);
						}
						if (i == 2 && opponentBoat2List.get(k).getShipBody().get(i).getIsHit()) {
							boat2up4hit3.setValue(true);
						}
					}
				}
			}
		}

		// Boat2 - Submarine - Buttonit punaisiksi, kun niihin kaikkiin on osuttu
		if (boat2down1hit1.getValue() && boat2down1hit2.getValue() && boat2down1hit3.getValue()) {
			boat2down1button1.setStyle("-fx-color: red");
			boat2down1button2.setStyle("-fx-color: red");
			boat2down1button3.setStyle("-fx-color: red");
		}

		if (boat2up1hit1.getValue() && boat2up1hit2.getValue() && boat2up1hit3.getValue()) {
			boat2up1button1.setStyle("-fx-color: red");
			boat2up1button2.setStyle("-fx-color: red");
			boat2up1button3.setStyle("-fx-color: red");
		}

		if (boat2down2hit1.getValue() && boat2down2hit2.getValue() && boat2down2hit3.getValue()) {
			boat2down2button1.setStyle("-fx-color: red");
			boat2down2button2.setStyle("-fx-color: red");
			boat2down2button3.setStyle("-fx-color: red");
		}

		if (boat2up2hit1.getValue() && boat2up2hit2.getValue() && boat2up2hit3.getValue()) {
			boat2up2button1.setStyle("-fx-color: red");
			boat2up2button2.setStyle("-fx-color: red");
			boat2up2button3.setStyle("-fx-color: red");
		}

		if (boat2down3hit1.getValue() && boat2down3hit2.getValue() && boat2down3hit3.getValue()) {
			boat2down3button1.setStyle("-fx-color: red");
			boat2down3button2.setStyle("-fx-color: red");
			boat2down3button3.setStyle("-fx-color: red");
		}

		if (boat2up3hit1.getValue() && boat2up3hit2.getValue() && boat2up3hit3.getValue()) {
			boat2up3button1.setStyle("-fx-color: red");
			boat2up3button2.setStyle("-fx-color: red");
			boat2up3button3.setStyle("-fx-color: red");
		}

		if (boat2down4hit1.getValue() && boat2down4hit2.getValue() && boat2down4hit3.getValue()) {
			boat2down4button1.setStyle("-fx-color: red");
			boat2down4button2.setStyle("-fx-color: red");
			boat2down4button3.setStyle("-fx-color: red");
		}

		if (boat2up4hit1.getValue() && boat2up4hit2.getValue() && boat2up4hit3.getValue()) {
			boat2up4button1.setStyle("-fx-color: red");
			boat2up4button2.setStyle("-fx-color: red");
			boat2up4button3.setStyle("-fx-color: red");
		}

		if (boat1amount.intValue() == 1) {
			for (int k = 0; k < boat1amount.intValue(); k++) {
				for (int i = 0; i < 2; i++) {
					if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
						boat1down1button1.setStyle("-fx-color: orange");
						boat1down1hit1.setValue(true);
					}
					if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
						boat1down1button2.setStyle("-fx-color: orange");
						boat1down1hit2.setValue(true);
					}

					if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
						boat1up1hit1.setValue(true);
					}
					if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
						boat1up1hit2.setValue(true);
					}
				}
			}
		}

		// Boat1 - Destroyer - Button oranssiksi, kun siihen on osuttu. Muutetaan
		// binäärimuuttujan tilaa osuma merkiksi.
		if (boat1amount.intValue() == 2) {
			for (int k = 0; k < boat1amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button1.setStyle("-fx-color: orange");
							boat1down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button2.setStyle("-fx-color: orange");
							boat1down1hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit2.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button1.setStyle("-fx-color: orange");
							boat1down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button2.setStyle("-fx-color: orange");
							boat1down2hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit2.setValue(true);
						}
					}
				}
			}
		}

		if (boat1amount.intValue() == 3) {
			for (int k = 0; k < boat1amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button1.setStyle("-fx-color: orange");
							boat1down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button2.setStyle("-fx-color: orange");
							boat1down1hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit2.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button1.setStyle("-fx-color: orange");
							boat1down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button2.setStyle("-fx-color: orange");
							boat1down2hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit2.setValue(true);
						}
					}

				} else if (k == 2) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down3button1.setStyle("-fx-color: orange");
							boat1down3hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down3button2.setStyle("-fx-color: orange");
							boat1down3hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up3hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up3hit2.setValue(true);
						}
					}
				}
			}
		}

		if (boat1amount.intValue() == 4) {
			for (int k = 0; k < boat1amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button1.setStyle("-fx-color: orange");
							boat1down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button2.setStyle("-fx-color: orange");
							boat1down1hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit2.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button1.setStyle("-fx-color: orange");
							boat1down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button2.setStyle("-fx-color: orange");
							boat1down2hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit2.setValue(true);
						}
					}

				} else if (k == 2) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down3button1.setStyle("-fx-color: orange");
							boat1down3hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down3button2.setStyle("-fx-color: orange");
							boat1down3hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up3hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up3hit2.setValue(true);
						}
					}

				} else if (k == 3) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down4button1.setStyle("-fx-color: orange");
							boat1down4hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down4button2.setStyle("-fx-color: orange");
							boat1down4hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up4hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up4hit2.setValue(true);
						}
					}

				}
			}
		}

		if (boat1amount.intValue() == 5) {
			for (int k = 0; k < boat1amount.intValue(); k++) {
				if (k == 0) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button1.setStyle("-fx-color: orange");
							boat1down1hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down1button2.setStyle("-fx-color: orange");
							boat1down1hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up1hit2.setValue(true);
						}
					}

				} else if (k == 1) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button1.setStyle("-fx-color: orange");
							boat1down2hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down2button2.setStyle("-fx-color: orange");
							boat1down2hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up2hit2.setValue(true);
						}
					}

				} else if (k == 2) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down3button1.setStyle("-fx-color: orange");
							boat1down3hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down3button2.setStyle("-fx-color: orange");
							boat1down3hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up3hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up3hit2.setValue(true);
						}
					}

				} else if (k == 3) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down4button1.setStyle("-fx-color: orange");
							boat1down4hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down4button2.setStyle("-fx-color: orange");
							boat1down4hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up4hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up4hit2.setValue(true);
						}
					}

				} else if (k == 4) {
					for (int i = 0; i < 2; i++) {
						if (i == 0 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down5button1.setStyle("-fx-color: orange");
							boat1down5hit1.setValue(true);
						}
						if (i == 1 && ownBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1down5button2.setStyle("-fx-color: orange");
							boat1down5hit2.setValue(true);
						}

						if (i == 0 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up5hit1.setValue(true);
						}
						if (i == 1 && opponentBoat1List.get(k).getShipBody().get(i).getIsHit()) {
							boat1up5hit2.setValue(true);
						}
					}

				}
			}
		}

		// Boat1 - Destroyer - Buttonit punaisiksi, kun niihin kaikkiin on osuttu
		if (boat1down1hit1.getValue() && boat1down1hit2.getValue()) {
			boat1down1button1.setStyle("-fx-color: red");
			boat1down1button2.setStyle("-fx-color: red");
		}

		if (boat1up1hit1.getValue() && boat1up1hit2.getValue()) {
			boat1up1button1.setStyle("-fx-color: red");
			boat1up1button2.setStyle("-fx-color: red");
		}

		if (boat1down2hit1.getValue() && boat1down2hit2.getValue()) {
			boat1down2button1.setStyle("-fx-color: red");
			boat1down2button2.setStyle("-fx-color: red");
		}

		if (boat1up2hit1.getValue() && boat1up2hit2.getValue()) {
			boat1up2button1.setStyle("-fx-color: red");
			boat1up2button2.setStyle("-fx-color: red");
		}

		if (boat1down3hit1.getValue() && boat1down3hit2.getValue()) {
			boat1down3button1.setStyle("-fx-color: red");
			boat1down3button2.setStyle("-fx-color: red");
		}

		if (boat1up3hit1.getValue() && boat1up3hit2.getValue()) {
			boat1up3button1.setStyle("-fx-color: red");
			boat1up3button2.setStyle("-fx-color: red");
		}

		if (boat1down4hit1.getValue() && boat1down4hit2.getValue()) {
			boat1down4button1.setStyle("-fx-color: red");
			boat1down4button2.setStyle("-fx-color: red");
		}

		if (boat1up4hit1.getValue() && boat1up4hit2.getValue()) {
			boat1up4button1.setStyle("-fx-color: red");
			boat1up4button2.setStyle("-fx-color: red");
		}

		if (boat1down5hit1.getValue() && boat1down5hit2.getValue()) {
			boat1down5button1.setStyle("-fx-color: red");
			boat1down5button2.setStyle("-fx-color: red");
		}

		if (boat1up5hit1.getValue() && boat1up5hit2.getValue()) {
			boat1up5button1.setStyle("-fx-color: red");
			boat1up5button2.setStyle("-fx-color: red");
		}

	}

}
