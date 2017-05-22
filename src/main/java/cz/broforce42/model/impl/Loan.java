package cz.broforce42.model.impl;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import cz.broforce42.model.ILoan;
import cz.broforce42.model.ISchedule;
import cz.broforce42.model.ITaxes;

/**
 * Created by Vekera on 28/11/2016.
 */

public class Loan implements ILoan {

	// Loan ID
	@NotNull(message = "error.id.notnull")
	private String id;

	// Divisor 100
	private static final int DIVISOR100 = 100;

	// Original or expected balance for loan.
	@DecimalMin(value = "0.1", message = "error.amount")
	private long amount;

	// Annual fixed interest rate for loan or interest rate from fixed period
	@DecimalMin(value = "0.0", message = "error.interestRate")
	private double interestRate;

	// The number of monthly payments.
	@Min(value = 1, message = "error.numberOfPayments")
	private int numberOfPayments;

	// The number of annual percentage rate
	private double annualPercentageRate;

	// The number of monthly principal and interest payment.
	@DecimalMin(value = "0.0", message = "error.monthlyPayment")
	private double monthlyPayment;

	// The number of monthlyTaxes
	private double monthlyTaxes;

	// The number of yearTaxes
	private double yearTaxes;

	// The number of beforeTaxes
	private double beforeTaxes;

	// The number of afterTaxes
	private double afterTaxes;

	// Interface schedule
	private ISchedule schedule;

	// Interface taxes
	private ITaxes taxes;

	/**
	 * Minimal constructor for loans with number of payments.
	 *
	 * @param amount
	 *            - Original or expected balance for loan.
	 * @param interestRate
	 *            - Annual fixed interest rate for loan.
	 * @param numberOfPayments
	 *            - The number of years over which you will repay loan.
	 */
	public Loan(@JsonProperty("id") String id, @JsonProperty("amount") long amount,
			@JsonProperty("interestRate") double interestRate, @JsonProperty("numberOfPayments") int numberOfPayments) {
		this.id = id;
		this.amount = amount;
		this.interestRate = interestRate;
		this.numberOfPayments = numberOfPayments;
	}

	/**
	 * Full constructor for loans with number of payments.
	 *
	 * @param amount
	 *            - Original or expected balance for loan.
	 * @param interestRate
	 *            - Annual fixed interest rate for loan.
	 * @param numberOfPayments
	 *            - The number of years over which you will repay loan.
	 */
	@JsonCreator
	public Loan(@JsonProperty("id") String id, @JsonProperty("amount") long amount,
			@JsonProperty("interestRate") double interestRate, @JsonProperty("numberOfPayments") int numberOfPayments,
			@JsonProperty("monthlyPayments") double monthlyPayment) {
		this(id, amount, interestRate, numberOfPayments);
		this.monthlyPayment = monthlyPayment;
	}

	/**
	 * @see ILoan#getAmount()
	 */
	public long getAmount() {
		return this.amount;
	}

	/**
	 * @see ILoan#getInterestRate()
	 */
	public double getInterestRate() {
		return this.interestRate / DIVISOR100;
	}

	/**
	 * @see ILoan#getNumberOfPayments()
	 */
	public long getNumberOfPayments() {
		return this.numberOfPayments;
	}

	/**
	 * @see ILoan#setAnnualPercentageRate(double)
	 */
	public void setAnnualPercentageRate(double annualPercentageRate) {
		this.annualPercentageRate = annualPercentageRate;
	}

	/**
	 * @see ILoan#getAnnualPercentageRate()
	 */
	public double getAnnualPercentageRate() {
		return this.annualPercentageRate;
	}

	/**
	 * @see ILoan#getMonthlyPayment()
	 */
	public double getMonthlyPayment() {
		if (this.monthlyPayment == 0) {
			if (schedule != null)
				this.monthlyPayment = schedule.getMonthlyPayment();
		}
		return this.monthlyPayment;
	}

	/**
	 * @see ILoan#getTotalPayments()
	 */
	public double getTotalPayments() {
		if (this.schedule != null) {
			return this.schedule.getTotalPayments();
		}
		return 0.0;
	}

	/**
	 * @see ILoan#getTotalTaxes()
	 */
	public double getTotalTaxes() {
		if (this.schedule != null) {
			return this.schedule.getTotalTaxes();
		}
		return 0.0;
	}

	/**
	 * @see ILoan#getTotalInterests()
	 */
	public double getTotalInterests() {
		return this.getTotalPayments() - (this.amount + this.getTotalTaxes());
	}

	/**
	 * @see ILoan#getSchedule()
	 */
	public ISchedule getSchedule() {
		return schedule;
	}

	/**
	 * @see ILoan#setSchedule(ISchedule)
	 */
	public void setSchedule(ISchedule schedule) {
		if (schedule == null) {
			throw new IllegalArgumentException("Loan schedule cannot be null");
		}
		this.schedule = schedule;
	}

	/**
	 * @see ILoan#getTaxes()
	 */
	public ITaxes getTaxes() {
		return taxes;
	}

	/**
	 * @see ILoan#setTaxes(ITaxes)
	 */
	public void setTaxes(ITaxes taxes) {
		if (taxes == null) {
			throw new IllegalArgumentException("Loan taxes cannot be null");
		}

		this.taxes = taxes;
	}

	/**
	 * See @see Comparable#compareTo(Object) method compare two loans
	 */
	public int compareTo(ILoan loan) {
		/**
		 * @return:
		 *          <p>
		 *          1 - first loan is cheaper then second - first have larger
		 *          amount in monthly payment ,second have more TAXES!
		 *          <p>
		 *          2 - first loan is more expensive then second - first have
		 *          larger amount in monthly payment ,first have more TAXES!
		 *          <p>
		 *          3 -second loan is cheaper then second- second have larger
		 *          amount in monthly payment ,first have more TAXES!
		 *          <p>
		 *          4 - second loan is more expensive then first - second have
		 *          larger amount in monthly payment ,second have more TAXES!
		 *          <p>
		 *          5 - equal
		 */
		if (loan == null) {
			throw new IllegalArgumentException("You try to compare empty/null loan instance.");
		}
		if (this.interestRate > loan.getInterestRate()) {
			if (this.annualPercentageRate < loan.getAnnualPercentageRate()) {
				return 1;
			} else {
				return 2;
			}
		} else if (this.interestRate < loan.getInterestRate()) {
			if (this.annualPercentageRate > loan.getAnnualPercentageRate()) {
				return 3;
			} else {
				return 4;
			}
		} else {
			return 5;
		}

	}

	/**
	 * @see ILoan#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * @see ILoan#setId(String id)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @see ILoan#getMonthlyTaxes()
	 */
	public double getMonthlyTaxes() {
		return monthlyTaxes;
	}

	/**
	 * @see ILoan#setMonthlyTaxes(double monthlyTaxes)
	 */
	public void setMonthlyTaxes(double monthlyTaxes) {
		this.monthlyTaxes = monthlyTaxes;
	}

	/**
	 * @see ILoan#getYearTaxes()
	 */
	public double getYearTaxes() {
		return yearTaxes;
	}

	/**
	 * @see ILoan#setYearTaxes(double yearTaxes)
	 */
	public void setYearTaxes(double yearTaxes) {
		this.yearTaxes = yearTaxes;
	}

	/**
	 * @see ILoan#getBeforeTaxes()
	 */
	public double getBeforeTaxes() {
		return beforeTaxes;
	}

	/**
	 * @see ILoan#setBeforeTaxes(double beforeTaxes)
	 */
	public void setBeforeTaxes(double beforeTaxes) {
		this.beforeTaxes = beforeTaxes;
	}

	/**
	 * @see ILoan#getAfterTaxes()
	 */
	public double getAfterTaxes() {
		return afterTaxes;
	}

	/**
	 * @see ILoan#setAfterTaxes(double afterTaxes)
	 */
	public void setAfterTaxes(double afterTaxes) {
		this.afterTaxes = afterTaxes;
	}

	/**
	 * @see ILoan#setInterestRate(double interestRate)
	 */
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	/**
	 * @see ILoan#setMonthlyPayment(double monthlyPayment)
	 */
	public void setMonthlyPayment(double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

}
