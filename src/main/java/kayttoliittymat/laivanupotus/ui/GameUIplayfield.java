package kayttoliittymat.laivanupotus.ui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import kayttoliittymat.laivanupotus.playfield.*;

/**
 * Pelikenttää mallintava luokka käyttöliittymän tasolla.
 *
 */
public class GameUIplayfield {
	
	private Playfield playfield;  // vastaava Playfield-olio (sis. pelitietoja ja tapahtumankäsittelijöitä)
	private GridPane playfieldGridPane;  // vastaava GridPane-olio (peliruudukko)
		
	public GameUIplayfield(int gameZoneDimension) {
		
		//System.out.println("debug (GameUIplayfield): PlayField created.");		
		
		playfield = new Playfield(gameZoneDimension);
		playfieldGridPane = playfield.getPlayfieldGridPane();
	}
	
	@SuppressWarnings("exports")
	public GridPane getPlayfieldGridPane() {
		return this.playfieldGridPane;
	}
	
	public Playfield getPlayfield() {
		return this.playfield;
	}
	
	public void setPlayfieldGridPane(GridPane g) {
		this.playfieldGridPane = g;
	}
	
	public void setPlayfield(Playfield p) {
		this.playfield = p;
	}

}
