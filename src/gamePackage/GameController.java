package gamePackage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class GameController implements Initializable {

    @FXML
    private GridPane gridPane;

    @FXML
    private Label playerOnTheMoveLabel;

    @FXML
    private TextArea textAreaInfo;

    @FXML
    private ImageView cubePicture;

    @FXML
    private ImageView imageInfo;

    @FXML
    private Label blueName;

    @FXML
    private Label greenName;

    @FXML
    private Label redName;

    @FXML
    private Label yellowName;

    @FXML
    private Label titleInfo;

    @FXML
    private TextField cubeHack;

    @FXML
    private Label winnerListTitle;

    @FXML
    private ListView winnerListArea;

    @FXML
    private CheckBox checkInfo;

    @FXML
    private Button backToMenu;


    Player clickedPlayer;
    private Circle clickedCircle;
    int cube;

    Paint colorBlue = Paint.valueOf("0x0000fff0");
    Paint colorGreen = Paint.valueOf("0x70f109ff");
    Paint colorRed = Paint.valueOf("0xff0000ff");
    Paint colorYellow = Paint.valueOf("0xffff00ff");
    Paint colorWhite = Paint.valueOf("0x1f93ff00");

    Player bluePlayer;
    Player redPlayer;
    Player greenPlayer;
    Player yellowPlayer;

    Player onTheMovePlayer;
    Boolean cubeThrown = false;

    ArrayList<Player> playersList;
    Image[] images = new Image[6];

    Image[] pawnsImage = new Image[4];
    Image cubeInfoImage;

    @FXML
    public void showHideInfo() {
        System.out.println("ShowHideInfo()");
        if (checkInfo.isSelected()) {
            titleInfo.setVisible(true);
            textAreaInfo.setVisible(true);
        } else {
            titleInfo.setVisible(false);
            textAreaInfo.setVisible(false);
        }
    }

    @FXML
    public void showRules(ActionEvent event) throws IOException {
        System.out.println("showRules()");
        showNewWindow("rules.fxml", "Pravidla hry");
    }

    public void showNewWindow(String fileName, String title) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource(fileName));
        Scene scene = new Scene(fxmlloader.load());
        Stage gameStage = new Stage();
        gameStage.setScene(scene);
        gameStage.setTitle(title);
        gameStage.show();
    }

    @FXML
    public void buttonExit(ActionEvent event) {

        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Doopravdy chceš odejít? Hra bude smazána", ButtonType.YES, ButtonType.NO);
        exitAlert.showAndWait();

        if (exitAlert.getResult() == ButtonType.YES) {
            Platform.exit();
            System.exit(0);
        }
    }

    @FXML
    public void resetGame(ActionEvent event) throws InterruptedException {
        System.out.println("resetGame()");
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Chceš resetovat hru? Aktuální hra bude smazána", ButtonType.YES, ButtonType.NO);
        exitAlert.showAndWait();

        if (exitAlert.getResult() == ButtonType.YES) {

            Player[] playersToReset = new Player[4];
            playersToReset[0] = bluePlayer;
            playersToReset[1] = greenPlayer;
            playersToReset[2] = redPlayer;
            playersToReset[3] = yellowPlayer;

            for (int i = 0; i < playersToReset.length; i++) {
                Player resetPlayer = playersToReset[i];

                getCircle("#" + resetPlayer.getPawn1()).setFill(Color.WHITE);
                getCircle("#" + resetPlayer.getPawn2()).setFill(Color.WHITE);
                getCircle("#" + resetPlayer.getPawn3()).setFill(Color.WHITE);
                getCircle("#" + resetPlayer.getPawn4()).setFill(Color.WHITE);

                resetPlayer.setPawn1(resetPlayer.getHome1());
                getCircle("#" + resetPlayer.getHome1()).setFill(resetPlayer.getColor());
                resetPlayer.setPawn2(resetPlayer.getHome2());
                getCircle("#" + resetPlayer.getHome2()).setFill(resetPlayer.getColor());
                resetPlayer.setPawn3(resetPlayer.getHome3());
                getCircle("#" + resetPlayer.getHome3()).setFill(resetPlayer.getColor());
                resetPlayer.setPawn4(resetPlayer.getHome4());
                getCircle("#" + resetPlayer.getHome4()).setFill(resetPlayer.getColor());

                resetPlayer.setIsWinner(false);
            }

            textAreaInfo.clear();
            onTheMovePlayer = null;
            changePlayerOnTheMove();

            winnerListArea.setVisible(false);
            winnerListTitle.setVisible(false);
            winnerListArea.getItems().removeAll();
        }
    }

    @FXML
    public void backToMenuButton(ActionEvent event) throws IOException {

        System.out.println("backToMenuButton()");

        Alert backToMenuAlert = new Alert(Alert.AlertType.CONFIRMATION, "Doopravdy chceš odejít do menu? Hra bude smazána", ButtonType.YES, ButtonType.NO);
        backToMenuAlert.showAndWait();

        if (backToMenuAlert.getResult() == ButtonType.YES) {
            Stage gameStage = (Stage) backToMenu.getScene().getWindow();
            gameStage.close();

            showNewWindow("menu.fxml", "Menu");
        }
    }


    @FXML
    public void buttonCube() throws InterruptedException {
        System.out.println("buttonCube()");

        if(onTheMovePlayer == bluePlayer){imageInfo.setImage(pawnsImage[0]);}
        else if(onTheMovePlayer == greenPlayer){imageInfo.setImage(pawnsImage[1]);}
        else if(onTheMovePlayer == redPlayer){imageInfo.setImage(pawnsImage[2]);}
        else{imageInfo.setImage(pawnsImage[3]);}

        Random rd = new Random();

        if (!cubeThrown) {
            cube = rd.nextInt(6) + 1;
            //cube = Integer.parseInt(cubeHack.getText());

            cubePicture.setImage(images[cube - 1]);
            cubeThrown = true;

            if (!canMove(cube)) {
                textAreaInfo.appendText(onTheMovePlayer.getName() + " nemáš jak pohnout \n");
                changePlayerOnTheMove();
            } else {
                textAreaInfo.appendText(onTheMovePlayer.getName() + " pohni figurkou\n");
            }
        } else { /*System.out.println("You can throw cube just once!");*/}
    }

    @FXML
    public void handle(MouseEvent mouseEvent) throws InterruptedException {
        System.out.println("handle()");

        if (!cubeThrown) {
            textAreaInfo.appendText(onTheMovePlayer.getName() + " musíš hodit kostkou \n");
            return;
        }

        clickedCircle = (Circle) mouseEvent.getSource();
        if(!clickedPawn(clickedCircle)){
            return;
        }
        cubeThrown = false;
    }

    public boolean clickedPawn(Circle circle) throws InterruptedException {
        System.out.println("clickedPawn()");
        clickedCircle = circle;

        if (colorBlue.equals(clickedCircle.getFill())) {
            clickedPlayer = bluePlayer;
        } else if (colorGreen.equals(clickedCircle.getFill())) {
            clickedPlayer = greenPlayer;
        } else if (colorRed.equals(clickedCircle.getFill())) {
            clickedPlayer = redPlayer;
        } else if (colorYellow.equals(clickedCircle.getFill())) {
            clickedPlayer = yellowPlayer;
        } else {
            return false;
        }
        if (clickedPlayer == onTheMovePlayer) {
            if (clickedCircle.getId().equals(clickedPlayer.getHome1()) || clickedCircle.getId().equals(clickedPlayer.getHome2()) ||
                    clickedCircle.getId().equals(clickedPlayer.getHome3()) || clickedCircle.getId().equals(clickedPlayer.getHome4())) {
                if (cube == 6) {
                    respawn(clickedCircle, clickedPlayer);
                } else { return false; }
            } else if (clickedCircle.getId().equals(clickedPlayer.getFinish1()) || clickedCircle.getId().equals(clickedPlayer.getFinish2()) ||
                    clickedCircle.getId().equals(clickedPlayer.getFinish3()) || clickedCircle.getId().equals(clickedPlayer.getFinish4())) {
                if (!moveInsideFinish()) {
                    return false;
                }
            } else if (Integer.parseInt(clickedCircle.getId()) >= 0) {
                if(!movePawn()){
                    return false;
                }
            }
        } else {
            return false; //This player is not on the move
        }
        changePlayerOnTheMove();
        return true;
    }

    public boolean moveInsideFinish() {
        System.out.println("moveInsideFinish()");
        String[] pawns = new String[4];
        pawns[0] = onTheMovePlayer.getPawn1();
        pawns[1] = onTheMovePlayer.getPawn2();
        pawns[2] = onTheMovePlayer.getPawn3();
        pawns[3] = onTheMovePlayer.getPawn4();

        String[] idSplit = clickedCircle.getId().split("-");

        int numberId = Integer.parseInt(idSplit[1]);
        int futureId = numberId + cube;
        String futureIdString = idSplit[0] + "-" + futureId;

        if (futureId > 3) { return false; } // if is out of home

        for (int i = 0; i < 4; i++) {
            if (("#" + futureIdString).equals(pawns[i])) { return false; //home is occupied
            } else {

                if (onTheMovePlayer.getPawn1().equals(clickedCircle.getId())) { //pawn1
                    getCircle("#" + onTheMovePlayer.getPawn1()).setFill(Color.WHITE);
                    onTheMovePlayer.setPawn1(futureIdString);
                    getCircle("#" + futureIdString).setFill(onTheMovePlayer.getColor());
                } else if (onTheMovePlayer.getPawn2().equals(clickedCircle.getId())) { //pawn2
                    getCircle("#" + onTheMovePlayer.getPawn2()).setFill(Color.WHITE);
                    onTheMovePlayer.setPawn2(futureIdString);
                    getCircle("#" + futureIdString).setFill(onTheMovePlayer.getColor());
                } else if (onTheMovePlayer.getPawn3().equals(clickedCircle.getId())) { //pawn3
                    getCircle("#" + onTheMovePlayer.getPawn3()).setFill(Color.WHITE);
                    onTheMovePlayer.setPawn3(futureIdString);
                    getCircle("#" + futureIdString).setFill(onTheMovePlayer.getColor());
                } else if (onTheMovePlayer.getPawn4().equals(clickedCircle.getId())) { //pawn4
                    getCircle("#" + onTheMovePlayer.getPawn4()).setFill(Color.WHITE);
                    onTheMovePlayer.setPawn4(futureIdString);
                    getCircle("#" + onTheMovePlayer.getPawn4()).setFill(onTheMovePlayer.getColor());
                }
            }
        }
        return true; //can move inside home
    }

    public void printInfo(Player printPlayer) {
        System.out.println("INFO************************");
        System.out.println("Color: " + printPlayer.getColor());
        System.out.println("Name: " + printPlayer.getName());
        System.out.println("Pawn1: " + printPlayer.getPawn1());
        System.out.println("Pawn2: " + printPlayer.getPawn2());
        System.out.println("Pawn3: " + printPlayer.getPawn3());
        System.out.println("Pawn4: " + printPlayer.getPawn4());
        System.out.println(" Is winner: " + printPlayer.getIsWinner());
        System.out.println("*****************************");
    }

    public boolean movePawn() {
        System.out.println("movePawn()");
        String lastId = clickedCircle.getId();
        int futureId = Integer.parseInt(lastId) + cube;

        if (futureId > 39 && clickedPlayer != bluePlayer) {
            futureId += -40;
        }

        if (futureId > Integer.parseInt(clickedPlayer.getLastPlace()) && Integer.parseInt(lastId) <= Integer.parseInt(clickedPlayer.getLastPlace()) &&
                Integer.parseInt(lastId) > (Integer.parseInt(clickedPlayer.getLastPlace()) - 10)) {
            if(!goHome()){ return false; }
        } else {
            checkTaking(String.valueOf(futureId));
            changeId(lastId, String.valueOf(futureId));
            getCircle("#" + clickedCircle.getId()).setFill(colorWhite);
            getCircle("#" + futureId).setFill(clickedPlayer.getColor());
        }
        return true;
    }

    public boolean goHome() {
        System.out.println("goHome()");
        String lastId = clickedCircle.getId();
        int futureId = Integer.parseInt(lastId) + (cube - 1) - Integer.parseInt(clickedPlayer.getLastPlace()); // CUBE!!
        String futureIdString = clickedPlayer.getColorString() + "F-" + futureId;
        String futureIdStringSave = clickedPlayer.getColorString() + "F-" + futureId;

        if (futureId > 3) {
            //System.out.println("I am out of the home");
            return false;
        } else {
            if (clickedPlayer.getPawn1().equals(futureIdString) || clickedPlayer.getPawn2().equals(futureIdString) ||
                    clickedPlayer.getPawn3().equals(futureIdString) || clickedPlayer.getPawn4().equals(futureIdString)) {
                return false;
            } else {
                //going home
                clickedCircle.setFill(colorWhite);
                getCircle("#" + futureIdString).setFill(clickedPlayer.getColor());
                changeId(lastId, futureIdStringSave);
            }
        }
        checkWinner();
        return true;
    }

    public void checkTaking(String futureId) {

        System.out.println("checkTaking()");

        if (!getCircle("#" + futureId).getFill().equals(colorWhite)) {

            Player checkTakingPlayer;

            for (int i = 0; i < playersList.size(); i++) {
                checkTakingPlayer = playersList.get(i);

                if (checkTakingPlayer.getPawn1().equals(futureId)) {
                    getCircle("#" + futureId).setFill(colorWhite);
                    getCircle("#" + checkTakingPlayer.getHome1()).setFill(checkTakingPlayer.getColor());
                    checkTakingPlayer.setPawn1(checkTakingPlayer.getHome1());
                    printInfo(checkTakingPlayer);
                } else if (checkTakingPlayer.getPawn2().equals(futureId)) {
                    getCircle("#" + futureId).setFill(colorWhite);
                    getCircle("#" + checkTakingPlayer.getHome2()).setFill(checkTakingPlayer.getColor());
                    checkTakingPlayer.setPawn2(checkTakingPlayer.getHome2());
                    printInfo(checkTakingPlayer);
                } else if (checkTakingPlayer.getPawn3().equals(futureId)) {
                    getCircle("#" + futureId).setFill(colorWhite);
                    getCircle("#" + checkTakingPlayer.getHome3()).setFill(checkTakingPlayer.getColor());
                    checkTakingPlayer.setPawn3(checkTakingPlayer.getHome3());
                    printInfo(checkTakingPlayer);
                } else if (checkTakingPlayer.getPawn4().equals(futureId)) {
                    getCircle("#" + futureId).setFill(colorWhite);
                    getCircle("#" + checkTakingPlayer.getHome4()).setFill(checkTakingPlayer.getColor());
                    checkTakingPlayer.setPawn4(checkTakingPlayer.getHome4());
                    printInfo(checkTakingPlayer);
                }
            }
        }
    }

    public void changeId(String lastId, String futureId) {
        System.out.println("changeId()");
        if (clickedPlayer.getPawn1().equals(lastId)) {
            clickedPlayer.setPawn1(futureId);
        } else if (clickedPlayer.getPawn2().equals(lastId)) {
            clickedPlayer.setPawn2(futureId);
        } else if (clickedPlayer.getPawn3().equals(lastId)) {
            clickedPlayer.setPawn3(futureId);
        } else if (clickedPlayer.getPawn4().equals(lastId)) {
            clickedPlayer.setPawn4(futureId);
        }
    }

    public void respawn(Circle clickedCircle, Player actualPlayer) {
        System.out.println("respawn()");

        getCircle("#" + clickedCircle.getId()).setFill(colorWhite);

        String lastId = clickedCircle.getId();
        String futureId = clickedPlayer.getStartPlace();
        checkTaking(futureId);
        changeId(lastId, futureId);

        getCircle("#" + actualPlayer.getStartPlace()).setFill(actualPlayer.getColor());
    }

    int onTheMoveIndex = 0;

    public void changePlayerOnTheMove() throws InterruptedException {
        System.out.println("changePlayerOnTheMove()");
        if (onTheMovePlayer == null) {
            onTheMovePlayer = bluePlayer;
        } else {
            if (cube != 6) {
                onTheMoveIndex++;
            } else {

                if(canMove(cube) && cubeThrown){
                    // player will play again
                }else if(!canMove(cube) && cubeThrown){
                    onTheMoveIndex++;
                }
            }
            if (onTheMoveIndex >= playersList.size()) {
                onTheMoveIndex = 0;
            }
            onTheMovePlayer = playersList.get(onTheMoveIndex);

            while (onTheMovePlayer.getIsWinner()) {

                onTheMoveIndex++;

                if (onTheMoveIndex >= playersList.size()) {
                    onTheMoveIndex = 0;
                }
                onTheMovePlayer = playersList.get(onTheMoveIndex);

                if (bluePlayer.getIsWinner() && greenPlayer.getIsWinner() && redPlayer.getIsWinner() && yellowPlayer.getIsWinner()) {
                    textAreaInfo.appendText("Všichni hráči jsou v cíli \n");
                    textAreaInfo.appendText("Hra končí \n");
                    onTheMovePlayer = null;
                    Alert endGame = new Alert(Alert.AlertType.INFORMATION, "Hra končí. Všichi hráči došli do cíle", ButtonType.YES);
                    endGame.showAndWait();
                    return;
                }
            }

        }
        playerOnTheMoveLabel.setText(onTheMovePlayer.getName());
        playerOnTheMoveLabel.setTextFill(onTheMovePlayer.getColor());
        textAreaInfo.appendText("NYNÍ HRAJE  " + onTheMovePlayer.getName() + "\n");
        textAreaInfo.appendText(onTheMovePlayer.getName() + " hoď kostkou \n");
        cubeThrown = false;
        imageInfo.setImage(cubeInfoImage);
/*
        if (onTheMovePlayer.getIsBot()) {
            playBot();
        }
        */

    }

    public boolean canMoveFuture(){
        if(canMove(1) || canMove(2) || canMove(3) ||
                canMove(4) || canMove(5) || canMove(6)){
            System.out.println("In future he can move canMoveFuture()");
            return true;
        }else{
            System.out.println("In future he can not move");
            return false;
        }
    }

    public void playBot() throws InterruptedException {
        System.out.println("playBot()");

        buttonCube();

        if(canMove(cube)){
            //clickedPawn(getCircle(onTheMovePlayer.getPawn1()));
        }
            String[] pawns = new String[4];
            pawns[0] = onTheMovePlayer.getPawn1();
            pawns[1] = onTheMovePlayer.getPawn2();
            pawns[2] = onTheMovePlayer.getPawn3();
            pawns[3] = onTheMovePlayer.getPawn4();

            String[] futureIds = new String[4];
            futureIds[0] = null;
            futureIds[1] = null;
            futureIds[2] = null;
            futureIds[3] = null;
    }

    public boolean canMove(int actualCube) {
        System.out.println("canMove()");
        String[] finishes = new String[4];
        finishes[0] = "#" + onTheMovePlayer.getFinish1();
        finishes[1] = "#" + onTheMovePlayer.getFinish2();
        finishes[2] = "#" + onTheMovePlayer.getFinish3();
        finishes[3] = "#" + onTheMovePlayer.getFinish4();

        String[] homes = new String[4];
        homes[0] = onTheMovePlayer.getHome1();
        homes[1] = onTheMovePlayer.getHome2();
        homes[2] = onTheMovePlayer.getHome3();
        homes[3] = onTheMovePlayer.getHome4();

        String actualPawn;

        String[] pawns = new String[4];
        pawns[0] = onTheMovePlayer.getPawn1();
        pawns[1] = onTheMovePlayer.getPawn2();
        pawns[2] = onTheMovePlayer.getPawn3();
        pawns[3] = onTheMovePlayer.getPawn4();

        Boolean[] canMovePawns = new Boolean[4];
        canMovePawns[0] = true;
        canMovePawns[1] = true;
        canMovePawns[2] = true;
        canMovePawns[3] = true;

        for (int i = 0; i < 4; i++) {
            actualPawn = pawns[i];
            actualPawn = actualPawn.replace(" ", "");

            if (actualPawn == homes[i]) { // if pawn is in home (cube is 6 = can move, else can not move
                if (actualCube == 6) {
                    //System.out.println("PAWN" + (i + 1) + " is home and cube is 6");
                    canMovePawns[i] = true;
                } else {
                    //System.out.println("PAWN" + (i + 1) + " is home and cube is not 6");
                    canMovePawns[i] = false;
                }
            } else if (("#" + actualPawn).equals(finishes[0]) || ("#" + actualPawn).equals(finishes[1]) || ("#" + actualPawn).equals(finishes[2]) || ("#" + actualPawn).equals(finishes[3])) {
                System.out.println("PAWN" + (i + 1) + " is in finish");
                String[] splitId = pawns[i].split("-");
                int idInt = Integer.parseInt(splitId[1]);
                int futureIntId = idInt + actualCube;
                String futureIdString = splitId[0] + "-" + futureIntId;

                if (futureIntId > 3) {
                    canMovePawns[i] = false;
                } else {
                    //check if future position is occupied
                    for (int x = 0; x < 4; x++) {
                        if (futureIdString.equals(pawns[x])) {
                            canMovePawns[i] = false;
                        }
                    }
                }
            } else {
                int lastId = Integer.parseInt(actualPawn);
                int futureId = Integer.parseInt(actualPawn) + actualCube;
                if (lastId <= Integer.parseInt(onTheMovePlayer.getLastPlace()) && futureId >= Integer.parseInt(onTheMovePlayer.getStartPlace())) {
                    futureId = futureId - 1 - Integer.parseInt(onTheMovePlayer.getLastPlace());
                    if (futureId > 3) {
                        canMovePawns[i] = false;
                    } else {
                        String futureIdString = "#" + onTheMovePlayer.getFinishText() + futureId;

                        for (int x = 0; x < 4; x++) {
                            if (futureIdString.equals("#" + pawns[x])) {
                                canMovePawns[i] = false;
                            }
                        }
                    }
                }
            }
        }

        if (canMovePawns[0] || canMovePawns[1] || canMovePawns[2] || canMovePawns[3]) {
            return true;
        } else {
            return false;
        }
    }

    public void checkWinner() {
        System.out.println("checkWinner()");

        boolean pawn1Finish = false;
        boolean pawn2Finish = false;
        boolean pawn3Finish = false;
        boolean pawn4Finish = false;

        String[] finishes = new String[4];
        finishes[0] = onTheMovePlayer.getFinish1();
        finishes[1] = onTheMovePlayer.getFinish2();
        finishes[2] = onTheMovePlayer.getFinish3();
        finishes[3] = onTheMovePlayer.getFinish4();

        for (int i = 0; i < finishes.length; i++) {
            if (finishes[i].equals(onTheMovePlayer.getPawn1())) {
                pawn1Finish = true;
            }
        }

        for (int i = 0; i < finishes.length; i++) {
            if (finishes[i].equals(onTheMovePlayer.getPawn2())) {
                pawn2Finish = true;
            }
        }

        for (int i = 0; i < finishes.length; i++) {
            if (finishes[i].equals(onTheMovePlayer.getPawn3())) {
                pawn3Finish = true;
            }
        }

        for (int i = 0; i < finishes.length; i++) {
            if (finishes[i].equals(onTheMovePlayer.getPawn4())) {
                pawn4Finish = true;
            }
        }

        if (pawn1Finish && pawn2Finish && pawn3Finish && pawn4Finish) {

            winnerListArea.setVisible(true);
            winnerListTitle.setVisible(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nový vítěz");
            alert.setHeaderText("Vítěz je: " + onTheMovePlayer.getName());
            alert.setContentText("Blahopřejeme vítězi");

            winnerListArea.getItems().add(onTheMovePlayer.getName());
            onTheMovePlayer.setIsWinner(true);
            alert.showAndWait();
        }
    }

    private Circle getCircle(String id) {
        Node n = gridPane.lookup(id);
        if (n instanceof Circle) {
            return ((Circle) n);
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        winnerListArea.setVisible(false);
        winnerListTitle.setVisible(false);

        String[] playersNames = MenuController.getNames();
        boolean[] playersBots = MenuController.getBots();

        try {
            images[0] = new Image(new FileInputStream("1.PNG"));
            images[1] = new Image(new FileInputStream("2.PNG"));
            images[2] = new Image(new FileInputStream("3.PNG"));
            images[3] = new Image(new FileInputStream("4.PNG"));
            images[4] = new Image(new FileInputStream("5.PNG"));
            images[5] = new Image(new FileInputStream("6.PNG"));
            pawnsImage[0] = new Image(new FileInputStream("bluePawn.png"));
            pawnsImage[1] = new Image(new FileInputStream("greenPawn.png"));
            pawnsImage[2] = new Image(new FileInputStream("redPawn.png"));
            pawnsImage[3] = new Image(new FileInputStream("yellowPawn.png"));
            cubeInfoImage = new Image(new FileInputStream("cube.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        cubePicture.setImage(images[0]);
        playersList = new ArrayList<>();

        bluePlayer = new Player(playersNames[0], colorBlue, "0", "blueH-1", "blueH-2", "blueH-3", "blueH-4",
                "blueH-1", "blueH-2", "blueH-3", "blueH-4", "39", "blueF-0", "blueF-1", "blueF-2", "blueF-3",
                "blue", "blueF-", 0, false, playersBots[0]);
        redPlayer = new Player(playersNames[2], colorRed, "20", "redH-1", "redH-2", "redH-3", "redH-4",
                "redH-1", "redH-2", "redH-3", "redH-4", "19", "redF-0", "redF-1", "redF-2", "redF-3",
                "red", "redF-", 2, false, playersBots[2]);
        greenPlayer = new Player(playersNames[1], colorGreen, "10", "greenH-1", "greenH-2", "greenH-3", "greenH-4",
                "greenH-1", "greenH-2", "greenH-3", "greenH-4", "9", "greenF-0", "greenF-1", "greenF-2", "greenF-3",
                "green", "greenF-", 1, false, playersBots[1]);
        yellowPlayer = new Player(playersNames[3], colorYellow, "30", "yellowH-1", "yellowH-2", "yellowH-3", "yellowH-4",
                "yellowH-1", "yellowH-2", "yellowH-3", "yellowH-4", "29", "yellowF-0", "yellowF-1", "yellowF-2", "yellowF-3",
                "yellow", "yellowF-", 3, false, playersBots[3]);

        playersList.add(bluePlayer);
        playersList.add(greenPlayer);
        playersList.add(redPlayer);
        playersList.add(yellowPlayer);

        try {
            changePlayerOnTheMove();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blueName.setText(bluePlayer.getName());
        greenName.setText(greenPlayer.getName());
        redName.setText(redPlayer.getName());
        yellowName.setText(yellowPlayer.getName());
    }
}