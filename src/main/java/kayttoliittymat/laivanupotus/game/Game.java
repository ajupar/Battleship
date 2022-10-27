package kayttoliittymat.laivanupotus.game;

import java.util.HashMap;

import javafx.scene.layout.GridPane;
import kayttoliittymat.laivanupotus.playfield.Playfield;
import kayttoliittymat.laivanupotus.ui.GameUIplayfield;
import kayttoliittymat.laivanupotus.ui.GameZoneSize;


/**
 * Peliä hallinnoiva luokka, jossa mm. asetukset.
 * Säilyttää tiedon pelin tilasta.
 *
 */
public class Game {
	
	// Eri pelitilat, joihin pelin kulun aikana edetään. public, koska tarvitaan kaikissa pelin paketeissa
	public enum GameState {SETTINGS, PLR1SETUP, PLR2SETUP, PLR1TURN, PLR2TURN, ENDSCREEN};
	// Selkeyden vuoksi enumit Player1:lle ja Player2:lle
	public enum PlayerNumber { PLAYER1, PLAYER2 }	
	// enumit laivatyypeille, auttaa pelitiedon käsittelyssä
	public enum ShipType {CARRIER, BATTLESHIP, CRUISER, SUBMARINE, DESTROYER}
	

	private Player player1;
	private Player player2;
	
	
	// Asetuksissa (Combobox) määritetty pelialueen koko
	private GameZoneSize gameZoneSizeSetting;
	
	// pelin tämänhetkinen tila; avuksi käyttöliittymän ja pelimekaniikan synkronoinnissa?
	private GameState currentGameState = GameState.SETTINGS;
	
	// Peliruudukon leveys * korkeus
	private int[] gridSize = new int[2];

	private GameUIplayfield player1GameUIplayfield;  // säilöö sisäänsä Playfield-olion ja pystyy muodostamaan GridPanen
	private GameUIplayfield player2GameUIplayfield;
	
	private GridPane player1PlayfieldOpponentView;
	private GridPane player2PlayfieldOpponentView;
	
	private int[] vesselAmounts;
	
	/**
	 * Konstruktori, jossa säädetään kaikki asetuksissa asetettavat pelin parametrit
	 */
	public Game(Player player1IN, Player player2IN, GameZoneSize gzs, 
			GameUIplayfield player1GameUIplayfieldIN, GameUIplayfield player2GameUIplayfieldIN, int[] vesselAmountsIN) {
		this.player1 = player1IN;
		this.player2 = player2IN;
		this.gameZoneSizeSetting = gzs;
//		this.player1Playfield = player1PlayfieldIN;
//		this.player2Playfield = player2PlayfieldIN;
		this.setPlayer1GameUIplayfield(player1GameUIplayfieldIN);
		this.setPlayer2GameUIplayfield(player2GameUIplayfieldIN);
		this.vesselAmounts = vesselAmountsIN;
	}
	
	public int[] getVesselAmounts() {
		return vesselAmounts;
	}

	public int[] getGridSize() {
		return this.gridSize;
	}
	
	public void setGameZoneSizeSetting(GameZoneSize gzs) {
		this.gameZoneSizeSetting = gzs;
		
	}
	
	public GameZoneSize getGameZoneSizeSetting() {
		return this.gameZoneSizeSetting;
	}
	
	public void setGameState(GameState state) {
		this.currentGameState = state;
	}
	
	public GameState getGameState() {
		return this.currentGameState;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		System.out.println("debug (Game) setPlayer1: Player1 name set.");
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		System.out.println("debug (Game) setPlayer2: Player2 name set.");
		this.player2 = player2;
	}


	public GameUIplayfield getPlayer1GameUIplayfield() {
		return player1GameUIplayfield;
	}


	public void setPlayer1GameUIplayfield(GameUIplayfield player1GameUIplayfield) {
		this.player1GameUIplayfield = player1GameUIplayfield;
	}


	public GameUIplayfield getPlayer2GameUIplayfield() {
		return player2GameUIplayfield;
	}


	public void setPlayer2GameUIplayfield(GameUIplayfield player2GameUIplayfield) {
		this.player2GameUIplayfield = player2GameUIplayfield;
	}

	public GridPane getPlayer1PlayfieldOpponentView() {
		return player1PlayfieldOpponentView;
	}

	public void setPlayer1PlayfieldOpponentView(GridPane player1PlayfieldOpponentView) {
		this.player1PlayfieldOpponentView = player1PlayfieldOpponentView;
	}

	public GridPane getPlayer2PlayfieldOpponentView() {
		return player2PlayfieldOpponentView;
	}

	public void setPlayer2PlayfieldOpponentView(GridPane player2PlayfieldOpponentView) {
		this.player2PlayfieldOpponentView = player2PlayfieldOpponentView;
	}
}
