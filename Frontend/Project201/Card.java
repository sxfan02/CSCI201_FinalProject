/*
 * Card class for Bannermen
 */
package Project201;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Card extends Button
{
	String card_name = "";
	String power = "";
	String banner = "";
	String unit_type = "";

	File file;
	
	Card(GridPane root, int x, int y)
	{
		this.setGraphic("src/Project201Images/back.png");
		//this.setMaxSize(150, 50);
		root.add(this, x, y); 
	}
	Card()
	{
		this.setGraphic("src/Project201Images/back.png");
		//this.setMaxSize(150, 50);
	}
	
	/*
	 * Method I wrote to change a card's graphic. Allows for easy manipulation of graphics. 
	 */
	public void setGraphic(String name)
	{
		file = new File(name);
		Image image = new Image(file.toURI().toString());
		ImageView iv = new ImageView();
	    iv.setFitHeight(85);
	    iv.setFitWidth(60);
		iv.setImage(image);
		this.setGraphic(iv);
	}
	/*
	 * Getters and setters
	 */
	public String getCard_Name() {
		return card_name;
	}

	public void setCard_Name(String card_name) {
		this.card_name = card_name;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public String getUnit_type() {
		return unit_type;
	}
	public void setUnit_type(String unit_type) {
		this.unit_type = unit_type;
	}
}