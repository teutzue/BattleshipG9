/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package g9;

import battleship.interfaces.BattleshipsPlayer;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Board;
import battleship.interfaces.Ship;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Tobias
 */
public class G9AI_Smasher implements BattleshipsPlayer {

    private final static Random rnd = new Random();
    private int sizeX;
    private int sizeY;

    public G9AI_Smasher() {
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        ArrayList<Position> posNewS = new ArrayList<>();
        ArrayList<Position> currentPosition = new ArrayList<>();

        //1) Simply records the size of the board:
        sizeX = board.sizeX();
        sizeY = board.sizeY();

        //2) Gets each ship and places it randomly
        for (int i = 0; i < fleet.getNumberOfShips(); ++i) {
            Ship s = fleet.getShip(i);
            boolean vertical = rnd.nextBoolean();
            Position pos;
            if (vertical) {
                int x = rnd.nextInt(sizeX);
                int y = rnd.nextInt(sizeY - (s.size() - 1));
                pos = new Position(x, y);
                posNewS.add(new Position(x, y));
            } else {
                int x = rnd.nextInt(sizeX - (s.size() - 1));
                int y = rnd.nextInt(sizeY);
                pos = new Position(x, y);
                posNewS.add(new Position(x, y));
            }

            //2.5)  Check for collions and if true, run again:
            for (Position pObj : posNewS) {
                for (Position existingPosition : currentPosition) {
                    if (pObj == existingPosition) {
                        i--;
                    } else {
                        //3) Actual placement on board
                        board.placeShip(pos, s, vertical);
                        currentPosition.add(pos);
                    }
                }
            }

        }
    }

    @Override
    public void incoming(Position pos) {

        //Do nothing
    }

    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
        int x = rnd.nextInt(sizeX);
        int y = rnd.nextInt(sizeY);
        return new Position(x, y);
    }

    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips) {
        //Do nothing
    }

    @Override
    public void startMatch(int rounds) {
        //Do nothing
    }

    @Override
    public void startRound(int round) {
        //Do nothing
    }

    @Override
    public void endRound(int round, int points, int enemyPoints) {
        //Do nothing
    }

    @Override
    public void endMatch(int won, int lost, int draw) {
        //Do nothing
    }
}
