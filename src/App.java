import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
 
public class App extends Application {

    static ArrayList<Game> savedGames = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {  
        MainMenu.show(primaryStage);
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}

class MainMenu {
    public static void show(Stage primaryStage) {
        primaryStage.setTitle("will hero");
            
        AnchorPane pane = new AnchorPane();

        Image bg;
        try {
            bg = new Image(new FileInputStream("lib/Images/main_menu_bg.jpg"));

            ImageView ivbg = new ImageView();

            ivbg.setImage(bg);
            ivbg.setOpacity(0.88);
            ivbg.setFitHeight(600);
            ivbg.setFitWidth(900);

            Label highScoreText = new Label("High Score : ");
            highScoreText.setLayoutX(14);
            highScoreText.setLayoutY(14);
            highScoreText.setFont(new Font("Georgia Bold", 24));
            highScoreText.setPrefHeight(38);
            highScoreText.setPrefWidth(162);
            highScoreText.setTextFill(Color.web("0x696969"));

            Label coinCount = new Label("0");
            coinCount.setLayoutX(826);
            coinCount.setLayoutY(18);
            coinCount.setFont(new Font("Georgia Bold", 24));
            coinCount.setTextFill(Color.WHITE);
            
            Label score = new Label("0");
            score.setLayoutX(428);
            score.setLayoutY(27);
            score.setFont(new Font("Georgia Bold", 75));
            score.setTextFill(Color.WHITE);

            Label highScore = new Label("0");
            highScore.setLayoutX(177);
            highScore.setLayoutY(18);
            highScore.setFont(new Font("Georgia Bold", 24));
            highScore.setTextFill(Color.WHITE);

            ImageView coins = new ImageView();
            Image coinImg = new Image(new FileInputStream("lib/Images/assets/Coin.png"));
            coins.setImage(coinImg);
            coins.setFitHeight(43);
            coins.setFitWidth(25);
            coins.setLayoutX(782);
            coins.setLayoutY(22);
            coins.setPreserveRatio(true);
            coins.setPickOnBounds(true);

            Button play = new Button();
            play.setText("PLAY GAME");
            play.setLayoutX(406);
            play.setLayoutY(431);
            play.setFont(new Font("Courier New", 13));
            play.setFocusTraversable(false);
            play.setOnMouseClicked(e -> {
                mouseClick(1, primaryStage);
            });

            Button stats = new Button();
            stats.setText("STATS");
            stats.setLayoutX(422);
            stats.setLayoutY(494);
            stats.setFont(new Font("Courier New", 13));
            stats.setFocusTraversable(false);
            stats.setOnMouseClicked(e -> {
                mouseClick(3, primaryStage);
            });

            Button load = new Button();
            load.setText("LOAD GAME");
            load.setLayoutX(406);
            load.setLayoutY(463);
            load.setFont(new Font("Courier New", 13));
            load.setFocusTraversable(false);
            load.setOnMouseClicked(e -> {
                mouseClick(2, primaryStage);
            });

            Button exit = new Button();
            exit.setText("EXIT");
            exit.setLayoutX(425);
            exit.setLayoutY(527);
            exit.setFont(new Font("Courier New", 13));
            exit.setFocusTraversable(false);
            exit.setOnMouseClicked(e -> {
                mouseClick(4, primaryStage);
            });

            pane.getChildren().add(ivbg);
            pane.getChildren().addAll(highScore, highScoreText, score, coinCount, play, exit, load, stats, coins);

            Scene scene = new Scene(pane, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void mouseClick(int buttonOpt, Stage primaryStage) {
        switch(buttonOpt) {
            case 1:
                Game game = new Game(primaryStage);
                break;
            
            case 2:
                try {
                    FileInputStream in = new FileInputStream("pause.save");
                    ObjectInputStream read = new ObjectInputStream(in);
                    Game newGame = (Game) read.readObject();
                    Game loadGame = new Game(primaryStage);
                    loadGame.loadGame(newGame, primaryStage);
                    read.close();
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
        
                break;
            
            case 3:
                break;

            case 4:
                System.exit(0);
        }
    }
}

class Game implements Serializable {
    private User user;

    public int count;

    transient private AnchorPane pane;
    
    private ArrayList<String> islands = new ArrayList<>();
    private ArrayList<Island> islandObjs = new ArrayList<>();
    private HashMap<Integer, Chest> chestMap = new HashMap<>();
    private HashMap<Integer, Orc> orcMap = new HashMap<>();
    private HashMap<Integer, Coin> coinMap = new HashMap<>();

    private transient Label highScore, highScoreText, score, coinCount;
    private int scoreText, coinCountText;

    public Game(Stage primaryStage) {
        user = new User(this);
        try{

            primaryStage.setTitle("will hero");
            
            pane = new AnchorPane();

            Image bg = new Image(new FileInputStream("lib/Images/sky_bg.jpg"));
            
            ImageView ivbg = new ImageView();

            ivbg.setImage(bg);
            
            pane.getChildren().add(ivbg);
            pane.getChildren().add(user.getHero().getHero());

            highScoreText = new Label("High Score : ");
            highScoreText.setLayoutX(14);
            highScoreText.setLayoutY(14);
            highScoreText.setFont(new Font("Georgia Bold", 24));
            highScoreText.setPrefHeight(38);
            highScoreText.setPrefWidth(162);
            highScoreText.setTextFill(Color.web("0x696969"));

            coinCount = new Label("0");
            coinCount.setLayoutX(826);
            coinCount.setLayoutY(18);
            coinCount.setFont(new Font("Georgia Bold", 24));
            coinCount.setTextFill(Color.WHITE);
            setCoinCountText(Integer.parseInt(coinCount.getText()));
            
            score = new Label("0");
            score.setLayoutX(428);
            score.setLayoutY(27);
            score.setFont(new Font("Georgia Bold", 75));
            score.setTextFill(Color.WHITE);
            setScoreText(Integer.parseInt(score.getText()));

            highScore = new Label("0");
            highScore.setLayoutX(177);
            highScore.setLayoutY(18);
            highScore.setFont(new Font("Georgia Bold", 24));
            highScore.setTextFill(Color.WHITE);

            ImageView coins = new ImageView();
            Image coinImg = new Image(new FileInputStream("lib/Images/assets/Coin.png"));
            coins.setImage(coinImg);
            coins.setFitHeight(43);
            coins.setFitWidth(25);
            coins.setLayoutX(782);
            coins.setLayoutY(22);
            coins.setPreserveRatio(true);
            coins.setPickOnBounds(true);

            Button pause = new Button("PAUSE");
            pause.setLayoutX(420);
            pause.setLayoutY(550);
            pause.setFont(new Font("Courier New", 14));
            pause.setFocusTraversable(false);
            pause.setOnMouseClicked(e -> {
                try {
                    FileOutputStream out = new FileOutputStream("pause.save");
                    ObjectOutputStream write = new ObjectOutputStream(out);
                    write.writeObject(this);
                    write.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                PauseMenu.show(primaryStage);
            });
            pane.getChildren().add(pause);

            islandSpawn(pane);
            chestSpawn(pane);
            orcSpawn(pane);
            coinSpwan(pane);

            pane.getChildren().addAll(highScore, highScoreText, score, coinCount, coins);

            Scene scene = new Scene(pane, 900, 600);

            scene.setOnKeyPressed((EventHandler<? super KeyEvent>) new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode().equals(KeyCode.SPACE)) {
                        setScoreText(getScoreText() + 1);
                        getScore().setText(getScoreText() + "");

                        if(user.getHero().getHero().getBoundsInParent().getMinX() >= 350) {


                            for(Island island: islandObjs) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(island.getIV());
                                worldMove.play();
                            }

                            for(Map.Entry<Integer, Orc> orcIV: orcMap.entrySet()) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(orcIV.getValue().getIV());
                                worldMove.play();
                            }
                            
                            for(Map.Entry<Integer, Chest> chestIV: chestMap.entrySet()) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(chestIV.getValue().getIV());
                                worldMove.play();
                            }

                            for(Map.Entry<Integer, Coin> coinIV: coinMap.entrySet()) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(coinIV.getValue().getIV());
                                worldMove.play();
                            }
                            
                        } else {
                            TranslateTransition worldMove = new TranslateTransition();
                            worldMove.setByX(60);
                            worldMove.setCycleCount(1);
                            worldMove.setDuration(Duration.millis(50));
                            worldMove.setNode(user.getHero().getHero());
                            worldMove.play();
                        }
                    }
                }
            });
            primaryStage.setScene(scene);
            primaryStage.show();  
        } catch(Exception e) {
            e.printStackTrace();
        }        
        
    }

    public Label getScore() {
        return score;
    }    

    public Label getCoins() {
        return coinCount;
    }

    public int getCoinCountText() {
        return coinCountText;
    }



    public void setCoinCountText(int coinCountText) {
        this.coinCountText = coinCountText;
    }



    public int getScoreText() {
        return scoreText;
    }



    public void setScoreText(int scoreText) {
        this.scoreText = scoreText;
    }



    public void loadGame(Game newGame, Stage primaryStage) {
        try {
            Game game = newGame;

            Iterator<Island> itr = islandObjs.iterator();

            this.coinCount.setText(game.coinCountText + "");
            this.score.setText(game.scoreText + "");
            this.setCoinCountText(game.coinCountText);
            this.setScoreText(game.scoreText);

            while(itr.hasNext()) {
                pane.getChildren().remove(itr.next().getIV());
            }

            itr = game.islandObjs.iterator();

            while(itr.hasNext()) {
                Island island = itr.next();
                island.setIV();
                island.getIV().setImage(new Image(new FileInputStream(island.getImgString())));
                island.getIV().setX(island.getPosition().getStartX());
                island.getIV().setY(island.getPosition().getstartY());
                island.getIV().setPreserveRatio(false);
                island.getIV().setFitWidth(island.getPosition().getFitWidth());
                island.getIV().setFitHeight(island.getPosition().getFitHeight());
                pane.getChildren().add(island.getIV());
            }
            this.islandObjs = game.islandObjs;

            HashMap<Integer, Chest> saveChest = chestMap;

            for(Map.Entry<Integer, Chest> chest: game.chestMap.entrySet()) {
                chest.getValue().setIV();
                chest.getValue().getIV().setImage(new Image(new FileInputStream(chest.getValue().getImgString())));
                chest.getValue().getIV().setX(chest.getValue().getPosition().getStartX());
                chest.getValue().getIV().setY(chest.getValue().getPosition().getstartY());
                chest.getValue().getIV().setPreserveRatio(false);
                chest.getValue().getIV().setFitWidth(chest.getValue().getPosition().getFitWidth());
                chest.getValue().getIV().setFitHeight(chest.getValue().getPosition().getFitHeight());
                pane.getChildren().add(chest.getValue().getIV());
            }
            this.chestMap = game.chestMap;

            for(Map.Entry<Integer, Chest> chest: saveChest.entrySet()) {
                pane.getChildren().remove(chest.getValue().getIV());
            }

            for(Map.Entry<Integer, Orc> orc: orcMap.entrySet()) {
                pane.getChildren().remove(orc.getValue().getIV());
            }

            this.orcMap = game.orcMap;

            for(Map.Entry<Integer, Orc> orc: orcMap.entrySet()) {
                orc.getValue().setIV();
                orc.getValue().getIV().setImage(new Image(new FileInputStream(orc.getValue().getImgString())));
                orc.getValue().getIV().setX(orc.getValue().getPosition().getStartX());
                if(orc.getValue().getType().equals("RedOrc")) {
                    orc.getValue().getIV().setY(221);
                } else {
                    orc.getValue().getIV().setY(236);
                }
                orc.getValue().getIV().setPreserveRatio(false);
                orc.getValue().getIV().setFitWidth(orc.getValue().getPosition().getFitWidth());
                orc.getValue().getIV().setFitHeight(orc.getValue().getPosition().getFitHeight());
                if(!orc.getValue().getIsDead()) {
                    pane.getChildren().add(orc.getValue().getIV());
                }
                
                orc.getValue().setTrans();
                orc.getValue().jump();
            }

            HashMap<Integer, Coin> saveCoins = coinMap;
            
            for(Map.Entry<Integer, Coin> coin: game.coinMap.entrySet()) {
                coin.getValue().setIV();
                coin.getValue().getIV().setImage(new Image(new FileInputStream(coin.getValue().getImgString())));
                coin.getValue().getIV().setX(coin.getValue().getPosition().getStartX());
                coin.getValue().getIV().setY(coin.getValue().getPosition().getstartY());
                coin.getValue().getIV().setPreserveRatio(false);
                coin.getValue().getIV().setFitWidth(coin.getValue().getPosition().getFitWidth());
                coin.getValue().getIV().setFitHeight(coin.getValue().getPosition().getFitHeight());
                if(!coin.getValue().getCollected()) {
                    pane.getChildren().add(coin.getValue().getIV());
                }
            }
            this.coinMap = game.coinMap;

            for(Map.Entry<Integer, Coin> coin: saveCoins.entrySet()) {
                pane.getChildren().remove(coin.getValue().getIV());
            }

            pane.getChildren().remove(this.user.getHero().getHero());
            game.user.getHero().setUser(this.user);
            this.user.setHero(game.user.getHero());
            this.user.getHero().setIv();
            ImageView heroIV = this.user.getHero().getHero();
            heroIV.setImage(new Image(new FileInputStream(user.getHero().getHeroImg())));
            heroIV.setX(this.user.getHero().getCoordinates().getStartX());
            heroIV.setY(236);
            heroIV.setFitHeight(this.user.getHero().getCoordinates().getFitHeight());
            heroIV.setFitWidth(this.user.getHero().getCoordinates().getFitWidth());
            user.getHero().loadHero();
            pane.getChildren().add(heroIV);

            if(game.user.getHero().getHelmet().hasLance() && game.user.getHero().getHelmet().getCurrentWeapon().getType().equals("Lance")) {
                System.out.println("lance");
                user.getHero().setHelmet(game.user.getHero().getHelmet());
                user.getHero().getHelmet().getCurrentWeapon().setIv();
                ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                Image img = new Image(new FileInputStream(game.user.getHero().getHelmet().getCurrentWeapon().getImageString()));
                iv.setImage(img);
                iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 7);
                iv.setFitHeight(14);
                iv.setFitWidth(40);
                iv.setPreserveRatio(false);
                user.getGame().getPane().getChildren().add(iv);
            }

            if(game.user.getHero().getHelmet().hasSword() && game.user.getHero().getHelmet().getCurrentWeapon().getType().equals("Sword")) {
                System.out.println("sword");
                user.getHero().setHelmet(game.user.getHero().getHelmet());
                user.getHero().getHelmet().getCurrentWeapon().setIv();
                ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                Image img = new Image(new FileInputStream(game.user.getHero().getHelmet().getCurrentWeapon().getImageString()));
                iv.setImage(img);
                iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 17.5);
                iv.setFitHeight(40);
                iv.setFitWidth(20);
                iv.setPreserveRatio(false);
                user.getGame().getPane().getChildren().add(iv);
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void islandSpawn(AnchorPane pane) {
        try {
            String i1 = "lib/Images/assets/Islands1.png";
            String i2 = "lib/Images/assets/Islands11.png";
            String i3 = "lib/Images/assets/Islands4.png";
            String i4 = "lib/Images/assets/Islands2.png";

            islands.add(i1);
            islands.add(i2);
            islands.add(i3);
            islands.add(i4);

            double start =  60.0;

            for(int i=0; i<20; i++) {
                int islandNum = new Random().nextInt(4);
                Island island;
                if(islandNum == 0) {
                    island = new Island(islands.get(islandNum), 0, 281.0, 220.0, 174.0);
                } else if(islandNum == 1) {
                    island = new Island(islands.get(islandNum), 0, 281.0, 160.0, 220.0);
                } else if(islandNum == 2) {
                    island = new Island(islands.get(islandNum), 0, 281.0, 247.0, 211.0);
                } else {
                    island = new Island(islands.get(islandNum), 0, 281.0, 247.0, 211.0);
                }
                islandObjs.add(island);
                island.getPosition().setX(start);
                ImageView iv = island.getIV();
                iv.setImage(island.getImg());
                iv.setX(island.getPosition().getStartX());
                iv.setY(island.getPosition().getstartY());
                iv.setFitHeight(island.getPosition().getFitHeight());
                iv.setFitWidth(island.getPosition().getFitWidth());
                iv.setPreserveRatio(false);
                pane.getChildren().add(iv);
                start += island.getPosition().getFitWidth();
                start += 100 + (int)(Math.random() * ((200 - 100) + 1));
            }
            Island island = new Island("lib/Images/assets/Islands1.png", islandObjs.get(19).getPosition().getStartX() + 100, 281.0, 1000.0, 174.0);
            islandObjs.add(island);
            island.getPosition().setX(start);
            ImageView iv = island.getIV();
            iv.setImage(island.getImg());
            iv.setX(island.getPosition().getStartX());
            iv.setY(island.getPosition().getstartY());
            iv.setFitHeight(island.getPosition().getFitHeight());
            iv.setFitWidth(island.getPosition().getFitWidth());
            iv.setPreserveRatio(false);
            pane.getChildren().add(iv);


        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void chestSpawn(AnchorPane pane) {
        Integer[] choice = {4, 5};

        int chestIndex = 2;
        while(chestIndex < islandObjs.size()) {
            ImageView islandIV = islandObjs.get(chestIndex).getIV();
            double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 50) - islandIV.getX()) + 1));
            int opt = new Random().nextInt(2);
            if(opt == 0) {
                CoinChest coinChest = new CoinChest(10 + (int)(Math.random() * ((20 - 10) + 1)) + 1, startX, 231, 50, 50);
                chestMap.put(chestIndex, coinChest);
                ImageView chestIv = coinChest.getIV();
                chestIv.setImage(coinChest.getImg());
                chestIv.setX(coinChest.getPosition().getStartX());
                chestIv.setY(coinChest.getPosition().getstartY());
                chestIv.setFitWidth(coinChest.getPosition().getFitWidth());
                chestIv.setFitHeight(coinChest.getPosition().getFitHeight());
                chestIv.setPreserveRatio(false);
                pane.getChildren().add(chestIv);
                chestIndex += choice[new Random().nextInt(2)];
            } else {
                WeaponChest swordChest = new WeaponChest(new Sword(30), 0, 231, 50, 50);
                WeaponChest lanceChest = new WeaponChest(new Lance(40), 0, 231, 50, 50);
                WeaponChest[] chests = {swordChest, lanceChest};
                WeaponChest weaponChest = chests[new Random().nextInt(2)];
                weaponChest.setPosition(startX, weaponChest.getPosition().getstartY());
                chestMap.put(chestIndex, weaponChest);
                ImageView chestIv = weaponChest.getIV();
                chestIv.setImage(weaponChest.getImg());
                chestIv.setX(weaponChest.getPosition().getStartX());
                chestIv.setY(weaponChest.getPosition().getstartY());
                chestIv.setFitWidth(weaponChest.getPosition().getFitWidth());
                chestIv.setFitHeight(weaponChest.getPosition().getFitHeight());
                chestIv.setPreserveRatio(false);
                pane.getChildren().add(chestIv);
                chestIndex += choice[new Random().nextInt(2)];
            }
        }
    }

    private void orcSpawn(AnchorPane pane) {
        int orcIndex = 1;
        while(orcIndex < islandObjs.size()-1) {
            if(!chestMap.containsKey(orcIndex)) {
                ImageView islandIV = islandObjs.get(orcIndex).getIV();
                int opt = new Random().nextInt(2);
                if(opt == 0) {
                    double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 45) - islandIV.getX()) + 1));
                    GreenOrc greenOrc = new GreenOrc(startX, 236, 45, 45, islandObjs);
                    greenOrc.setPosition(startX, greenOrc.getPosition().getstartY());
                    pane.getChildren().add(greenOrc.getIV());
                    orcMap.put(orcIndex, greenOrc);
                } else {
                    double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 60) - islandIV.getX()) + 1));
                    RedOrc redOrc = new RedOrc(startX, 221, 60, 60, islandObjs);
                    redOrc.setPosition(startX, redOrc.getPosition().getstartY());
                    pane.getChildren().add(redOrc.getIV());
                    orcMap.put(orcIndex, redOrc);
                }
            }
            orcIndex += 2 + new Random().nextInt(2);
        }
        ImageView islandIV = islandObjs.get(20).getIV();
        
        double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 70) - islandIV.getX()) + 1));
        BossOrc bossOrc = new BossOrc(startX, 206, 75, 75, islandObjs);
        bossOrc.setPosition(startX, bossOrc.getPosition().getstartY());
        pane.getChildren().add(bossOrc.getIV());
        orcMap.put(orcIndex, bossOrc);
        
    }

    private void coinSpwan(AnchorPane pane) {
        int coinCounter = 1;
        double startX = 300;
        for(int i=0; i<20; i++) {
            int choice = new Random().nextInt(2);
            if(choice == 0) {
                for(int j=0; j<4; j++) {
                    Coin coin = new Coin("lib/Images/assets/Coin.png", startX, 150);
                    ImageView coinIv = coin.getIV();
                    coinIv.setImage(coin.getImg());
                    coinIv.setX(coin.getPosition().getStartX());
                    coinIv.setY(coin.getPosition().getstartY());
                    coinIv.setFitHeight(coin.getPosition().getFitHeight());
                    coinIv.setFitWidth(coin.getPosition().getFitWidth());
                    coinIv.setPreserveRatio(false);
                    pane.getChildren().add(coinIv);
                    coinMap.put(coinCounter, coin);
                    coinCounter++;
                    startX += 40;
                }
            } else {
                for(int j=0; j<4; j++) {
                    Coin coin = new Coin("lib/Images/assets/Coin.png", startX, 130);
                    ImageView coinIv = coin.getIV();
                    coinIv.setImage(coin.getImg());
                    coinIv.setX(coin.getPosition().getStartX());
                    coinIv.setY(coin.getPosition().getstartY());
                    coinIv.setFitHeight(coin.getPosition().getFitHeight());
                    coinIv.setFitWidth(coin.getPosition().getFitWidth());
                    coinIv.setPreserveRatio(false);
                    pane.getChildren().add(coinIv);
                    coinMap.put(coinCounter, coin);
                    coinCounter++;

                    coin = new Coin("lib/Images/assets/Coin.png", startX, 170);
                    coinIv = coin.getIV();
                    coinIv.setImage(coin.getImg());
                    coinIv.setX(coin.getPosition().getStartX());
                    coinIv.setY(coin.getPosition().getstartY());
                    coinIv.setFitHeight(coin.getPosition().getFitHeight());
                    coinIv.setFitWidth(coin.getPosition().getFitWidth());
                    coinIv.setPreserveRatio(false);
                    pane.getChildren().add(coinIv);
                    coinMap.put(coinCounter, coin);
                    coinCounter++;
                    startX += 40;
                }
            }

            startX += 300 + (int)(Math.random() * ((1000 - 300) + 1));;
        }
    }

    public ArrayList<Island> getIslands() {
        return islandObjs;
    }

    public HashMap<Integer, Chest> getChests() {
        return chestMap;
    }

    public HashMap<Integer, Orc> getOrcs() {
        return orcMap;
    }

    public HashMap<Integer, Coin> getCoinMap() {
        return coinMap;
    }

    public AnchorPane getPane() {
        return pane;
    }
}

class PauseMenu {

    public static void show(Stage primaryStage) {

        primaryStage.setTitle("will hero");
            
        AnchorPane pane = new AnchorPane();

        Image bg;
        try {
            bg = new Image(new FileInputStream("lib/Images/sky_bg.jpg"));
            ImageView ivbg = new ImageView();

            ivbg.setImage(bg);
            ivbg.setOpacity(0.88);
            ivbg.setFitHeight(600);
            ivbg.setFitWidth(900);

            Label pause1 = new Label("PAUSED");
            pause1.setLayoutX(334);
            pause1.setLayoutY(109);
            pause1.setTextFill(Color.web("0xa9a9a9"));
            pause1.setFont(new Font("Constantia Bold", 62));

            Label pause2 = new Label("PAUSED");
            pause2.setLayoutX(337);
            pause2.setLayoutY(109);
            pause2.setTextFill(Color.web("0xe4860c"));
            pause2.setFont(new Font("Constantia Bold", 62));

            Button resume = new Button("RESUME");
            resume.setLayoutX(389);
            resume.setLayoutY(239);
            resume.prefHeight(30);
            resume.setPrefWidth(122);
            resume.setTextFill(Color.web("0x6b5200"));
            resume.setFocusTraversable(false);
            resume.setOnMouseClicked(e -> {
                try {
                    FileInputStream in = new FileInputStream("pause.save");
                    ObjectInputStream read = new ObjectInputStream(in);
                    Game newGame = (Game) read.readObject();
                    Game game = new Game(primaryStage);
                    game.loadGame(newGame, primaryStage);
                    read.close();
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            });

            Button restart = new Button("RESTART");
            restart.setLayoutX(388);
            restart.setLayoutY(300);
            restart.prefHeight(30);
            restart.setPrefWidth(122);
            restart.setTextFill(Color.web("0x6b5200"));
            restart.setFocusTraversable(false);

            Button saveGame = new Button("SAVE GAME");
            saveGame.setLayoutX(388);
            saveGame.setLayoutY(356);
            saveGame.prefHeight(30);
            saveGame.setPrefWidth(122);
            saveGame.setTextFill(Color.web("0x6b5200"));
            saveGame.setFocusTraversable(false);

            Button mainMenu = new Button("MAIN MENU");
            mainMenu.setLayoutX(388);
            mainMenu.setLayoutY(409);
            mainMenu.prefHeight(30);
            mainMenu.setPrefWidth(122);
            mainMenu.setTextFill(Color.web("0x6b5200"));
            mainMenu.setFocusTraversable(false);
            mainMenu.setOnMouseClicked(e -> {
                MainMenu.show(primaryStage);
            });

            pane.getChildren().addAll(ivbg, pause1, pause2, resume, restart, saveGame, mainMenu);

            Scene scene = new Scene(pane, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class User implements Serializable {
    private Hero hero;
    private Game game;
    public User(Game game) {
        this.game = game;
        this.hero = Hero.getSingletonHero(this);
    }

    public Hero getHero() {
        return this.hero;
    }

    public Game getGame() {
        return game;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

class Hero implements collison, Serializable {

    transient private ImageView hero;
    private User user;
    private Helmet helmet;
    private Coordinates coordinates;
    private static Hero singletonHero = null;
    private String img;

    public Hero(User user, String img) {
        this.hero = new ImageView();
        this.user = user;
        this.helmet = Helmet.getHelmet();
        this.img = img;
        try {
            Image heroImg = new Image(new FileInputStream(img));
            hero.setImage(heroImg);
            hero.setFitWidth(45.0);
            hero.setFitHeight(45.0);
            hero.setX(142.0);
            hero.setY(236.0);
            hero.setPreserveRatio(false);
            this.coordinates = new Coordinates(hero.getX(), hero.getY(), hero.getFitWidth(), hero.getFitHeight());

            coordinatesAnimation();
            movement();
            weaponMovement();
            collide();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadHero() {
        coordinatesAnimation();
        movement();
        weaponMovement();
        collide();
    }

    public static Hero getSingletonHero(User user) {
        if(singletonHero == null) {
            singletonHero = new Hero(user, "lib/Images/assets/Helmet3.png");
            return singletonHero;
        }
        return singletonHero;
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
    }

    public void setAllCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getHeroImg() {
        return img;
    }

    public void setIv() {
        this.hero = new ImageView();
    }

    private void movement() {
        TranslateTransition trans = new TranslateTransition();
        trans.setNode(hero);
        trans.setAutoReverse(true);
        trans.setDuration(Duration.millis(750));
        trans.setCycleCount(2);
        trans.setByY(-180);      
        trans.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                ArrayList<Island> islands = user.getGame().getIslands();
                int i;
                for(i=0; i<islands.size(); i++) {
                    if(islands.get(i).getIV().getBoundsInParent().getMinX() <= hero.getBoundsInParent().getMaxX()) {
                        continue;
                    } else {
                        break;
                    }
                }
                if(islands.get(i-1).getIV().getBoundsInParent().getMinX() <= hero.getBoundsInParent().getMaxX() && islands.get(i-1).getIV().getBoundsInParent().getMaxX() >= hero.getBoundsInParent().getMinX()) {
                    movement();
                   
                } else {
                    fall();
                }
            }
        });
        trans.play();
    }

    private void fall() {
        TranslateTransition trans = new TranslateTransition();
        trans.setNode(hero);
        trans.setDuration(Duration.millis(3000));
        trans.setCycleCount(1);
        trans.setByY(1000);
        trans.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                System.exit(0);                
            }
            
        });
        trans.play();
        System.out.println("Game Over!");
    }

    public ImageView getHero() {
        return this.hero;
    }

    @Override
    public void collide() {
        collideChests();
        collideCoins();
        collideOrcs();
    }

    private void collideChests() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                HashMap<Integer, Chest> chestMap = user.getGame().getChests();
                for(Map.Entry<Integer, Chest> chest: chestMap.entrySet()) {
                    if(user.getHero().getHero().getBoundsInParent().intersects(chest.getValue().getIV().getBoundsInParent()) && !chestMap.get(chest.getKey()).getOpened()) {
                        try {
                            System.out.println("hello");
                            System.out.println(chestMap.get(chest.getKey()).getType());
                            if(chestMap.get(chest.getKey()).getType().equals("WeaponChest")) {
                                WeaponChest wc = (WeaponChest) chestMap.get(chest.getKey());
                                if(wc.getWeapon().getType().equals("Sword")) {
                                    if(!user.getHero().getHelmet().hasSword()) {
                                        user.getHero().getHelmet().setHasSword();
                                        if(helmet.hasLance()) {
                                            user.getGame().getPane().getChildren().remove(helmet.getCurrentWeapon().getWeaponIV());
                                        }
                                        user.getHero().getHelmet().setCurrentWeapon(new Sword(30));
                                        ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                                        iv.setImage(user.getHero().getHelmet().getCurrentWeapon().getImage());
                                        iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                                        iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 17.5);
                                        iv.setFitHeight(40);
                                        iv.setFitWidth(20);
                                        iv.setPreserveRatio(false);
                                        user.getGame().getPane().getChildren().add(iv);
                                    } else {
                                        user.getHero().getHelmet().setSwordLevel();
                                        user.getHero().getHelmet().setCurrentWeapon(new Sword(30 + user.getHero().getHelmet().getSwordLevel()*10));
                                        ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                                        iv.setImage(user.getHero().getHelmet().getCurrentWeapon().getImage());
                                        iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                                        iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 17.5);
                                        iv.setFitHeight(40);
                                        iv.setFitWidth(20);
                                        iv.setPreserveRatio(false);
                                        user.getGame().getPane().getChildren().add(iv);
                                    }
                                } else {
                                    if(!user.getHero().getHelmet().hasLance()) {
                                        user.getHero().getHelmet().setHasLance();
                                        if(helmet.hasSword()) {
                                            user.getGame().getPane().getChildren().remove(helmet.getCurrentWeapon().getWeaponIV());
                                        }
                                        user.getHero().getHelmet().setCurrentWeapon(new Lance(40));
                                        ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                                        iv.setImage(user.getHero().getHelmet().getCurrentWeapon().getImage());
                                        iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                                        iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 7);
                                        iv.setFitHeight(14);
                                        iv.setFitWidth(40);
                                        iv.setPreserveRatio(false);
                                        user.getGame().getPane().getChildren().add(iv);
                                    } else {
                                        user.getHero().getHelmet().setSwordLevel();
                                        user.getHero().getHelmet().setCurrentWeapon(new Lance(40 + user.getHero().getHelmet().getlanceLevel()*10));
                                        ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                                        iv.setImage(user.getHero().getHelmet().getCurrentWeapon().getImage());
                                        iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                                        iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 7);
                                        iv.setFitHeight(14);
                                        iv.setFitWidth(40);
                                        iv.setPreserveRatio(false);
                                        user.getGame().getPane().getChildren().add(iv);
                                    }
                                }
                            } else {
                                CoinChest cc = (CoinChest) chest.getValue();
                                user.getGame().setCoinCountText(user.getGame().getCoinCountText() + cc.getCoins());
                                user.getGame().getCoins().setText(user.getGame().getCoinCountText() + "");
                            }
                            user.getGame().getPane().getChildren().remove(chest.getValue().getIV());
                            ImageView openChest = new ImageView();
                            Image openChestImg = new Image(new FileInputStream("lib/Images/assets/ChestOpen.png"));
                            openChest.setImage(openChestImg);
                            openChest.setX(chest.getValue().getIV().getBoundsInParent().getMinX());
                            openChest.setY(chest.getValue().getIV().getBoundsInParent().getMinY());
                            openChest.setFitHeight(chest.getValue().getIV().getFitHeight());
                            openChest.setFitWidth(chest.getValue().getIV().getFitWidth());
                            openChest.setPreserveRatio(false);
                            
                            user.getGame().getPane().getChildren().add(openChest);
                            chest.getValue().setIV(openChest);
                            chestMap.get(chest.getKey()).setOpened();
                            chestMap.get(chest.getKey()).setImageString("lib/Images/assets/ChestOpen.png");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
        };
        timer.start();
    }

    private void collideCoins() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                HashMap<Integer, Coin> coinMap = user.getGame().getCoinMap();
                for(Map.Entry<Integer, Coin> coin: coinMap.entrySet()) {
                    if(user.getHero().getHero().getBoundsInParent().intersects(coin.getValue().getIV().getBoundsInParent()) && !coin.getValue().getCollected()) {  
                        user.getGame().setCoinCountText(user.getGame().getCoinCountText() + 1);
                        user.getGame().getCoins().setText(user.getGame().getCoinCountText() + "");
                        user.getGame().getPane().getChildren().remove(coin.getValue().getIV());
                        coin.getValue().isCollected();
                    }
                }
            }
            
        };
        timer.start();
    }

    private void collideOrcs() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                HashMap<Integer, Orc> orcMap = user.getGame().getOrcs();
                for(Map.Entry<Integer, Orc> orc: orcMap.entrySet()) {
                    if(user.getHero().getHero().getBoundsInParent().intersects(orc.getValue().getIV().getBoundsInParent())) {
                        if(orc.getValue().getIV().getBoundsInParent().getMaxY() < hero.getBoundsInParent().getMinY() + 9 ) {
                            System.exit(0);
                        }
                        if(user.getHero().getHelmet().getCurrentWeapon() != null) {
                            orc.getValue().setStrength(orc.getValue().getStrength() - user.getHero().getHelmet().getCurrentWeapon().getStrength());
                        }
                        
                        if(orc.getValue().getStrength() <= 0) {
                            user.getGame().getPane().getChildren().remove(orc.getValue().getIV());
                            orc.getValue().getIsDead();
                        } else {
                            TranslateTransition orcCollison = new TranslateTransition();
                            orcCollison.setByX(40);
                            orcCollison.setCycleCount(1);
                            orcCollison.setDuration(Duration.millis(50));
                            orcCollison.setNode(orc.getValue().getIV());
                            orcCollison.play();
                        }

                        TranslateTransition heroCollision = new TranslateTransition();
                        heroCollision.setByX(-20);
                        heroCollision.setCycleCount(1);
                        heroCollision.setDuration(Duration.millis(50));
                        heroCollision.setNode(user.getHero().getHero());
                        heroCollision.play();
                    }
                }
            }
            
        };
        timer.start();
    }

    private void weaponMovement() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                if(helmet.hasLance() && helmet.getCurrentWeapon().getType().equals("Lance")) {
                    helmet.getCurrentWeapon().getWeaponIV().setX(hero.getBoundsInParent().getMaxX());
                    helmet.getCurrentWeapon().getWeaponIV().setY(hero.getBoundsInParent().getCenterY() - 7);
                } else if(helmet.hasSword() && helmet.getCurrentWeapon().getType().equals("Sword")) {
                    helmet.getCurrentWeapon().getWeaponIV().setX(hero.getBoundsInParent().getMaxX());
                    helmet.getCurrentWeapon().getWeaponIV().setY(hero.getBoundsInParent().getCenterY() - 11.25);
                }      
            }
            
        };
        timer.start();
    }

    private void coordinatesAnimation() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                coordinates.setCoordinates(hero.getBoundsInParent().getMinX(), hero.getBoundsInParent().getMinY());
            }
            
        };
        timer.start();
    }
} 

abstract class GameObjects implements Serializable {
    private Coordinates coordinates;
    transient private ImageView iv;
    private String img;
    
    protected GameObjects(String img, double startX, double startY, double widthX, double widthY) {
        coordinates = new Coordinates(startX, startY, widthX, widthY);
        this.iv = new ImageView();
        this.img = img;
        coordinatesAnimation();
    }

    public void setImageString(String img) {
        this.img = img;
    }

    public Coordinates getPosition() {
        return coordinates;
    }

    public void setPosition(double startX, double startY) {
        coordinates.setCoordinates(startX, startY);
    }

    public ImageView getIV() {
        return iv;
    }

    public void setIV() {
        this.iv = new ImageView();
    }

    protected void setIV(ImageView iv) {
        this.iv = iv;
    }

    public String getImgString() {
        return img;
    }

    public void setAllCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    private void coordinatesAnimation() {
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long arg0) {
                coordinates.setCoordinates(iv.getBoundsInParent().getMinX(), iv.getBoundsInParent().getMinY());
            }
            
        };
        timer.start();
    }
}

class Island extends GameObjects {
    transient private Image img;
    public Island(String img, double startX, double startY, double widthX, double widthY) {
        super(img, startX, startY, widthX, widthY);
        try {
            this.img = new Image(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Image getImg() {
        return img;
    }
}

abstract class Orc extends GameObjects implements collison {

    transient private Image img;
    protected final String type;
    transient private TranslateTransition trans;
    private ArrayList<Island> ivs;
    transient private ImageView iv;
    private int strength;
    private boolean isDead;

    protected Orc(String img, String type, double startX, double startY, double widthX, double widthY, ArrayList<Island> ivs, int strength) {
        super(img, startX, startY, widthX, widthY);
        this.strength = strength;
        this.type = type;
        this.isDead = false;
        this.ivs = ivs;
        this.trans = new TranslateTransition();
        iv = this.getIV();
        try {
            this.img = new Image(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.getIV().setImage(getImg());
        this.getIV().setFitWidth(widthX);
        this.getIV().setFitHeight(widthY);
        this.getIV().setX(startX);
        this.getIV().setY(startY);
        this.getIV().setPreserveRatio(false);
        jump();
    }

    public Image getImg() {
        return img;
    }

    public void setTrans() {
        this.trans = new TranslateTransition();
    }

    public String getType() {
        return type;
    }

    public void setIsDead() {
        this.isDead = true;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void jump() {
        trans.setNode(this.getIV());
        trans.setAutoReverse(true);
        trans.setDuration(Duration.millis(750));
        trans.setCycleCount(2);
        trans.setByY(-200);
        iv = this.getIV();
        trans.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                int i;
                for(i=0; i<ivs.size(); i++) {
                    if(ivs.get(i).getIV().getBoundsInParent().getMinX() <= iv.getBoundsInParent().getMaxX()) {
                        continue;
                    } else {
                        break;
                    }
                }
                if(ivs.get(i-1).getIV().getBoundsInParent().getMinX() <= iv.getBoundsInParent().getMaxX() && ivs.get(i-1).getIV().getBoundsInParent().getMaxX() >= iv.getBoundsInParent().getMinX()) {
                    jump();
                } else {
                    setIsDead();
                    fall();
                }
                
            }
            
        });
        trans.play();
    }

    private void fall() {
        trans.setNode(this.getIV());
        trans.setDuration(Duration.millis(3000));
        trans.setCycleCount(1);
        trans.setByY(1000);
        trans.play();
    }

    @Override
    public void collide() {

    }
}

class RedOrc extends Orc {

    public RedOrc(double startX, double startY, double widthX, double widthY, ArrayList<Island> ivs) {
        super("lib/Images/assets/RedOrc1.png", "RedOrc", startX, startY, widthX, widthY, ivs, 200);
    }
    
}

class GreenOrc extends Orc {

    public GreenOrc(double startX, double startY, double widthX, double widthY, ArrayList<Island> ivs) {
        super("lib/Images/assets/Orc3.png", "GreenOrc", startX, startY, widthX, widthY, ivs, 100);
    }
    
}

class BossOrc extends Orc {

    public BossOrc(double startX, double startY, double widthX, double widthY, ArrayList<Island> ivs) {
        super("lib/Images/assets/OrcBoss.png", "BossOrc", startX, startY, widthX, widthY, ivs, 500);
    }
    
}

abstract class Chest extends GameObjects {

    transient private Image img;
    protected final String type;
    private boolean opened;

    protected Chest(String img, String type, double startX, double startY, double widthX, double widthY) {
        super(img, startX, startY, widthX, widthY);
        this.type = type;
        this.opened = false;
        try {
            this.img = new Image(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Image getImg() {
        return img;
    }

    public void setOpened() {
        this.opened = true;
    }

    public boolean getOpened() {
        return opened;
    }

    public String getType() {
        return type;
    }
    
}

class CoinChest extends Chest {

    private final int coins;

    protected CoinChest(int coins, double startX, double startY, double widthX, double widthY) {
        super("lib/Images/assets/ChestClosed.png", "CoinChest", startX, startY, widthX, widthY);
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }
    
}

class WeaponChest extends Chest {

    private final Weapon weapon;

    protected WeaponChest(Weapon weapon, double startX, double startY, double widthX, double widthY) {
        super("lib/Images/assets/ChestClosed.png", "WeaponChest", startX, startY, widthX, widthY);
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }
    
}

class Helmet implements Serializable {
    private boolean hasSword;
    private boolean hasLance;
    private int swordLevel;
    private int lanceLevel;
    private Weapon currentWeapon;
    private static Helmet helmet = null;

    private Helmet() {
        this.hasLance = false;
        this.hasSword = false;
        this.swordLevel = 0;
        this.lanceLevel = 0;
        this.setCurrentWeapon(null);
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public int getlanceLevel() {
        return lanceLevel;
    }

    public void setlanceLevel() {
        this.lanceLevel++;
    }

    public int getSwordLevel() {
        return swordLevel;
    }

    public void setSwordLevel() {
        this.swordLevel++;
    }

    public boolean hasLance() {
        return hasLance;
    }

    public void setHasLance() {
        this.hasLance = true;
    }

    public boolean hasSword() {
        return hasSword;
    }

    public void setHasSword() {
        this.hasSword = true;
    }

    public static Helmet getHelmet() {
        if(helmet == null) {
            helmet = new Helmet();
            return helmet;
        }
        return helmet;
    }
}

abstract class Weapon implements Serializable {
    private final String type;
    transient private ImageView weaponIV;
    transient private Image weaponImg;
    private String imgString;
    private int strength;

    protected Weapon(String type, String img, int strength) {
        this.strength = strength;
        this.type = type;
        this.weaponIV = new ImageView();
        this.imgString = img;
        try {
            this.weaponImg = new Image(new FileInputStream(imgString));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return type;
    }

    public ImageView getWeaponIV() {
        return weaponIV;
    }

    public Image getImage() {
        return weaponImg;
    }

    public String getImageString() {
        return imgString;
    }

    public void setIv() {
        weaponIV = new ImageView();
    }

    public int getStrength() {
        return strength;
    }

}

class Sword extends Weapon {
    public Sword(int strength) {
        super("Sword", "lib/Images/assets/WeaponSword.png", strength);
    }
}

class Lance extends Weapon {
    public Lance(int strength) {
        super("Lance", "lib/Images/assets/WeaponChampionLance.png", strength);
    }
}

class Coin extends GameObjects {

    transient private Image img;
    private boolean collected;

    protected Coin(String image, double startX, double startY) {
        super(image, startX, startY, 20, 20);
        try {
            img = new Image(new FileInputStream(image));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        collected = false;
    }
    
    public Image getImg() {
        return img;
    }

    public void isCollected() {
        this.collected = true;
    }

    public boolean getCollected() {
        return collected;
    }
}

class Coordinates implements Serializable {
    private double startX;
    private double startY;
    private final double widthX;
    private final double widthY;
    private double endX;
    private double endY;

    public Coordinates (double startX, double startY, double widthX, double widthY) {
        this.startX = startX;
        this.startY = startY;
        this.widthX = widthX;
        this.widthY = widthY;
        this.endX = startX + widthX;
        this.endY = startY + widthY;
    }

    public void setCoordinates(double startX, double startY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = startX + widthX;
        this.endY = startY + widthY;
    }

    public void setX (double startX) {
        this.startX = startX;
        this.endX = startX + widthX;
    }

    public double getStartX () {
        return startX;
    }

    public double getstartY () {
        return startY;
    }

    public void setY (double startY) {
        this.startY = startY;
        this.endY = endY + widthY;
    }

    public double getEndX () {
        return endX;
    }

    public double getEndY () {
        return endY;
    }

    public double getFitWidth() {
        return widthX;
    }

    public double getFitHeight() {
        return widthY;
    }
}

interface collison {
    void collide();
}