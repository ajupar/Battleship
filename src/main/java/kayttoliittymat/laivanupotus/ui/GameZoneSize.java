package kayttoliittymat.laivanupotus.ui;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * ComboBoxin sisältö eli peliruudukon koon valinta
 *
 */
public enum GameZoneSize {size5 ("5 x 5", 5, 25), size6 ("6 x 6", 6, 36), size7 ("7 x 7", 7, 49),
	size8 ("8 x 8", 8, 64), size9("9 x 9", 9, 81), size10("10 x 10", 10, 100), size11("11 x 11", 11, 121), size12("12 x 12", 12, 144);
	
	private String text;
	private int dimension;
	private int area;
	
	GameZoneSize(String text, int dim, int area) {
		this.text = text;
		this.dimension = dim;
		this.area = area;
		
	}
	
	public String toString() {
		return text;
	}
	
	// jokaisen pelikenttäkoon pinta-ala, jonka avulla
	// voidaan kontrollerissa valvoa alusmääräehdon täyttymistä
	public int getArea() {
		return this.area;
	}
	
	public int getDimension() {
		
		return this.dimension;
		
		
	}
}


