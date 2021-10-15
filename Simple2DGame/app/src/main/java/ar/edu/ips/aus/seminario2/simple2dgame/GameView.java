package ar.edu.ips.aus.seminario2.simple2dgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private CharacterSprite characterSprite;//, player2;

    public GameView(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        characterSprite = new CharacterSprite(
                BitmapFactory.decodeResource(getResources(), R.drawable.android));

        //player2 = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.android));
        //player2.setX(100);
        //player2.setY(180);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry){
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        characterSprite.update();
        //player2.update();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        if (canvas != null){
            characterSprite.draw(canvas);
            //player2.draw(canvas);
        }
    }
}
