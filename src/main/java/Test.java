import cz.broforce42.calculator.LoanCalculator;
import cz.broforce42.calculator.apr.DefaultAprCalculator;
import cz.broforce42.calculator.apr.IAprCalculator;
import cz.broforce42.model.ISchedule;
import cz.broforce42.model.ITaxes;
import cz.broforce42.model.impl.Loan;
import cz.broforce42.model.impl.Schedule;
import cz.broforce42.model.impl.Taxes;

public class Test {

	public static void main(String[] args) {
		Loan loan = new Loan("1", 23432345, 4.5, 360);
		ITaxes taxes = new Taxes();
		
        taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.BEFORE_LOAN, 20000));
        taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, 70000));

        loan.setTaxes(taxes);
		LoanCalculator calculator = new LoanCalculator();
		calculator.generateSchedule(loan);
		
		IAprCalculator apr = new DefaultAprCalculator();
		loan.setAnnualPercentageRate(apr.getAPR(loan));
		System.out.println(loan.getAnnualPercentageRate());
		
		System.out.println(apr.getCountNumber());

	}

}
