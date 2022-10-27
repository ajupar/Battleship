package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Game.PlayerNumber;
import kayttoliittymat.laivanupotus.playfield.Playfield;

/**
 * Ampumistilan aikana vasemmanpuoleista UI-näkymää mallintava luokka
 * Näyttää pelitilastot
 *
 */
public class GameUIstatistics extends VBox {

	//https://stackoverflow.com/questions/14359763/setting-a-class-controller-for-anchor-pane
	//https://www.javatips.net/api/intellij-community-master/plugins/javaFX/testData/highlighting/injected/FooVBox.java
	//https://edencoding.com/location-not-set/
	
	private Parent parent;
	
	public GameUIstatistics(SimpleStringProperty informationText, 
			SimpleStringProperty informationTextColor, Game currentGame, PlayerNumber plrNumberX,
			MasterController masterControllerIN, Playfield ownPlayfieldIN, Playfield opponentPlayfieldIN) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kayttoliittymat/laivanupotus/GameUI-15-statistics.fxml"));
		//System.out.println("debug (GameUIstatistics.java): Ladattu: GameUI-15-statistics.fxml");
        //fxmlLoader.setRoot(this);
        //fxmlLoader.setController(this);
        fxmlLoader.setController(new GameUIstatisticsController(informationText, informationTextColor, currentGame, 
        		plrNumberX, masterControllerIN, ownPlayfieldIN, opponentPlayfieldIN));
        parent = fxmlLoader.load();
	}
		
	public Parent getPa() {
		
        return parent;
    }
}
