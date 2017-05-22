package cz.broforce42.model.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.broforce42.model.ISchedule;

/**
 * Schedule class collect information in array list- monthly payment without
 * interest, monthly installments, taxes, actual balance of loan
 */

public class Schedule implements ISchedule {

	// The number of monthly principal and interest payment.
	private double monthlyPayment;
	// The number of total taxes of whole loan
	private double totalTaxes;
	// The number of total payments of loan
	private double totalPayments;

	// Schedule installments collection
	private final List<ISchedule.Installment> installments;

	/**
	 * Main constructor. Init instance.
	 */
	public Schedule(double monthlyPayment) {
		this.installments = new ArrayList<ISchedule.Installment>();
		this.monthlyPayment = monthlyPayment;
	}

	/**
	 * @see cz.broforce42.model.ISchedule.Installment
	 */
	public static class Installment implements ISchedule.Installment {

		// Installment interest
		private final double interest;

		// Installment principal
		private final double principal;

		// Installment actualBalance
		private double actualBalance;

		// Installment tax
		private double tax;

		// Installment id
		private int id;

		/**
		 * Main constructor.
		 *
		 * @param interest
		 *            - interest value
		 * @param principal
		 *            - interest principal
		 */
		public Installment(double interest, double principal) {
			this.interest = interest;
			this.principal = principal;

		}

		/**
		 * Second constructor.
		 *
		 * @param interest
		 *            - interest value
		 * @param principal
		 *            - interest principal
		 * @param tax
		 *            - interest tax
		 */
		public Installment(double interest, double principal, double tax) {
			this(interest, principal);
			this.tax = tax;

		}

		/**
		 * Full constructor.
		 *
		 * @param interest
		 *            - interest value
		 * @param principal
		 *            - interest principal
		 * @param tax
		 *            - interest tax
		 * @param actualBalance
		 *            - interest catualBalance
		 */
		public Installment(double interest, double principal, double tax, double actualBalance) {
			this(interest, principal);
			this.tax = tax;
			this.actualBalance = actualBalance;

		}

		/**
		 * @see ISchedule.Installment#getAmount()
		 */
		public double getAmount() {
			return this.interest + this.principal + this.tax;
		}

		/**
		 * @see ISchedule.Installment#getActualBalance()
		 */
		public double getActualBalance() {
			return this.actualBalance;
		}

		/**
		 * @see ISchedule.Installment#getInterest()
		 */
		public double getInterest() {
			return this.interest;
		}

		/**
		 * @see ISchedule.Installment#getTax()
		 */
		public double getTax() {
			return this.tax;
		}

		/**
		 * @see ISchedule.Installment#getPrincipal()
		 */
		public double getPrincipal() {
			return this.principal;
		}

		/**
		 * @see ISchedule.Installment#getDate()
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * @see ISchedule.Installment#setDate(Date)
		 */
		public void setId(int id) {
			this.id = id;
		}
	}

	/**
	 * @see ISchedule#getStart()
	 */
	public int getStart() {
		return installments.get(0).getId();
	}

	/**
	 * @see ISchedule#getEnd()
	 */
	public int getEnd() {
		int size = installments.size();
		return installments.get(size - 1).getId();
	}

	/**
	 * @see ISchedule#getMonthlyPayment()
	 */
	public double getMonthlyPayment() {
		return this.monthlyPayment;
	}

	/**
	 * @see ISchedule#getInstallments()
	 */
	public List<ISchedule.Installment> getInstallments() {
		return this.installments;
	}

	/**
	 * @see ISchedule#addInstallment(ISchedule.Installment)
	 */
	public void addInstallment(ISchedule.Installment installment) {
		if (this.totalTaxes != 0 || this.totalPayments != 0) {
			this.totalTaxes += installment.getTax();
			this.totalPayments += installment.getAmount();
		}
		this.installments.add(installment);
	}

	/**
	 * @see ISchedule#getTotalTaxes()
	 */
	public double getTotalTaxes() {
		if(this.totalPayments == 0){
			calculateLoop();	
		}
		return round(this.totalTaxes);
	}

	/**
	 * @see ISchedule#getTotalPayments()
	 */
	public double getTotalPayments() {
		if(this.totalTaxes == 0){
			calculateLoop();	
		}
		return round(this.totalPayments);
	}

	/**
	 * calculateLoop() - private method to calculate totalPayments and
	 * totalTaxes
	 */
	private void calculateLoop() {
		double resultPayments = 0.0;
		double resultTaxes = 0.0;
			for (ISchedule.Installment ins : this.installments) {
				resultPayments += ins.getAmount();
				resultTaxes += ins.getTax();
			}
			this.totalPayments = resultPayments;
			this.totalTaxes = resultTaxes;
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
