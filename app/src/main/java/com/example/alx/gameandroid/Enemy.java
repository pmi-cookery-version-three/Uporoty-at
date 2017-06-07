package com.example.alx.gameandroid;

/**
 * Created by Alx on 08.06.2017.
 */

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Enemy
{
    /**Х и У коорданаты*/
    public int x;
    public int y;

    int r;

    /**Скорость*/
    public int speed;

    /**Выосота и ширина спрайта*/
    public int width;
    public int height;

    public GameView gameView;
    public Bitmap bmp;

    /**Конструктор класса*/
    public Enemy(GameView gameView, Bitmap bmp){
        this.gameView = gameView;
        this.bmp = bmp;

        Random rnd = new Random();
        this.x = 1800;
        this.y = rnd.nextInt(1000);
        this.speed = rnd.nextInt(9)+1;

        this.width = bmp.getWidth();
        this.height = bmp.getHeight();

        this.r = bmp.getWidth()/2;
    }

    public void update(){
        x -= speed;
    }

    public void onDraw(Canvas c){
        update();
        c.drawBitmap(bmp, x - r, y -r, null);
    }
}