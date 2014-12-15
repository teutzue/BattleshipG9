/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g9;

import battleship.interfaces.BattleshipsPlayer;

/**
 *
 * @author Cookie
 */
public class G9Factory implements tournament.player.PlayerFactory< battleship.interfaces.BattleshipsPlayer> {

  

    @Override
    public String getID() {
        return "G9";
    }

    @Override
    public String getName() {
        return "Badassss motha fucka AI";
    }

    @Override
    public BattleshipsPlayer getNewInstance() {
        return new G9AI_Smasher();
    
    
    }
    
}
