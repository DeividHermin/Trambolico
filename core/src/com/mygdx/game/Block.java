package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Deivi on 03/03/2017.
 */

public class Block extends Rectangle{

    private int vida;

    public Block(float x, float y, float width, float height, int vida) {
        super(x, y, width, height);
        this.vida=vida;
    }

    public Block(Rectangle rect, int vida) {
        super(rect);
        this.vida=vida;
    }

    public void setX(int x){
        this.x=x;
    }

    public float getX(){
        return x;
    }

    public void setY(int y){
        this.y=y;
    }

    public float getY(){
        return y;
    }

    public void setVida(int vida){
        this.vida=vida;
    }

    public int getVida(){
        return vida;
    }

    public void restaVida(){
        vida--;
    }
}
