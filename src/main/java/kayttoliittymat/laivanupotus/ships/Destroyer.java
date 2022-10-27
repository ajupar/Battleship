package kayttoliittymat.laivanupotus.ships;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.game.Player;
import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;

/**
 * Destroyer-laivaa mallintava luokka.
 *
 */
public class Destroyer extends Ship {
	
	private static int shipID = 1;

	public Destroyer() {
		this.type = ShipType.DESTROYER;
		this.shipLength = 2;
		this.uniqueID = shipID;
		shipID++;
	}
	
	public Destroyer(int x, int y, ArrayList<GameSquare> shipBody, Player playerX){
		this.type = ShipType.DESTROYER;
		this.shipLength = 2;
		this.uniqueID = shipID;
		shipID++;
		this.x = x;
		this.y = y;
		this.shipBody= shipBody;
		this.player = playerX;
	}
}
