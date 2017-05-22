package cz.broforce42.model.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import cz.broforce42.model.ITaxes;

/**
 * Created by vekera on 25/12/16.
 */

public class Taxes implements ITaxes {

	// Taxes collection, should be filled by addTax
	private List<ITaxes.Entry> tax;

	/**
	 * Main constructor.
	 */
	public Taxes() {
		this.tax = new ArrayList<ITaxes.Entry>();
	}

	/**
	 * See @see ITaxes#getTaxesByPeriod()
	 */

	public List<ITaxes.Entry> getTaxesByPeriod(TaxPeriod period) {
		return (List<ITaxes.Entry>) CollectionUtils.select(this.tax, new Entry(period));
	}

	/**
	 * See @see ITaxes#addTax()
	 */
	public void addTax(ITaxes.Entry tax) {
		this.tax.add(tax);
	}

	/**
	 * See @see ITaxes#getTaxes()
	 */
	public List<ITaxes.Entry> getTaxes() {
		return this.tax;
	}

	/**
	 * See @see cz.broforce42.model.ITaxes.Entry
	 */
	public static class Entry implements ITaxes.Entry {

		// TaxType - TAX, PENALTY, RETURN
		@NotNull(message = "error.taxType")
		private TaxType type;

		// TaxValueType- AMOUNT, PERCENT_BALANCE, PERCENT_PAYMENT
		@NotNull(message = "error.valueType")
		private TaxValueType valueType;

		// TaxPeriod - ANNUAL, MONTHLY, ONETIME
		@NotNull(message = "error.periodType")
		private TaxPeriod period;

		// Expected amount of tax
		@DecimalMin(value = "0.0", message = "error.taxAmount")
		private double amount;

		/**
		 * Constructor for filter.
		 * 
		 * @param period
		 */
		public Entry(TaxPeriod period) {
			this.period = period;
		}

		/**
		 * Main constructor.
		 *
		 * @param type
		 *            {@link ITaxes.TaxType}
		 * @param valueType
		 *            {@link ITaxes.TaxValueType}
		 * @param period
		 *            {@link ITaxes.TaxPeriod}
		 * @param amount
		 */

		@JsonCreator
		public Entry(@JsonProperty("type") TaxType type, @JsonProperty("valueType") TaxValueType valueType,
				@JsonProperty("period") TaxPeriod period, @JsonProperty("amount") double amount) {
			this.type = type;
			this.valueType = valueType;
			this.period = period;
			// amount set, if value type is in percentage then divide 100
			if (valueType != TaxValueType.AMOUNT) {
				this.amount = amount / 100;
			} else {
				this.amount = amount;
			}
		}

		/**
		 * See @see #getType()
		 */
		public final TaxType getType() {
			return this.type;
		}

		/**
		 * See @see #getValueType()
		 */
		public final TaxValueType getValueType() {
			return this.valueType;
		}

		/**
		 * See @see #getPeriod()
		 */
		public final TaxPeriod getPeriod() {
			return this.period;
		}

		/**
		 * See @see #getAmount()
		 */
		public double getAmount() {
			return this.amount;
		}

		/**
		 * See @see #setAmount()
		 */
		public void setAmount(double amount) {
			this.amount = amount;
		}

		/**
		 * See @see org.apache.commons.collections4.Predicate#evaluate
		 * 
		 * Use Tax period.
		 */
		public boolean evaluate(Object o) {
			return ((ITaxes.Entry) o).getPeriod() == this.getPeriod();

		}

		/**
		 * See @see #setType(TaxType type)
		 */
		public void setType(TaxType type) {
			this.type = type;
		}

		/**
		 * See @see #setValueType(TaxValueType valueType)
		 */
		public void setValueType(TaxValueType valueType) {
			this.valueType = valueType;
		}

		/**
		 * See @see #setValueType(TaxPeriod period)
		 */
		public void setPeriod(TaxPeriod period) {
			this.period = period;
		}
	}

	public void setTaxes(List<ITaxes.Entry> taxes) {
		this.tax = taxes;
	}

}