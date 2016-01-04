package florian.zipser.loan_planer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * An object of this class represents a single rate of the loan plan. A rate
 * contains the date in lifetime of the loan, the remaining loan, the interest
 * (abs.), the principal (abs.) and the payment (abs.). <br/>
 * A set of rates represents the loan plan along it's entire lifetime.
 * 
 * @author florian
 *
 */
public class LoanRate {
	/**
	 * Creates a rate with default values this("", 0d, 0d, 0d, 0d):
	 * {@link #LoanRate(String, double, double, double, double)}
	 */
	public LoanRate() {
		this("", 0d, 0d, 0d, 0d);
	}

	/**
	 * Creates and initializes a rate with the passed values.
	 * 
	 * @param date
	 *            when is the rate repaid
	 * @param loan
	 *            the amount of the remaining loan after paying this rate
	 * @param interest
	 *            the paid interest (abs.) of this rate
	 * @param principal
	 *            the principal (abs.) of this rate
	 * @param payment
	 *            the payment (abs.) of this rate
	 */
	public LoanRate(String date, double loan, double interest, double principal, double payment) {
		setDate(date);
		setRemainingLoan(loan);
		setInterest(interest);
		setPrincipal(principal);
		setPayment(payment);
	}

	private final SimpleStringProperty date = new SimpleStringProperty();

	/**
	 * @return the date when the rate is repaid
	 */
	public String getDate() {
		return date.get();
	}

	/**
	 * @param date
	 *            the date when the rate is repaid
	 */
	public void setDate(String date) {
		this.date.set(date);
	}

	private final SimpleDoubleProperty remainingLoan = new SimpleDoubleProperty();

	/**
	 * @return the amount of the remaining loan after paying this rate
	 */
	public double getRemainingLoan() {
		return remainingLoan.get();
	}

	/**
	 * @param remainingLoan
	 *            the amount of the remaining loan after paying this rate
	 */
	public void setRemainingLoan(double remainingLoan) {
		this.remainingLoan.set(remainingLoan);
	}

	private final SimpleDoubleProperty interest = new SimpleDoubleProperty();

	/**
	 * @return the paid interest (abs.) of this rate
	 */
	public double getInterest() {
		return interest.get();
	}

	/**
	 * @param interest
	 *            the paid interest (abs.) of this rate
	 */
	public void setInterest(double interest) {
		this.interest.set(interest);
	}

	private final SimpleDoubleProperty principal = new SimpleDoubleProperty();

	/**
	 * @return the principal (abs.) of this rate
	 */
	public double getPrincipal() {
		return principal.get();
	}

	/**
	 * @param principal
	 *            the principal (abs.) of this rate
	 */
	public void setPrincipal(double principal) {
		this.principal.set(principal);
	}

	private final SimpleDoubleProperty payment = new SimpleDoubleProperty();

	/**
	 * @return the payment (abs.) of this rate
	 */
	public double getPayment() {
		return payment.get();
	}

	/**
	 * @param payment
	 *            the payment rate (abs.) of this rate
	 */
	public void setPayment(double payment) {
		this.payment.set(payment);
	}
}