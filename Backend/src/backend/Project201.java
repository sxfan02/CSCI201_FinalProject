
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
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.application.Platform;
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
	Ear listen; 
	ServerSocket ssocket; // allows client to connect
	Socket csocket;
	String ip_address; // the IP number of the server
	int socketNumber = 3456; // random number for this app
	BufferedReader myIn = null; // Read to socket
	PrintWriter myOut = null; // Write to socket
	ScrollPane textOutputBox;
	TextArea writeIntoHere; // displays everything that has been said
	String toBeSent; // the raw text of all that has been said
	TextField talker; // where user types a new thing to say
	HBox controlPane;
	Label selected;
	int score;
	String usernameString, passwordString, winnerString;
	int windowX = 1000, windowY = 700;
	HBox h1, h2, h3, h4;
	GridPane gp;
	VBox v1, v2;
	String rUser = "", rPass ="", confirmPW = "";

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
		VBox registeredUserPane = new VBox(10);
		scene = new Scene(introRoot, 350, 300);
		stage.setTitle("Project 201");
		stage.setScene(scene);
		stage.show();

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
			
					if(Login.onLogin("login", usernameString, passwordString)) {
						System.out.println("login sucess");
					}
					else {
						System.out.println("Wrong username/password");
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
			controlPane.getChildren().add(ipLabel);

			TextField iptf = new TextField("localhost");
			controlPane.getChildren().add(iptf);
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

		/*
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
					rUser = createUsernameTF.getText();
					rPass = createPasswordTF.getText();
					confirmPW = confirmPasswordTF.getText();
					/*
					 * TODO BackEnd - You're inside the section to register a new user. The
					 * variables you need to register a new user here are:
					 * 
					 * rUser
					 * rPass
					 * 
					 * 
					 */
					 /*
					* TODO Frontend - If user inputs wrong username/password, prompt them
					* to put in the username/pw again
					*/
					/*
					 * Login.post( tablename, userstring, userpassword)
					 * Backend done, this is posting the user's information to
					 * the database
					 * Also, check if the username is taken
					 * Bizarre bug, rUser is NULL!
					 */
					if(Login.checkUserName("login", rUser) == true) {
						System.out.println(rUser + " already exists, try a new username");
					}
					else {
						Login.post("login", rUser, rPass);
						System.out.println("Account created");
					}
					
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

		/*
		 * Finally, this section handles what happens when a registered user logs in
		 */
//		registeredUserPane.setAlignment(Pos.CENTER);
//		registeredUserPane.setStyle("-fx-background-color: #FDFFCB;");
//		HBox rwm = new HBox();
//		rwm.setAlignment(Pos.CENTER);
//		Label regiWelcome = new Label("Welcome to Bannermen.");
//		regiWelcome.setFont(font2);
//		rwm.getChildren().add(regiWelcome);
//		
//		HBox rhc = new HBox();
//		rhc.setAlignment(Pos.CENTER);
//		rhc.getChildren().add(hostButton);
//		rhc.getChildren().add(clientButton);
//		
//		HBox ril = new HBox();
//		Label imgLabel3 = new Label();
//		setLabelGraphic(imgLabel3, "src/Project201Images/king.png");
//		ril.getChildren().add(imgLabel3);
//		ril.setAlignment(Pos.CENTER);
//		
//		registeredUserPane.getChildren().add(rwm);
//		registeredUserPane.getChildren().add(rhc);
//		registeredUserPane.getChildren().add(ril);
		// ----
		stage.setOnCloseRequest((WindowEvent g) -> {
			try {
				send("close_program");
				System.exit(0);
			} catch (Exception e) {
				System.out.println("Could not be stopped.");
			}
		});

		/*
		 * These are the sections the game will be built out of
		 */
		h1 = new HBox();
		h2 = new HBox();
		h3 = new HBox();
		h4 = new HBox();

		v1 = new VBox();
		v2 = new VBox();

		gamePane.getChildren().add(h1);
		gamePane.getChildren().add(h2);

		textOutputBox = new ScrollPane();
		gamePane.getChildren().add(textOutputBox);
		writeIntoHere = new TextArea();
		textOutputBox.setContent(writeIntoHere);
		toBeSent = "";

		controlPane = new HBox();
		gamePane.getChildren().add(controlPane);

	}

	public class BuildHost extends Thread {
		@Override
		public void run() {
			try {
				ssocket = new ServerSocket(socketNumber);
				addToConversation("Connection on socket: " + socketNumber);
				csocket = ssocket.accept();
				addToConversation("Client connected");
				InputStream in = csocket.getInputStream();
				myIn = new BufferedReader(new InputStreamReader(in));
				String default_a = myIn.readLine();
				addToConversation("Message:" + default_a);
				myOut = new PrintWriter(csocket.getOutputStream(), true);
				addToConversation("Greetings!");
				myOut.println("Greetings!");
				myOut.flush();
				listen = new Ear();
				listen.start();
			} catch (Exception e) {
				System.out.println("socket open error e=" + e);
			}
		}
	}

	public void buildClient() {
		addToConversation("client setup: starting ...");
		try {
			addToConversation("Transmitting wtih: " + ip_address + "/" + socketNumber);
			csocket = new Socket(ip_address, socketNumber);
			addToConversation("Client connected.");
			InputStream in = csocket.getInputStream();
			myIn = new BufferedReader(new InputStreamReader(in));
			myOut = new PrintWriter(csocket.getOutputStream(), true);
			myOut.println("Handshake completed.");
			myOut.flush();
			String line;
			line = myIn.readLine();
			addToConversation(line);
			startTHISend();

			listen = new Ear();
			listen.start();

		} catch (Exception e) {
			addToConversation("client setup error e=" + e);
		}
	}

	public void startTHISend() {
		controlPane.getChildren().clear();
		talker = new TextField();
		controlPane.getChildren().add(talker);
		talker.setOnAction(g -> {
			String s = talker.getText();
			addToConversation("me: " + s);
			send(s);
			talker.setText("");
		});
	}

	public class Ear extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					String s = myIn.readLine(); // hangs for input
					StringTokenizer len = new StringTokenizer(s);
					String phrase = len.nextToken();
					if (s.equals("close_program")) {
						System.exit(0);
					} else {
						addToConversation("you: " + s);
					}
				} catch (Exception h) {
					addToConversation("couldn't read from the other end");
				}
			}
		}
	}

// add this string to the conversation.
	public void addToConversation(String words) {
		toBeSent += words + "\n";
		writeIntoHere.setText(toBeSent);
		textOutputBox.setVvalue(1.0);
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
		iv.setFitHeight(100);
		iv.setFitWidth(85);
		iv.setImage(image);
		label.setGraphic(iv);
	}

	public void setButtonGraphic(Button button, String name) {
		File file = new File(name);
		Image image = new Image(file.toURI().toString());
		ImageView iv = new ImageView();
		iv.setFitHeight(100);
		iv.setFitWidth(85);
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