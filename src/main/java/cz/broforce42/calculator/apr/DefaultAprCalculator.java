package cz.broforce42.calculator.apr;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import cz.broforce42.model.ILoan;

/**
 * Created by vekera on 07/11/16. Calculate standard APR
 */
@Service
public class DefaultAprCalculator implements IAprCalculator {
	// Number of payments
	private long numberOfPayments;
	// Original or expected balance for loan.
	private double amount;
	// The number of monthly principal and interest payment.
	private double mounthlyPaymant;
	// The number of monthly fees
	private double mounthlyFees;
	// The number of one year fees
	private double yearFees;
	// The number of before loan fees
	private double before;
	// The number of after loan fees
	private double after;
	// The number of APR
	private double anualPercentageRate;
	// The iteration count
	private int xC = 0;

	/**
	 * See @see getAPR()
	 * 
	 * @param loan
	 * 
	 */
	public double getAPR(ILoan loan) {
		this.numberOfPayments = loan.getNumberOfPayments();
		this.amount = loan.getAmount();
		this.mounthlyPaymant = loan.getMonthlyPayment();
		this.mounthlyFees = loan.getMonthlyTaxes();
		this.yearFees = loan.getYearTaxes();
		this.before = loan.getBeforeTaxes();
		this.after = loan.getAfterTaxes();
		return round(annualPercentageRate());
	}

	/**
	 * See @see getInterest()
	 * 
	 * @param loan
	 * 
	 */
	public double getInterest(ILoan loan) {
		this.numberOfPayments = loan.getNumberOfPayments();
		this.amount = loan.getAmount();
		this.mounthlyPaymant = loan.getMonthlyPayment();
		this.mounthlyFees = 0.000;
		this.yearFees = 0.000;
		this.before = 0.000;
		this.after = 0.000;
		return round(annualPercentageRate());
	}

	/*******************************************************************
	 * Private methods
	 *******************************************************************/
	/**
	 * See @see annualPercentageRate() - method to calculate APR, compares the
	 * right - left
	 * 
	 */
	private double annualPercentageRate() {
		double x = higherBoundary(0.5, 1);
		double y = x;
		double iteration = 1.001;
		int count = 0;
		while (x == y && count < 10) {
			count++;
			y = x;
			x = lowerBoundary(x, iteration);
			iteration = Math.sqrt(iteration);
			y = x;
			x = higherBoundary(x, iteration);
			iteration = Math.sqrt(iteration);
		}
		this.anualPercentageRate = x * 100;
		return this.anualPercentageRate;
	}

	/**
	 * See @see temporalAPR() - method to calculate total count, use APR formula
	 * S = A (1 + x)^ -t when x is guessAPR
	 * 
	 * @param guessAPR
	 */
	private double temporalAPR(double guessAPR) {
		// Number of total count you pay, calculate with payments before the
		// mortgage loan
		double totalCount = this.before;
		// Number of payment monthly + taxes (monthly, year)
		double monthlyPaymentAll = this.mounthlyPaymant + this.mounthlyFees;
		// Unknown APR formula (1 + X) ^ t
		double x = 0;
		double bX = 0;
		double bXAncillary = 0;

		// S = A (1 + x)^ -t - FORMULA
		for (int i = 1; i < this.numberOfPayments + 1; i++) {
			// if there year fees in loan
			if (yearFees != 0) {
				if (i % 12 == 0) {
					monthlyPaymentAll += yearFees;
				}
			}
			// months of payment
			bX = -i;
			// actual number of payment divide 12
			bXAncillary = bX / 12;
			// formula (1 + X) ^ t
			x = Math.pow(guessAPR + 1, bXAncillary);
			totalCount += monthlyPaymentAll * x;
			monthlyPaymentAll = this.mounthlyPaymant + this.mounthlyFees;
		}
		// count after payments
		if (this.after != 0) {
			bX = -(this.numberOfPayments + 1);
			bXAncillary = bX / 12;
			x = Math.pow(guessAPR + 1, bXAncillary);
			totalCount += this.after * x;
		}
		return totalCount;
	}

	/**
	 * See @see lowerBoundary() - method to calculate lowerBoundary use in
	 * approximation
	 * 
	 * @param guessAPR
	 * @param iteration
	 */
	// Lower limit of calculation APR
	private double lowerBoundary(double guessAPR, double iteration) {
		double temporalGuessAPR = temporalAPR(guessAPR);
		int xY = 0;
		while (temporalGuessAPR < this.amount && xY < 2000) {
			xY++;	
			guessAPR = guessAPR / iteration;
			temporalGuessAPR = temporalAPR(guessAPR);
			this.xC = xY; 
		}
		
		return guessAPR;
	}

	/**
	 * See @see higherBoundary() - method to calculate higherBoundary use in
	 * approximation
	 * 
	 * @param guessAPR
	 * @param iteration
	 */
	// Higher limit of calculation APR
	private double higherBoundary(double guessAPR, double iteration) {
		double temporalGuessAPR = temporalAPR(guessAPR);
		int xX = 0;
		while (temporalGuessAPR > this.amount && xX < 1000 ) {
		xX++;
			guessAPR = guessAPR * iteration;
			temporalGuessAPR = temporalAPR(guessAPR);
		}
		return guessAPR;
	}

	/**
	 * See @see round(double) - method to round to 3 decimal place
	 * 
	 * @param value
	 */
	public double round(double value) {
		BigDecimal result = new BigDecimal(value);
		result = result.setScale(3, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	@Override
	public int getCountNumber() {
		return this.xC;
	}
	
 

}
