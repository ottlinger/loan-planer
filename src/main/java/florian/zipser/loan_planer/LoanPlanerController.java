package florian.zipser.loan_planer;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.math3.util.Precision;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * This class is the controller of the LoanPlaner application. It is referenced
 * in the fxml file (view) and controls the user interactions. <br/>
 * The core of the application is the table, which contains the loan plan. This
 * table is linked to the loan plan model {@link LoanRate}.
 * 
 * @author florian
 *
 */
public class LoanPlanerController implements Initializable {
	/** Warning message, when a field contains a non numeric. **/
	private static final String WARN_NO_NUM = "Der Wert in Feld '%s' ist kein gültiger numerischer Wert. ";
	private static final String WARN_VAL_NEG = "Der Wert in Feld '%s' ist kein positiver Wert. ";
	
	/** name of field loan**/
	private static final String FIELD_LOAN= "Darlehensbetrag";
	/** name of field lifetime**/
	private static final String FIELD_LIFETIME= "Laufzeit";
	/** name of field interest**/
	private static final String FIELD_INTEREST= "Zinsen";
	/** name of field principal**/
	private static final String FIELD_PRINCIPAL= "Tilgung";
	
	/**
	 * Reference on entire table
	 */
	@FXML
	private TableView<LoanRate> tableView;
	/**
	 * Reference to column date in table
	 */
	@FXML
	private TableColumn<LoanRate, String> dateCol;
	/**
	 * Reference to column loan in table
	 */
	@FXML
	private TableColumn<LoanRate, String> loanCol;
	/**
	 * Reference to column interest in table
	 */
	@FXML
	private TableColumn<LoanRate, String> interestCol;
	/**
	 * Reference to column principal in table
	 */
	@FXML
	private TableColumn<LoanRate, String> principalCol;
	/**
	 * Reference to column total rate in table
	 */
	@FXML
	private TableColumn<LoanRate, String> paymentCol;
	/**
	 * input field for loan
	 */
	@FXML
	private TextField loanTxt;
	/**
	 * input field for interest rate
	 */
	@FXML
	private TextField interestTxt;
	/**
	 * input field for principal rate
	 */
	@FXML
	private TextField principalTxt;
	/**
	 * input field for lifetime
	 */
	@FXML
	private TextField lifetimeTxt;
	/**
	 * Message field.
	 */
	@FXML
	private Label warnField;

	/**
	 * field for remaining loan after last rate
	 */
	@FXML
	private TextField totalLoanTxt;
	/**
	 * field for total payed interest
	 */
	@FXML
	private TextField totalInterestTxt;
	/**
	 * field for total paid principal
	 */
	@FXML
	private TextField totalPrincipalTxt;
	/**
	 * field for total payment
	 */
	@FXML
	private TextField totalPaymentTxt;

	/**
	 * Calculate button
	 */
	@FXML
	private Button calculateBtn;

	/**
	 * Validates the passed <em>value</em>, converts it to a double and returns
	 * it. If the <em>value</em> is not valid, a warning is printed to warn
	 * field.
	 * 
	 * @param value
	 *            value to be checked and converted
	 * @param fieldName
	 *            name of field to be validated
	 * @return <em>value</em> as Double, null if an error occured
	 */
	private Double validateDouble(String value, String fieldName) {
		value = value.replace(",", ".");
		double retVal = 0d;
		try {
			retVal = Double.valueOf(value);
		} catch (NumberFormatException e) {
			warn(String.format(WARN_NO_NUM, fieldName));
			return null;
		}
		if (retVal < 0) {
			warn(String.format(WARN_VAL_NEG, fieldName));
			return null;
		}
		warn("");
		return retVal;
	}

	/**
	 * Fired, When 'calculate button' was clicked. This method checks the user
	 * input and starts calculating the loan plan via calling
	 * {@link LoanPlan#calculate()}. If the user input is not correct, an error
	 * message is displayed in 'message field'.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleCalculateButtonAction(ActionEvent event) {
		Double loan = validateDouble(loanTxt.getText(), FIELD_LOAN);
		Double lifetimeYears = validateDouble(lifetimeTxt.getText(), FIELD_LIFETIME);
		Double interestRate = validateDouble(interestTxt.getText(), FIELD_INTEREST);
		Double principalRate = validateDouble(principalTxt.getText(), FIELD_PRINCIPAL);

		if (loan != null && lifetimeYears != null && interestRate != null && principalRate != null) {
			warnField.setText("");
			// clear cells in table in case it is the second run at least
			tableView.getItems().clear();

			// calculate lifetime in month
			int lifetime = 0;
			if (lifetimeYears % 1 > 0) {
				String[] lifeTimeStr = Double.toString(lifetimeYears).split("[.]");
				lifetime = 12 * Integer.valueOf(lifeTimeStr[0]);
				lifetime += Precision.round(Integer.valueOf(lifeTimeStr[1]) * 12 / Math.pow(10, lifeTimeStr[1].length()), 0);
			} else {
				lifetime = Double.valueOf(lifetimeYears * 12).intValue();
			}

			// initialize and calculate loan plan
			LoanPlan plan = new LoanPlan(loan, lifetime, interestRate, principalRate);

			// fill view
			tableView.getItems().addAll(plan.calculate());
			totalLoanTxt.setText(String.format("%1$,.2f", tableView.getItems().get(tableView.getItems().size() - 1).getRemainingLoan()) + " €");
			totalInterestTxt.setText(String.format("%1$,.2f", tableView.getItems().get(tableView.getItems().size() - 1).getInterest()) + " €");
			totalPrincipalTxt.setText(String.format("%1$,.2f", tableView.getItems().get(tableView.getItems().size() - 1).getPrincipal()) + " €");
			totalPaymentTxt.setText(String.format("%1$,.2f", tableView.getItems().get(tableView.getItems().size() - 1).getPayment()) + " €");
		}
	}

	/**
	 * Prints a warning in the warning field.
	 * 
	 * @param msg
	 *            message to be displayed
	 */
	private void warn(String msg) {
		warnField.setText(msg);
	}

	/**
	 * Called when controller is initialized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loanTxt.textProperty().addListener(new NumberFieldChangedListener(FIELD_LOAN));
		lifetimeTxt.textProperty().addListener(new NumberFieldChangedListener(FIELD_LIFETIME));
		interestTxt.textProperty().addListener(new NumberFieldChangedListener(FIELD_INTEREST));
		principalTxt.textProperty().addListener(new NumberFieldChangedListener(FIELD_PRINCIPAL));

		loanCol.setCellFactory(new Callback<TableColumn<LoanRate, String>, TableCell<LoanRate, String>>() {
			public TableCell call(TableColumn<LoanRate, String> c) {
				TableCell<LoanRate, Double> cell = new TableCell<LoanRate, Double>() {
					@Override
					public void updateItem(Double item, boolean empty) {
						super.updateItem(item, empty);
						String text = null;
						if (!empty) {
							if (getItem() != null) {
								text = "-" + String.format("%1$,.2f", getItem()) + " €";
							} else {
								text = "0,00 €";
							}
						}
						setText(text);
					}
				};
				return cell;
			}
		});
		interestCol.setCellFactory(new CellFormatter());
		principalCol.setCellFactory(new CellFormatter());
		paymentCol.setCellFactory(new CellFormatter());
	}

	/**
	 * Formats cells in table to bring them into localized number format and add
	 * a € character.
	 * 
	 * @author florian
	 *
	 */
	private class CellFormatter implements Callback<TableColumn<LoanRate, String>, TableCell<LoanRate, String>> {
		public TableCell call(TableColumn<LoanRate, String> c) {
			TableCell<LoanRate, Double> cell = new TableCell<LoanRate, Double>() {
				@Override
				public void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					String text = null;
					if (!empty) {
						if (getItem() != null) {
							text = String.format("%1$,.2f", getItem()) + " €";
						} else {
							text = "0,00 €";
						}
					}
					setText(text);
				}
			};
			return cell;
		}
	}

	/**
	 * A Listener to check user input in input fields.
	 * 
	 * @author florian
	 *
	 */
	private class NumberFieldChangedListener implements ChangeListener<String> {
		private String fieldName;

		public NumberFieldChangedListener(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if (newValue != null && !newValue.isEmpty()) {
				validateDouble(newValue, fieldName);
			}
		}
	}
}
