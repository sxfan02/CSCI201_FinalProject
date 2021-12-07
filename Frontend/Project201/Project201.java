package Project201;

/*
 * CSCI201 Group Project
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Project201 extends Application {
	HBox gamePane; // the whole window
	Scene scene;
	Listener listen;
	ServerSocket ssocket;
	Socket csocket;
	String ip_address;
	int socketNumber = 3456;
	BufferedReader myIn = null; // Read to socket
	PrintWriter myOut = null; // Write to socket
	ScrollPane writeTextBox;
	TextArea readChatBox;
	String toBeSent;
	TextField talker; // where user types a new thing to say
	Label selected;
	int score;
	int p1LaneWins = 0;
	int p2LaneWins = 0;
	String usernameString = "guest", passwordString, winnerString, loserString, player1, player2;
	int windowX = 800, windowY = 780;
	HBox h1, h2, h3, h4, h5, h6;
	GridPane gp;
	VBox v1, v2, v3;
	String rUser = "", rPass = "", confirmPW = "", p1Banner = "", p2Banner = "";
	ArrayList<String> deck = new ArrayList<String>();
	ArrayList<Card> p1Tiles = new ArrayList<Card>();
	ArrayList<Card> p2Tiles = new ArrayList<Card>();
	String discardName = "empty";
	Card pos00, pos05, discardTile, deckTile;
	int counterp1 = 0, counterp2 = 0;
	boolean boolBuildTheBoard = true, p1Turn = false, p2Turn = false, p1FirstTurn = true, p2FirstTurn = true, host = false;
	// Banners: U:Unknown P:Pink B:Blue G:Green R:Red
	// Classes N:Necromancer K:Knight B:Barbarian R:Royalty M:Magician
	// Levels, 1-9 0 = 10
	// 52 Cards in a deck
	List<String> new_deck = Arrays.asList("3BK", "4BK", "5BK", "6BK", "7BK", "8BK", "9BK", "0BK", "3RK", "4RK", "5RK",
			"6RK", "7RK", "8RK", "9RK", "0RK", "3PK", "4PK", "5PK", "6PK", "7PK", "8PK", "9PK", "0PK", "3GK", "4GK",
			"5GK", "6GK", "7GK", "8GK", "9GK", "0GK", "2UN1", "2UN2", "2UN3", "2UN4", "6UB1", "6UB2", "6UB3", "6UB4",
			"6UM1", "6UM2", "6UM3", "6UM4", "6UR1", "6UR2", "6UR3", "6UR4", "6UK1", "6UK2", "6UK3", "6UK4");

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		gamePane = new HBox();
		/*
		 * This section is for prompting the user to log in,
		 */
		VBox loginPane = new VBox(15);
		VBox introRoot = new VBox();
		HBox passwordPane = new HBox();
		HBox usernamePane = new HBox();
		HBox guestOrNewUser = new HBox(5);
		VBox hcPane = new VBox(10);
		VBox createNewUserPane = new VBox(10);
		VBox registeredUserPane = new VBox(2);
		gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setTranslateX(40);

		v1 = new VBox(5); // left side of game
		v2 = new VBox(5); // right side of game
		v3 = new VBox(15); // Side panel that contains deck, discard, score, and output box
		h1 = new HBox(5); // Read box
		h2 = new HBox(5); // typing box
		h3 = new HBox(25); // Deck
		h4 = new HBox(25); // Discard
		h5 = new HBox(25); // Score
		h6 = new HBox(25); // Output
		scene = new Scene(introRoot, 350, 300);
		stage.setTitle("Project 201");
		stage.setScene(scene);
		stage.show();

		/*****************************************************************************
		 * *********************** splash screen *************************************
		 * ***************************************************************************
		 */
		Label bannermen = new Label("       Bannermen");
		introRoot.getChildren().add(bannermen);
		bannermen.setMinWidth(50);
		bannermen.setMinHeight(50);
		Font font = Font.font("Vladimir Script", FontWeight.BOLD, FontPosture.ITALIC, 45);
		bannermen.setFont(font);
		introRoot.setStyle("-fx-background-color: #ECFFFB;");
		introRoot.getChildren().add(loginPane);

		Label usernameLabel = new Label(" Username: ");
		usernameLabel.setMinWidth(100);
		TextField usernameTF = new TextField();
		loginPane.getChildren().add(usernamePane);
		usernamePane.getChildren().add(usernameLabel);
		usernamePane.getChildren().add(usernameTF);

		Label passwordLabel = new Label(" Password: ");
		passwordLabel.setMinWidth(100);
		TextField passwordTF = new TextField();
		loginPane.getChildren().add(passwordPane);
		passwordPane.getChildren().add(passwordLabel);
		passwordPane.getChildren().add(passwordTF);

		Button enterButton = new Button("Enter");
		passwordPane.getChildren().add(enterButton);
		enterButton.setOnAction(e -> {
			/*
			 * TODO BackEnd - This is where a returning user would log in The variables you
			 * need to log in a user are: usernameString passwordString
			 * 
			 * Is there a way we can verify a user already exists in the database?
			 */
			usernameString = usernameTF.getText();
			passwordString = passwordTF.getText();
			if(!Login.onLogin("users", usernameString, passwordString)) {
				usernameString = usernameTF.getText();
				passwordString = passwordTF.getText();
			}
			else {
				scene.setRoot(hcPane);
			}
		});

		Button guestButton = new Button("Guest");
		guestOrNewUser.getChildren().add(guestButton);
		guestButton.setOnAction(e -> {
			scene.setRoot(hcPane);
		});

		Button createNewUser = new Button("Create New User");
		guestOrNewUser.getChildren().add(createNewUser);
		createNewUser.setOnAction(e -> {
			scene.setRoot(createNewUserPane);
		});

		guestOrNewUser.setAlignment(Pos.CENTER);
		loginPane.getChildren().add(guestOrNewUser);
		Button hostButton = new Button("Host"); // Host Button
		// hostOrClient.getChildren().add(hostButton);
		hostButton.setOnAction(e -> {
			scene.setRoot(gamePane);
			stage.setWidth(windowX);
			stage.setHeight(windowY);
			startTHISend();
			new BuildHost().start();
		});

		Button clientButton = new Button("Client");
		// hostOrClient.getChildren().add(clientButton);
		clientButton.setOnAction(e -> {
			scene.setRoot(gamePane);
			stage.setWidth(windowX);
			stage.setHeight(windowY);
			Label ipLabel = new Label("IP#?");
			h2.getChildren().add(ipLabel);

			TextField iptf = new TextField("localhost");
			h2.getChildren().add(iptf);
			iptf.setOnAction(f -> {
				ip_address = iptf.getText();
				buildClient();
			});
		});

		/*
		 * This section handles the logic for host or client
		 */
		Label imgLabel = new Label();
		setLabelGraphic(imgLabel, "banner.png");
		Label welcomeLabel = new Label("Welcome, please select host or client.");
		Font font2 = Font.font("Tahoma", FontWeight.BOLD, FontPosture.ITALIC, 12);
		welcomeLabel.setFont(font2);
		hcPane.getChildren().add(imgLabel);
		hcPane.getChildren().add(welcomeLabel);
		HBox holdsHostClient = new HBox(5);
		hcPane.setStyle("-fx-background-color: #C5FFD6;");
		hcPane.setAlignment(Pos.CENTER);
		holdsHostClient.setAlignment(Pos.CENTER);
		holdsHostClient.getChildren().add(hostButton);
		holdsHostClient.getChildren().add(clientButton);
		hcPane.getChildren().add(holdsHostClient);

		/****************************************************************************
		 ********************* CREATING A NEW USER*************************************
		 *****************************************************************************
		 * This section handles the logic for creating a new user The main pain used
		 * here is createNewUserPane
		 */
		createNewUserPane.setStyle("-fx-background-color: #FFD1D1;");
		Label createMessage = new Label("Please create a username/password");
		createMessage.setFont(font2);
		HBox userh1 = new HBox(5); // Username:
		HBox passh1 = new HBox(5); // pass
		HBox confh1 = new HBox(5); // Confirm password
		HBox pictureh1 = new HBox(5);

		// Username
		Label createUser = new Label("  Username:");
		createUser.setMinWidth(100);
		TextField createUsernameTF = new TextField();

		userh1.getChildren().add(createUser);
		userh1.getChildren().add(createUsernameTF);

		// password
		Label createPass = new Label("  Password:");
		createPass.setMinWidth(100);
		TextField createPasswordTF = new TextField();
		passh1.getChildren().add(createPass);
		passh1.getChildren().add(createPasswordTF);

		// Confirm Pass
		Label confirmPass = new Label(" Confirm Password:");
		confirmPass.setMinWidth(100);
		TextField confirmPasswordTF = new TextField();

		confh1.getChildren().add(confirmPass);
		confh1.getChildren().add(confirmPasswordTF);
		Button register = new Button("Register");
		confh1.getChildren().add(register);
		register.setOnAction(e -> {
			if (createPasswordTF.getText().equals(confirmPasswordTF.getText())) {
				rUser = createUsernameTF.getText();
				rPass = createPasswordTF.getText();
				confirmPW = confirmPasswordTF.getText();
				
				createMessage.setText("Success! User created.");
				System.out.println("The passwords match.");

				/*
				 * TODO BackEnd - You're inside the section to register a new user. The
				 * variables you need to register a new user here are:
				 * 
				 * Username variable (string): rUser 
				 * Password variable (string): rPass
				 * 
				 * 
				 */
				if(Login.checkUserName("users", rUser) == true || rUser.equals("guest")) {
					if(Login.checkUserName("users", rUser) == true)
						System.out.println(rUser + " already exists");
						createMessage.setText(rUser + " already exists");
					if(rUser.equals("guest"))
						System.out.println("You can't be guest.");
						createMessage.setText("You can't be guest.");
					//reateMessage.setText("Username already exists");
				}
				else {
					Login.post("users", rUser, rPass);
					//Leaderboard.post("leaderboard", rUser, "0", "0");
					System.out.println("Account created");
					createMessage.setText("Account created");
				}
			} else {
				createMessage.setText("The passwords do not match.");
			}
		});

		Label imgLabel2 = new Label();
		setLabelGraphic(imgLabel2, "tree.png");
		pictureh1.getChildren().add(imgLabel2);
		pictureh1.setAlignment(Pos.CENTER);

		createNewUserPane.getChildren().add(createMessage);
		createNewUserPane.getChildren().add(userh1);
		createNewUserPane.getChildren().add(passh1);
		createNewUserPane.getChildren().add(confh1);
		createNewUserPane.getChildren().add(pictureh1);

		/***********************************************
		 * End creating new user
		 ***********************************************
		 */

		stage.setOnCloseRequest((WindowEvent g) -> {
			try {
				send("!close_program");
				System.exit(0);
			} catch (Exception e) {
				System.out.println("Could not close.");
			}
		});

		/*
		 * These are the sections the game will be built out of
		 */

		writeTextBox = new ScrollPane();
		writeTextBox.setFitToHeight(true);
		writeTextBox.setFitToWidth(true);
		v2.getChildren().add(writeTextBox);
		readChatBox = new TextArea();
		readChatBox.setMaxWidth(280);
		writeTextBox.setContent(readChatBox);
		toBeSent = "";

		Label gameLogo = new Label();
		setLabelGraphic(gameLogo, "bannermen.png");
		v1.getChildren().add(gameLogo);
		gameLogo.setMinWidth(500);
		v1.setMinWidth(500);
		v1.setStyle("-fx-background-color:#fffacd;");
		v2.setStyle("-fx-background-color:#fffacd;");
		v2.setMinWidth(500);

		/*
		 * This section is for the board, column row formation
		 * 0,0...1,0...2,0...3,0...4,0 Row zero 
		 * 0,1...1,1...2,1...3,1...4,1 Row one
		 * 0,2...1,2...2,2...3,2...4,2 Row two 
		 * 0,3...1,3...2,3...4,3...4,3 Row three
		 * 0,4...1,4...2,4...3,4...4,4 Row four 
		 * 0,5...1,5...2,5...3,5...4,5 Row five
		 */
		pos00 = new Card(gp, 0, 0);
		pos00.setGraphic("bannerspile.png");
		pos00.setOnAction((ActionEvent event) -> {
			chooseBannerP1();
		});

		// Building the grid board
		for (int i = 0; i < 5; i++) {
			Card rowOne = new Card(gp, i, 1);
			p1Tiles.add(rowOne);
		}
		for (int i = 0; i < 5; i++) {
			Card rowTwo = new Card(gp, i, 2);
			p1Tiles.add(rowTwo);
		}
		for (int i = 0; i < 5; i++) {
			Card rowThree = new Card(gp, i, 3);
			p2Tiles.add(rowThree);
		}
		for (int i = 0; i < 5; i++) {
			Card rowFour = new Card(gp, i, 4);
			p2Tiles.add(rowFour);
		}
		/*
		 * Handles what happens when the user clicks a tile that belongs to them
		 */
		for (int i = 0; i < p1Tiles.size(); i++) {
			Card obj = p1Tiles.get(i);
			int index = i;
			obj.setOnAction((ActionEvent event) -> {
				if (p1FirstTurn) {
					if (counterp1 < 3) {
						obj.setGraphic(obj.card_name + ".png");
						obj.setActivated(true);
						send("!Card_flipped" + " " + "p1" + " " + index + " " + obj.card_name);
						counterp1++;
					}
					if (counterp1 == 3) {
						p1FirstTurn = false;
					}
				} 
				else 
				{
					setCard(obj, index, "p1");
				}
				
			});
		}
		for (int i = 0; i < p2Tiles.size(); i++) {
			Card obj = p2Tiles.get(i);
			int index = i;
			obj.setOnAction((ActionEvent event) -> {
				if (p2FirstTurn) {
					if (counterp2 < 3) {
						obj.setGraphic(obj.card_name + ".png");
						obj.setActivated(true);
						send("!Card_flipped" + " " + "p2" + " " + index + " " + obj.card_name);
						counterp2++;
					}
					if (counterp2 == 3) {
						p2FirstTurn = false;
					}
				} else 
				{
					setCard(obj, index, "p2");
				}
			});
		}

		pos05 = new Card(gp, 0, 5);
		pos05.setGraphic("bannerspile.png");
		pos05.setOnAction((ActionEvent event) -> {
			chooseBannerP2();
		});

		// Deck Tile
		Label deckLabel = new Label("Deck:");
		deckLabel.setMinWidth(50);
		deckTile = new Card();
		deckTile.setGraphic("back.png");
		deckTile.setOnAction((ActionEvent event) -> 
		{
			// Builds the board exactly once and stops the other play from pressing it
			if (boolBuildTheBoard) 
			{
				buildTheBoard();
				send("!who_is_player2?");
			} 
			else {
				System.out.println("You can't build the board twice");
			}
			String draw = deck.get(0);
			discardTile.setCard_Name(draw);
			discardTile.setGraphic(draw + ".png");
			send("!Remove" + " " + draw);
			send("!Set_discard" + " " + draw);
			deck.remove(draw);
			
			/*
			 * Test: Ensuring values of cards when they are face down
			 */
			test1();
		});
		h3.getChildren().add(deckLabel);
		h3.getChildren().add(deckTile);

		// Discard Tile
		Label discardLabel = new Label("Discard:");
		discardLabel.setMinWidth(50);
		discardTile = new Card();
		discardTile.setGraphic("back.png");
		discardTile.setOnAction((ActionEvent event) -> {
			discardName = discardTile.getCard_Name();
		});
		h4.getChildren().add(discardLabel);
		h4.getChildren().add(discardTile);

		// Initializations
		v1.getChildren().add(gp);
		v2.getChildren().add(h1);
		v2.getChildren().add(h2);
		v2.getChildren().add(v3);
		v3.getChildren().add(h3);
		v3.getChildren().add(h4);
		v3.getChildren().add(h5);
		v3.getChildren().add(h6);
		gamePane.getChildren().add(v1);
		gamePane.getChildren().add(v2);

		deck.addAll(new_deck);
		Collections.shuffle(deck);
		/*
		 * Disables everything but the deck
		 */
		for (Card obj : p1Tiles) {
			disable(obj);
		}
		for (Card obj : p2Tiles) {
			disable(obj);
		}
		disable(pos00);
		disable(pos05);

//		Scanner input = new Scanner(System.in);
//	    System.out.println("username: ");
//	    usernameString = input.next();
	}

	public void disable(Card obj) {
		obj.setDisable(true);
		obj.setStyle("-fx-opacity: .7");
	}

	public void undisable(Card obj) {
		obj.setDisable(false);
		obj.setStyle("-fx-opacity: 1");
	}

	public void setCard(Card obj, int index, String p) {
		//If they clicked the discard pile first do this 
		if (!discardName.equals("empty")) 
		{
			obj.setCard_Name(discardName);
			obj.setGraphic(obj.card_name + ".png");
			discardName = "empty";
			discardTile.setCard_Name("empty");
			discardTile.setGraphic("back.png");
			send("!Card_flipped" + " " + p + " " + index + " " + obj.card_name);
			send("!Remove_discard");
		}
		else{
			obj.setGraphic(obj.card_name + ".png");
			obj.setActivated(true);
			send("!Card_flipped" + " " + p + " " + index + " " + obj.card_name);
		}
		if(checkWinCondition())
		{
			send("!Game_over");
			System.out.println("Check win condition cleared");
			if(host)
			{
				System.out.println("host cleared");
				outputWinner();	
			}
		}
	}
	
	public boolean checkWinCondition() 
	{
		boolean win = false;
		int activep1 = 0;
		int activep2 = 0;
		for(Card p1t: p1Tiles)
		{
			if(p1t.getActivated() == true)
			{
				activep1++;	
			}
		}
		for(Card p2t: p2Tiles)
		{
			if(p2t.getActivated() == true)
			{
				activep2++;
			}
		}
		if(activep1 == 10)
		{
			if(activep2 == 10)
			{
				win = true;
			}
		}
		
		System.out.println("Player 1 active tiles: " + activep1);
		System.out.println("Player 2 active tiles: " + activep2);
		return win;
	}

	public void chooseBannerP1() {
		Card green = new Card(gp, 1, 0);
		green.setGraphic("Green.png");
		Card red = new Card(gp, 2, 0);
		red.setGraphic("Red.png");
		Card pink = new Card(gp, 3, 0);
		pink.setGraphic("Pink.png");
		Card blue = new Card(gp, 4, 0);
		blue.setGraphic("Blue.png");
		green.setOnAction((ActionEvent event) -> {
			System.out.println("Green banner chosen");
			pos00.setGraphic("Green.png");
			p1Banner = "G";
			send("!Banner_chosen P1 G");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos00.setDisable(true);
			pos00.setStyle("-fx-opacity: .7");
		});
		red.setOnAction((ActionEvent event) -> {
			System.out.println("Red banner chosen");
			pos00.setGraphic("Red.png");
			p1Banner = "R";
			send("!Banner_chosen P1 R");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos00.setDisable(true);
			pos00.setStyle("-fx-opacity: .7");
		});
		pink.setOnAction((ActionEvent event) -> {
			System.out.println("Pink banner chosen");
			pos00.setGraphic("Pink.png");
			p1Banner = "P";
			send("!Banner_chosen P1 P");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos00.setDisable(true);
			pos00.setStyle("-fx-opacity: .7");
		});
		blue.setOnAction((ActionEvent event) -> {
			System.out.println("Blue banner chosen");
			pos00.setGraphic("Blue.png");
			p1Banner = "B";
			send("!Banner_chosen P1 B");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos00.setDisable(true);
			pos00.setStyle("-fx-opacity: .7");
		});
	}

	public void chooseBannerP2() {
		Card green = new Card(gp, 1, 5);
		green.setGraphic("Green.png");
		Card red = new Card(gp, 2, 5);
		red.setGraphic("Red.png");
		Card pink = new Card(gp, 3, 5);
		pink.setGraphic("Pink.png");
		Card blue = new Card(gp, 4, 5);
		blue.setGraphic("Blue.png");

		green.setOnAction((ActionEvent event) -> {
			System.out.println("Green banner chosen");
			pos05.setGraphic("Green.png");
			p2Banner = "G";
			send("!Banner_chosen P2 G");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos05.setDisable(true);
			pos05.setStyle("-fx-opacity: .7");
		});
		red.setOnAction((ActionEvent event) -> {
			System.out.println("Red banner chosen");
			pos05.setGraphic("Red.png");
			p2Banner = "R";
			send("!Banner_chosen P2 R");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos05.setDisable(true);
			pos05.setStyle("-fx-opacity: .7");
		});
		pink.setOnAction((ActionEvent event) -> {
			System.out.println("Pink banner chosen");
			pos05.setGraphic("Pink.png");
			p2Banner = "P";
			send("!Banner_chosen P2 P");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos05.setDisable(true);
			pos05.setStyle("-fx-opacity: .7");
		});
		blue.setOnAction((ActionEvent event) -> {
			System.out.println("Blue banner chosen");
			pos05.setGraphic("Blue.png");
			p2Banner = "B";
			send("!Banner_chosen P2 B");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			pos05.setDisable(true);
			pos05.setStyle("-fx-opacity: .7");
		});
	}
	
	public void findWinner(int index1, int index2)
	{
		///Getting the power values off the cards 
		String[] p1Arr1 = p1Tiles.get(index1).getCard_Name().split("");
		int p1Power1 = Integer.parseInt( p1Arr1[0]);
		if(p1Power1 == 0)
		{
			p1Power1 = 10;
		}
		String[] p1Arr2 = p1Tiles.get(index2).getCard_Name().split("");
		int p1Power2 = Integer.parseInt( p1Arr2[0]);
		if(p1Power2 == 0)
		{
			p1Power2 = 10;
		}
		//Summing the powers into a larger number
		int p1LaneSum = p1Power1 + p1Power2;
		//Getting the banners off the cards to compare them to the players banner 
		String p1CardBanner1 = p1Arr1[1];
		String p1CardBanner2 = p1Arr2[1];
		//If the banners match 
		if(p1CardBanner1.equals(p1CardBanner2))
		{
			//if they match with the players banner 
			if(p1CardBanner1.equals(p1Banner))
			{
				p1LaneSum += 3;
			}
		}
		//Player 2's portion 
		String[] p2Arr1 = p2Tiles.get(index1).getCard_Name().split("");
		int p2Power1 = Integer.parseInt( p2Arr1[0]);
		if(p2Power1 == 0)
		{
			p2Power1 = 10;
		}
		String[] p2Arr2 = p2Tiles.get(index2).getCard_Name().split("");
		int p2Power2 = Integer.parseInt( p2Arr2[0]);
		if(p2Power2 == 0)
		{
			p2Power2 = 10;
		}
		//Summing the powers into a larger number
		int p2LaneSum = p2Power1 + p2Power2;
		//Getting the banners off the cards to compare them to the players banner 
		String p2CardBanner1 = p2Arr1[1];
		String p2CardBanner2 = p2Arr2[1];
		//If the banners match 
		if(p2CardBanner1.equals(p2CardBanner2))
		{
			//if they match with the players banner 
			if(p2CardBanner1.equals(p2Banner))
			{
				p2LaneSum += 3;
			}
		}
		if(p1LaneSum > p2LaneSum)
		{
			p1LaneWins++;
		}
		else if(p1LaneSum < p2LaneSum) 
		{
			p2LaneWins++;
		}
	}
	
	/*
	 * Outputs the winner 
	 */
	public void outputWinner() 
	{
		findWinner(0, 5);
		findWinner(1, 6);
		findWinner(2, 7);
		findWinner(3, 8);
		findWinner(4, 9);
		System.out.println("this is player1:" + player1);
		System.out.println("this is player2:" + player2);
		if(p1LaneWins > p2LaneWins)
		{
			winnerString = player1;
			loserString = player2;
			send("Top wins!");
			send("!winner" + " " + "Top");
			System.out.println("Player 1 wins!: " + winnerString);
			System.out.println("Player 2 loses!: " + loserString);
		}
		else if(p1LaneWins < p2LaneWins) 
		{
			winnerString = player2;
			loserString = player1;
			send("Bottom wins!");
			send("!winner" + " " + "Bottom");
			System.out.println("Player 2 wins!: " + winnerString);
			System.out.println("Player 1 loses!: " + loserString);
		}
		else if(p1LaneWins == p2LaneWins)
		{
			int random = (Math.random() <= 0.5) ? 1 : 2;
			if(random == 1)
			{
				winnerString = player1;
				loserString = player2;
				send("Top wins!");
				send("!winner" + " " + "Top");
				send("Top player wins on tie breaker coin flip!");
				System.out.println("Player 1 wins!: " + winnerString);
				System.out.println("Player 2 loses!: " + loserString);
			}
			else 
			{
				winnerString = player2;
				loserString = player1;
				send("!winner" + " " + "Bottom");
				send("Bottom wins!");
				send("Bottom player wins on tie breaker coin flip!");
				System.out.println("Player 2 wins!: " + winnerString);
				System.out.println("Player 1 loses!: " + loserString);
			}
		}
		if(!usernameString.equals("guest"))
		{
			send("!winner" +" " +  winnerString); //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			send(winnerString + " wins!");
			//If the user is not a guest then we will push the scores here 
			Leaderboard.updateWinLoss("leaderboard", winnerString, true);
			if(Leaderboard.getLosses("leaderboard", loserString) > 0) {
				Leaderboard.updateWinLoss("leaderboard", loserString, false);
			}
		}
		else {
			System.out.println("The user is guest");
		}
		
		for (Card obj : p1Tiles) {
			disable(obj);
		}
		for (Card obj : p2Tiles) {
			disable(obj);
		}
		disable(pos00);
		disable(pos05);
		disable(discardTile);
		disable(deckTile);
	}

	public void buildTheBoard() {
		Collections.shuffle(deck);
		// Giving the players face down cards value
		for (int i = 0; i < p1Tiles.size(); i++) {
			String s = deck.get(i);
			p1Tiles.get(i).card_name = s;
			int index = i;
			// Sending the information to the other side
			send("!p1_tiles" + " " + index + " " + s);
			deck.remove(i);
		}
		// Giving the other players face down cards value
		for (int i = 0; i < p2Tiles.size(); i++) {
			String s = deck.get(i);
			p2Tiles.get(i).card_name = s;
			int index = i;
			// Sending the information to the other side
			send("!p2_tiles" + " " + index + " " + s);
			deck.remove(i);
		}
		boolBuildTheBoard = false;
		send("!Build_the_board false");
		// This disables the other halfs buttons
		for (Card obj : p1Tiles) {
			undisable(obj);
		}
		undisable(pos00);
		for (Card obj : p2Tiles) {
			// Disable the bottom half
			obj.setDisable(true);
			pos05.setDisable(true);
			obj.setStyle("-fx-opacity: .7");
			pos05.setStyle("-fx-opacity: .7");
		}
		send("!Disable");
	}

	public class BuildHost extends Thread {
		@Override
		public void run() {
			try {
				ssocket = new ServerSocket(socketNumber);
				sendAsChat("Connection on socket: " + socketNumber);
				csocket = ssocket.accept();
				sendAsChat("Client connected. You are the host.");
				InputStream in = csocket.getInputStream();
				myIn = new BufferedReader(new InputStreamReader(in));
				String default_a = myIn.readLine();
				sendAsChat("Message:" + default_a);
				myOut = new PrintWriter(csocket.getOutputStream(), true);
				sendAsChat("Greetings!");
				myOut.println("Greetings!");
				myOut.flush();
				listen = new Listener();
				listen.start();
				player1 = usernameString;
				host = true;
				System.out.println("I am the host");
			} catch (Exception e) {
				System.out.println("socket open error e=" + e);
			}
		}
	}

	public void buildClient() {
		sendAsChat("client setup: starting ...");
		try {
			sendAsChat("Transmitting wtih: " + ip_address + "/" + socketNumber);
			csocket = new Socket(ip_address, socketNumber);
			sendAsChat("Client connected.");
			InputStream in = csocket.getInputStream();
			myIn = new BufferedReader(new InputStreamReader(in));
			myOut = new PrintWriter(csocket.getOutputStream(), true);
			myOut.println("Handshake completed.");
			myOut.flush();
			String line;
			line = myIn.readLine();
			sendAsChat(line);
			startTHISend();
			listen = new Listener();
			listen.start();
			player2 = usernameString;
		} catch (Exception e) {
			sendAsChat("client setup error e=" + e);
		}
	}
	
	/*
	 * Testing method to test that face down cards have a value. Returns true if all cases are valid. 
	 */
	public boolean test1() 
	{
		boolean check = true;
		System.out.println("Player 1 card values:");
		int c1 =0, c2=0;
		for(Card obj: p1Tiles)
		{
			System.out.println("Card value at index: " + c1++ +" "+ obj.card_name);
			if(obj.card_name.equals(""))
			{
				check = false;
			}
		}
		System.out.println("Player 2 card values:");
		for(Card obj: p2Tiles)
		{
			System.out.println("Card value at index: " + c2++ +" " + obj.card_name);
			if(obj.card_name.equals(""))
			{
				check = false;
			}
		}
		return check;
	}
	/*
	 * Testing that each card is marked as actively played when it is clicked and that this status is permanent returns true if cards that should be activated are activated.
	 */
	public boolean test2(int x, int y)
	{
		boolean check = true;
		int count_p1 = 0;
		int count_p2 = 0;
		for(Card obj: p1Tiles)
		{
			if(obj.getActivated() == true)
			{
				count_p1++;
			}
		}
		for(Card obj: p2Tiles)
		{
			if(obj.getActivated() == true)
			{
				count_p2++;
			}
		}
		
		if(x != count_p1)
		{
			check = false;
		}
		if(y != count_p1)
		{
			check = false;
		}
		return check;
	}
	/*
	 * Ensures that banner calculations are considered in final damage output. 
	 */
	public void test3() 
	{
		p1Banner = "B";
		p2Banner = "R";
		for(Card obj: p1Tiles)
		{
			obj.setCard_Name("4BK");
			obj.setActivated(true);
		}
		for(Card obj: p2Tiles)
		{
			obj.setCard_Name("4RK");
			obj.setActivated(true);
		}
		outputWinner();
	}
	public void startTHISend() {
		h2.getChildren().clear();
		talker = new TextField();
		h2.getChildren().add(talker);
		talker.setOnAction(g -> {
			String s = talker.getText();
			sendAsChat("me: " + s);
			send(s);
			talker.setText("");
		});
	}

	public class Listener extends Thread { 
		@Override
		public void run() {
			while (true) {
				try {
					String s = myIn.readLine(); 
					StringTokenizer len = new StringTokenizer(s);
					String phrase = len.nextToken();
					if (s.equals("!close_program")) {
						System.exit(0);
					} else if (phrase.equals("!p1_tiles")) {
						Platform.runLater(() -> {
							String i = len.nextToken();
							int index = Integer.parseInt(i);
							String card = len.nextToken();
							p1Tiles.get(index).setCard_Name(card);
						});

					} else if (phrase.equals("!p2_tiles")) {
						Platform.runLater(() -> {
							String i = len.nextToken();
							int index = Integer.parseInt(i);
							String card = len.nextToken();
							p2Tiles.get(index).setCard_Name(card);
						});
						
					} else if (phrase.equals("!Build_the_board")) {
						Platform.runLater(() -> {
							boolBuildTheBoard = false;
						});
					} else if (phrase.equals("!Remove")) {
						Platform.runLater(() -> {
							String word = len.nextToken();
							deck.remove(word);
						});
					} else if (phrase.equals("!Set_discard")) {
						Platform.runLater(() -> {
							String word = len.nextToken();
							discardTile.setCard_Name(word);
							discardTile.setGraphic(word + ".png");
						});
					}
					
					else if (phrase.equals("!Card_flipped")) {
						Platform.runLater(() -> {
							String word2 = len.nextToken(); // "p1"
							String word3 = len.nextToken(); // index
							String word4 = len.nextToken(); // obj.card_name
							int index = Integer.parseInt(word3);
							if (word2.equals("p1")) 
							{
								p1Tiles.get(index).setGraphic(word4 + ".png");
								p1Tiles.get(index).setCard_Name(word4);
								p1Tiles.get(index).setActivated(true);

							} else if (word2.equals("p2")) {
								p2Tiles.get(index).setGraphic(word4 + ".png");
								p2Tiles.get(index).setCard_Name(word4);
								p2Tiles.get(index).setActivated(true);
							}
						});
						
						
						
					} else if (phrase.equals("!Disable")) {
						Platform.runLater(() -> {
							for (Card obj : p1Tiles) 
							{
								obj.setDisable(true);
								pos00.setDisable(true);
								obj.setStyle("-fx-opacity: .7");
								pos00.setStyle("-fx-opacity: .7");
							}
							for (Card obj : p2Tiles) {
								undisable(obj);
							}
							undisable(pos05);
						});
					} else if (phrase.equals("!Banner_chosen")) {
						Platform.runLater(() -> {
							String word2 = len.nextToken(); // "p1"
							String word3 = len.nextToken(); // P
							if (word2.equals("P1")) {
								if (word3.equals("P")) {
									pos00.setGraphic("Pink.png");
									p1Banner = "P";
								}
								if (word3.equals("R")) {
									pos00.setGraphic("Red.png");
									p1Banner = "R";
								}
								if (word3.equals("G")) {
									pos00.setGraphic("Green.png");
									p1Banner = "G";
								}
								if (word3.equals("B")) {
									pos00.setGraphic("Blue.png");
									p2Banner = "B";
								}
							} else if (word2.equals("P2")) {
								if (word3.equals("P")) {
									pos05.setGraphic("Pink.png");
									p2Banner = "P";
								}
								if (word3.equals("R")) {
									pos05.setGraphic("Red.png");
									p2Banner = "R";
								}
								if (word3.equals("G")) {
									pos05.setGraphic("Green.png");
									p2Banner = "G";
								}
								if (word3.equals("B")) {
									pos05.setGraphic("Blue.png");
									p2Banner = "B";
								}
							}
						});
					} 
					else if (phrase.equals("!Remove_discard")) {
						Platform.runLater(() -> {
							discardName = "empty";
							discardTile.setCard_Name("empty");
							discardTile.setGraphic("back.png");
						});
					}
					else if (phrase.equals("!who_is_player2?"))
					{
						Platform.runLater(() -> 
						{
							send("!Player2:" + " " + usernameString);
						});
					}
					else if (phrase.equals("!Player2:"))
					{
						Platform.runLater(() -> 
						{
							//send("!Player2:" + " " + player2);
							String word2 = len.nextToken(); // "p1"
							player2 = word2;
						});
					}
					else if(phrase.equals("!Game_over"))  
					{
						Platform.runLater(() -> 
						{
							if(host)
							{
								outputWinner();	
							}
						});
					}
					else if(phrase.equals("!winner"))  
					{
						Platform.runLater(() -> 
						{
							String word2 = len.nextToken(); 
							send(word2 + " wins!");
							for (Card obj : p1Tiles) {
								disable(obj);
							}
							for (Card obj : p2Tiles) {
								disable(obj);
							}
							disable(pos00);
							disable(pos05);
							disable(discardTile);
							disable(deckTile);
						});
					}
					else {
						sendAsChat("chat: " + s);
					}
				} catch (Exception h) {
					sendAsChat("Error; couldn't recieve message");
				}
			}
		}
	}

// add this string to the conversation.
	public void sendAsChat(String words) {
		toBeSent += words + "\n";
		readChatBox.setText(toBeSent);
		writeTextBox.setVvalue(1.0);
	}

// if the output is established, send s to it.
	public void send(String words) {
		if (myOut != null) {
			myOut.println(words);
		}
	}

	public void setLabelGraphic(Label label, String name) {
	    Image image = ResourceLoader.loadImage(name);
	    ImageView iv = new ImageView(image);
		label.setGraphic(iv);
	}

	public void setButtonGraphic(Button button, String name) {
	    Image image = ResourceLoader.loadImage(name);
	    ImageView iv = new ImageView(image);
		button.setGraphic(iv);
	}

//	This section shows all the fonts available on JavafX
	public void showFonts() {
		List<String> fontFamilies = Font.getFamilies();
		List<String> fontNames = Font.getFontNames();
		for (String item : fontFamilies) {
			System.out.println(item);
		}
		for (String item : fontNames) {
			System.out.println(item);
		}
	}
}