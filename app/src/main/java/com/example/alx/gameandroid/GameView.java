package com.example.alx.gameandroid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameView extends SurfaceView implements Runnable
{
    /**Объект класса GameLoopThread*/
    private GameThread mThread;
    private Thread thred = new Thread(this);

    public int shotX;
    public int shotY;

    /**Переменная запускающая поток рисования*/
    private boolean running = false;


    int count = 0; // счет
    int game = 0; // 0 - игра, 1 - проиграл
    // Игрок
    Player player;
    Bitmap IMG_players;

    private List<Enemy> enemy = new ArrayList<Enemy>();

    Bitmap IMG_enemies = BitmapFactory.decodeResource(getResources(), R.drawable.e_rak);

    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.b_pistol);

    // Оружие: {0 - w_colt, 1 - w_mp4};
    Weapon[] weapon;
    Bitmap[] IMG_weapon;

    private List<Bullet> ball = new ArrayList<Bullet>();

    private SoundPool mSoundPool;
    private int mSoundId = 1;
    private int mStreamId;




    Paint p;

    Paint pCount;
    Paint pCountEnd;


    //-------------Start of GameThread--------------------------------------------------\\

    public class GameThread extends Thread
    {
        /**Объект класса*/
        private GameView view;

        /**Конструктор класса*/
        public GameThread(GameView view)
        {
            this.view = view;
        }

        /**Задание состояния потока*/
        public void setRunning(boolean run)
        {
            running = run;
        }

        /** Действия, выполняемые в потоке */
        public void run()
        {
            while (running)
            {
                Canvas canvas = null;
                try
                {
                    // подготовка Canvas-а
                    canvas = view.getHolder().lockCanvas();
                    synchronized (view.getHolder())
                    {
                        // собственно рисование
                        onDraw1(canvas);
                        testCollision();
                    }
                }
                catch (Exception e) { }
                finally
                {
                    if (canvas != null)
                    {
                        view.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
    }

    public void run() {
        Random rnd = new Random();
        int time = 2500;
        int dt = 2;
        while(true) {
            try {
                Thread.sleep(rnd.nextInt(time));

                if(time > 500){
                    enemy.add(new Enemy(this, IMG_enemies));
                    time -= dt;
                } else{
                    enemy.add(new Enemy(this, IMG_enemies));
                    enemy.add(new Enemy(this, IMG_enemies));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    // Конструктор
    public GameView(Context context)
    {
        super(context);

        mThread = new GameThread(this);
        /*Рисуем все наши объекты и все все все*/
        getHolder().addCallback(new SurfaceHolder.Callback()
        {
            /*** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;
                mThread.setRunning(false);
                while (retry)
                {
                    try
                    {
                        // ожидание завершение потока
                        mThread.join();
                        retry = false;
                    }
                    catch (InterruptedException e) { }
                }
            }

            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder)
            {
                mThread.setRunning(true);
                mThread.start();
            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
                thred.start();
            }
        });

        IMG_players = BitmapFactory.decodeResource(getResources(), R.drawable.cat1);
        player = new Player(this, IMG_players);
        player.x = 50;
        player.y = 300;

        Bitmap bmp_w_colt = BitmapFactory.decodeResource(getResources(), R.drawable.w_colt);
        Bitmap bmp_w_mp4 = BitmapFactory.decodeResource(getResources(), R.drawable.w_mp4);
        weapon = new Weapon[2];
        weapon[0] = new Weapon(this, bmp_w_colt, player);
        weapon[1] = new Weapon(this, bmp_w_mp4, player);


        player.setWeapon(weapon[1]);

        // Звук
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        mSoundPool.load(context, R.raw.gun_1, 1);

        p = new Paint();
        p.setColor(Color.DKGRAY);

        pCount = new Paint();
        pCount.setColor(Color.LTGRAY);
        pCount.setTextSize(30);

        pCountEnd = new Paint();
        pCountEnd.setColor(Color.RED);
        pCountEnd.setTextSize(200);
    }
    // Определение нажатия
    public boolean onTouchEvent(MotionEvent e)
    {
        // Перезапуск
        if(game == 1){
            player.x = 50;
            player.y = 300;

            player.weapon.setPosition(player.x, player.y);
            count = 0;
            enemy.clear();
            game = 0;
            return false;
        }


        int action = e.getAction();

        int shotX = (int) e.getX();
        int shotY = (int) e.getY();

        if(shotX < 200) {
            player.onMoveTo(shotX, shotY);
        }

        if(action == MotionEvent.ACTION_POINTER_DOWN || action == MotionEvent.ACTION_DOWN) {
            if (shotX > 200) {

                //Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.b_pistol);

                ball.add(new Bullet(this, bmp, player, shotX, shotY));

                // Угол поворота оружия
                player.onRotationWeapon(shotX, shotY);

                // Воспроизведение звука выстрела
                mStreamId = mSoundPool.play(mSoundId, 1, 1, 0, 0, 1);
            }
        }
        return true;
    }



    // Отрисовка графики
    public void onDraw1(Canvas canvas) {
        // Игровая область
        canvas.drawColor(Color.GRAY);

        // Область передвжения кота
        canvas.drawRect(0, 0, 200, canvas.getHeight(), p);

        // отрисовка врагов
        for(int i = 0; i < enemy.size(); i++){
            enemy.get(i).onDraw(canvas);
            if(enemy.get(i).x < -50)
                enemy.remove(enemy.get(i));
        }
        // Стабильно
        Iterator<Enemy> it = enemy.iterator();
        while(it.hasNext()) {
            Enemy e = it.next();
            if(e.x >= 1000 || e.x <= 1000) {
                e.onDraw(canvas);
            } else {
                it.remove();
            }
        }


        // отрисовка пуль
        for(int i = 0; i < ball.size(); i++){
            ball.get(i).onRotation(canvas);
            ball.get(i).onDraw(canvas);

            if(ball.get(i).x > canvas.getWidth()+50)
                ball.remove(ball.get(i));
        }

        if(game == 0){
            // Отрисовка игрока
            player.onDraw(canvas);
            // Отрисовка оружия игрока
            player.weapon.onDraw(canvas);
        } else{
            canvas.drawText("YOU DIED", canvas.getWidth() /2 - 450, canvas.getHeight() /2, pCountEnd);
        }



        // счет
        // вывод текста
        canvas.drawText("Счет: "+count, canvas.getWidth() /2, 30, pCount);
    }

    private void testCollision() {
        Iterator<Bullet> b = ball.iterator();
        while(b.hasNext()) {
            Bullet balls = b.next();
            Iterator<Enemy> i = enemy.iterator();
            while(i.hasNext()) {
                Enemy enemies = i.next();

                double dis = Math.sqrt( (balls.x - enemies.x)*(balls.x - enemies.x) + (balls.y - enemies.y)*(balls.y - enemies.y)  );

                if(dis < balls.r + enemies.r){
                    i.remove();
                    b.remove();
                    count++;
                }



                /*if ((Math.abs(balls.x - enemies.x) <= (balls.width + enemies.width) / 2f) && (Math.abs(balls.y - enemies.y) <= (balls.height + enemies.height) / 2f)) {
                    i.remove();
                    b.remove();
                }*/
            }
        }

        // проверка смерти игрока
        Iterator<Enemy> it = enemy.iterator();
        while(it.hasNext()) {
            Enemy enemies = it.next();

            double dis = Math.sqrt( (player.x - enemies.x)*(player.x - enemies.x) + (player.y - enemies.y)*(player.y - enemies.y)  );

            if(dis < player.r + enemies.r){
                it.remove();
                player.x = - 200;
                game = 1;
            }
        }

    }

}