package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import kayttoliittymat.laivanupotus.MainApp;
import kayttoliittymat.laivanupotus.game.Fleet;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Game.GameState;
import kayttoliittymat.laivanupotus.game.Game.PlayerNumber;
import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.playfield.Playfield;
import kayttoliittymat.laivanupotus.ships.Battleship;
import kayttoliittymat.laivanupotus.ships.Carrier;
import kayttoliittymat.laivanupotus.ships.Cruiser;
import kayttoliittymat.laivanupotus.ships.Destroyer;
import kayttoliittymat.laivanupotus.ships.Ship;
import kayttoliittymat.laivanupotus.ships.Submarine;

/**
 * Ylätason kontrolleri. Huolehtii käyttöliittymänäkymien vaihtamisesta.
 *
 */
public class MasterController {

	private GameUIController guiController;

	private AnchorPane leftAnchor;  // vasen puolisko
	private AnchorPane centerAnchor;  // keskiosa
	private AnchorPane bottomAnchor;  // alaosa

	private Game currentGame;

	private GameUIplayfield playfieldUISettingsTemplate; // käytetään Settings-vaiheessa eri kenttäkokojen kuvaamiseen

	private Stage rootStage;
	private Scene rootScene;
	private GameUI gameUI;

	private GameUIdragAndDrop dragAndDropUIPlayer1;
	private Playfield player1Playfield;
	private GridPane player1PlayfieldGridPane;
	private GridPane player1PlayfieldOpponentView;

	private GameUIdragAndDrop dragAndDropUIPlayer2;
	private Playfield player2Playfield;
	private GridPane player2PlayfieldGridPane;
	private GridPane player2PlayfieldOpponentView;

	private GameUIstatistics player1Statistics;
	private GameUIstatistics player2Statistics;
	private GameUIstatisticsController player1StatisticsController;
	private GameUIstatisticsController player2StatisticsController;

	private Fleet player1Fleet = new Fleet();
	private Fleet player2Fleet = new Fleet();
	
	private SimpleDoubleProperty player1ShotHitAmount = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty player1ShotTotalAmount = new SimpleDoubleProperty(0);
	
	private SimpleDoubleProperty player2ShotHitAmount = new SimpleDoubleProperty(0);
	private SimpleDoubleProperty player2ShotTotalAmount = new SimpleDoubleProperty(0);

	private SimpleStringProperty informationTextSimple = new SimpleStringProperty();
	private SimpleStringProperty informationTextColorSimple = new SimpleStringProperty();
	
	private SimpleBooleanProperty alwaysTrue = new SimpleBooleanProperty(true);

	@FXML
	private SimpleStringProperty informationText = new SimpleStringProperty();

	@FXML
	private SimpleStringProperty informationTextColor = new SimpleStringProperty();

	public MasterController() {

	}

	public MasterController(GameUIController guicont, SimpleStringProperty informationTextIN,
			SimpleStringProperty informationTextColorIN) {

		this.guiController = guicont;
		this.informationText = informationTextIN;
		this.informationTextColor = informationTextColorIN;
	}

	public void setInformationText(SimpleStringProperty text) {
		this.informationText = text;
	}

	public void setInformationTextColor(SimpleStringProperty textColor) {
		this.informationTextColor = textColor;
	}

	public void setGUIController(GameUIController guiControllerIN) {
		this.guiController = guiControllerIN;
	}

	public void setPlayer1GameUIStatisticsController(GameUIstatisticsController gsc) {
		player1StatisticsController = gsc;
	}

	public void setPlayer2GameUIStatisticsController(GameUIstatisticsController gsc) {
		player2StatisticsController = gsc;
	}
	
	public SimpleDoubleProperty getPlayer1ShotHitAmount() {
		return player1ShotHitAmount;
	}

	public SimpleDoubleProperty getPlayer1ShotTotalAmount() {
		return player1ShotTotalAmount;
	}

	public SimpleDoubleProperty getPlayer2ShotHitAmount() {
		return player2ShotHitAmount;
	}

	public SimpleDoubleProperty getPlayer2ShotTotalAmount() {
		return player2ShotTotalAmount;
	}

	public void addPlayer1ShotHitAmount() {
		this.player1ShotHitAmount.setValue(player1ShotHitAmount.getValue()+1);
	}

	public void addPlayer1ShotTotalAmount() {
		this.player1ShotTotalAmount.setValue(player1ShotTotalAmount.getValue()+1);
	}

	public void addPlayer2ShotHitAmount() {
		this.player2ShotHitAmount.setValue(player2ShotHitAmount.getValue()+1);
	}

	public void addPlayer2ShotTotalAmount() {
		this.player2ShotTotalAmount.setValue(player2ShotTotalAmount.getValue()+1);
	}

	/**
	 * Ladataan käyttöliittymän pohjanäkymä.
	 * @param rootStageIN
	 */
	public void loadRootGameUI(Stage rootStageIN) {
		this.rootStage = rootStageIN;
		gameUI = new GameUI(rootStage, this);

		rootScene = new Scene(gameUI.getPa());

		rootStage.setScene(rootScene);

		rootStage.show();

	}
	
	/**
	 * Ladataan peliasetusten käyttöliittymänäkymä.
	 * 
	 * @param leftAnchorIN
	 * @param centerAnchorIN
	 * @param bottomAnchorIN
	 * @throws IOException
	 */
	public void initializeSettings(AnchorPane leftAnchorIN, AnchorPane centerAnchorIN, AnchorPane bottomAnchorIN)
			throws IOException {

		leftAnchor = leftAnchorIN;
		centerAnchor = centerAnchorIN;
		bottomAnchor = bottomAnchorIN;

		GameUIsettings settingsUI = new GameUIsettings(guiController, informationText, informationTextColor, this);

		leftAnchor.getChildren().addAll(settingsUI.getPa());

		playfieldUISettingsTemplate = new GameUIplayfield(10);


		centerAnchor.getChildren().addAll(playfieldUISettingsTemplate.getPlayfieldGridPane());

	}

	/**
	 * Päivitetään keskimmäinen käyttöliittymänäkymä.
	 * @param gameAreaDimension
	 */
	public void updateCenterAnchor(int gameAreaDimension) {

		playfieldUISettingsTemplate = new GameUIplayfield(gameAreaDimension);

		centerAnchor.getChildren().clear();
		centerAnchor.getChildren().addAll(playfieldUISettingsTemplate.getPlayfieldGridPane());
	}

	/**
	 * Tyhjennetään käyttöliittymän keski- ja vasemmanpuoleinen alue.
	 */
	public void clearCenterAndLeft() {
		leftAnchor.getChildren().clear();
		centerAnchor.getChildren().clear();
	}

	/**
	 * Ladataan väliin infonäkymä, joka peittää peliruudun tilanteen ja pyytää
	 * toista pelaajaa tulemaan paikalle.
	 * 
	 * Seuraavaksi käynnistettävä käyttöliittymänäkymä riippuu siitä, missä
	 * pelitilassa (GameState) ollaan.
	 * 
	 * @param currentGameIN
	 */
	public void loadSwitchPlayerScreen(Game currentGameIN) {

		clearCenterAndLeft();

		currentGame = currentGameIN;

		final Stage switchPlayerPopupStage = new Stage();
		switchPlayerPopupStage.initModality(Modality.APPLICATION_MODAL);

		VBox PopUpVbox = new VBox(20);

		Text popupText = new Text();
		popupText.setStyle("-fx-font-size: 24px");
		Button popupButton = new Button("OK");
		popupButton.setStyle("-fx-font-size: 24px");
		popupButton.setMinSize(80, 30);

		if (currentGame.getGameState().equals(GameState.SETTINGS)) { // ASETUSTEN JÄLKEEN --> PLR1 SETUP

			popupText.setText(currentGame.getPlayer1().getName() + "'s turn");

			popupButton.setOnAction(e -> {

				currentGame.setGameState(GameState.PLR1SETUP);

				leftAnchor.getChildren().addAll(dragAndDropUIPlayer1.getPa());
				centerAnchor.getChildren().addAll(currentGame.getPlayer1GameUIplayfield().getPlayfieldGridPane());

				switchPlayerPopupStage.close();
			});

		} else if (currentGame.getGameState().equals(GameState.PLR1SETUP)) { // PLR1 SETUPIN JÄLKEEN --> PLR2 SETUP

			popupText.setText(currentGame.getPlayer2().getName() + "'s turn");

			popupButton.setOnAction(e -> {

				currentGame.setGameState(GameState.PLR2SETUP);

				leftAnchor.getChildren().addAll(dragAndDropUIPlayer2.getPa());
				centerAnchor.getChildren().addAll(currentGame.getPlayer2GameUIplayfield().getPlayfieldGridPane());

				switchPlayerPopupStage.close();
			});

		} else if (currentGame.getGameState().equals(GameState.PLR2SETUP)) { // KUN PELI ALKAA --> PLR1 VUORO

			popupText.setText(currentGame.getPlayer1().getName() + "'s turn");

			popupButton.setOnAction(e -> {

				try {

					currentGame.setGameState(GameState.PLR1TURN);

					player1Playfield.setGameState(currentGame.getGameState());
					player2Playfield.setGameState(currentGame.getGameState());

					player2PlayfieldOpponentView = player2Playfield.generateOpponentView();
					currentGame.setPlayer2PlayfieldOpponentView(player2PlayfieldOpponentView);

					player1PlayfieldOpponentView = player1Playfield.generateOpponentView();
					currentGame.setPlayer1PlayfieldOpponentView(player1PlayfieldOpponentView);

					// pitää lähettää myös vastustajan Playfield, jotta vuorovaikutus osumien yms.
					// kanssa toimii

					player1Statistics = new GameUIstatistics(informationText, informationTextColor, currentGame,
							PlayerNumber.PLAYER1, this, player1Playfield, player2Playfield);


				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("IO-EXCEPTION");
					e1.printStackTrace();
				}

				player1StatisticsController.setOpponentPlayfieldGridPane(player2PlayfieldOpponentView);

				leftAnchor.getChildren().addAll(player1Statistics.getPa());
				centerAnchor.getChildren().addAll(player2PlayfieldOpponentView);

				switchPlayerPopupStage.close();
			});

		} else if (currentGame.getGameState().equals(GameState.PLR1TURN)) { // PLR1 VUORO VALMIS --> PLR2 VUORO

			popupText.setText(currentGame.getPlayer2().getName() + "'s turn");

			popupButton.setOnAction(e -> {

				currentGame.setGameState(GameState.PLR2TURN);

				player1Playfield.setGameState(currentGame.getGameState());
				player2Playfield.setGameState(currentGame.getGameState());

				player1PlayfieldOpponentView = currentGame.getPlayer1PlayfieldOpponentView();
				currentGame.setPlayer1PlayfieldOpponentView(player1PlayfieldOpponentView);

				try {
					player2Statistics = new GameUIstatistics(informationText, informationTextColor, currentGame,
							PlayerNumber.PLAYER2, this, player2Playfield, player1Playfield);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				player2StatisticsController.setInformationTextAndColor(informationText, informationTextColor);

				player2StatisticsController.setOpponentPlayfieldGridPane(player1PlayfieldOpponentView);

				leftAnchor.getChildren().addAll(player2Statistics.getPa());
				centerAnchor.getChildren().addAll(player1PlayfieldOpponentView);

				switchPlayerPopupStage.close();
			});

		} else if (currentGame.getGameState().equals(GameState.PLR2TURN)) { // PLR2 VUORO VALMIS --> PLR1 VUORO

			// System.out.println("debug (masterController): PLR2 second turn");

			popupText.setText(currentGame.getPlayer1().getName() + "'s turn");

			popupButton.setOnAction(e -> {

				currentGame.setGameState(GameState.PLR1TURN);

				player1Playfield.setGameState(currentGame.getGameState());
				player2Playfield.setGameState(currentGame.getGameState());

				player2PlayfieldOpponentView = currentGame.getPlayer2PlayfieldOpponentView();
				currentGame.setPlayer2PlayfieldOpponentView(player2PlayfieldOpponentView);

				try {
					player1Statistics = new GameUIstatistics(informationText, informationTextColor, currentGame,
							PlayerNumber.PLAYER1, this, player1Playfield, player2Playfield);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				player1StatisticsController.setInformationTextAndColor(informationText, informationTextColor);

				player1StatisticsController.setOpponentPlayfieldGridPane(player2PlayfieldOpponentView);

				leftAnchor.getChildren().addAll(player1Statistics.getPa());
				centerAnchor.getChildren().addAll(player2PlayfieldOpponentView);

				switchPlayerPopupStage.close();
			});
		}

		PopUpVbox.getChildren().addAll(popupText, popupButton);

		PopUpVbox.setAlignment(Pos.CENTER);

		Scene dialogScene = new Scene(PopUpVbox, 300, 200);
		switchPlayerPopupStage.setTitle("Player change");
		switchPlayerPopupStage.setScene(dialogScene);
		switchPlayerPopupStage.show();
	}

	/**
	 * Ladataan laivojen asettelu pelaajalle 1.
	 * 
	 * @param currentGameIN
	 * @throws IOException
	 */
	public void loadPlayer1Setup(Game currentGameIN) throws IOException {

		this.currentGame = currentGameIN;

		currentGame.setGameState(GameState.PLR1SETUP);

		player1Playfield = currentGame.getPlayer1GameUIplayfield().getPlayfield();

		player1PlayfieldGridPane = currentGame.getPlayer1GameUIplayfield().getPlayfieldGridPane();

		dragAndDropUIPlayer1 = new GameUIdragAndDrop(guiController, informationText, informationTextColor, currentGame,
				this, currentGame.getPlayer1(), player1PlayfieldGridPane, player1Playfield, player1Fleet);

		leftAnchor.getChildren().clear();

		leftAnchor.getChildren().addAll(dragAndDropUIPlayer1.getPa());

		// Asetetaan pelaajan 1 pelikenttä (GridPane) käyttöliittymään
		centerAnchor.getChildren().addAll(currentGame.getPlayer1GameUIplayfield().getPlayfieldGridPane());

	}

	/**
	 * Tallennetaan pelaajan 1 laivojen asettelu.
	 * 
	 * @param player1playFieldGridPane
	 * @param currentGameIN
	 */
	public void savePlayer1Setup(GridPane player1playFieldGridPane, Game currentGameIN) {

		this.currentGame = currentGameIN;

		currentGame.getPlayer1GameUIplayfield().setPlayfieldGridPane(player1playFieldGridPane);
	}

	/**
	 * Ladataan pelaajan 2 laivojen asettelu.
	 * 
	 * @param currentGameIN
	 * @throws IOException
	 */
	public void loadPlayer2Setup(Game currentGameIN) throws IOException {

		this.currentGame = currentGameIN;

		currentGame.setGameState(GameState.PLR2SETUP);

		player2PlayfieldGridPane = currentGame.getPlayer2GameUIplayfield().getPlayfieldGridPane();

		player2Playfield = currentGame.getPlayer2GameUIplayfield().getPlayfield();

		dragAndDropUIPlayer2 = new GameUIdragAndDrop(guiController, informationText, informationTextColor, currentGame,
				this, currentGame.getPlayer2(), player2PlayfieldGridPane, player2Playfield, player2Fleet);

		leftAnchor.getChildren().clear();

		leftAnchor.getChildren().addAll(dragAndDropUIPlayer2.getPa());

		centerAnchor.getChildren().clear();

		// Asetetaan pelaajan 1 pelikenttä (GridPane) käyttöliittymään
		centerAnchor.getChildren().addAll(currentGame.getPlayer2GameUIplayfield().getPlayfieldGridPane());
	}

	/**
	 * Tallennetaan pelaajan 2 laivojen asettelu.
	 * 
	 * @param player2PlayfieldGridPane
	 * @param currentGameIN
	 */
	public void savePlayer2Setup(GridPane player2PlayfieldGridPane, Game currentGameIN) {

		// System.out.println("debug (masterController): savePlayer2Setup()");

		this.currentGame = currentGameIN;

		currentGame.getPlayer2GameUIplayfield().setPlayfieldGridPane(player2PlayfieldGridPane);

		try {
			startGame(currentGame);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPlayer1Fleet(Fleet playerXFleet) {
		this.player1Fleet = playerXFleet;
	}

	public void setPlayer2Fleet(Fleet playerXFleet) {
		this.player2Fleet = playerXFleet;
	}

	public Fleet getPlayer1Fleet() {
		return player1Fleet;
	}

	public Fleet getPlayer2Fleet() {
		return player2Fleet;
	}

	/**
	 * Siivotaan ja ladataan uudestaan pelikenttä, kun käyttäjä
	 * painaa Reset-nappia.
	 * 
	 * @param currentGameIN
	 */
	public void relocateButtonResetPlayfield(Game currentGameIN) {

		currentGame = currentGameIN;

		if (currentGame.getGameState().equals(GameState.PLR1SETUP)) { // PLAYER 1

			GameUIplayfield player1GameUIplayfield = new GameUIplayfield(
					currentGame.getGameZoneSizeSetting().getDimension());

			currentGame.setPlayer1GameUIplayfield(player1GameUIplayfield);

			player1Playfield = currentGame.getPlayer1GameUIplayfield().getPlayfield();

			player1PlayfieldGridPane = currentGame.getPlayer1GameUIplayfield().getPlayfieldGridPane();

			try {
				dragAndDropUIPlayer1 = new GameUIdragAndDrop(guiController, informationText, informationTextColor,
						currentGame, this, currentGame.getPlayer1(), player1PlayfieldGridPane, player1Playfield,
						player1Fleet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			centerAnchor.getChildren().clear();
			leftAnchor.getChildren().clear();

			leftAnchor.getChildren().addAll(dragAndDropUIPlayer1.getPa());
			centerAnchor.getChildren().addAll(currentGame.getPlayer1GameUIplayfield().getPlayfieldGridPane());

		} else if (currentGame.getGameState().equals(GameState.PLR2SETUP)) { // PLAYER 2

			GameUIplayfield player2GameUIplayfield = new GameUIplayfield(
					currentGame.getGameZoneSizeSetting().getDimension());

			currentGame.setPlayer2GameUIplayfield(player2GameUIplayfield);

			player2Playfield = currentGame.getPlayer2GameUIplayfield().getPlayfield();

			player2PlayfieldGridPane = currentGame.getPlayer2GameUIplayfield().getPlayfieldGridPane();

			try {
				dragAndDropUIPlayer2 = new GameUIdragAndDrop(guiController, informationText, informationTextColor,
						currentGame, this, currentGame.getPlayer2(), player2PlayfieldGridPane, player2Playfield,
						player1Fleet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			centerAnchor.getChildren().clear();
			leftAnchor.getChildren().clear();

			leftAnchor.getChildren().addAll(dragAndDropUIPlayer2.getPa());
			centerAnchor.getChildren().addAll(currentGame.getPlayer2GameUIplayfield().getPlayfieldGridPane());
		}
	}

	/**
	 * Käynnistetään peli, kun molempien pelaajien laivojen asettelu on valmis.
	 * 
	 * @param currentGameIN
	 */
	public void startGame(Game currentGameIN) {

		currentGame = currentGameIN;

		if (currentGame.getGameState() != GameState.PLR2SETUP) {
			throw new Error("debug (masterController) startGame: Invalid GameState. Must be PLR2SETUP");
		}

		System.out.println("debug (masterController) startGame: startGame launched");

		player2Playfield.setGameState(currentGame.getGameState()); // annetaan tähän koska plr2Playfield generoi
																	// opponent-näkymän

		loadSwitchPlayerScreen(currentGame);

	}

	/**
	 * Siirrytään pelaajan 1 vuoroon. Metodi loadSwitchPlayerScreen ja siinä oleva if-lohko lataa käyttöliittymänäkymän.
	 * @param currentGameIN
	 */
	public void nextTurnPlayer1(Game currentGameIN) {

		loadSwitchPlayerScreen(currentGame);

	}

	/**
	 * Siirrytään pelaajan 2 vuoroon. Metodi loadSwitchPlayerScreen ja siinä oleva if-lohko lataa käyttöliittymänäkymän.
	 * 
	 * @param currentGameIN
	 */
	public void nextTurnPlayer2(Game currentGameIN) {

		loadSwitchPlayerScreen(currentGame);

	}

	/**
	 * 
	 * Jommankumman pelaajan kaikki alukset on upotettu ja peli päättyy. Ladataan lopetusnäkymä.
	 * 
	 * @param currentGameIN
	 * @param winner
	 */
	public void gameOver(Game currentGameIN, PlayerNumber winner) {

		final Stage gameOverPopupStage = new Stage();
		gameOverPopupStage.initModality(Modality.APPLICATION_MODAL);
		
		ArrayList<String> endPicFileNames = new ArrayList<String>();
		endPicFileNames.add("endPic1.jpg");
		endPicFileNames.add("endPic2.jpg");
		endPicFileNames.add("endPic3.jpg");
		endPicFileNames.add("endPic4.jpg");
		
		Random pickImage = new Random();
		int randomPicIndex = pickImage.nextInt(4);
		
		Image endImage = new Image(
				getClass().getResourceAsStream("/kayttoliittymat/laivanupotus/" + endPicFileNames.get(randomPicIndex)));
		ImageView endImageView = new ImageView(endImage);
		endImageView.setFitWidth(400.0);
		endImageView.setFitHeight(320.0);		
		
		VBox gameOverVbox = new VBox(20);

		Text gameOverText = new Text();		


		if (winner.equals(PlayerNumber.PLAYER1)) {
			gameOverText.setText(currentGame.getPlayer1().getName() + " won!");
		} else {
			gameOverText.setText(currentGame.getPlayer2().getName() + " won!");
		}

		gameOverText.setStyle("-fx-font-size: 24px");
		Button newGameButton = new Button("Start new game");
		Button endGameButton = new Button("Exit");
		newGameButton.setStyle("-fx-font-size: 24px");
		newGameButton.setMinSize(80, 30);
		endGameButton.setStyle("-fx-font-size: 24px");
		endGameButton.setMinSize(80, 30);

		// Käynnistetään uusi peli
		newGameButton.setOnAction(e -> {
			
			clearCenterAndLeft();
			bottomAnchor.getChildren().clear();
			
			player1ShotHitAmount.setValue(0);
			player1ShotTotalAmount.setValue(0);
			
			player2ShotHitAmount.setValue(0);
			player2ShotTotalAmount.setValue(0);
			

			gameOverPopupStage.close();
			
			loadRootGameUI(rootStage);
			
		});

		// Sammutetaan ohjelma.
		endGameButton.setOnAction(e -> {

			gameOverPopupStage.close();
			
			Platform.exit();

		});

		HBox buttonHbox = new HBox(endGameButton, newGameButton);

		gameOverVbox.getChildren().addAll(endImageView, gameOverText, buttonHbox);
		
		gameOverText.setTextAlignment(TextAlignment.CENTER);
		buttonHbox.setAlignment(Pos.CENTER);
		
		buttonHbox.setSpacing(15);


		gameOverVbox.setAlignment(Pos.CENTER);

		Scene gameOverScene = new Scene(gameOverVbox, 600, 480);
		gameOverPopupStage.setTitle("Game over");
		gameOverPopupStage.setScene(gameOverScene);
		gameOverPopupStage.show();

	}


	/**
	 * Muodostetaan reaktiiviset sidokset käyttöliittymäelementtien välille.
	 */
	private void setBindings() {
		informationText.bind(Bindings.when(alwaysTrue).then("Waiting player....").otherwise("Waiting player...."));
		informationTextColor.bind(Bindings.when(alwaysTrue).then("red").otherwise("red"));

		informationText.bindBidirectional(informationTextSimple);
		informationTextColor.bindBidirectional(informationTextColorSimple);
	}
}
