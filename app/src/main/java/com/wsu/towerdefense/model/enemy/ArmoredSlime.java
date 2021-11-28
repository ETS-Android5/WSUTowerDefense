package com.wsu.towerdefense.model.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.wsu.towerdefense.R;
import com.wsu.towerdefense.Util;

import java.util.List;

public class ArmoredSlime extends Slime {

    private static final Bitmap LEVEL_1_BITMAP = Util.getBitmapByID(R.mipmap.armored_slime_1);
    private static final Bitmap LEVEL_2_BITMAP = Util.getBitmapByID(R.mipmap.armored_slime_2);
    private static final Bitmap LEVEL_3_BITMAP = Util.getBitmapByID(R.mipmap.armored_slime_3);

    private static final Bitmap ARMOR_1 = Util.getBitmapByID(R.mipmap.armor_1);
    private static final Bitmap ARMOR_2 = Util.getBitmapByID(R.mipmap.armor_2);
    private static final Bitmap ARMOR_3 = Util.getBitmapByID(R.mipmap.armor_3);

    public enum Level {
        ONE(100, 30, 20, 1, LEVEL_1_BITMAP, ARMOR_1, 1),
        TWO(150, 40, 25, 2, LEVEL_2_BITMAP, ARMOR_2, 2),
        THREE(250, 50, 30, 3, LEVEL_3_BITMAP, ARMOR_3, 3);

        final int speed, startHp, dropValue, damage, armorHp;
        final Bitmap bitmap, armorBitmap;
        Level(int speed, int startHp, int dropValue, int damage, Bitmap bitmap,
              Bitmap armorBitmap, int armorHp) {
            this.speed = speed;
            this.startHp = startHp;
            this.dropValue = dropValue;
            this.damage = damage;
            this.bitmap = bitmap;
            this.armorBitmap = armorBitmap;
            this.armorHp = armorHp;
        }
    }


    private Bitmap armorBitmap;
    private int armorHp;

    public ArmoredSlime(Level level, List<PointF> path) {
        super(level.speed, level.startHp, level.dropValue, level.damage, path, level.bitmap);
        this.armorBitmap = level.armorBitmap;
        this.armorHp = level.armorHp;
    }

    @Override
    public void render(double lerp, Canvas canvas, Paint paint) {
        super.render(lerp, canvas, paint);

        // Interpolate position
        float x = (float) (position.x + velX * lerp);
        float y = (float) (position.y + velY * lerp);

        if (armorBitmap != null) {
            canvas.drawBitmap(armorBitmap, x - bitmap.getWidth() / 2f,
                    y - bitmap.getHeight() / 2f, null);
        }

    }

    @Override
    public void takeDamage(int damage) {
        if (armorHp == 0) {
            hp = Math.max(hp - damage, 0);
        }
    }

    public void damageArmor(int damage) {
        armorHp = Math.max(armorHp - damage, 0);

        if (armorHp == 0) armorBitmap = null;
    }

}
