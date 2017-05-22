package cz.broforce42.model;

import java.util.List;

/**
 * Created by Vekera on 25/12/16.
 * 
 */
public interface ISchedule {

	/**
	 * Starting id
	 * 
	 * @return id
	 */
	int getStart();

	/**
	 * Ending id
	 * 
	 * @return id
	 */
	int getEnd();

	/**
	 * Method to get monthly payment
	 * 
	 * @return monthlyPayment
	 */
	double getMonthlyPayment();

	/**
	 * Method to get installments of loan
	 * 
	 * @return installment
	 */
	List<Installment> getInstallments();

	/**
	 * Method to add installments of loan
	 * 
	 * @param installment
	 */
	void addInstallment(Installment installment);

	/**
	 * Method to get total taxes
	 * 
	 * @return totalTaxest
	 */
	double getTotalTaxes();

	/**
	 * Method to get total payment of loan
	 * 
	 * @return toalPayments
	 */
	double getTotalPayments();

	/**
	 * Loan calculator generate schedule (add installments) or user can add
	 * specific installments.
	 * 
	 */
	interface Installment {

		/**
		 * Method to get Amount from installment. Use to calculate total
		 * payments (principal + interest + tax)
		 * 
		 * @return loan amount
		 */
		double getAmount();

		/**
		 * Method to get interest from installment
		 * 
		 * @return loan interest
		 */
		double getInterest();

		/**
		 * Method to get tax from installment
		 * 
		 * @return loan tax
		 */
		double getTax();

		/**
		 * Method to get principal from installment
		 * 
		 * @return loan principal
		 */
		double getPrincipal();

		/**
		 * Method to get actual balance- amortisation of loan
		 * 
		 * @return actual balance
		 */
		double getActualBalance();

		/**
		 * Method to get id
		 * 
		 * @return id
		 */
		int getId();

		/**
		 * Method to set id
		 * 
		 * @param id
		 */
		void setId(int id);
	}

	/**
	 * Enum ScheduleType - AMORTISATION, TAX, MONTLY_PAYMENT
	 */
	enum ScheduleType {
		AMORTISATION, TAX, MONTLY_PAYMENT
	}
}
