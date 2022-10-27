package kayttoliittymat.laivanupotus.ships;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;

/**
 * Cruiser-laivaa mallintava luokka.
 *
 */
public class Cruiser extends Ship {
	
	private static int shipID = 1;
		
	public Cruiser() {
		this.type = ShipType.CRUISER;
		this.shipLength = 3;
		this.uniqueID = shipID;
		shipID++;
	}
	
	public Cruiser(int x, int y, ArrayList<GameSquare> shipBody, Player playerX){
		this.type = ShipType.CRUISER;
		this.shipLength = 3;
		this.uniqueID = shipID;
		shipID++;
		this.x = x;
		this.y = y;
		this.shipBody = shipBody;
		this.player = playerX;
	}
}
