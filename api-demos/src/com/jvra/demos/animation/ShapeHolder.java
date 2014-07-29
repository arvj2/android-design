package com.jvra.demos.animation;

import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 7/14/2014.
 */
public class ShapeHolder {

    private float x = 0, y = 0;
    private ShapeDrawable shape;
    private int color;
    private RadialGradient gradient;
    private float alpha = 1f;
    private Paint paint;


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ShapeDrawable getShape() {
        return shape;
    }

    public void setShape(ShapeDrawable shape) {
        this.shape = shape;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        shape.getPaint().setColor(color);
        this.color = color;
    }

    public RadialGradient getGradient() {
        return gradient;
    }

    public void setGradient(RadialGradient gradient) {
        this.gradient = gradient;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        shape.setAlpha((int) ((alpha*255f)+.5f));
        this.alpha = alpha;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }


   public float getWidth(){
       return  shape.getShape().getWidth();
   }

    public void setWidth(float width){
        final Shape s = shape.getShape();
        shape.getShape().resize( width, s.getHeight() );
    }

    public float getHeight(){
        return shape.getShape().getHeight();
    }

    public void setHeight( float height ){
        final Shape s = shape.getShape();
        shape.getShape().resize(s.getWidth(),height);
    }


    public ShapeHolder( ShapeDrawable shape ){
        this.shape = shape;
    }
}
