package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable {

    @FXML
    private TextField blueName;

    @FXML
    private TextField greenName;

    @FXML
    private TextField redName;

    @FXML
    private TextField yellowName;

    @FXML
    private Button startGameButton;

    @FXML
    private CheckBox blueBot;

    @FXML
    private CheckBox greenBot;

    @FXML
    private CheckBox redBot;

    @FXML
    private CheckBox yellowBot;

    static String[] names = new String[4];
    String bluePlayer = "Player1";
    String greenPlayer = "Player2";
    String redPlayer = "Player3";
    String yellowPlayer = "Player4";

    static boolean[] bots = new boolean[4];


    @FXML
    private void startGame(ActionEvent event) throws IOException {

        bluePlayer = blueName.getText();
        greenPlayer = greenName.getText();
        redPlayer = redName.getText();
        yellowPlayer = yellowName.getText();

        MenuController.names[0] = bluePlayer;
        MenuController.names[1] = greenPlayer;
        MenuController.names[2] = redPlayer;
        MenuController.names[3] = yellowPlayer;

        bots[0] = false; //blueBot.isSelected();
        bots[1] = false; //greenBot.isSelected();
        bots[2] = false; //redBot.isSelected();
        bots[3] = false; //yellowBot.isSelected();


        //close menuWindow
        Stage menuStage = (Stage) startGameButton.getScene().getWindow();
        menuStage.close();

        //Open game window
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("game.fxml"));
        Scene scene = new Scene(fxmlloader.load());
        Stage gameStage = new Stage();
        gameStage.setScene(scene);
        gameStage.setTitle("Človeče nezlob se HRACÍ DESKA");
        gameStage.show();

    }

    public static String[] getNames(){
        return names;
    }
    public static boolean[] getBots(){return bots;}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
