import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
 
public class App extends Application {

    private User user = new User(this);

    public int count;

    private AnchorPane pane;
    
    private ArrayList<Island> islands = new ArrayList<>();
    private ArrayList<ImageView> ivs = new ArrayList<>();
    private HashMap<Integer, Chest> chestMap = new HashMap<>();
    private HashMap<Integer, ImageView> chestIVs = new HashMap<>();
    private HashMap<Integer, Orc> orcMap = new HashMap<>();
    private HashMap<Integer, ImageView> orcIVs = new HashMap<>();
    private HashMap<Integer, ImageView> coinIVs = new HashMap<>();
    private HashMap<Integer, Coin> coinMap = new HashMap<>();


    @Override
    public void start(Stage primaryStage) {  
        try{
            primaryStage.setTitle("will hero");
            
            pane = new AnchorPane();

            Image bg = new Image(new FileInputStream("lib/Images/sky_bg.jpg"));
            
            ImageView ivbg = new ImageView();

            ivbg.setImage(bg);
            
            pane.getChildren().add(ivbg);
            pane.getChildren().add(user.getHero().getHero());

            islandSpawn(pane);
            chestSpawn(pane);
            orcSpawn(pane);
            coinSpwan(pane);

            Scene scene = new Scene(pane, 900, 600);

            scene.setOnKeyPressed((EventHandler<? super KeyEvent>) new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if(event.getCode().equals(KeyCode.SPACE)) {
                        count++;
                        if(user.getHero().getHero().getBoundsInParent().getMinX() >= 350) {


                            for(ImageView iv : ivs) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(iv);
                                worldMove.play();
                            }

                            for(Map.Entry<Integer, ImageView> orcIV: orcIVs.entrySet()) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(orcIV.getValue());
                                worldMove.play();
                            }
                            
                            for(Map.Entry<Integer, ImageView> chestIV: chestIVs.entrySet()) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(chestIV.getValue());
                                worldMove.play();
                            }

                            for(Map.Entry<Integer, ImageView> coinIV: coinIVs.entrySet()) {
                                TranslateTransition worldMove = new TranslateTransition();
                                worldMove.setByX(-60);
                                worldMove.setCycleCount(1);
                                worldMove.setDuration(Duration.millis(50));
                                worldMove.setNode(coinIV.getValue());
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

    private void islandSpawn(AnchorPane pane) {
        try {

            Island i1 = new Island("lib/Images/assets/Islands1.png", 0, 281.0, 220.0, 174.0);
            Island i2 = new Island("lib/Images/assets/Islands11.png", 0, 281.0, 160.0, 220.0);
            Island i3 = new Island("lib/Images/assets/Islands4.png", 0, 281.0, 247.0, 211.0);
            Island i4 = new Island("lib/Images/assets/Islands2.png", 0, 281.0, 300.0, 100.0);

            islands.add(i1);
            islands.add(i2);
            islands.add(i3);
            islands.add(i4);

            double start =  60.0;

            for(int i=0; i<30; i++) {
                int islandNum = new Random().nextInt(4);
                Island island = islands.get(islandNum);
                island.getPosition().setX(start);
                ImageView iv = new ImageView();
                iv.setImage(islands.get(islandNum).getImg());
                iv.setX(island.getPosition().getStartX());
                iv.setY(island.getPosition().getstartY());
                iv.setFitHeight(island.getPosition().getFitHeight());
                iv.setFitWidth(island.getPosition().getFitWidth());
                iv.setPreserveRatio(false);
                pane.getChildren().add(iv);
                ivs.add(iv);
                start += island.getPosition().getFitWidth();
                start += 100 + (int)(Math.random() * ((200 - 100) + 1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void chestSpawn(AnchorPane pane) {
        Integer[] choice = {4, 5};

        int chestIndex = 2;
        while(chestIndex < ivs.size()) {
            ImageView islandIV = ivs.get(chestIndex);
            double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 50) - islandIV.getX()) + 1));
            int opt = new Random().nextInt(2);
            if(opt == 0) {
                CoinChest coinChest = new CoinChest(10 + (int)(Math.random() * ((20 - 10) + 1)) + 1, startX, 231, 50, 50);
                chestMap.put(chestIndex, coinChest);
                ImageView chestIv = new ImageView();
                chestIv.setImage(coinChest.getImg());
                chestIv.setX(coinChest.getPosition().getStartX());
                chestIv.setY(coinChest.getPosition().getstartY());
                chestIv.setFitWidth(coinChest.getPosition().getFitWidth());
                chestIv.setFitHeight(coinChest.getPosition().getFitHeight());
                chestIv.setPreserveRatio(false);
                pane.getChildren().add(chestIv);
                chestIVs.put(chestIndex, chestIv);
                chestIndex += choice[new Random().nextInt(2)];
            } else {
                WeaponChest swordChest = new WeaponChest(new Sword(), 0, 231, 50, 50);
                WeaponChest lanceChest = new WeaponChest(new Lance(), 0, 231, 50, 50);
                WeaponChest[] chests = {swordChest, lanceChest};
                WeaponChest weaponChest = chests[new Random().nextInt(2)];
                weaponChest.setPosition(startX, weaponChest.getPosition().getstartY());
                chestMap.put(chestIndex, weaponChest);
                ImageView chestIv = new ImageView();
                chestIv.setImage(weaponChest.getImg());
                chestIv.setX(weaponChest.getPosition().getStartX());
                chestIv.setY(weaponChest.getPosition().getstartY());
                chestIv.setFitWidth(weaponChest.getPosition().getFitWidth());
                chestIv.setFitHeight(weaponChest.getPosition().getFitHeight());
                chestIv.setPreserveRatio(false);
                pane.getChildren().add(chestIv);
                chestIVs.put(chestIndex, chestIv);
                chestIndex += choice[new Random().nextInt(2)];
            }
        }
    }

    private void orcSpawn(AnchorPane pane) {
        int orcIndex = 1;
        while(orcIndex < ivs.size()) {
            if(!chestIVs.containsKey(orcIndex)) {
                ImageView islandIV = ivs.get(orcIndex);
                int opt = new Random().nextInt(2);
                if(opt == 0) {
                    double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 45) - islandIV.getX()) + 1));
                    GreenOrc greenOrc = new GreenOrc(startX, 236, 45, 45, ivs, orcIVs);
                    greenOrc.setPosition(startX, greenOrc.getPosition().getstartY());
                    pane.getChildren().add(greenOrc.getOrcIV());
                    orcIVs.put(orcIndex, greenOrc.getOrcIV());
                    orcMap.put(orcIndex, greenOrc);
                } else {
                    double startX = islandIV.getX() + (int)(Math.random() * (((islandIV.getX() + islandIV.getFitWidth() - 60) - islandIV.getX()) + 1));
                    RedOrc redOrc = new RedOrc(startX, 221, 60, 60, ivs, orcIVs);
                    redOrc.setPosition(startX, redOrc.getPosition().getstartY());
                    pane.getChildren().add(redOrc.getOrcIV());
                    orcIVs.put(orcIndex, redOrc.getOrcIV());
                    orcMap.put(orcIndex, redOrc);
                }
            }
            orcIndex += 2 + new Random().nextInt(2);
        }
    }

    private void coinSpwan(AnchorPane pane) {
        int coinCounter = 1;
        double startX = 300;
        for(int i=0; i<20; i++) {
            int choice = new Random().nextInt(2);
            if(choice == 0) {
                for(int j=0; j<4; j++) {
                    Coin coin = new Coin(startX, 150);
                    ImageView coinIv = new ImageView();
                    coinIv.setImage(coin.getImg());
                    coinIv.setX(coin.getPosition().getStartX());
                    coinIv.setY(coin.getPosition().getstartY());
                    coinIv.setFitHeight(coin.getPosition().getFitHeight());
                    coinIv.setFitWidth(coin.getPosition().getFitWidth());
                    coinIv.setPreserveRatio(false);
                    pane.getChildren().add(coinIv);
                    coinIVs.put(coinCounter, coinIv);
                    coinMap.put(coinCounter, coin);
                    coinCounter++;
                    startX += 40;
                }
            } else {
                for(int j=0; j<4; j++) {
                    Coin coin = new Coin(startX, 130);
                    ImageView coinIv = new ImageView();
                    coinIv.setImage(coin.getImg());
                    coinIv.setX(coin.getPosition().getStartX());
                    coinIv.setY(coin.getPosition().getstartY());
                    coinIv.setFitHeight(coin.getPosition().getFitHeight());
                    coinIv.setFitWidth(coin.getPosition().getFitWidth());
                    coinIv.setPreserveRatio(false);
                    pane.getChildren().add(coinIv);
                    coinIVs.put(coinCounter, coinIv);
                    coinMap.put(coinCounter, coin);
                    coinCounter++;

                    coin = new Coin(startX, 170);
                    coinIv = new ImageView();
                    coinIv.setImage(coin.getImg());
                    coinIv.setX(coin.getPosition().getStartX());
                    coinIv.setY(coin.getPosition().getstartY());
                    coinIv.setFitHeight(coin.getPosition().getFitHeight());
                    coinIv.setFitWidth(coin.getPosition().getFitWidth());
                    coinIv.setPreserveRatio(false);
                    pane.getChildren().add(coinIv);
                    coinIVs.put(coinCounter, coinIv);
                    coinMap.put(coinCounter, coin);
                    coinCounter++;
                    startX += 40;
                }
            }

            startX += 300 + (int)(Math.random() * ((1000 - 300) + 1));;
        }
    }

    public ArrayList<ImageView> getIVs() {
        return ivs;
    }

    public ArrayList<Island> getIslands() {
        return islands;
    }

    public HashMap<Integer, ImageView> getChestIvs() {
        return chestIVs;
    }

    public HashMap<Integer, Chest> getChests() {
        return chestMap;
    }

    public HashMap<Integer, Orc> getOrcs() {
        return orcMap;
    }

    public HashMap<Integer, ImageView> getOrcIVs() {
        return orcIVs;
    }

    public HashMap<Integer, ImageView> getCoinIVs() {
        return coinIVs;
    }

    public HashMap<Integer, Coin> getCoinMap() {
        return coinMap;
    }

    public AnchorPane getPane() {
        return pane;
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}

class User {
    private Hero hero;
    private App game;
    public User(App game) {
        this.game = game;
        this.hero = new Hero(this);
    }

    public Hero getHero() {
        return this.hero;
    }

    public App getGame() {
        return game;
    }
}

class Hero implements collison {
    private ImageView hero;
    private TranslateTransition trans;
    private User user;
    private Helmet helmet;

    public Hero(User user) {
        this.hero = new ImageView();
        trans = new TranslateTransition();
        this.user = user;
        this.helmet = Helmet.getHelmet();
        try {
            Image heroImg = new Image(new FileInputStream("lib/Images/assets/Helmet3.png"));
            hero.setImage(heroImg);
            hero.setFitWidth(45.0);
            hero.setFitHeight(45.0);
            hero.setX(142.0);
            hero.setY(236.0);
            hero.setPreserveRatio(false);

            movement();
            weaponMovement();
            collide();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Helmet getHelmet() {
        return helmet;
    }

    private void movement() {
        trans.setNode(hero);
        trans.setAutoReverse(true);
        trans.setDuration(Duration.millis(750));
        trans.setCycleCount(2);
        trans.setByY(-180);      
        trans.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                ArrayList<ImageView> ivs = user.getGame().getIVs();
                int i;
                for(i=0; i<ivs.size(); i++) {
                    if(ivs.get(i).getBoundsInParent().getMinX() <= hero.getBoundsInParent().getMaxX()) {
                        continue;
                    } else {
                        break;
                    }
                }
                if(ivs.get(i-1).getBoundsInParent().getMinX() <= hero.getBoundsInParent().getMaxX() && ivs.get(i-1).getBoundsInParent().getMaxX() >= hero.getBoundsInParent().getMinX()) {
                    movement();
                   
                } else {
                    fall();
                }
            }
        });
        trans.play();
    }

    private void fall() {
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

    public TranslateTransition getJumpTran() {
        return trans;
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
                HashMap<Integer, ImageView> chestIVs = user.getGame().getChestIvs();
                HashMap<Integer, Chest> chestMap = user.getGame().getChests();
                for(Map.Entry<Integer, ImageView> chestIV: chestIVs.entrySet()) {
                    if(user.getHero().getHero().getBoundsInParent().intersects(chestIV.getValue().getBoundsInParent()) && !chestMap.get(chestIV.getKey()).getOpened()) {
                        try {
                            System.out.println("hello");
                            System.out.println(chestMap.get(chestIV.getKey()).getType());
                            if(chestMap.get(chestIV.getKey()).getType().equals("WeaponChest")) {
                                WeaponChest wc = (WeaponChest) chestMap.get(chestIV.getKey());
                                if(wc.getWeapon().getType().equals("Sword")) {
                                    if(!user.getHero().getHelmet().hasSword()) {
                                        user.getHero().getHelmet().setHasSword();
                                        if(helmet.hasLance()) {
                                            user.getGame().getPane().getChildren().remove(helmet.getCurrentWeapon().getWeaponIV());
                                        }
                                        user.getHero().getHelmet().setCurrentWeapon(new Sword());
                                        ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                                        iv.setImage(user.getHero().getHelmet().getCurrentWeapon().getImage());
                                        iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                                        iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 17.5);
                                        iv.setFitHeight(40);
                                        iv.setFitWidth(20);
                                        iv.setPreserveRatio(false);
                                        user.getGame().getPane().getChildren().add(iv);
                                    } else {

                                    }
                                } else {
                                    if(!user.getHero().getHelmet().hasLance()) {
                                        user.getHero().getHelmet().setHasLance();
                                        if(helmet.hasSword()) {
                                            user.getGame().getPane().getChildren().remove(helmet.getCurrentWeapon().getWeaponIV());
                                        }
                                        user.getHero().getHelmet().setCurrentWeapon(new Lance());
                                        ImageView iv = user.getHero().getHelmet().getCurrentWeapon().getWeaponIV();
                                        iv.setImage(user.getHero().getHelmet().getCurrentWeapon().getImage());
                                        iv.setX(user.getHero().getHero().getBoundsInParent().getMaxX());
                                        iv.setY(user.getHero().getHero().getBoundsInParent().getCenterY() - 7);
                                        iv.setFitHeight(14);
                                        iv.setFitWidth(40);
                                        iv.setPreserveRatio(false);
                                        user.getGame().getPane().getChildren().add(iv);
                                    } else {

                                    }
                                }
                            } else {

                            }
                            user.getGame().getPane().getChildren().remove(chestIV.getValue());
                            ImageView openChest = new ImageView();
                            Image openChestImg = new Image(new FileInputStream("lib/Images/assets/ChestOpen.png"));
                            openChest.setImage(openChestImg);
                            openChest.setX(chestIV.getValue().getBoundsInParent().getMinX());
                            openChest.setY(chestIV.getValue().getBoundsInParent().getMinY());
                            openChest.setFitHeight(chestIV.getValue().getFitHeight());
                            openChest.setFitWidth(chestIV.getValue().getFitWidth());
                            openChest.setPreserveRatio(false);
                            
                            user.getGame().getPane().getChildren().add(openChest);
                            chestIVs.replace(chestIV.getKey(), openChest);
                            chestMap.get(chestIV.getKey()).setOpened();
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
                HashMap<Integer, ImageView> coinIVs = user.getGame().getCoinIVs();
                HashMap<Integer, Coin> coinMap = user.getGame().getCoinMap();
                for(Map.Entry<Integer, ImageView> coinIV: coinIVs.entrySet()) {
                    if(user.getHero().getHero().getBoundsInParent().intersects(coinIV.getValue().getBoundsInParent()) && !coinMap.get(coinIV.getKey()).getCollected()) {  
                        user.getGame().getPane().getChildren().remove(coinIV.getValue());
                        coinMap.get(coinIV.getKey()).isCollected();
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
                HashMap<Integer, ImageView> orcIVs = user.getGame().getOrcIVs();
                HashMap<Integer, Orc> orcMap = user.getGame().getOrcs();
                for(Map.Entry<Integer, ImageView> orcIV: orcIVs.entrySet()) {
                    if(user.getHero().getHero().getBoundsInParent().intersects(orcIV.getValue().getBoundsInParent())) {
                        TranslateTransition orcCollison = new TranslateTransition();
                        orcCollison.setByX(40);
                        orcCollison.setCycleCount(1);
                        orcCollison.setDuration(Duration.millis(50));
                        orcCollison.setNode(orcIV.getValue());
                        orcCollison.play();
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
                if(helmet.hasLance()) {
                    helmet.getCurrentWeapon().getWeaponIV().setX(hero.getBoundsInParent().getMaxX());
                    helmet.getCurrentWeapon().getWeaponIV().setY(hero.getBoundsInParent().getCenterY() - 7);
                } else if(helmet.hasSword()) {
                    helmet.getCurrentWeapon().getWeaponIV().setX(hero.getBoundsInParent().getMaxX());
                    helmet.getCurrentWeapon().getWeaponIV().setY(hero.getBoundsInParent().getCenterY() - 11.25);
                }      
            }
            
        };
        timer.start();
    }
} 

abstract class GameObjects {
    private Coordinates coordinates;
    
    protected GameObjects(double startX, double startY, double widthX, double widthY) {
        coordinates = new Coordinates(startX, startY, widthX, widthY);
    }

    protected Coordinates getPosition() {
        return coordinates;
    }

    protected void setPosition(double startX, double startY) {
        coordinates.setCoordinates(startX, startY);
    }
}

class Island extends GameObjects {
    private Image img;
    public Island(String img, double startX, double startY, double widthX, double widthY) {
        super(startX, startY, widthX, widthY);
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
    private Image img;
    protected final String type;
    private TranslateTransition trans;
    private ImageView iv;
    private ArrayList<ImageView> ivs;
    private HashMap<Integer, ImageView> orcIVs;

    protected Orc(String img, String type, double startX, double startY, double widthX, double widthY, ArrayList<ImageView> ivs, HashMap<Integer, ImageView> orcIVs) {
        super(startX, startY, widthX, widthY);
        this.type = type;
        this.ivs = ivs;
        this.orcIVs = orcIVs;
        this.trans = new TranslateTransition();
        iv = new ImageView();
        try {
            this.img = new Image(new FileInputStream(img));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        iv.setImage(getImg());
        iv.setFitWidth(widthX);
        iv.setFitHeight(widthY);
        iv.setX(startX);
        iv.setY(startY);
        iv.setPreserveRatio(false);
        jump();
    }

    public ImageView getOrcIV() {
        return iv;
    }

    public Image getImg() {
        return img;
    }

    public void jump() {
        trans.setNode(iv);
        trans.setAutoReverse(true);
        trans.setDuration(Duration.millis(750));
        trans.setCycleCount(2);
        trans.setByY(-200);
        trans.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                int i;
                for(i=0; i<ivs.size(); i++) {
                    if(ivs.get(i).getBoundsInParent().getMinX() <= iv.getBoundsInParent().getMaxX()) {
                        continue;
                    } else {
                        break;
                    }
                }
                if(ivs.get(i-1).getBoundsInParent().getMinX() <= iv.getBoundsInParent().getMaxX() && ivs.get(i-1).getBoundsInParent().getMaxX() >= iv.getBoundsInParent().getMinX()) {
                    jump();
                } else {
                    fall();
                }
                
            }
            
        });
        trans.play();
    }

    private void fall() {
        trans.setNode(iv);
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

    public RedOrc(double startX, double startY, double widthX, double widthY, ArrayList<ImageView> ivs, HashMap<Integer, ImageView> orcIVs) {
        super("lib/Images/assets/RedOrc1.png", "RedOrc", startX, startY, widthX, widthY, ivs, orcIVs);
    }
    
}

class GreenOrc extends Orc {

    public GreenOrc(double startX, double startY, double widthX, double widthY, ArrayList<ImageView> ivs, HashMap<Integer, ImageView> orcIVs) {
        super("lib/Images/assets/Orc3.png", "GreenOrc", startX, startY, widthX, widthY, ivs, orcIVs);
    }
    
}

class BossOrc extends Orc {

    public BossOrc(double startX, double startY, double widthX, double widthY, ArrayList<ImageView> ivs, HashMap<Integer, ImageView> orcIVs) {
        super("lib/Images/assets/OrcBoss.png", "BossOrc", startX, startY, widthX, widthY, ivs, orcIVs);
    }
    
}

abstract class Chest extends GameObjects {

    private Image img;
    protected final String type;
    private boolean opened;

    protected Chest(String img, String type, double startX, double startY, double widthX, double widthY) {
        super(startX, startY, widthX, widthY);
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

class Helmet {
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

abstract class Weapon {
    private final String type;
    private ImageView weaponIV;
    private Image weaponImg;

    protected Weapon(String type, String img) {
        this.type = type;
        this.weaponIV = new ImageView();
        try {
            this.weaponImg = new Image(new FileInputStream(img));
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

}

class Sword extends Weapon {
    public Sword() {
        super("Sword", "lib/Images/assets/WeaponSword.png");
    }
}

class Lance extends Weapon {
    public Lance() {
        super("Lance", "lib/Images/assets/WeaponChampionLance.png");
    }
}

class Coin extends GameObjects {

    private Image img;
    private boolean collected;

    protected Coin(double startX, double startY) {
        super(startX, startY, 20, 20);
        try {
            img = new Image(new FileInputStream("lib/Images/assets/Coin.png"));
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

class Coordinates {
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