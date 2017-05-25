package broforce42.cz.jalc;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class ResultFragment extends Fragment {

    private String amount;
    private String apr;
    private String monthlyPaymant;
    private String monthlyPaymantX;
    private String totalPaymants;
    private String totalInterest;
    private String totalTaxes;
    private String months;

    private List<String> amountLoan = new ArrayList<>();
    private List<String> aprList = new ArrayList<>();
    private List<String> totalMonths = new ArrayList<>();
    private List<String> monthlyList = new ArrayList<>();
    private List<String> monthlyListX = new ArrayList<>();
    private List<String> totalPaymantsList = new ArrayList<>();
    private List<String> totalInterestList = new ArrayList<>();
    private List<String> totalTaxesList = new ArrayList<>();

    private int x;

    //View to find views
    private View rootView;

    //PieChart view
    private PieChart chart;

    //Result table view

    private TableLayout resultLayout;

    public ResultFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_result, container, false);
        chart = (PieChart) rootView.findViewById(R.id.result_chart);
        resultLayout = (TableLayout) rootView.findViewById(R.id.result_table);
        bundleResult();
        setupPieChart();
        reultTableFill();
        return rootView;
    }

    //Schedule intent to get form result activity
    private void bundleResult() {
        x = this.getArguments().getInt("ID");
        amountLoan = this.getArguments().getStringArrayList("AMOUNTBUNDLE");
        monthlyList = this.getArguments().getStringArrayList("MONTHLYPAYMANT");
        monthlyListX = this.getArguments().getStringArrayList("MONTHLYPAYMANTX");
        aprList = this.getArguments().getStringArrayList("APRBUNDLE");
        totalPaymantsList = this.getArguments().getStringArrayList("TOTALPAYMANTS");
        totalInterestList = this.getArguments().getStringArrayList("INTERESTBUNDE");
        totalTaxesList = this.getArguments().getStringArrayList("TAXESBUNDLE");
        totalMonths = this.getArguments().getStringArrayList("MONTHS");
    }

    private void setResults(int y) {
        amount = amountLoan.get(y);
        monthlyPaymant = monthlyList.get(y);
        monthlyPaymantX = monthlyListX.get(y);
        apr = aprList.get(y);
        totalPaymants = totalPaymantsList.get(y);
        totalInterest = totalInterestList.get(y);
        totalTaxes = totalTaxesList.get(y);
        months = totalMonths.get(y);
    }

    //Method tu setup chart
    private void setupPieChart() {
        setResults(x);
        final int[] MY_COLORS = {Color.rgb(251, 189, 8), Color.rgb(115, 115, 115), Color.rgb(158, 157, 36)};
        List<PieEntry> pieEntries = new ArrayList<>();
        PieEntry loanEntry = new PieEntry(Float.valueOf(amount), getString(R.string.principal));
        PieEntry interestEntry = new PieEntry(Float.valueOf(totalInterest), getString(R.string.interest));
        pieEntries.add(loanEntry);
        pieEntries.add(interestEntry);
        if (Float.valueOf(totalTaxes) != 0.0) {
            PieEntry taxEntry = new PieEntry(Float.valueOf(totalTaxes), getString(R.string.taxes));
            pieEntries.add(taxEntry);
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(MY_COLORS);
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12);
        dataSet.setAutomaticallyDisableSliceSpacing(false);
        PieData data = new PieData(dataSet);
        chart.setCenterText("Loan");
        chart.setCenterTextSize(20);
        chart.setDescription(null);
        chart.getLegend().setFormSize(20);
        chart.setDrawSliceText(false);
        chart.setUsePercentValues(true);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

    //table result
    private void reultTableFill() {
        //Loan amount
        TableRow rowA = new TableRow(getActivity());
        TextView loanAmount = new TextView(getActivity());
        loanAmount.setTypeface(null, Typeface.BOLD);
        loanAmount.setGravity(Gravity.LEFT);
        loanAmount.setText(R.string.loan_amount);
        rowA.addView(loanAmount);
        TextView loanAmountNumber = new TextView(getActivity());
        loanAmountNumber.setGravity(Gravity.RIGHT);
        loanAmountNumber.setPadding(10, 0, 0, 0);
        loanAmountNumber.setText(decimalFormater(Long.valueOf(amount)));
        rowA.addView(loanAmountNumber);

        TableRow rowX = new TableRow(getActivity());
        TextView monthsX = new TextView(getActivity());
        monthsX.setTypeface(null, Typeface.BOLD);
        monthsX.setGravity(Gravity.LEFT);
        monthsX.setText(R.string.months);
        rowX.addView(monthsX);
        TextView totalMonths = new TextView(getActivity());
        totalMonths.setGravity(Gravity.RIGHT);
        totalMonths.setPadding(10, 0, 0, 0);
        totalMonths.setText(months);
        rowX.addView(totalMonths);

        TableRow rowB = new TableRow(getActivity());
        TextView monthlyAmount = new TextView(getActivity());
        monthlyAmount.setTypeface(null, Typeface.BOLD);
        monthlyAmount.setGravity(Gravity.LEFT);
        monthlyAmount.setText(R.string.montly_paymant_n);
        rowB.addView(monthlyAmount);
        TextView monthlyAmountNumber = new TextView(getActivity());
        monthlyAmountNumber.setGravity(Gravity.RIGHT);
        monthlyAmountNumber.setPadding(10, 0, 0, 0);
        monthlyAmountNumber.setText(decimalFormaterX(Double.valueOf(monthlyPaymant)));
        rowB.addView(monthlyAmountNumber);

        TableRow rowC = new TableRow(getActivity());
        TextView monthlyAmountX = new TextView(getActivity());
        monthlyAmountX.setTypeface(null, Typeface.BOLD);
        monthlyAmountX.setGravity(Gravity.LEFT);
        monthlyAmountX.setText(R.string.montly_paymant_m);
        rowC.addView(monthlyAmountX);
        TextView monthlyAmountNumberX = new TextView(getActivity());
        monthlyAmountNumberX.setGravity(Gravity.RIGHT);
        monthlyAmountNumberX.setPadding(10, 0, 0, 0);
        monthlyAmountNumberX.setText(decimalFormaterX(Double.valueOf(monthlyPaymantX)));
        rowC.addView(monthlyAmountNumberX);

        TableRow rowD = new TableRow(getActivity());
        TextView anualPR = new TextView(getActivity());
        anualPR.setTypeface(null, Typeface.BOLD);
        anualPR.setGravity(Gravity.LEFT);
        anualPR.setText(R.string.anual_percentage_r);
        rowD.addView(anualPR);
        TextView anualNumber = new TextView(getActivity());
        anualNumber.setGravity(Gravity.RIGHT);
        anualNumber.setPadding(10, 0, 0, 0);
        anualNumber.setText(apr + " %");
        rowD.addView(anualNumber);

        TableRow rowE = new TableRow(getActivity());
        TextView totalTaxesN = new TextView(getActivity());
        totalTaxesN.setTypeface(null, Typeface.BOLD);
        totalTaxesN.setGravity(Gravity.LEFT);
        totalTaxesN.setText(R.string.taxes_total);
        rowE.addView(totalTaxesN);
        TextView totalTaxesNumber = new TextView(getActivity());
        totalTaxesNumber.setGravity(Gravity.RIGHT);
        totalTaxesNumber.setPadding(10, 0, 0, 0);
        totalTaxesNumber.setText(decimalFormaterX(Double.valueOf(totalTaxes)));
        rowE.addView(totalTaxesNumber);

        TableRow rowF = new TableRow(getActivity());
        TextView interestT = new TextView(getActivity());
        interestT.setTypeface(null, Typeface.BOLD);
        interestT.setGravity(Gravity.LEFT);
        interestT.setText(R.string.total_interests_n);
        rowF.addView(interestT);
        TextView interestTNumber = new TextView(getActivity());
        interestTNumber.setGravity(Gravity.RIGHT);
        interestTNumber.setPadding(10, 0, 0, 0);
        interestTNumber.setText(decimalFormaterX(Double.valueOf(totalInterest)));
        rowF.addView(interestTNumber);

        TableRow rowG = new TableRow(getActivity());
        TextView totalN = new TextView(getActivity());
        totalN.setTypeface(null, Typeface.BOLD);
        totalN.setGravity(Gravity.LEFT);
        totalN.setText(R.string.total_p);
        rowG.addView(totalN);
        TextView totalNumber = new TextView(getActivity());
        totalNumber.setGravity(Gravity.RIGHT);
        totalNumber.setPadding(10, 0, 0, 0);
        totalNumber.setText(decimalFormaterX(Double.valueOf(totalPaymants)));
        rowG.addView(totalNumber);

        resultLayout.addView(rowA);
        resultLayout.addView(rowX);
        resultLayout.addView(rowB);
        resultLayout.addView(rowC);
        resultLayout.addView(rowD);
        resultLayout.addView(rowE);
        resultLayout.addView(rowF);
        resultLayout.addView(rowG);
    }

    private String decimalFormater(long val) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
        formatter.applyPattern("##,###,###");
        String formattedString = formatter.format(val);
        return formattedString;
    }

    private String decimalFormaterX(double val) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
        formatter.applyPattern("###,###,###");
        String formattedString = formatter.format(val);
        return formattedString;
    }
}
