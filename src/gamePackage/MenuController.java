package gamePackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private AnchorPane anchorBackground;

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
        loadFXML("game.fxml", "Člověče nezlob se HRACÍ PLOCHA");

    }

    @FXML
    public void rulesButton(ActionEvent event) throws IOException {

        loadFXML("rules.fxml", "Pravidla hry");
    }

    public void loadFXML(String fileName, String title) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource(fileName));
        Scene scene = new Scene(fxmlloader.load());
        Stage gameStage = new Stage();
        gameStage.setScene(scene);
        gameStage.setTitle(title);
        gameStage.show();
    }

    public static String[] getNames(){
        return names;
    }
    public static boolean[] getBots(){return bots;}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            FileInputStream input = new FileInputStream("cubesBackground.jpg");

            // create a image
            Image image = new Image(input);

            // create a background image
            BackgroundImage backgroundimage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(2880, 1800, true, true, true, true));

            // create Background
            Background background = new Background(backgroundimage);
            anchorBackground.setBackground(background);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
