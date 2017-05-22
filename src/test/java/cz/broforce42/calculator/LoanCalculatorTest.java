package cz.broforce42.calculator;

import cz.broforce42.model.ILoan;
import cz.broforce42.model.ISchedule;
import cz.broforce42.model.ITaxes.TaxPeriod;
import cz.broforce42.model.impl.Loan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Ranajkus on 28/11/2016.
 */
public class LoanCalculatorTest {

    private ILoan loan;

    private LoanCalculator calculator;

    @Before
    public void setUp() throws Exception {
        this.loan = new Loan("1",500000, 3.58, 72);
        this.loan.setAnnualPercentageRate(3.64);

        this.calculator = new LoanCalculator();
    }

    @After
    public void tearDown() throws Exception {
        this.loan = null;
    }

    @Test
    public void calculateMonthlyPayment() throws Exception {
        double result = calculator.calculateMonthlyPayment(this.loan);
        assertEquals(7727.2721, result, 0.0);
        assertEquals(7727.2721, this.loan.getMonthlyPayment(), 0.0);
    }

    @Test
    public void specificTaxesSum() throws Exception {
        double result = calculator.specificTaxesSum(this.loan,TaxPeriod.MONTHLY);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void generateSchedule() throws Exception {
        ISchedule result = calculator.generateSchedule(this.loan);
        assertNotNull(result);
       assertEquals(556363.5912, result.getTotalPayments(), 0.0);
        assertEquals(7727.2721, result.getMonthlyPayment(), 0.0);
        assertNotNull(loan.getSchedule());
        assertEquals(556363.5912, loan.getSchedule().getTotalPayments(), 0.0);
        assertEquals(7727.2721, loan.getSchedule().getMonthlyPayment(), 0.0);
    }
}