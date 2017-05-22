package cz.broforce42.model;

import java.util.List;

import org.apache.commons.collections4.Predicate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cz.broforce42.model.impl.Taxes;

/**
 * Created by vekera on 25/12/16.
 * 
 */
@JsonDeserialize(as = Taxes.class)
public interface ITaxes {

	/**
	 * Return list of taxes by period.
	 *
	 * @return list of taxes.
	 */
	List<Entry> getTaxesByPeriod(TaxPeriod period);

	/**
	 * Add tax entry.
	 *
	 * @param tax
	 *            entry {@link ITaxes.Entry}
	 */
	void addTax(ITaxes.Entry tax);

	/**
	 * Get tax list.
	 *
	 * @return tax taxes list
	 * 
	 */
	public List<ITaxes.Entry> getTaxes();

	public void setTaxes(List<ITaxes.Entry> taxes);
	
	/**
	 * Tax entry
	 */


	@JsonDeserialize(as = Taxes.Entry.class)
	interface Entry extends Predicate<Object> {

		/**
		 * User set bank taxes, amount.
		 *
		 * @param amount
		 * @set tax amount
		 */

		void setAmount(double amount);

		/**
		 * Return tax amount.
		 *
		 * @return tax amount
		 */

		double getAmount();

		/**
		 * Return tax type. E.g. TAX - annual bank tax, fees. PENALTY - special
		 * tax if you repay load before loan anniversary.
		 *
		 * @return tax type {@link TaxType}
		 */
		TaxType getType();

		/**
		 * Return tax value type. E.g. AMOUNT, PERCENT_BALANCE, PERCENT_PAYMENT
		 *
		 * @return value type {@link TaxValueType}
		 */
		TaxValueType getValueType();

		/**
		 * Taxes time period. E.g. ANNUAL - monthly tax, One time - tax at the
		 * beginning ...
		 *
		 * @return tax time period {@link TaxPeriod}
		 */
		TaxPeriod getPeriod();

		/**
		 * User set taxes type.
		 *
		 * @param type
		 * @set tax type
		 */
		public void setType(TaxType type);

		/**
		 * User set taxes valueType.
		 *
		 * @param valueType
		 * @set tax valueType
		 */
		public void setValueType(TaxValueType valueType);

		/**
		 * User set taxes period.
		 *
		 * @param period
		 * @set tax period
		 */
		public void setPeriod(TaxPeriod period);
	}

	/**
	 * Enum TaxType - TAX, PENALTY, RETURN to use in type of tax
	 */

	enum TaxType {
		TAX, PENALTY, RETURN

	}

	/**
	 * Enum TaxValueType - AMOUNT, PERCENT_BALANCE, PERCENT_PAYMENT to use in
	 * value type in tax
	 */
	enum TaxValueType {
		AMOUNT, PERCENT_OF_MONTHLY_PAYMENT, PERCENT_OF_FULL_LOAN
	}

	/**
	 * Enum TaxPeriod - ANNUAL, MONTHLY, ONETIME to use in value type in tax
	 */
	enum TaxPeriod {
		ANNUAL, MONTHLY, ONETIME, BEFORE_LOAN, AFTER_LOAN
	}

}
