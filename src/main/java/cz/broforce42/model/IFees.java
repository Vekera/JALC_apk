package cz.broforce42.model;

import org.apache.commons.collections4.Predicate;

/**
 * Created by Vekera on 25/12/16. IFees interface to extending the loan
 * calculator
 */
public interface IFees {

	/**
	 * Return total fees amount.
	 *
	 * @return total fee amount
	 */
	double getAmount();

	/**
	 * Return total fees amount by type.
	 *
	 * @return fee amount
	 */
	double getAmountByType(FeeType type);

	/**
	 * Add fee entry.
	 *
	 * @param fee
	 *            entry {@link IFees.Entry}
	 */
	void addFee(IFees.Entry fee);

	/**
	 * Fee entry
	 */
	interface Entry extends Predicate<Object> {

		/**
		 * User set fees for housing
		 *
		 * @set fees amount
		 */
		void setAmount(double amount);

		/**
		 * Return fee amount.
		 *
		 * @return fee amount
		 */
		double getAmount();

		/**
		 * Return fee type- electricity, gas, house fees , water, garbage
		 *
		 * @return fee type {@link FeeType}
		 */
		FeeType getType();
	}

	/**
	 * Enum FeeType - Electricity, Gas, House_Fees, Water, Garbage TODO tu sa tu
	 * musi este domysliet. Asi primrnym zdrojom nakladou na uver je TAX. Toto
	 * by malo sluzit len ako doplnok v buducnosti, ked do vypoctu bude mozne
	 * pridat dalsie a dalsie poplatky.
	 */
	enum FeeType {
		ELECTRI_CITY, GAS, HOUSE_FEES, WATER, GARBAGE
	}

}
