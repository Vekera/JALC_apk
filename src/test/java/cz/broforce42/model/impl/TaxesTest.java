package cz.broforce42.model.impl;

import cz.broforce42.model.ITaxes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by vekera on 28/11/2016.
 */
public class TaxesTest {

	private ITaxes taxes;

	@Before
	public void setUp() throws Exception {
		this.taxes = new Taxes();
		taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 25));
		taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 150));
		taxes.addTax(
				new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.BEFORE_LOAN, 2500));
		taxes.addTax(
				new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.BEFORE_LOAN, 500));
	}

	@After
	public void tearDown() throws Exception {
		this.taxes = null;
	}

	@Test
	public void getTaxesByPeriod() throws Exception {
		List<ITaxes.Entry> list = taxes.getTaxesByPeriod(ITaxes.TaxPeriod.BEFORE_LOAN);
		assertEquals(2, list.size());
		assertEquals(2500, list.get(0).getAmount(), 0.0);
		assertEquals(ITaxes.TaxType.TAX, list.get(0).getType());
		assertEquals(ITaxes.TaxValueType.AMOUNT, list.get(0).getValueType());

		assertEquals(500, list.get(1).getAmount(), 0.0);
		assertEquals(ITaxes.TaxType.TAX, list.get(1).getType());
		assertEquals(ITaxes.TaxValueType.AMOUNT, list.get(1).getValueType());

		list = taxes.getTaxesByPeriod(ITaxes.TaxPeriod.MONTHLY);
		assertEquals(150, list.get(1).getAmount(), 0.0);
		assertEquals(ITaxes.TaxType.TAX, list.get(1).getType());
		assertEquals(ITaxes.TaxValueType.AMOUNT, list.get(1).getValueType());
	}

	@Test
	public void addTax() throws Exception {
		ITaxes taxes = new Taxes();
		taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 25));
		List<ITaxes.Entry> list = taxes.getTaxesByPeriod(ITaxes.TaxPeriod.MONTHLY);
		assertEquals(1, list.size());
		list = taxes.getTaxesByPeriod(ITaxes.TaxPeriod.BEFORE_LOAN);
		assertEquals(0, list.size());
	}

}