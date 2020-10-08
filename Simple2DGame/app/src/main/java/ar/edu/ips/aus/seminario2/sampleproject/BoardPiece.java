package ar.edu.ips.aus.seminario2.sampleproject;

import java.util.Vector;

class BoardPiece {

    private final Vector<MazeBoard.Direction> openings = new Vector<MazeBoard.Direction>();

    public BoardPiece(boolean west, boolean north, boolean east, boolean south){
        if (west)
            openings.add(MazeBoard.Direction.WEST);
        if (north)
            openings.add(MazeBoard.Direction.NORTH);
        if (east)
            openings.add(MazeBoard.Direction.EAST);
        if (south)
            openings.add(MazeBoard.Direction.SOUTH);
    }

    public boolean isOpen(MazeBoard.Direction direction){
        return openings.contains(direction);
    }

}
