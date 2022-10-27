package kayttoliittymat.laivanupotus.playfield;

import javafx.scene.control.Button;

/**
 * Peliruutua mallintava perusluokka, joka perii JavaFX:n Buttonin. Toiminta
 * osana käyttöliittymää perustuu siis Buttonin perusominaisuuksiin.
 * 
 *
 */
public class GameSquare extends Button {

	private boolean shipPresent = false; // onko tässä ruudussa alus
	private boolean deployPermitted = true; // onko tähän sallittua asettaa alus "yksi ruutu välimatkaa" -säännön
											// perusteella
	private boolean dragOverShipDeployPermitted = false; // oletusarvo. Tarkistetaan checkDeployPermitted-metodissa, voiko juuri
															// kyseisen alustyypin sijoittaa tähän ruutuun (kokoehtojen
															// tarkistus)
	
	private boolean isHit = false;  // onko ruudussa oleva alus saanut osuman
	private boolean isFired = false;  // onko ruutua ammuttu (sis. alus tai ei alusta)

	public void setDeployPermitted(boolean permit) {
		this.deployPermitted = permit;
	}

	public boolean getDeployPermitted() {
		return this.deployPermitted;
	}

	public void setShipPresent(boolean present) {
		// System.out.println("DEBUG: GS setShipPresent, present value: " + present);
		
		if(present == true) {
			this.setStyle("-fx-color: blue");
		} else {
			this.setStyle("");
		}
		
		this.shipPresent = present;
		
	}

	public boolean getShipPresent() {
		return this.shipPresent;
	}

	public boolean getDragOverShipDeployPermitted() {
		return dragOverShipDeployPermitted;
	}

	public void setDragOverShipDeployPermitted(boolean dragOverShipDeployPermitted) {
		this.dragOverShipDeployPermitted = dragOverShipDeployPermitted;
	}

	public boolean getIsHit() {
		return isHit;
	}

	public void setIsHit(boolean isHit) {
		this.isHit = isHit;
	}

	public boolean getIsFired() {
		return isFired;
	}

	public void setIsFired(boolean isFired) {
		this.isFired = isFired;
	}

}
