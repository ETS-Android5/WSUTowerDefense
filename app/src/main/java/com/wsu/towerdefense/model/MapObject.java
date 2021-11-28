package com.wsu.towerdefense.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.wsu.towerdefense.Util;

public abstract class MapObject {
    protected transient PointF position;
    protected final Bitmap bitmap;

    public MapObject(PointF position, Bitmap bitmap) {
        this.position = position;
        this.bitmap = bitmap;
    }

    /** Updates a MapObject's attributes to reflect all changes that occurred during a time interval
     *
     * @param deltaTime amount of time that has passed between updates
     */
    protected abstract void update(double deltaTime);

    /** Draws a MapObject to the provided canvas, interpolating changes to provide smooth movement.
     *
     * @param lerp interpolation factor
     * @param canvas the canvas to draw the MapObject on
     * @param paint the paint to use for drawing
     */
    protected abstract void render(double lerp, Canvas canvas, Paint paint);

    /**
     * @return A Bitmap representation of the MapObject
     */
    protected final Bitmap getBitmap() { return bitmap; }

    public PointF getPosition() { return this.position; }

    public void setPosition(PointF position) {
        this.position = position;
    }
}
