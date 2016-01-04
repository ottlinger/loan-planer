package florian.zipser.loan_planer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.Precision;

/**
 * This class is the core of the application and calculated the payment plan
 * of the loan. <br/>
 * To create such a plan, initialize an object of this class, pass the amount of
 * loan, it's lifetime, the interest rate and the principal rate. To start the
 * calculation call {@link #calculate()}. The returned list is a list of
 * {@link LoanRate} objects, realizing the entire payment plan. Each item of
 * the list is one rate in a specific month.
 * 
 * @author florian
 */
public class LoanPlan {

	/**
	 * fix interest rate in percent
	 */
	private double interestRate;
	/**
	 * start amount of loan
	 */
	private double loan;
	/**
	 * payment per month
	 */
	private double payment;
	/**
	 * total lifetime of loan
	 */
	private int lifetime;
	/**
	 * list of all loan rates (this is the resulting list)
	 */
	private List<LoanRate> loanRates = null;
	/**
	 * a calendar instance to emit the loan rate's date
	 */
	private Calendar cal = Calendar.getInstance();
	/**
	 * a formatter to format the date in German tradition
	 */
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 * the finally remaining loan
	 */
	private double remainingLoan = 0;

	/**
	 * @return the finally remaining loan
	 */
	public double getRemainingLoan() {
		return remainingLoan;
	}

	/**
	 * the sum of all paid interests in lifetime
	 */
	private double payedInterests = 0;

	/**
	 * @return the sum of all paid interests in lifetime
	 */
	public double getPayedInterests() {
		return payedInterests;
	}

	/**
	 * the sum of all paid principals in lifetime
	 */
	private double totalPrincipal = 0;

	/**
	 * @return the sum of principal in lifetime
	 */
	public double getTotalPrincipal() {
		return totalPrincipal;
	}

	/**
	 * Initializes a LoanPlan object. To calculate the plan run
	 * {@link #calculate()}.
	 * 
	 * @param loan
	 *            the amount of the loan
	 * @param lifetime
	 *            the loan's lifetime in month
	 * @param interestRate
	 *            the rate of interest in percent
	 * @param principalRate
	 *            the rate of the principal in percent
	 */
	public LoanPlan(double loan, int lifetime, double interestRate, double principalRate) {
		this.interestRate = interestRate;
		this.loan = loan;
		this.lifetime = lifetime;
		// calculate monthly payment rate
		this.payment = Precision.round(loan * (interestRate + principalRate) / 100 / 12, 2);
		// set calendar to last day in month
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

		loanRates = new LinkedList<>();
		loanRates.add(new LoanRate(dateFormat.format(cal.getTime()), loan, 0d, (loan * -1), (loan * -1)));
	}

	/**
	 * Starts the calculation of the loan plan. The returned list has one entry
	 * per month in the loan's lifetime and an additional entry containing the
	 * remaining loan, the sum of interest and principal.
	 * 
	 * @return list of rates per month
	 */
	public List<LoanRate> calculate() {
		calculate(lifetime, loan);
		loanRates.add(new LoanRate("Zinsbindungsende", remainingLoan, payedInterests, totalPrincipal, Math.round((payedInterests + totalPrincipal) * 100d) / 100d));
		return (loanRates);
	}

	/**
	 * Recursive function to calculate the loan rates.
	 * 
	 * @param lifetime
	 *            loan's remaining lifetime
	 * @param loan
	 *            the remaining loan
	 */
	private void calculate(int lifetime, Double loan) {
		if (lifetime > 0 && loan > 0) {
			// calculate absolute interest rate and round them with precision 2
			double interestAbs = Precision.round(loan * interestRate / 100 / 12, 2);
			// calculate absolute principal and round them with precision 2
			double newPrincipal = Precision.round(payment - interestAbs, 2);
			if (loan < newPrincipal) {
				newPrincipal = loan;
			}

			// round remaining loan, payed interests and principal with
			// precision 2
			remainingLoan = Precision.round(loan - newPrincipal, 2);
			payedInterests = Precision.round(payedInterests + interestAbs, 2);
			totalPrincipal = Precision.round(totalPrincipal + newPrincipal, 2);

			// set calendar next month and to last day in month
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

			// add rate to list of rates
			loanRates.add(new LoanRate(dateFormat.format(cal.getTime()).toString(), remainingLoan, interestAbs, newPrincipal, payment));
			calculate(lifetime - 1, remainingLoan);
		}
	}
}
