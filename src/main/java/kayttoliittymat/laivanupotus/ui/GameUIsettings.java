package kayttoliittymat.laivanupotus.ui;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * Peliasetusten säätämistilaa vastaava käyttöliittymäluokka.
 *
 */
public class GameUIsettings extends VBox {
	
	//https://stackoverflow.com/questions/14359763/setting-a-class-controller-for-anchor-pane
	//https://www.javatips.net/api/intellij-community-master/plugins/javaFX/testData/highlighting/injected/FooVBox.java
	//https://edencoding.com/location-not-set/
	
	private Parent parent;
	
	public GameUIsettings(GameUIController controller, SimpleStringProperty informationText, 
			SimpleStringProperty informationTextColor, MasterController masterControllerIN) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kayttoliittymat/laivanupotus/GameUI-05-settings.fxml"));
		System.out.println("debug (GameUIsettings.java): Ladattu: GameUI-05-settings.fxml");
		//fxmlLoader.setRoot(this);
        //fxmlLoader.setController(this);
        fxmlLoader.setController(new GameUIsettingsController(controller, informationText, informationTextColor, masterControllerIN));
        parent = fxmlLoader.load();
	}
	
	public Parent getPa() {
		
        return parent;
    }
}
