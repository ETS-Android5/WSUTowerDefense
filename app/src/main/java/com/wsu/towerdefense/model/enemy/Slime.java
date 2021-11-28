package com.wsu.towerdefense.model.enemy;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.wsu.towerdefense.R;
import com.wsu.towerdefense.Util;

import java.util.List;
import java.util.ListIterator;

public class Slime extends Enemy {

    // TODO: Use Library to efficiently handle bitmaps
    private static final Bitmap LEVEL_1_BITMAP = Util.getBitmapByID(R.mipmap.standard_slime_1);
    private static final Bitmap LEVEL_2_BITMAP = Util.getBitmapByID(R.mipmap.standard_slime_2);
    private static final Bitmap LEVEL_3_BITMAP = Util.getBitmapByID(R.mipmap.standard_slime_3);

    public enum Level {
        ONE(200, 20, 15, 1, LEVEL_1_BITMAP),
        TWO(250, 30, 20, 2, LEVEL_2_BITMAP),
        THREE(350, 40, 25, 3, LEVEL_3_BITMAP);

        final int speed, startHp, dropValue, damage;
        final Bitmap bitmap;
        Level (int speed, int startHp, int dropValue, int damage, Bitmap bitmap) {
            this.speed = speed;
            this.startHp = startHp;
            this.dropValue = dropValue;
            this.damage = damage;
            this.bitmap = bitmap;
        }
    }

    /**
     * Constructs a Slime with attributes of <code>level</code> that will move along <code>path</code>.
     *
     * @param level The level of the Slime, determines attributes such as speed, hp, etc.
     * @param path  The list of points the Slime will travel to as it moves across the map
     */
    public Slime(Level level, List<PointF> path) {
        super(level.speed, level.startHp, level.dropValue, level.damage, path, level.bitmap);
    }

    /**
     * A constructor used to give child classes access to the Enemy class constructor.
     *
     * @param speed
     * @param maxHp
     * @param dropValue
     * @param damage
     * @param path
     * @param bitmap
     */
    protected Slime(float speed, int maxHp, int dropValue, int damage, List<PointF> path, Bitmap bitmap) {
        super(speed, maxHp, dropValue, damage, path, bitmap);
    }
}
