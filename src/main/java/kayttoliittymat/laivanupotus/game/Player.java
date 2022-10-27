package kayttoliittymat.laivanupotus.game;

import kayttoliittymat.laivanupotus.game.Game.PlayerNumber;

/**
 * Pelaajaa mallintava luokka
 *
 */
public class Player {

	private PlayerNumber playerNumber;	
	private String playerName;	
	private Fleet playerFleet;
	
	public Player (String name, PlayerNumber nr) {
		
		this.playerName = name;
		this.playerNumber = nr;
	}
	
	public void setFleet(Fleet fleet) {
		
		this.playerFleet = fleet;
	}
	
	public Fleet getFleet() {
		return this.playerFleet;
	}
	
	public String getName() {
		return this.playerName;
	}

	public PlayerNumber getPlayerNumber() {
		return playerNumber;
	}
	

}
