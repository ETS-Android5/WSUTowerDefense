package com.wsu.towerdefense.model.enemy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import androidx.annotation.CallSuper;

import com.wsu.towerdefense.Util;
import com.wsu.towerdefense.model.MapObject;

import java.util.List;
import java.util.ListIterator;

public abstract class Enemy extends MapObject {
    public enum Type {
        SLIME,
        ARMORED_SLIME;
    }

    private static final int HEALTH_BAR_Y_OFFSET = -70;
    private static final int HEALTH_BAR_WIDTH = 90;
    private static final int HEALTH_BAR_HEIGHT = 15;
    private static final int HEALTH_BAR_BG_COLOR = Color.RED;
    private static final int HEALTH_BAR_FG_COLOR = Color.GREEN;

    private static final int INVISIBLE_OPACITY = 150;

    protected final float speed;
    protected final int dropValue;
    protected final int damage;
    protected final int maxHp;
    protected final ListIterator<PointF> path;

    protected int hp;
    protected boolean isInvisible;
    protected PointF destination;

    protected float velX = 0.0f;
    protected float velY = 0.0f;

    protected double timeSlowed = 0.0;
    protected double movePercent = 1f;

    protected Enemy(float speed, int maxHp, int dropValue, int damage, List<PointF> path, Bitmap bitmap) {
        super(new PointF(path.get(0).x, path.get(0).y), bitmap);
        this.speed = speed;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.dropValue = dropValue;
        this.damage = damage;
        this.path = path.listIterator(1);
        this.destination = this.path.next();
        updateVelocity();
    }

    /**
     * Updates attributes of an enemy, such as position.
     *
     * @param deltaTime The amount of time that has passed since this method has been called
     */
    @Override @CallSuper
    public void update(double deltaTime) {
        // Update the time and percent enemy is slowed
        if (timeSlowed > 0) {
            timeSlowed = timeSlowed > deltaTime ? timeSlowed - deltaTime : 0d;
            if (timeSlowed == 0) movePercent = 1f;
        }

        double destDist = Math.hypot(destination.x - position.x, destination.y - position.y);

        // Update destination
        if (destDist <= Math.abs(speed * deltaTime)) {
            position.set(destination);
            if (path.hasNext()) {
                destination = path.next();
                updateVelocity();
            } else {
                destination = null;
            }
        }

        if(destination != null)
            move(deltaTime);
    }

    /**
     * Draws this Enemy's Bitmap image to the provided Canvas, interpolating changes in position to
     * maintain smooth movement regardless of updates since last drawn.
     *
     * @param lerp   interpolation factor
     * @param canvas the canvas this Enemy will be drawn on
     * @param paint  the paint object used to paint onto the canvas
     */
    @Override
    public void render(double lerp, Canvas canvas, Paint paint) {
        // Interpolate position
        float x = (float) (position.x + velX * lerp);
        float y = (float) (position.y + velY * lerp);

        paint.reset();
        if (isInvisible) {
            paint.setAlpha(INVISIBLE_OPACITY);
        }

        canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2f,
                y - bitmap.getHeight() / 2f, paint);

        // Only display health bar when damaged
        if (hp < maxHp) {
            paint.reset();

            paint.setColor(HEALTH_BAR_BG_COLOR);
            canvas.drawRect(
                    x - HEALTH_BAR_WIDTH / 2f,
                    y + HEALTH_BAR_Y_OFFSET,
                    x + HEALTH_BAR_WIDTH / 2f,
                    y + HEALTH_BAR_Y_OFFSET + HEALTH_BAR_HEIGHT,
                    paint
            );

            float healthPercent = (float) hp / maxHp;
            paint.setColor(HEALTH_BAR_FG_COLOR);
            canvas.drawRect(
                    x - HEALTH_BAR_WIDTH / 2f,
                    y + HEALTH_BAR_Y_OFFSET,
                    x - HEALTH_BAR_WIDTH / 2f + healthPercent * HEALTH_BAR_WIDTH,
                    y + HEALTH_BAR_Y_OFFSET + HEALTH_BAR_HEIGHT,
                    paint
            );
        }
    }

    // Helper Methods

    private void updateVelocity() {
        float dx = destination.x - position.x;
        float dy = destination.y - position.y;
        double distance = Math.hypot(dx, dy);
        velX = speed * (float) (dx / distance);
        velY = speed * (float) (dy / distance);
    }

    /**
     * Moves an enemy towards the current destination point on its path.
     *
     * @param deltaTime The time passed since last move
     */
    private void move(double deltaTime) {
        float moveX = (float) (velX * deltaTime * movePercent);
        float moveY = (float) (velY * deltaTime * movePercent);

        this.position.offset(moveX, moveY);
    }

    public float getSpeed() { return speed; }

    public int getDropValue() { return dropValue; }

    public int getDamage() { return damage; }

    public int getHp() { return hp; }

    public boolean isAlive() { return hp > 0; }

    public boolean isInvisible() { return isInvisible; }

    public void setInvisible(boolean invisible) { isInvisible = invisible; }

    public boolean isAtPathEnd() { return destination == null; }

    public void takeDamage(int damage) { hp = Math.max(hp - damage, 0); }

    /**
     * Sets this enemy's movement speed to <code>movePercent</code> for <code>duration</code> seconds.
     *
     * <br><br>ex: slow(4.5, 0.25) - Enemy will move at 25% speed for 4.5 seconds.
     *
     * <br><br>Special cases:<br>
     * If this enemy is currently slowed, its movement speed will be set to the min of
     * <code>movePercent</code> and the current movement percent. <br><br>
     * If the current movement percent is equal to <code>movePercent</code> the duration it is slowed for
     * will be set to the max of <code>duration</code> and the current duration.
     *
     * @param duration      The duration (in seconds) to slow the enemy
     * @param movePercent   The percent to reduce enemy movement speed by
     */
    public void slow(double duration, double movePercent) {
        this.movePercent = Math.min(this.movePercent, movePercent);
        if (this.movePercent == movePercent) timeSlowed = duration;
    }

    /**
     * Determines whether a given hitbox collides with this enemy's hitbox
     *
     * @param x         The hitbox's center point x coordinate
     * @param y         The hitbox's center point y coordinate
     * @param width     The width of the hitbox
     * @param height    The height of the hitbox
     * @return  True if the given hitbox overlaps this enemy's hitbox at any point <br>
     *          False otherwise
     */
    public boolean collidesWithHitbox(float x, float y, float width, float height) {
        return x - width / 2 <= position.x + bitmap.getWidth() / 2f &&
                x + width / 2 >= position.x - bitmap.getWidth() / 2f &&
                y - height / 2 <= position.y + bitmap.getHeight() / 2f &&
                y + height / 2 >= position.y - bitmap.getHeight() / 2f;
    }
}
