package broforce42.cz.jalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class CompareFragment extends Fragment {

    private List<String> amountLoan = new ArrayList<>();
    private List<String> totalMonths = new ArrayList<>();
    private List<String> aprList = new ArrayList<>();
    private List<String> monthlyList = new ArrayList<>();
    private List<String> totalPaymantsList = new ArrayList<>();
    private List<String> totalInterestList = new ArrayList<>();
    private List<String> totalTaxesList = new ArrayList<>();
    private List<String> monthlyListX = new ArrayList<>();

    //View to find views
    private View rootView;

    //Vertical chart
    private BarChart barChart;

    //Scroll view to hide all
    private ScrollView scrollView;

    //Scroll view
    private TableLayout compareLayout;

    private LinearLayout linearLayout;

    //Button to change loans
    private FloatingActionButton changeCompare;

    //Dailogue to choose loans
    private Dialog dialog;
    private List<String> loansCheckBoxList = new ArrayList<>();
    private List<Integer> itemsSelected = new ArrayList<>();

    public CompareFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_compare, container, false);
        barChart = (BarChart) rootView.findViewById(R.id.barchart);
        compareLayout = (TableLayout) rootView.findViewById(R.id.compare_table);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_comp);
        changeCompare = (FloatingActionButton) rootView.findViewById(R.id.change_compare);
        bundleResult();
        if (amountLoan.size() > 1) {
            createChart(0, 1);
            compareTableFill(0, 1);
        } else {
            barChart.setVisibility(View.INVISIBLE);
            changeCompare.setVisibility(View.INVISIBLE);
            TextView text = new TextView(getActivity());
            text.setText(R.string.no_data);
            text.setGravity(Gravity.CENTER_HORIZONTAL);
            text.setTextSize(20);
            linearLayout.addView(text);
        }
        butoonChange();
        return rootView;
    }

    private void butoonChange() {
        changeCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogueMakerCahnege();
            }
        });
    }

    private void checkBox() {
        for (int x = 1; x <= amountLoan.size(); x++) {
            loansCheckBoxList.add(getString(R.string.loan_check) + x);
        }
    }

    private void dialogueMakerCahnege() {
        checkBox();
        String[] items = new String[loansCheckBoxList.size()];
        items = loansCheckBoxList.toArray(items);
        itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(R.string.select_loan);
        builder.setCancelable(false);
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton(R.string.change_loan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (itemsSelected.size() > 2 || itemsSelected.size() < 1) {
                            Toast.makeText(getContext(), R.string.set_two_loans, Toast.LENGTH_LONG).show();
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        } else {
                            compareLayout.removeAllViews();
                            createChart(itemsSelected.get(0), itemsSelected.get(1));
                            compareTableFill(itemsSelected.get(0), itemsSelected.get(1));
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        itemsSelected.clear();
                        loansCheckBoxList.clear();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    //Schedule intent to get form result activity
    private void bundleResult() {
        amountLoan = this.getArguments().getStringArrayList("AMOUNTBUNDLE");
        monthlyList = this.getArguments().getStringArrayList("MONTHLYPAYMANT");
        monthlyListX = this.getArguments().getStringArrayList("MONTHLYPAYMANTX");
        aprList = this.getArguments().getStringArrayList("APRBUNDLE");
        totalPaymantsList = this.getArguments().getStringArrayList("TOTALPAYMANTS");
        totalInterestList = this.getArguments().getStringArrayList("INTERESTBUNDE");
        totalTaxesList = this.getArguments().getStringArrayList("TAXESBUNDLE");
        totalMonths = this.getArguments().getStringArrayList("MONTHS");
    }

    //Create graph
    private void createChart(int x, int y) {
        float barWidth = 0.3f;
        float barSpace = 0.1f;
        float groupSpace = 0.2f;
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(true);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        int groupCount = 4;
        ArrayList xVal1 = new ArrayList();
        xVal1.add(getString(R.string.am));
        xVal1.add(getString(R.string.ti));
        xVal1.add(getString(R.string.tt));
        xVal1.add(getString(R.string.tp));

        ArrayList yVal1 = new ArrayList();
        ArrayList yVal2 = new ArrayList();
        yVal1.add(new BarEntry(1, Float.parseFloat(amountLoan.get(x))));
        yVal2.add(new BarEntry(1, Float.parseFloat(amountLoan.get(y))));
        yVal1.add(new BarEntry(2, Float.parseFloat(totalInterestList.get(x))));
        yVal2.add(new BarEntry(2, Float.parseFloat(totalInterestList.get(y))));
        yVal1.add(new BarEntry(3, Float.parseFloat(totalTaxesList.get(x))));
        yVal2.add(new BarEntry(3, Float.parseFloat(totalTaxesList.get(y))));
        yVal1.add(new BarEntry(4, Float.parseFloat(totalPaymantsList.get(x))));
        yVal2.add(new BarEntry(4, Float.parseFloat(totalPaymantsList.get(y))));

        BarDataSet set1, set2;
        set1 = new BarDataSet(yVal1, getString(R.string.loan1));
        set1.setColors(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        set1.setValueTextSize(12);
        set2 = new BarDataSet(yVal2, getString(R.string.loan2));
        set2.setColors(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        set2.setValueTextSize(12);
        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barChart.groupBars(0, groupSpace, barSpace);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(4);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVal1));

        barChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        barChart.animateXY(2000, 2000);
    }

    //table compare
    private void compareTableFill(int x, int y) {
        //Loan amount
        TableRow row = new TableRow(getActivity());
        TextView freePlace = new TextView(getActivity());
        row.addView(freePlace);
        TextView loan1 = new TextView(getActivity());
        loan1.setTypeface(null, Typeface.BOLD);
        loan1.setGravity(Gravity.CENTER);
        loan1.setText(getString(R.string.loan1));
        loan1.setPadding(20, 0, 0, 0);
        row.addView(loan1);
        TextView loan2 = new TextView(getActivity());
        loan2.setTypeface(null, Typeface.BOLD);
        loan2.setGravity(Gravity.CENTER);
        loan2.setPadding(45, 0, 0, 0);
        loan2.setText(getString(R.string.loan2));
        row.addView(loan2);

        TableRow rowA = new TableRow(getActivity());
        TextView loanAmount = new TextView(getActivity());
        loanAmount.setTypeface(null, Typeface.BOLD);
        loanAmount.setGravity(Gravity.LEFT);
        loanAmount.setText(R.string.loan_amount);
        loanAmount.setPadding(10, 0, 0, 0);
        rowA.addView(loanAmount);
        TextView loanAmountNumber1 = new TextView(getActivity());
        loanAmountNumber1.setGravity(Gravity.RIGHT);
        loanAmountNumber1.setPadding(20, 0, 0, 0);
        loanAmountNumber1.setText(decimalFormater(Long.valueOf(amountLoan.get(x))));
        rowA.addView(loanAmountNumber1);
        TextView loanAmountNumber2 = new TextView(getActivity());
        loanAmountNumber2.setGravity(Gravity.RIGHT);
        loanAmountNumber2.setPadding(45, 0, 0, 0);
        loanAmountNumber2.setText(decimalFormater(Long.valueOf(amountLoan.get(y))));
        rowA.addView(loanAmountNumber2);

        TableRow rowX = new TableRow(getActivity());
        TextView monthsX = new TextView(getActivity());
        monthsX.setTypeface(null, Typeface.BOLD);
        monthsX.setGravity(Gravity.LEFT);
        monthsX.setText(R.string.months);
        monthsX.setPadding(10, 0, 0, 0);
        rowX.addView(monthsX);
        TextView monthsXY = new TextView(getActivity());
        monthsXY.setGravity(Gravity.RIGHT);
        monthsXY.setText(totalMonths.get(x));
        rowX.addView(monthsXY);
        TextView monthsXYX = new TextView(getActivity());
        monthsXYX.setGravity(Gravity.RIGHT);
        monthsXYX.setText(totalMonths.get(y));
        rowX.addView(monthsXYX);

        TableRow rowB = new TableRow(getActivity());
        TextView monthlyAmount = new TextView(getActivity());
        monthlyAmount.setTypeface(null, Typeface.BOLD);
        monthlyAmount.setGravity(Gravity.LEFT);
        monthlyAmount.setText(R.string.montly_paymant_n);
        monthlyAmount.setPadding(10, 0, 0, 0);
        rowB.addView(monthlyAmount);
        TextView monthlyAmountNumber1 = new TextView(getActivity());
        monthlyAmountNumber1.setGravity(Gravity.RIGHT);
        monthlyAmountNumber1.setText(decimalFormaterX(Double.valueOf(monthlyList.get(x))));
        rowB.addView(monthlyAmountNumber1);
        TextView monthlyAmountNumber2 = new TextView(getActivity());
        monthlyAmountNumber2.setGravity(Gravity.RIGHT);
        monthlyAmountNumber2.setText(decimalFormaterX(Double.valueOf(monthlyList.get(y))));
        rowB.addView(monthlyAmountNumber2);

        TableRow rowC = new TableRow(getActivity());
        TextView monthlyAmountX = new TextView(getActivity());
        monthlyAmountX.setTypeface(null, Typeface.BOLD);
        monthlyAmountX.setGravity(Gravity.LEFT);
        monthlyAmountX.setText(R.string.montly_paymant_m);
        monthlyAmountX.setPadding(10, 0, 0, 0);
        rowC.addView(monthlyAmountX);
        TextView monthlyAmountNumber1X = new TextView(getActivity());
        monthlyAmountNumber1X.setGravity(Gravity.RIGHT);
        monthlyAmountNumber1X.setText(decimalFormaterX(Double.valueOf(monthlyListX.get(x))));
        rowC.addView(monthlyAmountNumber1X);
        TextView monthlyAmountNumber2X = new TextView(getActivity());
        monthlyAmountNumber2X.setGravity(Gravity.RIGHT);
        monthlyAmountNumber2X.setText(decimalFormaterX(Double.valueOf(monthlyListX.get(y))));
        rowC.addView(monthlyAmountNumber2X);

        TableRow rowD = new TableRow(getActivity());
        TextView anualPR = new TextView(getActivity());
        anualPR.setTypeface(null, Typeface.BOLD);
        anualPR.setGravity(Gravity.LEFT);
        anualPR.setText(R.string.apr_c);
        anualPR.setPadding(10, 0, 0, 0);
        rowD.addView(anualPR);
        TextView anualNumber1 = new TextView(getActivity());
        anualNumber1.setGravity(Gravity.RIGHT);
        anualNumber1.setText(aprList.get(x) + "%");
        rowD.addView(anualNumber1);
        TextView anualNumber2 = new TextView(getActivity());
        anualNumber2.setGravity(Gravity.RIGHT);
        anualNumber2.setText(aprList.get(y) + "%");
        rowD.addView(anualNumber2);

        TableRow rowE = new TableRow(getActivity());
        TextView totalTaxesN = new TextView(getActivity());
        totalTaxesN.setTypeface(null, Typeface.BOLD);
        totalTaxesN.setGravity(Gravity.LEFT);
        totalTaxesN.setText(R.string.taxes_total);
        totalTaxesN.setPadding(10, 0, 0, 0);
        rowE.addView(totalTaxesN);
        TextView totalTaxesNumber1 = new TextView(getActivity());
        totalTaxesNumber1.setGravity(Gravity.RIGHT);
        totalTaxesNumber1.setText(decimalFormaterX(Double.valueOf(totalTaxesList.get(x))));
        rowE.addView(totalTaxesNumber1);
        TextView totalTaxesNumber2 = new TextView(getActivity());
        totalTaxesNumber2.setGravity(Gravity.RIGHT);
        totalTaxesNumber2.setText(decimalFormaterX(Double.valueOf(totalTaxesList.get(y))));
        rowE.addView(totalTaxesNumber2);

        TableRow rowF = new TableRow(getActivity());
        TextView interestT = new TextView(getActivity());
        interestT.setTypeface(null, Typeface.BOLD);
        interestT.setGravity(Gravity.LEFT);
        interestT.setText(R.string.total_interests_n);
        interestT.setPadding(10, 0, 0, 0);
        rowF.addView(interestT);
        TextView interestTNumber1 = new TextView(getActivity());
        interestTNumber1.setGravity(Gravity.RIGHT);
        interestTNumber1.setText(decimalFormaterX(Double.valueOf(totalInterestList.get(x))));
        rowF.addView(interestTNumber1);
        TextView interestTNumber2 = new TextView(getActivity());
        interestTNumber2.setGravity(Gravity.RIGHT);
        interestTNumber2.setText(decimalFormaterX(Double.valueOf(totalInterestList.get(y))));
        rowF.addView(interestTNumber2);

        TableRow rowG = new TableRow(getActivity());
        TextView totalN = new TextView(getActivity());
        totalN.setTypeface(null, Typeface.BOLD);
        totalN.setGravity(Gravity.LEFT);
        totalN.setText(R.string.total_p);
        totalN.setPadding(10, 0, 0, 0);
        rowG.addView(totalN);
        TextView totalNumber1 = new TextView(getActivity());
        totalNumber1.setGravity(Gravity.RIGHT);
        totalNumber1.setText(decimalFormaterX(Double.valueOf(totalPaymantsList.get(x))));
        rowG.addView(totalNumber1);
        TextView totalNumber2 = new TextView(getActivity());
        totalNumber2.setGravity(Gravity.RIGHT);
        totalNumber2.setText(decimalFormaterX(Double.valueOf(totalPaymantsList.get(y))));
        rowG.addView(totalNumber2);

        compareLayout.addView(row);
        compareLayout.addView(rowA);
        compareLayout.addView(rowX);
        compareLayout.addView(rowB);
        compareLayout.addView(rowC);
        compareLayout.addView(rowD);
        compareLayout.addView(rowE);
        compareLayout.addView(rowF);
        compareLayout.addView(rowG);
        compareLayout.setGravity(Gravity.CENTER);
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
