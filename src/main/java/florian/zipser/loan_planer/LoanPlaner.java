package florian.zipser.loan_planer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * An application to create a payment plan for a loan. You can enter the
 * amount of the loan, it's lifetime (in years), the rate of interest and the
 * principal rate. The program will calculate the entire rate for each month
 * including:
 * <ul>
 * <li>the date of the rate,</li>
 * <li>the remaining loan in this month,</li>
 * <li>the absolute interests to be paid in this month,</li>
 * <li>the principal in this month and</li>
 * <li>the payment to be paid.</li>
 * </ul>
 * The last entry shows the remaining debit, the sum of paid interests and the
 * sum of principal.
 * 
 * @author florian
 *
 */
public class LoanPlaner extends Application {

	/**
	 * name of FXML file.
	 */
	public static final String FXML_FILE = "loan-planer.fxml";

	/**
	 * Starts the JavaFX environment and loads the controller via fxml file
	 * {@value #FXML_FILE}.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Kreditplaner");
		Parent myPane = (Parent) FXMLLoader.load(getClass().getResource(FXML_FILE));
		Scene myScene = new Scene(myPane);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}

	/**
	 * Entry point of the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
