package cz.broforce42.model.impl;

import cz.broforce42.calculator.LoanCalculator;
import cz.broforce42.calculator.apr.DefaultAprCalculator;
import cz.broforce42.calculator.apr.IAprCalculator;
import cz.broforce42.model.ILoan;
import cz.broforce42.model.ISchedule;
import cz.broforce42.model.ITaxes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by vekera on 28/11/2016.
 */
public class LoanTest {

	private ILoan loan;
	private IAprCalculator calculator;
	
	@Before
	public void setUp() throws Exception {
		this.loan = new Loan("1",500000, 3.58, 72);
		this.loan.setAnnualPercentageRate(3.64);
		ISchedule schedule = new Schedule(7825);
		schedule.addInstallment(new Schedule.Installment(0, 0, 500));
		schedule.addInstallment(new Schedule.Installment(646.39, 0));
		schedule.addInstallment(new Schedule.Installment(1491.67, 6333.33));
		this.loan.setSchedule(schedule);
		this.calculator = new DefaultAprCalculator();
	}

	@After
	public void tearDown() throws Exception {
		this.loan = null;
	}

	@Test
	public void getAmount() throws Exception {
		assertEquals(500000, loan.getAmount(), 0.0);
	}

	@Test
	public void getInterestRate() throws Exception {
		assertEquals(0.0358D, loan.getInterestRate(), 0.0);
	}

	@Test
	public void getNumberOfPayments() throws Exception {
		assertEquals(72, loan.getNumberOfPayments());
	}

	@Test
	public void getAnnualPercentageRate() throws Exception {
		
		assertEquals(3.64D, loan.getAnnualPercentageRate(), 0.0);
	}

	@Test
	public void getMonthlyPayment() throws Exception {
		this.loan = new Loan("1",1000000, 2.3, 72);
		LoanCalculator calculator = new LoanCalculator();
		calculator.generateSchedule(loan);
		assertEquals(14882.5418, loan.getMonthlyPayment(),0.0);
	}

	@Test
	public void getTotalPayments() throws Exception {
		assertEquals(8971.39, this.loan.getTotalPayments(), 0.0);
	}

	@Test
	public void getTotalTaxes() throws Exception {
		assertEquals(500, this.loan.getTotalTaxes(), 0.0);
	}

	@Test
	public void getTotalInterests() throws Exception {
		LoanCalculator claculator = new LoanCalculator();
		this.loan = new Loan("1",1000000, 2.4, 72);
		claculator.generateSchedule(this.loan);
		ITaxes taxes = new Taxes();
		taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 25));
		taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 50));
		this.loan.setTaxes(taxes);
		assertEquals(74725, Math.round(this.loan.getTotalInterests()), 0.0);

	}

	@Test
	public void getSchedule() throws Exception {
		ILoan loan = new Loan("1",500000, 3.58, 72);
		loan.setAnnualPercentageRate(3.64);
		assertNull(loan.getSchedule());

		ISchedule schedule = new Schedule(7825);
		loan.setSchedule(schedule);
		assertNotNull(loan.getSchedule());
	}

	@Test
	public void setSchedule() throws Exception {
		ISchedule schedule = new Schedule(7825);
		this.loan.setSchedule(schedule);
		assertNotNull(this.loan.getSchedule());
	}

	@Test
	public void getTaxes() throws Exception {
		ITaxes taxes = new Taxes();
		taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 25));
		this.loan.setTaxes(taxes);
		assertNotNull(this.loan.getTaxes());
	}

	@Test
	public void setTaxes() throws Exception {
		this.loan.setTaxes(new Taxes());
		assertNotNull(this.loan.getTaxes());
	}

	@Test
	public void compareTo() throws Exception {
		// TODO
	}

}