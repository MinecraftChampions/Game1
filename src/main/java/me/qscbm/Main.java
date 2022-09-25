package me.qscbm;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.time.TimerAction;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Main extends GameApplication {
    public static Entity player;

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50);
        textPixels.setTranslateY(100);

        FXGL.getGameScene().addUINode(textPixels);
        Rectangle ui = new Rectangle(50, 50);
        ui.setStroke(Color.YELLOW);
        Rectangle UI = new Rectangle(50,50);
        UI.setFill(null);
        FXGL.addUINode(new StackPane(ui,FXGL.getUIFactoryService().newText(FXGL.getip("Count").asString())),25,25);
        Text text = FXGL.getUIFactoryService().newText(FXGL.getip("Time").asString());
        text.setFill(Color.BLACK);
        FXGL.addUINode(new StackPane(UI,text),425,38);

        Text text2 = new Text("最高记录：" + Config.getMax());
        Rectangle U = new Rectangle(50,50);
        U.setFill(null);
        text2.setFill(Color.BLACK);
        text2.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        FXGL.addUINode(new StackPane(U,text2),400,628d);
    }


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(500);
        settings.setHeight(640);
        settings.setTitle("第一个游戏");
        settings.setVersion("0.1");
    }

    Entity e;

    @Override
    protected void initGame() {
        Rectangle u = new Rectangle(378,42);
        u.setFill(null);
        Text text = new Text("按下回车键开始游戏");
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 42));
        text.setFill(Color.BLACK);
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(210, 560)
                .with(new CollidableComponent(true))
                .viewWithBBox("player.png")
                .buildAndAttach();
        FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(0,0)
                .with(new CollidableComponent(true))
                .viewWithBBox(new Rectangle(5,640,Color.BLACK))
                .buildAndAttach();
        FXGL.entityBuilder()
                .type(EntityType.WALL)
                .at(495,0)
                .with(new CollidableComponent(true))
                .viewWithBBox(new Rectangle(5,640,Color.BLACK))
                .buildAndAttach();
        e = FXGL.entityBuilder()
                .at(56,299)
                .with(new CollidableComponent(true))
                .viewWithBBox(new StackPane(u,text))
                .buildAndAttach();
        getGameWorld().addEntityFactory(new SpawnCoin());
    }

    public static void main(String[] args) {
        Config.init();
        launch(args);
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.D, () -> player.translateX(5));

        onKey(KeyCode.RIGHT, () -> player.translateX(5));

        onKey(KeyCode.A, () -> player.translateX(-5));

        onKey(KeyCode.LEFT, () -> player.translateX(-5));

        onKey(KeyCode.ENTER, () -> {
            int isInt = FXGL.getip("isInit").intValue();
            FXGL.getWorldProperties().setValue("isInit",1);
            if (isInt == 0)  {
                TimerAction a = getGameTimer().runAtInterval(this::update1, Duration.seconds(Config.getSpawn()));
                TimerAction b = getGameTimer().runAtInterval(this::update2, Duration.seconds(1));
                e.removeFromWorld();
                getGameTimer().runOnceAfter(() -> {
                    for (Entity entity : list) {
                        entity.removeFromWorld();
                    }
                    list = new ArrayList<>();
                    if (Config.getMax() < FXGL.getip("Count").intValue()) {
                        Config.putMax(FXGL.getip("Count").intValue());
                    }
                    Rectangle u = new Rectangle(378,42);
                    u.setFill(null);
                    Text text = new Text("游戏结束");
                    text.setFont(Font.font("Verdana", FontWeight.BOLD, 42));
                    text.setFill(Color.BLACK);
                    FXGL.entityBuilder()
                            .at(161,299)
                            .with(new CollidableComponent(true))
                            .viewWithBBox(new StackPane(u,text))
                            .buildAndAttach();
                    a.expire();
                    b.expire();
                },Duration.seconds(60));
            }
        });
    }

    public static List<Entity> list = new ArrayList<>();
    public void update1() {
        Entity coin = getGameWorld().spawn("coin");
        coin.setPosition(new Random().nextDouble(10,460),0);
        list.add(coin);
    }

    public void update2() {
        int time = FXGL.getip("Time").intValue() - 1;
        FXGL.getWorldProperties().setValue("Time",time);
    }

    @Override
    public void onUpdate(double tpf) {
        if (player.getX() < 0 || player.getX() > 640) {
            player.setPosition(210, 560);
        }
        if (list.isEmpty()) return;
        List<Entity> entityList = new ArrayList<>();
        for (Entity cin : list) {
            cin.translateY(Config.getSpeed());
            if (cin.getY() >= 640) {
                int count = FXGL.getip("Count").intValue() - 1;
                FXGL.getWorldProperties().setValue("Count",count);
                entityList.add(cin);
                cin.removeFromWorld();
            }
        }
        list.removeAll(entityList);
    }



    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                list.remove(coin);
                coin.removeFromWorld();
                inc("Count",1);
            }
        });
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity player, Entity wall) {
                if (player.getX() < 320) {
                    player.translateX(10);
                } else {
                    player.translateX(-10);
                }
            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("Count", 0);
        vars.put("Time",60);
        vars.put("isInit",0);
    }
}