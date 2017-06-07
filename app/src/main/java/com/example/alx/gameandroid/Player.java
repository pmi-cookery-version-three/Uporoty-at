package com.example.alx.gameandroid;

/**
 * Created by Alx on 06.06.2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Player
{
    /**Объект главного класса*/
    GameView gameView;

    //спрайт
    Bitmap bmp;

    //х и у координаты рисунка
    int x;
    int y;
    int r;

    int width;
    int height;

    // Угол поворота кота
    int angle;
    int speed;

    Weapon weapon;

    // конструктор
    public Player(GameView gameView, Bitmap bmp)
    {

        this.weapon = null;
        this.gameView = gameView;
        this.bmp = bmp;                    //возвращаем рисунок

        this.x = 0;                        //отступ по х нет
        this.y = gameView.getHeight() / 2; //делаем по центру
        this.r = gameView.getHeight() / 2;

        this.width = bmp.getWidth();
        this.height = bmp.getWidth();
        this.speed = 10;

    }

    public void onRotation()
    {
        angle = (int)Math.atan((double)(y - gameView.shotY) / (x - gameView.shotX));
    }

    public void onRotationWeapon(int shotX, int shotY)
    {
        weapon.angle = (int)(Math.atan((double)(y - shotY) / (x - shotX)) *180 / Math.PI);
    }

    // двигаться к точке
    public  void onMoveTo(int shotX, int shotY){

        int vx = shotX - x;
        int vy = shotY - y;

        double dis = Math.sqrt(vx*vx + vy*vy);

        if(dis == 0)
            dis = 1;

        double nvx = vx/dis;
        double nvy = vy/dis;

        nvx *= speed;
        nvy *= speed;

        x += nvx;
        y += nvy;

        // передвигаем оружие
        weapon.setPosition(x, y);
    }


    public void setWeapon(Weapon w) {
        weapon = w;
    }


    //рисуем наш спрайт
    public void onDraw(Canvas сtx)
    {
        сtx.save();
        сtx.rotate(angle, x + r, y + r);
        сtx.drawBitmap(bmp, x, y, null);
        сtx.restore();
    }
}