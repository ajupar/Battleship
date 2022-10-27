package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kayttoliittymat.laivanupotus.MainApp;

/**
 * Pelin pohjana olevan käyttöliittymän mallintava luokka.
 *
 */
public class GameUI {
	
	private Stage stage;
	
	private Parent parent;
	
	public GameUI(Stage rootStageIN, MasterController masterControllerIN) {
		
		this.stage = rootStageIN;		
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kayttoliittymat/laivanupotus/GameUI-00-MAIN.fxml"));
    	
        // Scene scene = new Scene(loadFXML("GameUI-00-MAIN.fxml"));
        System.out.println("debug (MainApp.java): Ladattu: GameUI-00-MAIN.fxml");
        
        fxmlLoader.setController(new GameUIController(rootStageIN, masterControllerIN));
        
        stage.setTitle("Battleship");
        
        try {
			parent = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml));
        return fxmlLoader.load();
    }
    
    public Parent getPa() {
    	return this.parent;
    }
	
	

}
