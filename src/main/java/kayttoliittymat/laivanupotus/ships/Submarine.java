package kayttoliittymat.laivanupotus.ships;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;

/**
 * Sukellusvenettä mallintava luokka.
 *
 */
public class Submarine extends Ship {

	private static int shipID = 1;
	
	public Submarine() {
		this.type = ShipType.SUBMARINE;
		this.shipLength = 3;
		this.uniqueID = shipID;
		shipID++;
	}
	
	public Submarine(int x, int y, ArrayList<GameSquare> shipBody, Player playerX){
		this.type = ShipType.SUBMARINE;
		this.shipLength = 3;
		this.uniqueID = shipID;
		shipID++;
		this.x = x;
		this.y = y;
		this.shipBody = shipBody;
		this.player = playerX;
	}
}
