package com.example.alx.gameandroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Alx on 07.06.2017.
 */

public class Weapon {
    /**Объект главного класса*/
    GameView gameView;

    //спрайт
    Bitmap bmp;

    //х и у координаты рисунка
    int x;
    int y;

    int angle;  // Угол в градусах

    // конструктор
    public Weapon(GameView gameView, Bitmap bmp, Player plr)
    {
        this.gameView = gameView;
        this.bmp = bmp;                             //возвращаем рисунок

        this.x = plr.x - 25;                        //отступ по х нет
        this.y = plr.y + 60; //делаем по центру

        this.angle = 0;
    }

    public Weapon(Weapon w)
    {
        this.gameView = w.gameView;
        this.bmp = w.bmp;                    //возвращаем рисунок
        this.x = w.x;                        //отступ по х нет
        this.y = w.y; //делаем по центру
    }

    public void setPosition(int _x, int _y){
        this.x = _x - 25;                        //отступ по х нет
        this.y = _y + 60;
    }

    public void onRotation(Canvas c, int angel)
    {
        c.save();
        c.rotate(angel, x + bmp.getWidth()/2, y + bmp.getHeight()/2);
        c.restore();
    }

    //рисуем наш спрайт
    public void onDraw(Canvas сtx)
    {
        сtx.save();
        сtx.rotate(angle, x + bmp.getWidth()/2, y + bmp.getHeight()/2);
        сtx.drawBitmap(bmp, x, y, null);
        сtx.restore();
    }
}
