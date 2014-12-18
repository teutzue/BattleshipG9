/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g9;

import battleship.interfaces.BattleshipsPlayer;
import tournament.player.PlayerFactory;

/**
 *
 * @author Cookie
 */
public class G9 {

    public static PlayerFactory<BattleshipsPlayer> getPlayerFactory() {
        return new G9Factory();
    }

}
