package kayttoliittymat.laivanupotus.game;

import java.util.ArrayList;

import kayttoliittymat.laivanupotus.ships.Battleship;
import kayttoliittymat.laivanupotus.ships.Carrier;
import kayttoliittymat.laivanupotus.ships.Cruiser;
import kayttoliittymat.laivanupotus.ships.Destroyer;
import kayttoliittymat.laivanupotus.ships.Ship;
import kayttoliittymat.laivanupotus.ships.Submarine;

/**
 * Laivuetta mallintava luokka. Pelaajalla on aina oma laivue.
 * Tallentaa tiedon pelaajille kuuluvista laivoista ja siitä, mitkä on upotettu / saanut osuman.
 * 
 */
public class Fleet {

	
	private ArrayList<Ship> allShipsList = new ArrayList<Ship>();
	
	private ArrayList<Carrier> carrierList = new ArrayList<Carrier>();
	private ArrayList<Battleship> battleshipList = new ArrayList<Battleship>();
	private ArrayList<Cruiser> cruiserList = new ArrayList<Cruiser>();
	private ArrayList<Submarine> submarineList = new ArrayList<Submarine>();
	private ArrayList<Destroyer> destroyerList = new ArrayList<Destroyer>();


	public void setCarrierList(ArrayList<Carrier> carrierList) {
		this.carrierList = carrierList;
		allShipsList.addAll(carrierList);
	}

	public void setBattleshipList(ArrayList<Battleship> battleshipList) {
		this.battleshipList = battleshipList;
		allShipsList.addAll(battleshipList);
	}

	public void setCruiserList(ArrayList<Cruiser> cruiserList) {
		this.cruiserList = cruiserList;
		allShipsList.addAll(cruiserList);
	}

	public void setSubmarineList(ArrayList<Submarine> submarineList) {
		this.submarineList = submarineList;
		allShipsList.addAll(submarineList);
	}

	public void setDestroyerList(ArrayList<Destroyer> destroyerList) {
		this.destroyerList = destroyerList;
		allShipsList.addAll(destroyerList);
	}
	
    public ArrayList<Ship> getAllShipsList() {
		return allShipsList;
	}

	public ArrayList<Carrier> getCarrierList() {
		return carrierList;
	}

	public ArrayList<Battleship> getBattleshipList() {
		return battleshipList;
	}

	public ArrayList<Cruiser> getCruiserList() {
		return cruiserList;
	}

	public ArrayList<Submarine> getSubmarineList() {
		return submarineList;
	}

	public ArrayList<Destroyer> getDestroyerList() {
		return destroyerList;
	}

	@Override
    public String toString() { 
        //return String.format(allShipsList.toString());
        return String.format("                    " + carrierList.toString() + "\n" 
        			   	   + "                    " + battleshipList.toString() + "\n"
        			   	   + "                    " + cruiserList.toString() + "\n"
        			   	   + "                    " + submarineList.toString()+ "\n"
        			   	   + "                    " + destroyerList.toString()); 
    }
}
