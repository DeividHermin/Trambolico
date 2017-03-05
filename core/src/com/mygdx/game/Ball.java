package com.mygdx.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Deivi on 03/03/2017.
 */

public class Ball extends Circle {

    private int speedx, speedy, maxX, maxY;


    public Ball(int x, int y, int radius, int maxX, int maxY) {
        super(x, y, radius);

        this.maxX = maxX;
        this.maxY = maxY;
        speedx = 4;
        speedy = 4;
    }

    public void move(){
        x=x+speedx;
        y=y+speedy;

        if (x < 0) {
            x = 0;
            speedx=-speedx;
        }
        if (x > maxX - radius) {
            x = maxX - radius;
            speedx=-speedx;
        }
        if (y > maxY - radius) {
            y = maxY - radius;
            speedy=-speedy;
        }
    }

    public void bounceX(){
        speedx=-speedx;
        x=x+speedx;
    }

    public void bounceY(){
        speedy=-speedy;
        y=y+speedy;
    }

    public void increaseSpeedX(){
        if(speedx<0)
            speedx--;
        else
            speedx++;
    }

    public void increaseSpeedY(){
        if(speedy<0)
            speedy--;
        else
            speedy++;
    }

    public void addPositiveXSpeed(boolean positivo){
        if(positivo){ //si quieres mover la bola a la derecha
            //speedx++;
            if(speedx<0) //si se esta moviendo hacia la izquierda
                bounceX(); //rebotaX
        }else{
            //speedx--;
            if(speedx>0)
                bounceX();
        }
    }


    //-------------------------------GETTERS AND SETTERS--------------------------------

    public float getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeedx() {
        return speedx;
    }

    public void setSpeedx(int speedx) {
        this.speedx = speedx;
    }

    public int getSpeedy() {
        return speedy;
    }

    public void setSpeedy(int speedy) {
        this.speedy = speedy;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}
