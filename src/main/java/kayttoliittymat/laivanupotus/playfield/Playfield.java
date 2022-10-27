package kayttoliittymat.laivanupotus.playfield;

import java.util.ArrayList;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import kayttoliittymat.laivanupotus.game.Game.GameState;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.ships.Battleship;
import kayttoliittymat.laivanupotus.ships.Carrier;
import kayttoliittymat.laivanupotus.ships.Cruiser;
import kayttoliittymat.laivanupotus.ships.Destroyer;
import kayttoliittymat.laivanupotus.ships.Submarine;
import kayttoliittymat.laivanupotus.ui.GameUIdragAndDropController;
import kayttoliittymat.laivanupotus.ui.GameUIstatisticsController;

/**
 * 
 * Pelikenttää mallintava luokka, joka myös generoi GridPane-olion, josta
 * peliruudukko muodostuu.
 * 
 * Rakennetaan pelaajien pelikentät GridPanen pohjalle. Gridpanen yksittäisiin
 * ruutuihin todennäköisesti Buttonit.
 *
 */
public class Playfield extends GridPane {

	private int gridSize; // ruudukon ulottuvuudet, jotka määritetty peliasetuksissa

	private GridPane playFieldGridPane; // peliruudukko
	private GridPane opponentView; // vastustajalle näytettävä näkymä peliruudukosta

	private GameUIdragAndDropController dragDropController; // kontrolleri dragDrop-pelivaiheessa
	private GameUIstatisticsController ownGameUIStatisticsController; // oma kontrolleri ampumisvaiheessa
	private GameUIstatisticsController opponentGameUIStatisticsController; // vastustajan kontrolleri ampumisvaiheessa

	private Orientation deployOrientation; // alukseen vaaka- vai pystysuora asettelu
	private GameState gameState; // Pelin tila

	Button sourceButton; // nappula, josta raahausliike alkaa dragDrop-tilassa

	private int dimensionXY = 35; // "35*35" - Yhden ruudun koko
	private double xy;
	private boolean shootingBlocked = false; // onko ampuminen sallittua. ei saa ampua, kun on jo ammuttu laukaus
												// samalla vuorolla.

	public enum PaintType {
		BLUE, GREEN, RED, RESET
	}

	public enum Orientation {
		HORIZONTAL, VERTICAL
	}

	/**
	 * Konstruktori settings-vaiheeseen
	 * 
	 * @param startGrid
	 */
	public Playfield(int gridSizeIN) {
		gridSize = gridSizeIN;

		setPlayfieldGridPane();
	}

	/**
	 * Konstruktori setup-vaiheeseen
	 * 
	 * @param gridSizeIN
	 * @param dragDropControllerIN
	 */
	public Playfield(int gridSizeIN, GameUIdragAndDropController dragDropControllerIN) {

		this.dragDropController = dragDropControllerIN;
		this.deployOrientation = dragDropControllerIN.getDeployOrientation();

		gridSize = gridSizeIN;

		setPlayfieldGridPane();
	}

	public void setDragAndDropController(GameUIdragAndDropController guidrag) {
		this.dragDropController = guidrag;
	}

	public void setDeployOrientation(Orientation o) {
		this.deployOrientation = o;
	}

	/**
	 * Getteri, jota GameUIdragAndDropController tarvitsee
	 * 
	 * @return
	 */
	public GridPane getPlayfieldGridPane() {
		return playFieldGridPane;
	}

	public void setOwnGameUIStatisticsController(GameUIstatisticsController gsc) {
		ownGameUIStatisticsController = gsc;
	}

	public void setOpponentGameUIStatisticsController(GameUIstatisticsController gsc) {
		opponentGameUIStatisticsController = gsc;
	}

	public void setGameState(GameState g) {
		this.gameState = g;
	}

	public GridPane getOpponentView() {
		return this.opponentView;
	}

	public boolean isShootingBlocked() {
		return shootingBlocked;
	}

	public void setShootingBlocked(boolean shootingBlocked) {
		this.shootingBlocked = shootingBlocked;
	}

	/**
	 * Luodaan pelikenttä GridPane-oliona. Pelikentän koko vaikuttaa siihen, mitä
	 * aluksia voi ottaa
	 * 
	 * 
	 * @param size
	 */

	public void setPlayfieldGridPane() {
		playFieldGridPane = new GridPane();

		// dimensionXY = 35; // "35*35" - Yhden ruudun koko

		xy = 15 + (dimensionXY * (12 - gridSize)) / 2; // Gridpanen etäisyys "vasemmasta" yläkulmasta.

		// System.out.println("debug (Playfield) xy: " + xy);
		playFieldGridPane.setLayoutX(xy);
		playFieldGridPane.setLayoutY(xy);

		// Lisätään napit pelikentälle
		for (int j = 1; j < gridSize + 1; j++) {
			for (int k = 1; k < gridSize + 1; k++) {
				// Tehdään uusi ruutu ja sijoitetaan se taulukkoon
				GameSquare gs = new GameSquare();
				gs.setMinSize(dimensionXY, dimensionXY);
				gs.setPrefSize(dimensionXY, dimensionXY);

				GridPane.setConstraints(gs, j, k);
				playFieldGridPane.getChildren().add(gs);

				// PELIRUUDUN REAKTIO KLIKKAUKSEEN.
				gs.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {

						// pelitila ei saa olla null
						// ampumisen pitää olla sallittu (ei ammuttu tällä vuorolla): shootingBlocked
						// ruutua ei ole vielä aiemmin ammuttu: isFired
						// ampuminen tapahtuu vain, jos ollaan ampumista vastaavasssa pelitilassa:
						// gameState
						if (gameState != null && shootingBlocked == false && !gs.getIsFired()
								&& (gameState.equals(GameState.PLR2SETUP) | gameState.equals(GameState.PLR2TURN)
										| gameState.equals(GameState.PLR1TURN))) {

							shootSquare(gs);

							// Mikäli pelaaja on jo aiemmin ampunut, niin ampuminen ei aiheuta enää
							// toimenpiteitä
//							System.out.println("debug (Playfield) MouseClicked: yritetään ampua...");
//							System.out.println("debug (Playfield) MouseClicked: " + ownGameUIStatisticsController.getShotFired().getValue());
//							if(!ownGameUIStatisticsController.getShotFired().getValue()){
//								shootSquare(gs);
//							}
						}
					}
				});

				// Tarvitaan alusten raahaamisessa. Määritetään piste, johon alus yritetään
				// raahata
				gs.setOnDragDropped(new EventHandler<DragEvent>() {

					@Override
					public void handle(DragEvent event) {
						// System.out.println("debug (Playfield) setOnDragDropped: Row:" +
						// GridPane.getRowIndex(gs) + " "
						// + "Column:" + GridPane.getColumnIndex(gs));

						dragDropped(event, gs);
					}
				});

				// täältä vinkkiä
				// https://stackoverflow.com/questions/22424082/drag-and-drop-vbox-element-with-show-snapshot-in-javafx
				// pelialueen tarkistus, kun sijoitettavaa alusta raahataan sen ympärillä
				gs.setOnDragOver(new EventHandler<DragEvent>() {

					@Override
					public void handle(DragEvent event) {
						// System.out.println("debug (Playfield) setOnDragOver: Row:" +
						// GridPane.getRowIndex(gs) + " "
						// + "Column:" + GridPane.getColumnIndex(gs));

						dragOver(event, gs);
					}
				});

				// pelialueen siivous, kun sijoitetta alus raahataan pois sen päältä
				gs.setOnDragExited(new EventHandler<DragEvent>() {

					@Override
					public void handle(DragEvent event) {
						// System.out.println("debug (Playfield) setOnDragExited: Row:" +
						// GridPane.getRowIndex(gs) + " "
						// + "Column:" + GridPane.getColumnIndex(gs));

						dragExited(event, gs, GridPane.getRowIndex(gs), GridPane.getColumnIndex(gs));
					}
				});

			} // for k
		} // for j

	}// setPlayfield

	/**
	 * Ampumisvaiheessa generoidaan pelikentästä näkymä, joka näytetään vastustaja
	 * Vain osuman saaneet aluksen osat tulevat näkyviin. Lisäksi rasti ruuduissa,
	 * joihin on ammuttu.
	 * 
	 */
	public GridPane generateOpponentView() {
		opponentView = new GridPane();

		opponentView.getChildren().addAll(playFieldGridPane.getChildren());

		for (Node node_temp : opponentView.getChildren()) {
			GameSquare gs_temp = (GameSquare) node_temp;
			if (gs_temp.getIsHit() == false) {
				gs_temp.setStyle("");
			}
		}

		opponentView.setLayoutX(xy);
		opponentView.setLayoutY(xy);

		return opponentView;

	}

	/**
	 * Päivitä ruudukon arvot pelitilanteen mukaisesti
	 */
	public void updateGrid() {

	}

	/**
	 * Ammu ruutua, jota klikattiin ampumistilassa Värejä:
	 * https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html
	 */
	public void shootSquare(GameSquare gs) {

		gs.setIsFired(true); // ruutua ammuttu

		if (gs.getShipPresent() == true) {
			opponentGameUIStatisticsController.setOnShotFired(true); // osuma
			gs.setStyle("-fx-color: orange");
			gs.setIsHit(true); // alus saanut osuman
		} else {
			opponentGameUIStatisticsController.setOnShotFired(false); // ei osumaa
			gs.setStyle("-fx-color: paleturquoise");

		}
	}

	/**
	 * Testataan reaaliaikaisesti, voiko laivan sijoittaa tähän ruutuun.
	 * 
	 * @param event
	 */
	private void dragOver(DragEvent event, GameSquare gs) {
		// If drag board has a string, let the event know that
		// the target accepts copy and move transfer modes
		Dragboard dragboard = event.getDragboard();

		ShipType shipTypeDragOver = (ShipType) dragboard.getContent(DataFormat.PLAIN_TEXT);

		if (dragboard.hasString()) {
			event.acceptTransferModes(TransferMode.MOVE);

			// tarkistetaan, onko sijoitus sallittu
			checkDeployPermitted(GridPane.getRowIndex(gs), GridPane.getColumnIndex(gs), shipTypeDragOver,
					this.deployOrientation, gs);
		}
		event.consume();
	}

	/**
	 * Siivotaan jälkeen jäävät pelialueen värjäykset, kun siirrytään tarkistamaan
	 * sijoitusaluetta toisen ruudun kohdalla.
	 * 
	 * @param event
	 * @param gs
	 * @param rowIndex
	 * @param columnIndex
	 */
	private void dragExited(DragEvent event, GameSquare gs, int rowIndex, int columnIndex) {

		// eventiin säilötyn alustiedon perusteella voidaan resetoida tarvittava määrä
		// ruutuja ympäriltä

		ShipType exitShipType = (ShipType) event.getDragboard().getContent(DataFormat.PLAIN_TEXT);

		int checkRadius1 = getRadiusValues(exitShipType)[0];
		int checkRadius2 = getRadiusValues(exitShipType)[1];

		this.deployOrientation = dragDropController.getDeployOrientation();
		// System.out.println("debug (Playfield) dragExited: " + exitShipType + "-" +
		// deployOrientation + "-ROW:"
		// + rowIndex + "-COLUMN:" + columnIndex);

		// vaakasuora sijoitus
		if (deployOrientation.equals(Orientation.HORIZONTAL)) {
			if (columnIndex - checkRadius1 < 1) {
				paintCheckedSquares(1, (columnIndex + checkRadius2 + 1), rowIndex, Orientation.HORIZONTAL,
						PaintType.RESET);
			} else if (columnIndex + checkRadius2 > playFieldGridPane.getWidth()) {
				paintCheckedSquares(columnIndex - checkRadius1, (int) playFieldGridPane.getWidth(), rowIndex,
						Orientation.HORIZONTAL, PaintType.RESET);
			} else {
				paintCheckedSquares((columnIndex - checkRadius1), (columnIndex + checkRadius2 + 1), rowIndex,
						Orientation.HORIZONTAL, PaintType.RESET);
			}

			// pystysuora sijoitus
		} else if (deployOrientation.equals(Orientation.VERTICAL)) {
			if (rowIndex - checkRadius1 < 1) {
				paintCheckedSquares(1, (rowIndex + checkRadius2 + 1), columnIndex, Orientation.VERTICAL,
						PaintType.RESET);
			} else if (rowIndex + checkRadius2 > playFieldGridPane.getHeight()) {
				paintCheckedSquares(rowIndex - checkRadius1, (int) playFieldGridPane.getHeight(), columnIndex,
						Orientation.VERTICAL, PaintType.RESET);
			} else {
				paintCheckedSquares((rowIndex - checkRadius1), (rowIndex + checkRadius2 + 1), columnIndex,
						Orientation.VERTICAL, PaintType.RESET);
			}
		}

		gs.setDragOverShipDeployPermitted(false); // palautetaan oletusarvo, kun poistutaan ruudusta

	}

	/*
	 * Aluksen sijoittaminen
	 */
	private void dragDropped(DragEvent event, GameSquare gs) {

		Dragboard db = event.getDragboard();

		ShipType shipToDrop = (ShipType) db.getContent(DataFormat.PLAIN_TEXT);

		boolean canDropThisShip = gs.getDragOverShipDeployPermitted();

		Dragboard dragboard = event.getDragboard();

		// kerrotaan kontrollerille, onnistuuko aluksen sijoitus tähän
		dragDropController.setCurrentDropSuccessful(canDropThisShip);

		if (dragboard.hasString()) {

			// aluksen sijoitus onnistuu
			if (canDropThisShip == true) {

				event.setDropCompleted(true);

				sourceButton = (Button) event.getGestureSource();

				// Asetetaan laiva kentälle
				deployShip(gs, shipToDrop, this.deployOrientation, sourceButton);

				// sijoitus ei onnistu, eli ei tapahdu mitään
			} else if (canDropThisShip == false) {
				event.setDropCompleted(false);

				// System.out.println("debug (Playfield) dragDropped: canDropThisShip is
				// FALSE");
			}

			// System.out.println("debug (Playfield) dragDropped: dragboard content IS
			// SHIPTYPE ENUM: "
			// + (dragboard.getContent(DataFormat.PLAIN_TEXT) instanceof ShipType));

			
		// tämä lohko suoritetaan, jos alus yritetään pudottaa pelikentän ulkopuolelle
		} else {
			event.setDropCompleted(false);
		}
		event.consume();
	}

	/**
	 * Sijoitetaan alus, kun on ensin varmistettu, että sijoitus onnistuu.
	 * 
	 * @param gs
	 * @param shipToDeploy
	 * @param deployOrientationIN
	 * @param sourceButton
	 */
	private void deployShip(GameSquare gs, ShipType shipToDeploy, Orientation deployOrientationIN,
			Button sourceButton) {

		deployOrientation = deployOrientationIN;

		int deployCoordinateX = GridPane.getRowIndex(gs);
		int deployCoordinateY = GridPane.getColumnIndex(gs);

		// sijoitettavan alustyypin viemä tila
		int[] deployRadius = getRadiusValues(shipToDeploy);

		// System.out.println("debug (Playfield) deployShip radius values: " +
		// deployRadius[0] + " " + deployRadius[1]);

		// vaakasuora sijoitus
		if (deployOrientation.equals(Orientation.HORIZONTAL)) {

			// System.out.println("debug (Playfield) deployShip: Horizontal.");

			ArrayList<GameSquare> setDeployZoneGSList = new ArrayList<GameSquare>();
			for (int i = (deployCoordinateY - deployRadius[0]); i < (deployCoordinateY + deployRadius[1] + 1); i++) {
				GameSquare gs_temp = getNodeByRowColumnIndex(deployCoordinateX, i, playFieldGridPane);
				gs_temp.setShipPresent(true);

				setDeployZoneGSList.add(gs_temp);
			}
			
			// merkitään peliruudut, joihin alus on sijoitettu
			setDeployNotPermittedZone(setDeployZoneGSList, Orientation.HORIZONTAL);

			// Luodaan uusi Ship luokan instanssi X-pelaajalle

			// PELKÄT ALUKSEN OSAT HORISONTAALISESTI ELI MUUTTUVANA ARVONA CoordinateX
			// TÄSSÄ COORDINATEX KERTOO RIVIN NUMERON ELI KUN X MUUTTUU --> LIIKUTAAN
			// PYSTYSUUNNASSA
			// VAIKUTTEITA R-KIELESTÄ
			
			// tallennetaan Player-olioon tieto sijoitetusta laivasta ja siihen liitetyistä peliruuduista
			addNewShipToPlayerX(sourceButton, deployCoordinateX, deployCoordinateY, setDeployZoneGSList);

		// pystysuora sijoitus, muuten sama kuin yllä
		} else if (deployOrientation.equals(Orientation.VERTICAL)) {

			ArrayList<GameSquare> setDeployZoneGSList = new ArrayList<GameSquare>();
			// System.out.println("debug (Playfield) deployShip: Vertical.");

			// ALUKSEN YMPÄRÖIVÄT RUUDUT, SIS. ALUS
			for (int i = (deployCoordinateX - deployRadius[0]); i < (deployCoordinateX + deployRadius[1] + 1); i++) {
				GameSquare gs_temp = getNodeByRowColumnIndex(i, deployCoordinateY, playFieldGridPane);
				gs_temp.setShipPresent(true);

				setDeployZoneGSList.add(gs_temp);
			}
			setDeployNotPermittedZone(setDeployZoneGSList, Orientation.VERTICAL);

			ArrayList<GameSquare> shipBodyGSList = new ArrayList<GameSquare>();

			// PELKÄT ALUKSEN RUUDUT VERTIKAALISESTI ELI MUUTTUVANA ARVONA CoordinateY
			// TÄSSÄ COORDINATEX KERTOO RIVIN NUMERON ELI KUN X MUUTTUU --> LIIKUTAAN
			// PYSTYSUUNNASSA

			// Luodaan uusi Ship luokan instanssi X-pelaajalle
			addNewShipToPlayerX(sourceButton, deployCoordinateX, deployCoordinateY, setDeployZoneGSList);
		}
	}

	/**
	 * Laivan asettamisen yhteydessä asetetaan sen ympärillä vyöhyke, johon ei voi
	 * asettaa laivoja.
	 * 
	 * @param deployZoneGSList
	 * @param o
	 */
	private void setDeployNotPermittedZone(ArrayList<GameSquare> deployZoneGSList, Orientation o) {

		// vaakasuora sijoitus
		if (o.equals(Orientation.HORIZONTAL)) {

			for (int i = 0; i < deployZoneGSList.size(); i++) {
				if (i == 0) { // laivan vasen reuna
					GameSquare gs_temp = deployZoneGSList.get(i);
					int rowIndex = GridPane.getRowIndex(gs_temp);
					int columnIndex = GridPane.getColumnIndex(gs_temp);
					int columnIndexLeft = GridPane.getColumnIndex(gs_temp) - 1; // vasemmalla oleva sarake tarkistusta
																				// varten

					for (int k = rowIndex - 1; k < rowIndex + 2; k++) {
						GameSquare gs_tempLeft = this.getNodeByRowColumnIndex(k, columnIndexLeft, playFieldGridPane);
						GameSquare gs_tempThisColumn = this.getNodeByRowColumnIndex(k, columnIndex, playFieldGridPane);
						if (gs_tempLeft != null) {
							gs_tempLeft.setDeployPermitted(false); // kielletään sijoittaminen tähän ruutuun
						}
						if (gs_tempThisColumn != null) {
							gs_tempThisColumn.setDeployPermitted(false);
						}
					}

				} else if (i == deployZoneGSList.size() - 1) { // laivan oikea reuna

					GameSquare gs_temp = deployZoneGSList.get(i);
					int rowIndex = GridPane.getRowIndex(gs_temp);
					int columnIndex = GridPane.getColumnIndex(gs_temp);
					int columnIndexRight = GridPane.getColumnIndex(gs_temp) + 1; // vasemmalla oleva sarake tarkistusta
																					// varten

					for (int k = rowIndex - 1; k < rowIndex + 2; k++) {
						GameSquare gs_tempRight = this.getNodeByRowColumnIndex(k, columnIndexRight, playFieldGridPane);
						GameSquare gs_tempThisColumn = this.getNodeByRowColumnIndex(k, columnIndex, playFieldGridPane);
						if (gs_tempRight != null) {
							gs_tempRight.setDeployPermitted(false);  // kielletään sijoittaminen tähän ruutuun
						}
						if (gs_tempThisColumn != null) {
							gs_tempThisColumn.setDeployPermitted(false);
						}
					}

				} else { // laivan keskiosat

					GameSquare gs_temp = deployZoneGSList.get(i);
					int rowIndex = GridPane.getRowIndex(gs_temp);
					int columnIndex = GridPane.getColumnIndex(gs_temp);

					for (int k = rowIndex - 1; k < rowIndex + 2; k++) {
						GameSquare gs_tempThisColumn = this.getNodeByRowColumnIndex(k, columnIndex, playFieldGridPane);

						if (gs_tempThisColumn != null) {
							gs_tempThisColumn.setDeployPermitted(false);  // kielletään sijoittaminen tähän ruutuun
						}
					}
				}

			}

		// pystysuora sijoitus, muuten sama kuin yllä
		} else if (o.equals(Orientation.VERTICAL)) {

			for (int i = 0; i < deployZoneGSList.size(); i++) {
				if (i == 0) { // laivan vasen reuna
					GameSquare gs_temp = deployZoneGSList.get(i);
					int rowIndex = GridPane.getRowIndex(gs_temp);
					int columnIndex = GridPane.getColumnIndex(gs_temp);
					int rowIndexUp = GridPane.getRowIndex(gs_temp) - 1; // vasemmalla oleva sarake tarkistusta
																		// varten

					for (int k = columnIndex - 1; k < columnIndex + 2; k++) {
						GameSquare gs_tempUp = this.getNodeByRowColumnIndex(rowIndexUp, k, playFieldGridPane);
						GameSquare gs_tempThisRow = this.getNodeByRowColumnIndex(rowIndex, k, playFieldGridPane);
						if (gs_tempUp != null) {
							gs_tempUp.setDeployPermitted(false);
						}
						if (gs_tempThisRow != null) {
							gs_tempThisRow.setDeployPermitted(false);
						}
					}

				} else if (i == deployZoneGSList.size() - 1) { // laivan oikea reuna

					GameSquare gs_temp = deployZoneGSList.get(i);
					int rowIndex = GridPane.getRowIndex(gs_temp);
					int columnIndex = GridPane.getColumnIndex(gs_temp);
					int rowIndexDown = GridPane.getRowIndex(gs_temp) + 1; // vasemmalla oleva sarake tarkistusta
																			// varten

					for (int k = columnIndex - 1; k < columnIndex + 2; k++) {
						GameSquare gs_tempDown = this.getNodeByRowColumnIndex(rowIndexDown, k, playFieldGridPane);
						GameSquare gs_tempThisRow = this.getNodeByRowColumnIndex(rowIndex, k, playFieldGridPane);
						if (gs_tempDown != null) {
							gs_tempDown.setDeployPermitted(false);
						}
						if (gs_tempThisRow != null) {
							gs_tempThisRow.setDeployPermitted(false);
						}
					}

				} else { // laivan keskiosat

					GameSquare gs_temp = deployZoneGSList.get(i);
					int rowIndex = GridPane.getRowIndex(gs_temp);
					int columnIndex = GridPane.getColumnIndex(gs_temp);

					for (int k = columnIndex - 1; k < columnIndex + 2; k++) {
						GameSquare gs_tempThisRow = this.getNodeByRowColumnIndex(rowIndex, k, playFieldGridPane);

						if (gs_tempThisRow != null) {
							gs_tempThisRow.setDeployPermitted(false);
						}
					}
				}

			}

		}

	}

	/**
	 * Tämä metodi tarkistaa, voiko juuri tähän kohtaan pelikenttää asettaa halutun
	 * alustyypin. Käydään siis peliruutuja läpi tarvittava määrä pysty- tai
	 * vaakasuunnassa ja tarkistetaan niistä deployPermitted == true
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * @param shipType
	 * @param orientation
	 * @return
	 */
	private void checkDeployPermitted(int rowIndex, int columnIndex, ShipType shipType, Orientation orientation,
			GameSquare gs) {

		boolean permitted = true;

		int checkRadius1 = getRadiusValues(shipType)[0];
		int checkRadius2 = getRadiusValues(shipType)[1];

		int fixedCoordinate = rowIndex; // oletusarvo, ei käytetä sellaisenaan
		int startIndex = 0; // oletusarvo, ei käytetä sellaisenaan. TOIMII VAIN HORISONTAALITARKISTUKSESSA
		int endIndex = 0; // oletusarvo, ei käytetä sellaisenaan

		ArrayList<GameSquare> squaresToCheck = new ArrayList<GameSquare>();

		// vaakasuora
		if (orientation.equals(Orientation.HORIZONTAL)) {

			fixedCoordinate = rowIndex;

			// ruudukon vasemmalla reunalla
			if (columnIndex - checkRadius1 < 1) {
				permitted = false;
				startIndex = 1;
				endIndex = columnIndex + checkRadius2;

			// ruudukon oikealla reunalla
			} else if ((columnIndex + checkRadius2 + 1) > ((int) playFieldGridPane.getWidth())) {
				permitted = false;
				startIndex = columnIndex - checkRadius1;
				endIndex = (int) playFieldGridPane.getWidth();

			// ruudukon keskellä
			} else {
				startIndex = columnIndex - checkRadius1;
				endIndex = columnIndex + checkRadius2;
				for (int i = startIndex; i < endIndex + 1; i++) {
					GameSquare gs_temp = this.getNodeByRowColumnIndex(rowIndex, i, playFieldGridPane);
					squaresToCheck.add(gs_temp);
				}

				// voiko sijoittaa?
				for (GameSquare gs_temp : squaresToCheck) {
					if (gs_temp == null) {
						permitted = false;
					} else if ((gs_temp.getDeployPermitted() == false)) {
						permitted = false;
					}
				}
			}

		// pystysuora, muuten sama kuin yllä
		} else if (orientation.equals(Orientation.VERTICAL)) { // sama juttu vertikaaliselle tarkistukselle

			fixedCoordinate = columnIndex;

			if (rowIndex - checkRadius1 < 1) {
				permitted = false;
				startIndex = 1;
				endIndex = rowIndex + checkRadius2;

			} else if ((rowIndex + checkRadius2 + 1) > ((int) playFieldGridPane.getHeight())) {
				permitted = false;
				startIndex = rowIndex - checkRadius1;
				endIndex = (int) playFieldGridPane.getHeight();

			} else {
				startIndex = rowIndex - checkRadius1;
				endIndex = rowIndex + checkRadius2;
				for (int i = startIndex; i < endIndex + 1; i++) {
					GameSquare gs_temp = this.getNodeByRowColumnIndex(i, columnIndex, playFieldGridPane);
					squaresToCheck.add(gs_temp);
				}

				for (GameSquare gs_temp : squaresToCheck) {
					if (gs_temp == null) {
						permitted = false;
					} else if ((gs_temp.getDeployPermitted() == false)) {
						permitted = false;
					}
				}
			}
		}

		if (permitted == true) {
			gs.setDragOverShipDeployPermitted(true); // ruutuun voi sijoittaa juuri tämän alustyypin. drop-metodi
														// tarvitsee tämän tiedon.
			paintCheckedSquares(startIndex, endIndex, fixedCoordinate, orientation, PaintType.GREEN);

		} else if (permitted == false) {
			gs.setDragOverShipDeployPermitted(false); // ei voi sijoittaa. drop-metodi tarvitsee tämän tiedon.
			paintCheckedSquares(startIndex, endIndex, fixedCoordinate, orientation, PaintType.RED);
		}
	}

	/**
	 * Apumetodi tarkistettavien ruutujen maalaamiseen.
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @param fixedCoordinate
	 * @param orientation
	 * @param allowed
	 */
	private void paintCheckedSquares(int startIndex, int endIndex, int fixedCoordinate, Orientation orientation,
			PaintType paintType) {

		ArrayList<GameSquare> squaresToPaint = new ArrayList<GameSquare>();

		for (int i = startIndex; i < endIndex + 1; i++) {
			if (orientation == Orientation.HORIZONTAL) {
				GameSquare temp_gs = getNodeByRowColumnIndex(fixedCoordinate, i, playFieldGridPane);
				squaresToPaint.add(temp_gs);
			} else if (orientation == Orientation.VERTICAL) {
				GameSquare temp_gs = getNodeByRowColumnIndex(i, fixedCoordinate, playFieldGridPane);
				squaresToPaint.add(temp_gs);
			}

			for (GameSquare gs : squaresToPaint) {
				if (paintType == PaintType.BLUE) {  // sininen väri: laiva
					if (gs == null) {
						break;
					} else {
						gs.setStyle("-fx-color: blue");
					}
				} else if (paintType == PaintType.GREEN) {  // vihreä: sijoittaminen sallittu
					if (gs == null) {
						break;
					} else {
						if (gs.getShipPresent() == false) {
							gs.setStyle("-fx-color: green");
						}
					}
				} else if (paintType == PaintType.RED) {  // punainen: sijoittaminen kielletty
					if (gs == null) {
						break;
					} else {
						if (gs.getShipPresent() == false) {
							gs.setStyle("-fx-color: red");
						}
					}
				} else if (paintType == PaintType.RESET) {  // värin poistaminen
					if (gs == null) {
						break;
					} else {
						if (gs.getShipPresent() == false) {
							gs.setStyle("");
						}
					}
				}
			}
		}
	}

	/**
	 * Apumetodi, jota tarvitaan deployPermit-säännön tarkistamiseen
	 * 
	 * https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column/20828724
	 * 
	 * @param row
	 * @param column
	 * @param gridPane
	 * @return
	 */
	public GameSquare getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
		Node result = null;
		ObservableList<Node> childrens = gridPane.getChildren();

		for (Node node : childrens) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				result = node;
				break;
			}
		}
		return (GameSquare) result;
	}

	/**
	 * Palautetaan kullekin laivatyypille sen vaatiman sijoitusalueen rajat
	 * 
	 * @param shipType
	 * @return
	 */
	private int[] getRadiusValues(ShipType shipType) {

		int checkRadius1 = -100;
		int checkRadius2 = -100;

		switch (shipType) {
		case CARRIER:
			checkRadius1 = 2;
			checkRadius2 = 2;
			break;
		case BATTLESHIP:
			checkRadius1 = 1;
			checkRadius2 = 2;
			break;
		case CRUISER:
			checkRadius1 = 1;
			checkRadius2 = 1;
			break;
		case SUBMARINE:
			checkRadius1 = 1;
			checkRadius2 = 1;
			break;
		case DESTROYER:
			checkRadius1 = 0;
			checkRadius2 = 1;
			break;
		}
		return new int[] { checkRadius1, checkRadius2 };
	}

	/**
	 * Lisätään alus vastaavan aluksen listaan dragDropController:iin. Muutetaan
	 * drag&drop buttonin tilaa.
	 */
	private void addNewShipToPlayerX(Button buttonX, int x, int y, ArrayList<GameSquare> shipBody) {

		// ArrayList<GameSquare> setDeployZoneGSList = new ArrayList<GameSquare>();

		// buttonX.setStyle("-fx-color: red");

		String buttonId = buttonX.getId();
		// System.out.println("debug (Playfield) dragDropped - changing GestureSource
		// color of: " + buttonId);

		switch (buttonId) {
		case "boat5Button":
			// Lisätään alus dragDropController listaan
			dragDropController.addBoat5sPx(x, y, shipBody);
			break;
		case "boat4Button":
			// Lisätään alus dragDropController listaan
			dragDropController.addBoat4sPx(x, y, shipBody);
			break;
		case "boat3Button":
			// Lisätään alus dragDropController listaan
			dragDropController.addBoat3sPx(x, y, shipBody);
			break;
		case "boat2Button":
			// Lisätään alus dragDropController listaan
			dragDropController.addBoat2sPx(x, y, shipBody);
			break;
		case "boat1Button":
			// Lisätään alus dragDropController listaan
			dragDropController.addBoat1sPx(x, y, shipBody);
			break;
		}
	}
}
