package ar.edu.ips.aus.seminario2.sampleproject;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameAnimationThread extends Thread {
    private static final String TAG = GameAnimationThread.class.getSimpleName();
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;
    private static int ANIMATION_DELAY_MS = 10;

    public GameAnimationThread(SurfaceHolder surfaceHolder, GameView gameView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        final int COUNT_INTERVAL = 200;
        long startWhen = System.nanoTime();
        int intervalCount = 0;

        long previousTick = startWhen;
        while (running){
            canvas = null;
            long now = System.nanoTime();
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gameView.update(now-previousTick);
                    this.gameView.checkFinished();
                    this.gameView.draw(canvas);
                }
                previousTick = now;
                Thread.sleep(ANIMATION_DELAY_MS);
                intervalCount++;
                if (COUNT_INTERVAL <= intervalCount){
                    double framesPerSec = 1000000000.0 / ((now - startWhen) / (intervalCount));
                    Log.d(TAG, String.format("FPS: %2.2f", framesPerSec));
                    startWhen = now;
                    intervalCount = 0;
                }
            } catch (Exception e){
                Log.d(TAG, e.getMessage());
            } finally {
                if (canvas != null ){
                    try {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e){
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
        }
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }
}
