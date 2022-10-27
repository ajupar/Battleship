package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import kayttoliittymat.laivanupotus.game.Fleet;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.playfield.Playfield;

/**
 * Raahaustilaa mallintava luokka.
 *
 */
public class GameUIdragAndDrop extends VBox {

	//https://stackoverflow.com/questions/14359763/setting-a-class-controller-for-anchor-pane
	//https://www.javatips.net/api/intellij-community-master/plugins/javaFX/testData/highlighting/injected/FooVBox.java
	//https://edencoding.com/location-not-set/
	
	private Parent parent;
	
	public GameUIdragAndDrop(GameUIController controller, SimpleStringProperty informationTextIN, 
			SimpleStringProperty informationTextColorIN, Game currentGameIN, 
			MasterController masterControllerIN, Player playerIN, GridPane playerXPlayfieldGridPaneIN,
			Playfield playerXPlayfieldIN, Fleet playerXFleetIN) throws IOException  {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kayttoliittymat/laivanupotus/GameUI-10-dragdrop.fxml"));
		System.out.println("debug (GameUIdragAndDrop.java): Ladattu: GameUI-10-dragdrop.fxml");
        //fxmlLoader.setRoot(this);
		//fxmlLoader.setController(this);
		
		GameUIdragAndDropController guidragdropController = new GameUIdragAndDropController(controller, informationTextIN, 
        		informationTextColorIN, currentGameIN, masterControllerIN, playerIN, playerXPlayfieldGridPaneIN,
        		playerXPlayfieldIN, playerXFleetIN);
		
        fxmlLoader.setController(guidragdropController);
        parent = fxmlLoader.load();
	}
	
	public Parent getPa() {
		
        return parent;
    }
}
