package cz.broforce42.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import cz.broforce42.model.IFees;

/**
 * Created by Vekera on 25/12/16. Fees class to extending the loan calculator
 */
public class Fees implements IFees {

	// Fees collection, need to be filled by addFee.
	private List<IFees.Entry> fees;

	/**
	 * Main constructor.
	 */
	public Fees() {
		this.fees = new ArrayList<IFees.Entry>();
	}

	/**
	 * See @see IFees#getAmount()
	 */
	public double getAmount() {
		double result = 0.0;
		for (IFees.Entry entry : this.fees) {
			result += entry.getAmount();
		}
		return result;
	}

	/**
	 * See @see IFees#getAmountByType
	 */
	public double getAmountByType(FeeType type) {
		double result = 0.0;
		List<IFees.Entry> fees = (List<IFees.Entry>) CollectionUtils.select(this.fees, new Entry(type));
		for (IFees.Entry entry : fees) {
			result += entry.getAmount();
		}
		return result;
	}

	/**
	 * See @see IFees#addFee
	 */
	public void addFee(IFees.Entry fee) {
		this.fees.add(fee);
	}

	/**
	 * See @see IFees.Entry
	 */
	public class Entry implements IFees.Entry {

		// Fee type - Electricity, Gas, House_Fees, Water, Garbage
		private FeeType type;

		// Number of amount- fee
		private double amount;

		/**
		 * Constructor for filter.
		 * 
		 * @param type
		 */

		public Entry(FeeType type) {
			this.type = type;
		}

		/**
		 * Main constructor.
		 *
		 * @param type
		 *            {@link cz.broforce42.model.IFees.FeeType}
		 * @param amount
		 *
		 */
		public Entry(FeeType type, double amount) {
			this.type = type;
			this.amount = amount;
		}

		/**
		 * See @see #getAmoun()
		 */
		public double getAmount() {
			return this.amount;
		}

		/**
		 * See @see #getType()
		 */
		public FeeType getType() {
			return this.type;
		}

		/**
		 * See @see #setAmoun()
		 */
		public void setAmount(double amount) {
			this.amount = amount;
		}

		/**
		 * See @see org.apache.commons.collections4.Predicate#evaluate
		 *
		 * Use Fee type.
		 */
		public boolean evaluate(Object o) {
			return ((IFees.Entry) o).getType() == this.getType();
		}
	}

}
