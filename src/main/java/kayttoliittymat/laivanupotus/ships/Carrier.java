package kayttoliittymat.laivanupotus.ships;

import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.game.Player;

/**
 * Carrier-laivaa mallintava luokka.
 *
 */
public class Carrier extends Ship {
	
	private static int shipID = 1;
	
	public Carrier() {
		this.type = ShipType.CARRIER;
		this.shipLength = 5;
		this.uniqueID = shipID;
		shipID++;
	}
	
	public Carrier(int x, int y, ArrayList<GameSquare> shipBody, Player playerX){
		this.type = ShipType.CARRIER;
		this.shipLength = 5;
		this.uniqueID = shipID;
		shipID++;
		this.x = x;
		this.y = y;
		this.shipBody = shipBody;
		this.player = playerX;
	}
}
