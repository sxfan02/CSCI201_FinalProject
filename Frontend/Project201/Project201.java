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
	String usernameString, passwordString, winnerString;
	int windowX = 800, windowY = 780;
	HBox h1, h2, h3, h4, h5, h6;
	GridPane gp;
	VBox v1, v2, v3;
	String rUser = "", rPass ="", confirmPW = "", playerBanner = "";
	ArrayList<String> deck = new ArrayList<String>();
	ArrayList<Card> p1Tiles = new ArrayList<Card>();
	ArrayList<Card> p2Tiles = new ArrayList<Card>();
	String dir = "src/Project201Images/";
	Card pos00;
	boolean boolBuildTheBoard = true;
	//Banners: U:Unknown P:Pink B:Blue G:Green R:Red
	//Classes N:Necromancer K:Knight B:Barbarian R:Royalty M:Magician 
	//Levels, 1-9 0 = 10 
	//52 Cards in a deck
	List<String> new_deck = Arrays.asList(
		 "3BK", "4BK", "5BK", "6BK", "7BK", "8BK", "9BK", "0BK",
		 "3RK", "4RK", "5RK", "6RK", "7RK", "8RK", "9RK", "0RK",
		 "3PK", "4PK", "5PK", "6PK", "7PK", "8PK", "9PK", "0PK",
		 "3GK", "4GK", "5GK", "6GK", "7GK", "8GK", "9GK", "0GK",
		 "2UN1", "2UN2", "2UN3", "2UN4",
		 "6UB1", "6UB2", "6UB3", "6UB4",
		 "6UM1", "6UM2", "6UM3", "6UM4",
		 "6UR1", "6UR2", "6UR3", "6UR4",
		 "6UK1", "6UK2", "6UK3", "6UK4");

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

		v1 = new VBox(5); //left side of game
		v2 = new VBox(5); //right side of game
		v3 = new VBox(15); //Side panel that contains deck, discard, score, and output box
		h1 = new HBox(5); //Read box
		h2 = new HBox(5); //typing box
		h3 = new HBox(25); //Deck
		h4 = new HBox(25); //Discard
		h5 = new HBox(25); //Score
		h6 = new HBox(25); //Output
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
			scene.setRoot(hcPane);
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
		setLabelGraphic(imgLabel, "src/Project201Images/banner.png");
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
		 *********************CREATING A NEW USER*************************************
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
		register.setOnAction(e -> 
		{
			if (createPasswordTF.getText().equals(confirmPasswordTF.getText()))
			{

					createMessage.setText("Success! User created.");
					System.out.println("The passwords match.");
					
					/*
					 * TODO BackEnd - You're inside the section to register a new user. The
					 * variables you need to register a new user here are:
					 * 
					 * rUser
					 * rPass
					 * 
					 * 
					 */
					System.out.println("rUser " + rUser + " rPass " + rPass);
			} 
			else {
				createMessage.setText("The passwords do not match.");
			}
		});

		Label imgLabel2 = new Label();
		setLabelGraphic(imgLabel2, "src/Project201Images/tree.png");
		pictureh1.getChildren().add(imgLabel2);
		pictureh1.setAlignment(Pos.CENTER);

		createNewUserPane.getChildren().add(createMessage);
		createNewUserPane.getChildren().add(userh1);
		createNewUserPane.getChildren().add(passh1);
		createNewUserPane.getChildren().add(confh1);
		createNewUserPane.getChildren().add(pictureh1);
		
		/***********************************************
		 *             End creating new user 
		 ************************************************
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
//		readChatBox.setMinHeight(300);
		writeTextBox.setContent(readChatBox);
		toBeSent = "";
		
		Label gameLogo = new Label();
		setLabelGraphic(gameLogo, "src/Project201Images/bannermen.png");
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
		pos00 = new Card(gp, 0,0); 
		pos00.setOnAction((ActionEvent event) -> 
		{
			chooseBanner();
		});
		
		for(int i=0; i<5; i++)
		{
				Card rowOne = new Card(gp, i, 1);
				rowOne.setOnAction((ActionEvent event) -> 
				{
					System.out.println("Card name:" + rowOne.card_name);
				});
				p1Tiles.add(rowOne);
		}
		for(int i=0; i<5; i++)
		{
				Card rowTwo = new Card(gp, i, 2);
				rowTwo.setOnAction((ActionEvent event) -> 
				{
					System.out.println("Card name:" + rowTwo.card_name);
				});
				p1Tiles.add(rowTwo);
		}
		for(int i=0; i<5; i++)
		{
				Card rowThree = new Card(gp, i, 3);
				rowThree.setOnAction((ActionEvent event) -> 
				{
					System.out.println("Card name:" + rowThree.card_name);
				});
				p2Tiles.add(rowThree);
		}
		for(int i=0; i<5; i++)
		{
				Card rowFour = new Card(gp, i, 4);
				rowFour.setOnAction((ActionEvent event) -> 
				{
					System.out.println("Card name:" + rowFour.card_name);
				});
				p2Tiles.add(rowFour);
		}
		
		Card pos05 = new Card(gp, 0,5); 
		pos05.setOnAction((ActionEvent event) -> 
		{

		});
	
		//Deck Tile
		Label deckLabel = new Label("Deck:");
		deckLabel.setMinWidth(50);
		Card deckTile = new Card();
		deckTile.setGraphic("src/Project201Images/back.png");
		deckTile.setOnAction((ActionEvent event) -> 
		{
			if(boolBuildTheBoard)
			{
				buildTheBoard();
			}
			else {
				System.out.println("You can't build the board twice");
			}
		});
		h3.getChildren().add(deckLabel);
		h3.getChildren().add(deckTile);
		
		//Discard Tile
		Label discardLabel = new Label("Discard:");
		discardLabel.setMinWidth(50);
		Card discardTile = new Card();
		discardTile.setGraphic("src/Project201Images/back.png");
		h4.getChildren().add(discardLabel);
		h4.getChildren().add(discardTile);
		
		//Scores 
		Label p1ScoreLabel = new Label("P1 Score: ");
		h5.getChildren().add(p1ScoreLabel);
		Label p2ScoreLabel = new Label("P2 Score: ");
		h5.getChildren().add(p2ScoreLabel);
		
		//Output Label 
		Label output = new Label("Sample output text");
		h6.getChildren().add(output);
		
		//Initializations
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
		 * TODO backend 
		 * Here is the place for the winner string
		 * 
		 */
		winnerString = usernameString;
		Leaderboard.updateWinLoss("leaderboard", winnerString, true);
	}
	
	public void chooseBanner() {
		Card green = new Card(gp, 1, 0);
		green.setGraphic("src/Project201Images/Green.png");
		Card red = new Card(gp, 2, 0);
		red.setGraphic("src/Project201Images/Red.png");
		Card pink = new Card(gp, 3, 0);
		pink.setGraphic("src/Project201Images/Pink.png");
		Card blue = new Card(gp, 4, 0);
		blue.setGraphic("src/Project201Images/Blue.png");
		
		green.setOnAction((ActionEvent event) -> 
		{
			System.out.println("Green banner chosen");
			pos00.setGraphic("src/Project201Images/Green.png");
			playerBanner = "G";
			send("!Banner_Chosen G");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			
		});
		
		red.setOnAction((ActionEvent event) -> 
		{
			System.out.println("Red banner chosen");
			pos00.setGraphic("src/Project201Images/Red.png");
			playerBanner = "R";
			send("!Banner_Chosen R");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			
		});
		pink.setOnAction((ActionEvent event) -> 
		{
			System.out.println("Pink banner chosen");
			pos00.setGraphic("src/Project201Images/Pink.png");
			playerBanner = "P";
			send("!Banner_Chosen P");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			
		});
		blue.setOnAction((ActionEvent event) -> 
		{
			System.out.println("Blue banner chosen");
			pos00.setGraphic("src/Project201Images/Blue.png");
			playerBanner = "B";
			send("!Banner_Chosen B");
			gp.getChildren().remove(green);
			gp.getChildren().remove(red);
			gp.getChildren().remove(pink);
			gp.getChildren().remove(blue);
			
		});
	}
	
	public void buildTheBoard()
	{
		Collections.shuffle(deck);
		//Giving the players face down cards value
		for(int i=0; i<p1Tiles.size(); i++)
		{
			String s = deck.get(i);
			p1Tiles.get(i).card_name = s;
			int index = i;
			//p1Tiles.get(i).setGraphic("src/Project201Images/" + s + ".png");
			//Sending the information to the other side
			send("!p1_tiles" + " " + index + " " + s);
			deck.remove(i);
		}
		//Giving the other players face down cards value
		for(int i=0; i<p2Tiles.size(); i++)
		{
			String s = deck.get(i);
			p2Tiles.get(i).card_name = s;
			int index = i;
			//p2Tiles.get(i).setGraphic("src/Project201Images/" + s + ".png");
			//Sending the information to the other side
			send("!p2_tiles" + " " + index + " " + s);
			deck.remove(i);
		}
		boolBuildTheBoard = false;
		send("!Build_the_board false");
	}
	
	public class BuildHost extends Thread {
		@Override
		public void run() {
			try {
				ssocket = new ServerSocket(socketNumber);
				sendAsChat("Connection on socket: " + socketNumber);
				csocket = ssocket.accept();
				sendAsChat("Client connected");
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

		} catch (Exception e) {
			sendAsChat("client setup error e=" + e);
		}
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
					String s = myIn.readLine(); // hangs for input
					StringTokenizer len = new StringTokenizer(s);
					String phrase = len.nextToken();
					if (s.equals("!close_program")) {
						System.exit(0);
					}
					else if(phrase.equals("!p1_tiles"))
					{
						Platform.runLater( ()->{
							String i = len.nextToken();
							int index = Integer.parseInt(i);
							String card = len.nextToken();
							p1Tiles.get(index).setCard_Name(card);
							//p1Tiles.get(index).setGraphic("src/Project201Images/" + card + ".png");
						});
						
					}
					else if(phrase.equals("!p2_tiles"))
					{
						Platform.runLater( ()->{
							String i = len.nextToken();
							int index = Integer.parseInt(i);
							String card = len.nextToken();
							p2Tiles.get(index).setCard_Name(card);
							//p2Tiles.get(index).setGraphic("src/Project201Images/" + card + ".png");
						});
					}
					else if(phrase.equals("!Build_the_board"))
					{
						Platform.runLater( ()->{
							boolBuildTheBoard = false;
						});
					}
					else 
					{
						sendAsChat("you: " + s);
					}
				} catch (Exception h) {
					sendAsChat("couldn't read from the other end");
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
		File file = new File(name);
		Image image = new Image(file.toURI().toString());
		ImageView iv = new ImageView();
//		iv.setFitHeight(100);
//		iv.setFitWidth(85);
		iv.setImage(image);
		label.setGraphic(iv);
	}

	public void setButtonGraphic(Button button, String name) {
		File file = new File(name);
		Image image = new Image(file.toURI().toString());
		ImageView iv = new ImageView();
//		iv.setFitHeight(100);
//		iv.setFitWidth(85);
		iv.setImage(image);
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


/*
 * Finally, this section handles what happens when a registered user logs in
 */
//registeredUserPane.setAlignment(Pos.CENTER);
//registeredUserPane.setStyle("-fx-background-color: #FDFFCB;");
//HBox rwm = new HBox();
//rwm.setAlignment(Pos.CENTER);
//Label regiWelcome = new Label("Welcome to Bannermen.");
//regiWelcome.setFont(font2);
//rwm.getChildren().add(regiWelcome);
//
//HBox rhc = new HBox();
//rhc.setAlignment(Pos.CENTER);
//rhc.getChildren().add(hostButton);
//rhc.getChildren().add(clientButton);
//
//HBox ril = new HBox();
//Label imgLabel3 = new Label();
//setLabelGraphic(imgLabel3, "src/Project201Images/king.png");
//ril.getChildren().add(imgLabel3);
//ril.setAlignment(Pos.CENTER);
//
//registeredUserPane.getChildren().add(rwm);
//registeredUserPane.getChildren().add(rhc);
//registeredUserPane.getChildren().add(ril);
// ----