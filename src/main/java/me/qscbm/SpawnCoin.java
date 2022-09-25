package me.qscbm;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SpawnCoin implements EntityFactory {
    @Spawns("coin")
    public Entity newCoin(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COIN)
                .with(new CollidableComponent(true))
                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .build();
    }
}