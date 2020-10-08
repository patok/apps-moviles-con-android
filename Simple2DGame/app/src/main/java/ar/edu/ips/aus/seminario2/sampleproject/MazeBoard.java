package ar.edu.ips.aus.seminario2.sampleproject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * A maze 2D rectangular board. Knows the maze layout, dimensions. Can be queried for width, height
 * and piece by positional (0 based index). Can export/import textual representation.
 */
public class MazeBoard {

    public enum Direction {
        NONE,
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private static final String TAG = MazeBoard.class.getSimpleName();

    private int width = 0;
    private int height = 0;
    private BoardPiece[] board = null;
    private int finishX;
    private int finishY;

    public int getVerticalTileCount() {return height;}

    public int getHorizontalTileCount() { return width;}

    public BoardPiece getPiece(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new ArrayIndexOutOfBoundsException("Check your coordinates!");
        }
        return board[x%width+height*y];
    }

    public int getFinishX() {
        return finishX;
    }

    public int getFinishY() {
        return finishY;
    }

    private MazeBoard() {}

    public static MazeBoard from(String repr) {
        if (repr == null || repr.isEmpty())
            throw new IllegalArgumentException("Empty representation.");

        MazeBoard maze = new MazeBoard();

        // test with simple layout
        maze.board = new BoardPiece[81];
        maze.height = 9; maze.width = 9;
        maze.board[0] = new BoardPiece(false,false,false,true);
        maze.board[1] = new BoardPiece(false,false, true, true);
        maze.board[2] = new BoardPiece(true,false,true,false);
        maze.board[3] = new BoardPiece(true,false,true,false);
        maze.board[4] = new BoardPiece(true,true,true,false);
        maze.board[5] = new BoardPiece(true,false,false,true);
        maze.board[6] = new BoardPiece(false,false,true,true);
        maze.board[7] = new BoardPiece(true,false,true,false);
        maze.board[8] = new BoardPiece(true,false,false, true);

        maze.board[9] = new BoardPiece(false,true,true,false);
        maze.board[10] = new BoardPiece(true,true,false,false);
        maze.board[11] = new BoardPiece(false,false, true, false);
        maze.board[12] = new BoardPiece(true,false,true,false);
        maze.board[13] = new BoardPiece(true,false,true,true);
        maze.board[14] = new BoardPiece(true,true,false,true);
        maze.board[15] = new BoardPiece(false,true,false,true);
        maze.board[16] = new BoardPiece(false,false,false,true);
        maze.board[17] = new BoardPiece(false,true,false,true);

        maze.board[18] = new BoardPiece(false,false,true,true);
        maze.board[19] = new BoardPiece(true,false,true,false);
        maze.board[20] = new BoardPiece(true,false, true, false);
        maze.board[21] = new BoardPiece(true,false,true,false);
        maze.board[22] = new BoardPiece(true,true,false,false);
        maze.board[23] = new BoardPiece(false,true,true,false);
        maze.board[24] = new BoardPiece(true,true,false,false);
        maze.board[25] = new BoardPiece(false,true,true,false);
        maze.board[26] = new BoardPiece(true,true,false,true);

        maze.board[27] = new BoardPiece(false,true,true,false);
        maze.board[28] = new BoardPiece(true,false,false,true);
        maze.board[29] = new BoardPiece(false,false,true,true);
        maze.board[30] = new BoardPiece(true,false, true, false);
        maze.board[31] = new BoardPiece(true,false,true,false);
        maze.board[32] = new BoardPiece(true,false,true,false);
        maze.board[33] = new BoardPiece(true,false,false,true);
        maze.board[34] = new BoardPiece(false,false,true,true);
        maze.board[35] = new BoardPiece(true,true,false,false);

        maze.board[36] = new BoardPiece(false,false,true,true);
        maze.board[37] = new BoardPiece(true,true,false,true);
        maze.board[38] = new BoardPiece(false,true,false,true);
        maze.board[39] = new BoardPiece(false,false,true,false);
        maze.board[40] = new BoardPiece(true,false, true, false);
        maze.board[41] = new BoardPiece(true,false,true,true);
        maze.board[42] = new BoardPiece(true,true,false,false);
        maze.board[43] = new BoardPiece(false,true,true,true);
        maze.board[44] = new BoardPiece(true,false,false,true);

        maze.board[45] = new BoardPiece(false,true,false,true);
        maze.board[46] = new BoardPiece(false,true,false,false);
        maze.board[47] = new BoardPiece(false,true,true,false);
        maze.board[48] = new BoardPiece(true,false,true,false);
        maze.board[49] = new BoardPiece(true,false,false,true);
        maze.board[50] = new BoardPiece(false,true, true, false);
        maze.board[51] = new BoardPiece(true,false,true,false);
        maze.board[52] = new BoardPiece(true,true,false,false);
        maze.board[53] = new BoardPiece(false,true,false,true);

        maze.board[54] = new BoardPiece(false,true,false,true);
        maze.board[55] = new BoardPiece(false,false,true,true);
        maze.board[56] = new BoardPiece(true,false,true,false);
        maze.board[57] = new BoardPiece(true,false,false,true);
        maze.board[58] = new BoardPiece(false,true,true,false);
        maze.board[59] = new BoardPiece(true,false,true,false);
        maze.board[60] = new BoardPiece(true,false, false, true);
        maze.board[61] = new BoardPiece(false,false,true,true);
        maze.board[62] = new BoardPiece(true,true,false,false);

        maze.board[63] = new BoardPiece(false,true,false,true);
        maze.board[64] = new BoardPiece(false,true,true,false);
        maze.board[65] = new BoardPiece(true,false,false,true);
        maze.board[66] = new BoardPiece(false,true,true,false);
        maze.board[67] = new BoardPiece(true,false,true,false);
        maze.board[68] = new BoardPiece(true,false,false,true);
        maze.board[69] = new BoardPiece(false,true,false,true);
        maze.board[70] = new BoardPiece(false,true, true, false);
        maze.board[71] = new BoardPiece(true,false,false,true);

        maze.board[72] = new BoardPiece(false,true,true,false);
        maze.board[73] = new BoardPiece(true,false,false,false);
        maze.board[74] = new BoardPiece(false,true,true,false);
        maze.board[75] = new BoardPiece(true,false,true,false);
        maze.board[76] = new BoardPiece(true,false,false,true);
        maze.board[77] = new BoardPiece(false,true,true,false);
        maze.board[78] = new BoardPiece(true,true,false,false);
        maze.board[79] = new BoardPiece(false,false,true,false);
        maze.board[80] = new BoardPiece(true,true, false, false);

        maze.finishX = 4;
        maze.finishY = 8;
        return maze;
    }

    public static MazeBoard fromJSON(String jsonString){
        Gson json = new Gson();
        MazeBoard mazeBoard = null;
        try {
            mazeBoard = json.fromJson(jsonString, MazeBoard.class);
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "Error: invalid JSON representation.");
        }
        return mazeBoard;
    }

    public static MazeBoard fromRandom(){
        MazeBoard maze = new MazeBoard();
        maze.height = 9; maze.width = 9;
        maze.board = new BoardPiece[maze.height*maze.width];

        BoardPiece finishPiece = new BoardPiece(false, false, false, false);
        maze.board[maze.finishX%maze.width+maze.height*maze.finishY] = finishPiece;
        int remaining = maze.height*maze.width-1;

        while(remaining > 0){
            int nrOfPieces = 0;



            remaining -= nrOfPieces;
        }

        return null;
    }

    public String toString() {return null;}

    public String toJSON() {
        Gson json = new Gson();
        return json.toJson(this);
    }
}
