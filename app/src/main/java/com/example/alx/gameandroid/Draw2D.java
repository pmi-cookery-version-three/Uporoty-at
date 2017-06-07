package com.example.alx.gameandroid;

/**
 * Created by Alx on 25.05.2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Draw2D extends View {

    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    public Bitmap mBitmap;

    public  Canvas ctx;
    public Draw2D(Context context) {
        super(context);

        // Выводим значок из ресурсов
        Resources res = this.getResources();
        mBitmap = BitmapFactory.decodeResource(res, R.drawable.icon_winmid_200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ctx = canvas;
        super.onDraw(ctx);



        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // стиль Заливка
        mPaint.setStyle(Paint.Style.FILL);

        // закрашиваем холст белым цветом
        mPaint.setColor(Color.WHITE);
        canvas.drawPaint(mPaint);

        // Рисуем желтый круг
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);
        // canvas.drawCircle(950, 30, 25, mPaint);
        canvas.drawCircle(width - 30, 30, 25, mPaint);

        // Рисуем зеленый прямоугольник
        mPaint.setColor(Color.GREEN);
        //  canvas.drawRect(20, 650, 950, 680, mPaint);
        canvas.drawRect(0, canvas.getHeight() - 30, width, height, mPaint);

        // Рисуем текст
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(32);
        //  canvas.drawText("Лужайка только для котов", 30, 648, mPaint);
        canvas.drawText("Текст", 30, height - 32, mPaint);

        // Текст под углом
        // int x = 810;
        int x = width - 170;
        int y = 190;

        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(27);
        String beam = "Еще текст";

        canvas.save();
        // Создаем ограничивающий прямоугольник для наклонного текста
        // поворачиваем холст по центру текста
        canvas.rotate(-45, x + mRect.exactCenterX(), y + mRect.exactCenterY());

        // Рисуем текст
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(beam, x, y, mPaint);

        // восстанавливаем холст
        canvas.restore();

        // Выводим изображение
        // canvas.drawBitmap(mBitmap, 450, 530, mPaint);
        canvas.drawBitmap(mBitmap, width - mBitmap.getWidth(), height - mBitmap.getHeight() - 10, mPaint);


        DrawPoration(ctx, mBitmap, 40, 100, 90);

    }

    // Перехват нажатия
    public void Click(float x, float y){
        try {
            DrawPoration(ctx, mBitmap, 40, 100, 90);
        } catch (Exception e){
            Log.e("Error", "Eroor");
            e.fillInStackTrace();
        }

    }

    // Отрисовывает картинк в заданных координатах и под определенным углом
    public void DrawPoration(Canvas canvas, Bitmap img, float x, float y, float a){
        canvas.save();
        canvas.rotate(-45, x + mRect.exactCenterX(), y + mRect.exactCenterY());

        canvas.drawBitmap(mBitmap, x,y, mPaint);

        canvas.restore();

    }

}