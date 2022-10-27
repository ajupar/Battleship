package kayttoliittymat.laivanupotus.ships;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;

/**
 * Battleship-laivaa mallintava luokka.
 *
 */
public class Battleship extends Ship {
	
	private static int shipID = 1;
	
	public Battleship() {
		this.type = ShipType.BATTLESHIP;
		this.shipLength = 4;
		this.uniqueID = shipID;
		shipID++;
	}
	
	public Battleship(int x, int y, ArrayList<GameSquare> shipBody, Player playerX){
		this.type = ShipType.BATTLESHIP;
		this.shipLength = 4;
		this.uniqueID = shipID;
		shipID++;
		this.x = x;
		this.y = y;
		this.shipBody = shipBody;
		this.player = playerX;
	}
}
