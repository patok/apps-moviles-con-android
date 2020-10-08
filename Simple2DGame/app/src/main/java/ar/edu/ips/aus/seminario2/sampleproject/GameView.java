package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.abemart.wroup.client.WroupClient;
import com.abemart.wroup.common.messages.MessageWrapper;
import com.abemart.wroup.service.WroupService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GameView.class.getSimpleName();
    public static final String STATUS_PAUSED = "paused";
    public static final String STATUS_UPDATING = "updating";
    private boolean updating = false;
    private GameAnimationThread thread;
    private Player player;
    private Map<String, Player> players = new HashMap<>();
    private PlayerSprite playerSprites;
    private int moves = 0;
    private static final int SERVER_UPDATE_RATIO = 3;
    private static final int CLIENT_UPDATE_RATIO = 2;
    private MazeBoardActivity activity = (MazeBoardActivity)this.getContext();

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context){
        super(context);
        init();
    }

    private void init() {
        getHolder().addCallback(this);

        playerSprites = new PlayerSprite(getResources());

        if (GameApp.getInstance().isGameServer()) {
            String id = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            player = new Player(id, 0.5, 0.5);
            player.setOrder(PlayerSprite.getRandomSpriteNumber());
            players.put(id, player);
        }
        thread = new GameAnimationThread(getHolder(), this);
        setFocusable(true);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
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
                Log.d(TAG, "Error " + e.getMessage());
            }
            retry = false;
        }
    }

    // update game world
    public void update(long delta) {
        if (this.updating) {
            MazeBoard board = GameApp.getInstance().getMazeBoard();
            // update only actual player
            if (board != null) {
                player.move(board, delta);
                this.moves++;

                // if we are server send all players data
                if (GameApp.getInstance().isGameServer()) {
                    if (this.moves % SERVER_UPDATE_RATIO == 0) {
                        WroupService server = GameApp.getInstance().getServer();
                        MessageWrapper message = new MessageWrapper();
                        Gson json = new Gson();
                        Message<Player[]> data = new Message<Player[]>(Message.MessageType.PLAYER_DATA,
                                players.values().toArray(new Player[]{}));
                        String msg = json.toJson(data);
                        message.setMessage(msg);
                        message.setMessageType(MessageWrapper.MessageType.NORMAL);
                        server.sendMessageToAllClients(message);
                    }
                } else {
                    // if we are client send player data
                    if (this.moves % CLIENT_UPDATE_RATIO == 0) {
                        WroupClient client = GameApp.getInstance().getClient();
                        MessageWrapper message = new MessageWrapper();
                        Gson json = new Gson();
                        Message<Player[]> data = new Message<Player[]>(Message.MessageType.PLAYER_DATA,
                                new Player[]{player});
                        String msg = json.toJson(data);
                        message.setMessage(msg);
                        message.setMessageType(MessageWrapper.MessageType.NORMAL);
                        client.sendMessageToServer(message);
                    }
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if (canvas != null) {
            MazeBoard board = GameApp.getInstance().getMazeBoard();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if (board != null) {
                for (Player p : this.players.values()) {
                    Rect srcRect = playerSprites.getSourceRectangle(this, board, p, p.getOrder());
                    Rect dstRect = playerSprites.getDestinationRectangle(this, board, p);
                    canvas.drawBitmap(playerSprites.getSprites(), srcRect, dstRect, null);
                }
            }
        }
    }

    public void updatePlayerData(String message) {
        Gson gson = new Gson();
        Message<Player[]> playerData = gson.fromJson(message,
                new TypeToken<Message<Player[]>>(){}.getType());
        for (Player pd:playerData.getPayload()) {
            if (player == null){
                String id = Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                player = new Player(id, pd.getX(), pd.getY());
                player.setOrder(pd.getOrder());
                players.put(pd.getID(), player);
            } else
            if (!player.getID().equals(pd.getID())) {
                Player p = players.get(pd.getID());
                if (p == null) {
                    p = new Player(pd.getID(), pd.getX(), pd.getY());
                    p.setOrder(pd.getOrder());
                    players.put(pd.getID(), p);
                }
                p.setX(pd.getX());
                p.setY(pd.getY());
                p.setXVel(pd.getXVel());
                p.setYVel(pd.getYVel());
            }
        }
    }

    public void updateStatus(String message) {
        Gson gson = new Gson();
        Message<String> gameStatus = gson.fromJson(message,
                new TypeToken<Message<String>>(){}.getType());
        if (gameStatus.getType() == Message.MessageType.GAME_STATUS){
            String data = gameStatus.getPayload();
            switch (data){
                case STATUS_PAUSED:
                    this.updating = false;
                    Log.d(TAG, "Game paused.");
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "GAME PAUSED by SERVER", Toast.LENGTH_LONG).show();
                        }
                    });
                    activity.soundPool.play(activity.peepSound, MazeBoardActivity.FX_VOLUME, MazeBoardActivity.FX_VOLUME, 0, 0, 1);
                    break;
                case STATUS_UPDATING:
                    this.updating = true;
                    Log.d(TAG, "Game updating.");
                    this.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "GAME RESUMED by SERVER", Toast.LENGTH_LONG).show();
                        }
                    });
                    activity.soundPool.play(activity.beepSound, MazeBoardActivity.FX_VOLUME, MazeBoardActivity.FX_VOLUME, 0, 0, 1);
                    break;
                default:
                    break;
            }
        }
    }

    public void toggleStatus() {
        if (GameApp.getInstance().isGameServer()) {
            String status = null;
            if (this.updating) {
                this.updating = false;
                status = STATUS_PAUSED;
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "GAME PAUSED", Toast.LENGTH_LONG).show();
                    }
                });
                activity.soundPool.play(activity.peepSound, MazeBoardActivity.FX_VOLUME, MazeBoardActivity.FX_VOLUME, 0, 0, 1);
            } else {
                this.updating = true;
                status = STATUS_UPDATING;
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "GAME RESUMED", Toast.LENGTH_LONG).show();
                    }
                });
                activity.soundPool.play(activity.beepSound, MazeBoardActivity.FX_VOLUME, MazeBoardActivity.FX_VOLUME, 0, 0, 1);
            }
            WroupService server = GameApp.getInstance().getServer();
            MessageWrapper message = new MessageWrapper();
            Gson json = new Gson();
            Message<String> data = new Message<String>(Message.MessageType.GAME_STATUS,
                    status);
            String msg = json.toJson(data);
            message.setMessage(msg);
            message.setMessageType(MessageWrapper.MessageType.NORMAL);
            server.sendMessageToAllClients(message);
        }
    }

    public void checkFinished() {
        if (GameApp.getInstance().isGameServer()
                && this.updating ) {
            Vector<Player> finished = new Vector<Player>();
            final StringBuffer winnersMessage = new StringBuffer("Congratulate Winners: ");
            for (Player player : players.values()) {
                MazeBoard mazeBoard = GameApp.getInstance().getMazeBoard();
                if (player.getX() <= mazeBoard.getFinishX() + 0.75d
                        && player.getX() >= mazeBoard.getFinishX() + 0.25d
                        && player.getY() >= mazeBoard.getFinishY() + 0.25d
                        && player.getY() <= mazeBoard.getFinishY() + 0.75d) {
                    finished.add(player);
                    winnersMessage.append("\"").append(player.getName()).append("\", ");
                }
            }
            if (!finished.isEmpty()) {
                toggleStatus();
                this.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), winnersMessage, Toast.LENGTH_LONG).show();
                    }
                });

                ScoreDatabase db = ScoreDatabase.getDatabase(activity);
                AsyncTask<Void, Void, Void> updates = new UpdateScoresTask(db, player);
                updates.execute();

                // call new activity
                Intent intent = new Intent(this.getContext(), FinalScoresActivity.class);
                getContext().startActivity(intent);
            }
        }
    }
}

class UpdateScoresTask extends AsyncTask<Void, Void, Void> {

    public static final int WINNER_POINTS = 10;
    ScoreDatabase db;
    Player player;

    public UpdateScoresTask(ScoreDatabase db, Player player) {
        this.db = db;
        this.player = player;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Score score = db.scoreDAO().findScoreById(player.getID());
        if (score == null) {
            score = new Score();
            score.setPlayerId(player.getID());
            score.setPlayerName(player.getName());
            score.setPoints(WINNER_POINTS);
            db.scoreDAO().insert(score);
        } else {
            score.setPlayerName(player.getName());
            score.setPoints(score.getPoints() + WINNER_POINTS);
            db.scoreDAO().update(score);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
