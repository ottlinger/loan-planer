package florian.zipser.loan_planer.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import florian.zipser.loan_planer.LoanPlan;
import florian.zipser.loan_planer.LoanRate;

/**
 * Tests class {@link LoanPlan}.
 * 
 * @author florian
 *
 */
public class LoanPlanTest {

	private LoanPlan fixture = null;

	public LoanPlan getFixture() {
		return fixture;
	}

	public void setFixture(LoanPlan fixture) {
		this.fixture = fixture;
	}

	/**
	 * Checks a 'normal' runtime, which means the loan is not repaid completely
	 * when runtime ends.
	 */
	@Test
	public void testRuntime() {
		setFixture(new LoanPlan(100000, 10 * 12, 2.12d, 2d));
		List<LoanRate> items = getFixture().calculate();
		assertEquals(10 * 12 + 2, items.size());
	}

	/**
	 * Checks a runtime, when a loan is completely repaid during runtime.
	 */
	@Test
	public void testRuntime2() {
		setFixture(new LoanPlan(100000, 14 * 12, 2d, 10d));
		List<LoanRate> items = getFixture().calculate();
		assertEquals(112, items.size());
	}

	/**
	 * Checks the values of specification.
	 */
	@Test
	public void testSpec() {
		setFixture(new LoanPlan(100000, 10 * 12, 2.12d, 2d));
		List<LoanRate> items = getFixture().calculate();

		// 1st row
		assertEquals(100000d, items.get(0).getRemainingLoan(), 0);
		assertEquals(0d, items.get(0).getInterest(), 0);
		assertEquals(-100000d, items.get(0).getPrincipal(), 0);
		assertEquals(-100000d, items.get(0).getPayment(), 0);

		// 2n row
		assertEquals(99833.34d, items.get(1).getRemainingLoan(), 0);
		assertEquals(176.67d, items.get(1).getInterest(), 0);
		assertEquals(166.66d, items.get(1).getPrincipal(), 0);
		assertEquals(343.33d, items.get(1).getPayment(), 0);

		// 3rd row
		assertEquals(99666.38d, items.get(2).getRemainingLoan(), 0);
		assertEquals(176.37d, items.get(2).getInterest(), 0);
		assertEquals(166.96d, items.get(2).getPrincipal(), 0);
		assertEquals(343.33d, items.get(2).getPayment(), 0);

		// n-2 th last
		assertEquals(77949.76d, items.get(items.size() - 3).getRemainingLoan(), 0);
		assertEquals(138.07d, items.get(items.size() - 3).getInterest(), 0);
		assertEquals(205.26d, items.get(items.size() - 3).getPrincipal(), 0);
		assertEquals(343.33d, items.get(items.size() - 3).getPayment(), 0);

		// n-1 th last
		assertEquals(77744.14d, items.get(items.size() - 2).getRemainingLoan(), 0);
		assertEquals(137.71d, items.get(items.size() - 2).getInterest(), 0);
		assertEquals(205.62d, items.get(items.size() - 2).getPrincipal(), 0);
		assertEquals(343.33d, items.get(items.size() - 2).getPayment(), 0);

		// n th row
		assertEquals(77744.14d, items.get(items.size() - 1).getRemainingLoan(), 0);
		assertEquals(18943.74d, items.get(items.size() - 1).getInterest(), 0);
		assertEquals(22255.86d, items.get(items.size() - 1).getPrincipal(), 0);
		assertEquals(41199.60d, items.get(items.size() - 1).getPayment(), 0);
	}

	/**
	 * Checks that the sum of interest and principal is equal to payment.
	 */
	@Test
	public void testCheckRowSum() {
		setFixture(new LoanPlan(100000, 10 * 12, 2.12d, 2d));
		List<LoanRate> items = getFixture().calculate();

		for (LoanRate item : items) {
			assertEquals(item.getPayment(), Math.round((item.getInterest() + item.getPrincipal()) * 100d) / 100d, 0);
		}
	}
}
