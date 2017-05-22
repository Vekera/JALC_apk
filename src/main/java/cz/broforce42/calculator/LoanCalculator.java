package cz.broforce42.calculator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cz.broforce42.model.ILoan;
import cz.broforce42.model.ISchedule;
import cz.broforce42.model.ITaxes;
import cz.broforce42.model.ITaxes.TaxPeriod;
import cz.broforce42.model.impl.Schedule;

/**
 * Created by Vekera on 28/11/2016. TODO a JavaDoc
 */
@Service
public class LoanCalculator {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	// Constant for the number of months in a year
	static final int MONTHS_IN_YEAR = 12;

	/**
	 * Calculate loan schedule table.
	 *
	 * 1. calculate monthly payment without interest 2. calculate monthly
	 * installments to schedule table 3. add taxes (insurance, account tax
	 * etc.)4. add actual loan balance
	 *
	 * Set schedule to loan param.
	 *
	 * @param loan
	 *            loan instance
	 *
	 * @return loan schedule table
	 * @throws IOException
	 * @throws SecurityException
	 */
	public ISchedule generateSchedule(ILoan loan) {
		// Calculate monthly payment, and create schedule loan instance
		try {
			if (calculateMonthlyPayment(loan) <= 0.0) {
				throw new IllegalStateException("Cannot calculate loan monthly payment");
			}
		} catch (IllegalStateException e) {
			LOG.error("Cannot calculate loan monthly payment", e);
		}
		double monthlyInterestRate = loan.getInterestRate() / MONTHS_IN_YEAR;
		double actualBalance = loan.getAmount();
		double interest;
		double principal;
		int months = 1;
		double monthlyTax;
		do {
			try {
				if (loan.getAmount() < actualBalance || actualBalance < 0)
					throw new IllegalArgumentException("An incorect data");

			} catch (IllegalStateException e) {
				LOG.error("Cannot calculate loan monthly payment", e);
			}

			monthlyTax = specificTaxesSum(loan, TaxPeriod.MONTHLY);

			if (months == 1) {
				monthlyTax += specificTaxesSum(loan, TaxPeriod.BEFORE_LOAN);
			} else if (months % 12 == 0) {
				monthlyTax += specificTaxesSum(loan, TaxPeriod.ANNUAL);
			} else if (months >= loan.getNumberOfPayments()) {
				monthlyTax += specificTaxesSum(loan, TaxPeriod.ANNUAL) + specificTaxesSum(loan, TaxPeriod.AFTER_LOAN);
			}

			interest = round(actualBalance * monthlyInterestRate);
			principal = round(loan.getMonthlyPayment() - interest);
			if(months< loan.getNumberOfPayments()){
			actualBalance = round(actualBalance - principal);
			}else{
				actualBalance = 0.00;
			}
			monthlyTax = round(monthlyTax);
			ISchedule.Installment installment = new Schedule.Installment(interest, principal, monthlyTax,
					actualBalance);
			installment.setId(months);
			loan.getSchedule().addInstallment(installment);
			months++;
		} while (months <= loan.getNumberOfPayments());

		taxesSum(loan);
		return loan.getSchedule();
	}

	/**
	 * Method to calculate monthly payment and set up to Schedule loan instance.
	 *
	 * @param loan
	 *            instance
	 *
	 * @return calculated monthly payment without any taxes
	 */
	public double calculateMonthlyPayment(ILoan loan) {
		if (loan.getMonthlyPayment() == 0) {
			double monthlyPayment = 0.00;
			double monthlyInterestRate = loan.getInterestRate() / MONTHS_IN_YEAR;
			double percentage = 1 / (1 + monthlyInterestRate);
			double mortgageNumerator = monthlyInterestRate * loan.getAmount();
			double mortgageDenominator = 1 - Math.pow(percentage, loan.getNumberOfPayments());
			try {
				if (mortgageDenominator == 0 || mortgageNumerator == 0) {
					throw new ArithmeticException("Devide by zero");
				} else {
					monthlyPayment = mortgageNumerator / mortgageDenominator;
				}
			} catch (IllegalStateException e) {
				LOG.error("Devide by zero", e);
			}
			monthlyPayment = round(monthlyPayment);
			loan.setSchedule(new Schedule(monthlyPayment));
		} else {
			loan.setSchedule(new Schedule(loan.getMonthlyPayment()));
		}
		return loan.getSchedule().getMonthlyPayment();
	}

	/**
	 * Get specific loan taxes - Annual, Before, After, .....
	 *
	 * @param loan
	 *            loan with taxes instance
	 * @param period
	 *            ITaxes.TaxPeriod - annual, before, after,...
	 * @return calculate monthly tax (account tax, insurance).
	 */
	public double specificTaxesSum(ILoan loan, ITaxes.TaxPeriod period) {
		if (loan.getTaxes() == null) {
			return 0.0;
		}
		List<ITaxes.Entry> taxes = loan.getTaxes().getTaxesByPeriod(period);
		double result = 0.0;
		for (ITaxes.Entry tax : taxes) {
			if (tax.getValueType() == ITaxes.TaxValueType.AMOUNT) {
				result += tax.getAmount();
			} else if (tax.getValueType() == ITaxes.TaxValueType.PERCENT_OF_FULL_LOAN) {
				result += tax.getAmount() * loan.getAmount();
			} else {
				result += tax.getAmount() * loan.getMonthlyPayment();
			}
		}
		return round(result);
	}

	/**
	 * Method to set instance variables of loan
	 *
	 * @param loan
	 *            loan with taxes instance
	 */
	private void taxesSum(ILoan loan) {
		loan.setAfterTaxes(specificTaxesSum(loan, TaxPeriod.AFTER_LOAN));
		loan.setBeforeTaxes(specificTaxesSum(loan, TaxPeriod.BEFORE_LOAN));
		loan.setMonthlyTaxes(specificTaxesSum(loan, TaxPeriod.MONTHLY));
		loan.setYearTaxes(specificTaxesSum(loan, TaxPeriod.ANNUAL));
	}

	/**
	 * Rounding method
	 *
	 * @param value
	 */

	private double round(double value) {
		BigDecimal result = new BigDecimal(value);
		result = result.setScale(4, RoundingMode.HALF_DOWN);

		return result.doubleValue();
	}
}