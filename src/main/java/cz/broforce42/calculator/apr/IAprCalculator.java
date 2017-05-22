package cz.broforce42.calculator.apr;

import cz.broforce42.model.ILoan;

/**
 * Created by Ranajkus on 28/11/2016.
 */
public interface IAprCalculator {
	/**
	 * Method to get annual percentage rate
	 *
	 * @param loan
	 *
	 * @return calculated APR
	 */
	double getAPR(ILoan loan);

	/**
	 * Method to get interest rate
	 * 
	 * @param loan
	 *
	 * @return calculated APR
	 */
	double getInterest(ILoan loan);
	
	/**
	 * Method to get count number
	 * 
	 *  */
	int getCountNumber();
	
}
