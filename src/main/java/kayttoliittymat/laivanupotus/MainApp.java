package kayttoliittymat.laivanupotus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kayttoliittymat.laivanupotus.game.Game;
import kayttoliittymat.laivanupotus.ui.GameUI;
import kayttoliittymat.laivanupotus.ui.GameUIsettings;
import kayttoliittymat.laivanupotus.ui.MasterController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	
    	MasterController masterController = new MasterController(); 	
    	
    	// Pelin ajaminen käynnistyy tästä. MasterController huolehtii pelin kulusta.
    	masterController.loadRootGameUI(stage);

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml));
        return fxmlLoader.load();
    }

//    public static void main(String[] args) {
//        launch();
//    }

}