package kayttoliittymat.laivanupotus.ships;

import kayttoliittymat.laivanupotus.game.Game.ShipType;
import kayttoliittymat.laivanupotus.playfield.GameSquare;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.game.Player;

/**
 * Laivojen abstrakti yläluokka
 * 
 *
 */
public abstract class Ship {

	ShipType type;
	int shipLength; // Laivan pituus ruudukkoina.
	int uniqueID;  // yksilöllinen ID-numero

	int x; // Sijoituksen x-koodinaatti
	int y; // Sijoituksen y-koodinaatti

	Player player;

	ArrayList<GameSquare> shipBody;  // peliruudut, joihin tämä laiva on sijoitettu
	private boolean isSunk;  // onko upotettu

	public ShipType getShipType() {
		return this.type;
	}

	public ArrayList<GameSquare> getShipBody() {
		return this.shipBody;
	}

	@Override
	public String toString() {
		return String.format(player.getPlayerNumber() + " " + type + " ID:" + uniqueID + " LENGTH:" + shipLength + " X:"
				+ x + " Y:" + y);
	}

	public boolean isSunk() {

		this.isSunk = true;

		for (GameSquare shipPart : shipBody) {
			if (shipPart.getIsHit() == false) {
				this.isSunk = false;
			}
		}

		return isSunk;
	}

	public void setSunk(boolean isSunk) {
		this.isSunk = isSunk;
	}
}
