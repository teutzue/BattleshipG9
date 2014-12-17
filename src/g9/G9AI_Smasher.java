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
import java.util.Stack;

/**
 *
 * @author Tobias
 */
public class G9AI_Smasher implements BattleshipsPlayer {

    private ArrayList<Position> currentP = new ArrayList<>();
    private final static Random rnd = new Random();
    private int sizeX;
    private int sizeY;
    private int nextX;
    private int nextY;
    private boolean offset = false;

    private Stack<Position> stack = new Stack<>();

    private ArrayList<Position> previousShots;
    private Position lastShot;
    private boolean hit;
    private int numShips;
    private int previousNumShips;

    public G9AI_Smasher() {

        previousShots = new ArrayList<>();

    }

    private boolean checkCord(Position pos, Ship s, boolean vertical) {

        for (int i = 0; i < s.size(); i++) {

            if (vertical) {
                for (Position posOcupied : currentP) {
                    if (posOcupied.x == pos.x && posOcupied.y == pos.y + i) {
                        return false;
                    }
                }
            } else {// for horizontal
                for (Position posOcupied : currentP) {
                    if (posOcupied.x == pos.x + i && posOcupied.y == pos.y) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void placeShips(Fleet fleet, Board board) {

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
                if (checkCord(pos, s, true)) {
                    board.placeShip(pos, s, vertical);
                    for (int j = 0; j < s.size(); j++) {
                        currentP.add(new Position(pos.x, pos.y + j));
                    }
                } else {
                    i--;
                }

            }
            if (!vertical) {
                int x = rnd.nextInt(sizeX - (s.size() - 1));
                int y = rnd.nextInt(sizeY);
                pos = new Position(x, y);
                if (checkCord(pos, s, false)) {
                    board.placeShip(pos, s, vertical);
                    for (int j = 0; j < s.size(); j++) {
                        currentP.add(new Position(pos.x + j, pos.y));
                    }
                } else {
                    i--;
                }
            }

            //2.5)  Check for collions and if true, run again:
//            for (Position pObj : currentP) {
//
//                for (Position existingPosition : currentPosition) {
//                    if (pObj == existingPosition) {
//                        i--;
//                    } else {
//                        //3) Actual placement on board
//                        board.placeShip(pos, s, vertical);
//                        currentPosition.add(pos);
//                    }
//                }
//            }
        }
    }

    @Override
    public void incoming(Position pos) {

        //Do nothing
    }

    private Position systemShoot() {

        Position shot = new Position(nextX, nextY);
        ++nextX;
        if (nextX >= sizeX) {
            nextX = 0;
            ++nextY;
            if (nextY >= sizeY) {
                nextY = 0;
            }
        }
        return shot;

    }

    private Position hunt() {

        Position shot = new Position(nextX, nextY);
        stack.push(shot);

        nextX -= 2;
        if (nextX < 0) {
            if (!offset) {
                nextX = 8;
                offset = true;
            } else {
                nextX = 9;
                offset = false;
            }
            nextY--;
            if (nextY <= 0) {
                nextY = 9;
            }
        }

        lastShot = shot;
        previousShots.add(shot);
        previousNumShips = numShips;
        return stack.pop();

    }

    private boolean isNotPreviousShots(Position pos) {

        for (Position posit : previousShots) {
            if (pos.x == posit.x && pos.y == posit.y) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPosition(Position pos) {

        if (!(pos.x < 0) && !(pos.x > sizeX-1) && !(pos.y < 0) && !(pos.y > sizeY-1) && isNotPreviousShots(pos)) {

            return true;

        } else {
            return false;
        }

    }

    private Position attack() {

        Position topShot = new Position(lastShot.x, lastShot.y - 1);
        Position rightShot = new Position(lastShot.x + 1, lastShot.y);
        Position downShot = new Position(lastShot.x, lastShot.y + 1);
        Position leftShot = new Position(lastShot.x - 1, lastShot.y);
        
        if (isValidPosition(topShot)) {
            stack.push(topShot);
        }
        if (isValidPosition(rightShot)) {
            stack.push(rightShot);
        }
        if (isValidPosition(downShot)) {
            stack.push(downShot);
        }
        if (isValidPosition(leftShot)) {
            stack.push(leftShot);
        }
        this.lastShot = stack.peek();
        this.previousShots.add(stack.peek());
        previousNumShips = numShips;
        return stack.pop();
        

    }

    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
//        int x = rnd.nextInt(sizeX);
//        int y = rnd.nextInt(sizeY);
//        // Calculate next shot
//        //Safe position of variable lastShot
//        //Create shooting pattern (f.x. increment x by 2 and y by 1)
//        // hit is true use go around pattern
//        
//        
//        lastShot = new Position(x, y);
//        return lastShot;
        if (hit) {
            return attack();
        } else {
            return hunt();
        }
    }

    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips) {
        
        this.numShips = enemyShips.getNumberOfShips();
        if (numShips < previousNumShips) {
            hit = false;
        }else{
            this.hit = hit;
        }

        //if hit is true set global variable lasthit to false (true if hit)
        //Do nothing
    }

    @Override
    public void startMatch(int rounds) {
        //Do nothing
    }

    @Override
    public void startRound(int round) {
        nextX = 9;
        nextY = 9;
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
