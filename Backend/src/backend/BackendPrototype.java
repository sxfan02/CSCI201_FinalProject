import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BackendPrototype extends Application {
	VBox root; // the whole window
	Scene scene;
	String usernameString, passwordString, winnerString;

	public static void main(String[] args) {
		launch(args); 
		
	}

	public void start(Stage stage) 
	{
		root = new VBox();
		scene = new Scene(root, 700, 700);
		stage.setTitle("Project 201");
		stage.setScene(scene);
		stage.show();
		
		HBox usernamePane = new HBox();
		Label usernameLabel = new Label("Username: ");
		usernameLabel.setMinWidth(100);
		TextField usernameTF = new TextField();
		root.getChildren().add(usernamePane);
		usernamePane.getChildren().add(usernameLabel);
		usernamePane.getChildren().add(usernameTF);
		
		
		HBox passwordPane = new HBox();
		Label passwordLabel = new Label("Password: ");
		passwordLabel.setMinWidth(100);
		TextField passwordTF = new TextField();
		root.getChildren().add(passwordPane);
		passwordPane.getChildren().add(passwordLabel);
		passwordPane.getChildren().add(passwordTF);
		
		Button enterButton = new Button("Enter");
		enterButton.setOnAction(e -> 
		{
			usernameString = usernameTF.getText();
			passwordString = passwordTF.getText();
			System.out.println("The username entered was: " + usernameString);
			System.out.println("The password entered was: " + passwordString);
			test.post("login", usernameString, passwordString);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			winnerString = usernameString;
			System.out.println("The winner is: " + winnerString);
			//Test login
			test.onLogin("login", usernameString, passwordString);
		});
		passwordPane.getChildren().add(enterButton);
	}
}