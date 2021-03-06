package gamePackage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class RulesController implements Initializable {

    @FXML
    private TextArea rulesArea;

    @FXML
    public void closeRules(ActionEvent event){
        Stage rulesStage = (Stage) rulesArea.getScene().getWindow();
        rulesStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            File rulesFile = new File("rules.txt");
            Scanner reader = new Scanner(rulesFile);
            while (reader.hasNextLine()) {
                String text = reader.nextLine();
                rulesArea.appendText(text);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        rulesArea.setScrollTop(Double.MAX_VALUE);
    }
}
