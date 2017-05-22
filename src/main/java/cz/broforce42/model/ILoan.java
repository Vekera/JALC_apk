package cz.broforce42.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cz.broforce42.model.impl.Loan;

/**
 * Created by Vekera on 25/12/16.
 * 
 */

@JsonDeserialize(as = Loan.class)
public interface ILoan extends Comparable<ILoan> {

	/**
	 * Return original or expected balance for loan.
	 *
	 * @return balance for loan
	 */

	long getAmount();

	/**
	 * Annual fixed interest rate for loan in decimal form.
	 *
	 * @return fixed interest rate for loan
	 */
	double getInterestRate();

	/**
	 * The number of monthly payments.
	 *
	 * @return number of payments.
	 */
	long getNumberOfPayments();

	/**
	 * Calculated annual percentage rate field in decimal form. To calculate APR
	 * you should use proper APR calculator.
	 *
	 * @return annual percentage rate
	 */
	double getAnnualPercentageRate();

	/**
	 * Set calculated annual percentage rate field. To calculate APR you should
	 * use proper APR calculator.
	 *
	 */
	void setAnnualPercentageRate(double apr);

	/**
	 * Monthly principal and interest payment with all annual taxes (for bank
	 * account, insurance etc).
	 *
	 * @return clean monthly payment without home owners insurance, property
	 *         taxes and etc.
	 */
	double getMonthlyPayment();

	/**
	 * Total of all monthly payments over the full term of the mortgage (with
	 * taxes).
	 *
	 * @return loan total price
	 */
	double getTotalPayments();

	/**
	 * Total of all loan taxes (oneTime, annual, monthly ...).
	 *
	 * @return total loan taxes
	 */
	double getTotalTaxes();

	/**
	 * Total loan interests.
	 *
	 * @return total loan interests
	 */
	double getTotalInterests();

	/**
	 * Returns loan schedule. See @see ISchedule.
	 *
	 * @return loan schedule object with list of payments.
	 */
	ISchedule getSchedule();

	/**
	 * Set loan schedule. See @see ISchedule.
	 */
	void setSchedule(ISchedule schedule);

	/**
	 * Returns loan taxes. See @see ITaxes.
	 *
	 * @return loan taxes.
	 */
	ITaxes getTaxes();

	/**
	 * Set loan taxes. See @see ITaxes.
	 */
	void setTaxes(ITaxes taxes);

	/**
	 * Get loan ID.
	 */
	public String getId();

	/**
	 * Set loan ID.
	 * 
	 * @param id
	 */
	public void setId(String id);

	/**
	 * Set loan after taxes.
	 * 
	 * @param afterTaxes
	 */
	public void setAfterTaxes(double afterTaxes);

	/**
	 * Get loan after taxes.
	 */
	public double getAfterTaxes();

	/**
	 * Set loan before taxes.
	 * 
	 * @param beforeTaxes
	 */
	public void setBeforeTaxes(double beforeTaxes);

	/**
	 * Get loan before taxes.
	 */
	public double getBeforeTaxes();

	/**
	 * Set loan year taxes.
	 * 
	 * @param yearTaxes
	 */
	public void setYearTaxes(double yearTaxes);

	/**
	 * Get loan year taxes.
	 */
	public double getYearTaxes();

	/**
	 * Set loan monthly taxes.
	 * 
	 * @param monthlyTaxes
	 */
	public void setMonthlyTaxes(double monthlyTaxes);

	/**
	 * Get loan monthly taxes.
	 */
	public double getMonthlyTaxes();

	/**
	 * Set loan interest rate.
	 * 
	 * @param interestRate
	 */
	public void setInterestRate(double interestRate);

	/**
	 * Set loan monthly payment.
	 * 
	 * @param monthlyPayment
	 */
	public void setMonthlyPayment(double monthlyPayment);

}
