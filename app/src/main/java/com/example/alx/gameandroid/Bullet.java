package com.example.alx.gameandroid;

/**
 * Created by Alx on 07.06.2017.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bullet
{
    /**Картинка*/
    private Bitmap bmp;

    /**Позиция*/
    public int x;
    public int y;

    /**Скорость по Х=15*/
    private int mSpeed=25;

    public double angle;

    /**Ширина*/
    public int width;

    /**Ввыоста*/
    public  int height;

    int r;  // радиус

    public GameView gameView;

    /**Конструктор*/
    public Bullet(GameView gameView, Bitmap bmp, Player plr, int shotX, int shotY) {
        this.gameView=gameView;
        this.bmp = bmp;

        this.x = plr.x + plr.width/2;                    //позиция по Х
        this.y = plr.y + plr.height/2;     //позиция по У
        this.width = bmp.getWidth();       //ширина снаряда
        this.height = bmp.getHeight();     //высота снаряда
        this.r = bmp.getWidth()/2;
        //угол полета пули в зависипости от координаты косания к экрану
        angle = Math.atan((double)(y - shotY) / (x - shotX));
    }

    /**Перемещение объекта, его направление*/
    private void update() {
        x += mSpeed * Math.cos(angle);         // движение по Х со скоростью mSpeed и углу заданном координатой angle
        y += mSpeed * Math.sin(angle);         // движение по У -//-
    }

    public void onRotation(Canvas сtx)
    {
        сtx.save();
        сtx.rotate((int)angle, x + width/2, y + height/2);
        сtx.restore();
    }

    /**Рисуем наши спрайты*/
    public void onDraw(Canvas сtx) {
        update();                              //говорим что эту функцию нам нужно вызывать для работы класса

        сtx.save();
        сtx.rotate((int)(angle*180/Math.PI), x + r, y + r);
        сtx.drawBitmap(bmp, x - r, y - r, null);
        сtx.restore();
    }
}