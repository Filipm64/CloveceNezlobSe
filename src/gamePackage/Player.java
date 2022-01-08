package gamePackage;

import javafx.scene.paint.Paint;

public class Player {

    private String name;

    private Paint color;
    private String colorString;

    private String startPlace;
    private String lastPlace;

    private String pawn1;
    private String pawn2;
    private String pawn3;
    private String pawn4;

    private String home1;
    private String home2;
    private String home3;
    private String home4;

    private String finish1;
    private String finish2;
    private String finish3;
    private String finish4;
    private String finishText;

    private int indexPlayer;
    private boolean isWinner;
    private boolean isBot;

    public Player(String name, Paint color, String startPlace, String pawn1, String pawn2, String pawn3,
                  String pawn4, String home1, String home2, String home3, String home4, String lastPlace,
                  String finish1, String finish2, String finish3, String finish4, String colorString,
                  String finishText, int indexPlayer, boolean isWinner, boolean isBot) {
        this.name = name;
        this.color = color;
        this.startPlace = startPlace;
        this.pawn1 = pawn1;
        this.pawn2 = pawn2;
        this.pawn3 = pawn3;
        this.pawn4 = pawn4;
        this.home1 = home1;
        this.home2 = home2;
        this.home3 = home3;
        this.home4 = home4;
        this.lastPlace = lastPlace;
        this.finish1 = finish1;
        this.finish2 = finish2;
        this.finish3 = finish3;
        this.finish4 = finish4;
        this.colorString = colorString;
        this.finishText = finishText;
        this.indexPlayer = indexPlayer;
        this.isWinner = isWinner;
        this.isBot = isBot;
    }


    public String getName() {
        return this.name;
    }
    public Paint getColor(){return this.color;}
    public String getStartPlace(){return this.startPlace;}
    public String getPawn1(){return this.pawn1;}
    public String getPawn2(){return this.pawn2;}
    public String getPawn3(){return this.pawn3;}
    public String getPawn4(){return this.pawn4;}
    public void setPawn1(String value){this.pawn1 = value;}
    public void setPawn2(String value){this.pawn2 = value;}
    public void setPawn3(String value){this.pawn3 = value;}
    public void setPawn4(String value){this.pawn4 = value;}
    public String getHome1(){ return this.home1;}
    public String getHome2(){ return this.home2;}
    public String getHome3(){ return this.home3;}
    public String getHome4(){ return this.home4;}
    public String getLastPlace(){return this.lastPlace;}
    public String getFinish1(){return this.finish1;}
    public String getFinish2(){return this.finish2;}
    public String getFinish3(){return this.finish3;}
    public String getFinish4(){return this.finish4;}
    public String getColorString(){return this.colorString;}
    public String getFinishText(){return this.finishText;}
    public int getIndexPlayer(){return this.indexPlayer;}
    public boolean getIsWinner(){return this.isWinner;}
    public void setIsWinner(boolean value){this.isWinner = value;}
    public boolean getIsBot(){return this.isBot;}
}
