package ar.edu.ips.aus.seminario2.sampleproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

class PlayerSprite {

    private static final String TAG = PlayerSprite.class.getSimpleName();
    private static final int SPRITE_HEIGHT = 72;
    private static final int SPRITE_WIDTH = 52;
    private static final int[][] SPRITE_ORIGIN = new int[][]{
            {0,SPRITE_WIDTH * 6}, // skeleton
            {0,0},
            {0,SPRITE_WIDTH * 3},
            {0,SPRITE_WIDTH * 9},
            {SPRITE_HEIGHT * 4, 0},
            {SPRITE_HEIGHT * 4, SPRITE_WIDTH * 3},
            {SPRITE_HEIGHT * 4, SPRITE_WIDTH * 6},
            {SPRITE_HEIGHT * 4, SPRITE_WIDTH * 9},
    };

    private final Bitmap sprites;

    public PlayerSprite(Resources resources){
        sprites = BitmapFactory.decodeResource(resources, R.drawable.characters);
        Log.d(TAG, String.format("metadata -bytes: %d - size: %d x %d",
                sprites.getByteCount(), sprites.getWidth(), sprites.getHeight()));
    }

    public Bitmap getSprites() {
        return sprites;
    }

    public Rect getSourceRectangle(GameView view, MazeBoard board, Player player, int spriteNumber){
        float tileWidth = view.getWidth()/board.getHorizontalTileCount();
        float tileHeight = view.getHeight()/board.getVerticalTileCount();

        float x = (float) (player.getX() * tileWidth) - (SPRITE_WIDTH/2);
        float y = (float) (player.getY() * tileHeight) - (SPRITE_HEIGHT/ 2);

        int srcTop = SPRITE_ORIGIN[spriteNumber][0];
        int srcLeft = SPRITE_ORIGIN[spriteNumber][1];
        int srcRight = 0;
        int srcBottom = 0;
        int[] spriteOffset = { srcLeft, srcLeft+SPRITE_WIDTH, srcLeft+2*SPRITE_WIDTH, srcLeft+SPRITE_WIDTH, srcLeft};
        switch (player.getDirection()){
            case WEST:
                srcTop = srcTop + SPRITE_HEIGHT;
                srcLeft = spriteOffset[(int) (x % spriteOffset.length)];
                break;
            case NORTH:
                srcTop = srcTop + SPRITE_HEIGHT * 3;
                srcLeft = spriteOffset[(int) (y % spriteOffset.length)];
                break;
            case EAST:
                srcTop = srcTop + SPRITE_HEIGHT * 2;
                srcLeft = spriteOffset[(int) (x % spriteOffset.length)];
                break;
            case SOUTH:
                srcTop = srcTop;
                srcLeft = spriteOffset[(int) (y % spriteOffset.length)];
                break;
            case NONE:
                // FIXME fill in
                break;

        }
        srcBottom = srcTop + SPRITE_HEIGHT;
        srcRight = srcLeft + SPRITE_WIDTH;
        return new Rect(srcLeft, srcTop, srcRight, srcBottom);
    }

    public Rect getDestinationRectangle(GameView view, MazeBoard board, Player player) {
        float tileWidth = view.getWidth()/board.getHorizontalTileCount();
        float tileHeight = view.getHeight()/board.getVerticalTileCount();

        float x = (float) (player.getX() * tileWidth) - (SPRITE_WIDTH/2);
        float y = (float) (player.getY() * tileHeight) - (SPRITE_HEIGHT/ 2);

        return new Rect((int)x,(int)y,(int)x+SPRITE_WIDTH,(int)y+SPRITE_HEIGHT);
    }

    public static int getRandomSpriteNumber(){
        Random random = new Random();
        return random.nextInt(SPRITE_ORIGIN.length);
    }
}
