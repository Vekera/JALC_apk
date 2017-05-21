package broforce42.cz.jalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.broforce42.calculator.LoanCalculator;
import cz.broforce42.calculator.apr.DefaultAprCalculator;
import cz.broforce42.model.ISchedule;
import cz.broforce42.model.ITaxes;
import cz.broforce42.model.impl.Loan;
import cz.broforce42.model.impl.Taxes;

public class ResultActivity extends AppCompatActivity {

    //Variabiles intent
    private static List<String> amountData = new ArrayList<>();
    private static List<String> interestData = new ArrayList<>();
    private static List<String> numberMonthsData = new ArrayList<>();
    private static List<String> beforeTax = new ArrayList<>();
    private static List<String> afterTax = new ArrayList<>();
    private static List<String> monthlyTax = new ArrayList<>();
    private static List<String> yearTax = new ArrayList<>();

    //controllLoan
    private static int x = 0;

    //Variabile result
    private static List<String> amountLoan = new ArrayList<>();
    private static List<String> apr = new ArrayList<>();
    private static List<String> monthlyPaymant = new ArrayList<>();
    private static List<String> totalPaymants = new ArrayList<>();
    private static List<String> totalInterest = new ArrayList<>();
    private static List<String> totalTaxes = new ArrayList<>();

    //Variabiles schedule
    private List<String> id = new ArrayList<>();
    private List<String> interestLoan = new ArrayList<>();
    private List<String> principal = new ArrayList<>();
    private List<String> actualBalance = new ArrayList<>();


    //Library- loan calculator
    private Loan loan;
    private LoanCalculator calcultator;
    private DefaultAprCalculator aprCalculator;
    private ITaxes taxes;

    //Toolbar - action bar
    private Toolbar toolbar;


    //File
    private File myFile;

    //To send data
    private Bundle bundle;

    //Dailogue to choose loans
    private Dialog dialog;
    private List<String> loansCheckBoxList = new ArrayList<>();
    private List<Integer> itemsSelected = new ArrayList<>();

    private ResultActivity.PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        getIntentLoan();
        calculateLoan(x);
        initToolBar();
        tabLayout();
        setVariabiles(x);
        scheduleGenerator();

    }

    public void initToolBar() {
        toolbar.setTitle("JALC");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.result_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menu_pdf:
                dialogueMakerPDF();
                return true;
            case R.id.menu_email:
                dialogueMakerEmail();
                return true;
            case R.id.menu_change:
                if (amountData.size() > 1) {
                    dialogueMakerCahnege();
                } else {
                    Toast.makeText(getApplicationContext(), "Set only one loan", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.result_tab);
        tabLayout.addTab(tabLayout.newTab().setText("Result"));
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Compare"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ResultActivity.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private final class PagerAdapter extends FragmentStatePagerAdapter {
        int nNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.nNumOfTabs = NumOfTabs;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ResultFragment resultFragment = new ResultFragment();
                    sendDataResultCompare();
                    resultFragment.setArguments(bundle);
                    return resultFragment;
                case 1:
                    ScheduleFragment scheduleFragment = new ScheduleFragment();
                    sendDataSchedule();
                    scheduleFragment.setArguments(bundle);
                    return scheduleFragment;
                case 2:
                    CompareFragment compareFragment = new CompareFragment();
                    sendDataResultCompare();
                    compareFragment.setArguments(bundle);
                    return compareFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return nNumOfTabs;
        }
    }

    private void getIntentLoan() {
        Intent intent = getIntent();
        amountData= intent.getStringArrayListExtra("AMOUNT");
        interestData= intent.getStringArrayListExtra("INTEREST");
        numberMonthsData= intent.getStringArrayListExtra("MONTHS");
        beforeTax= intent.getStringArrayListExtra("BEFORET");
        monthlyTax= intent.getStringArrayListExtra("MONTHLYT");
        yearTax= intent.getStringArrayListExtra("YEART");
        afterTax= intent.getStringArrayListExtra("AFTERT");
        x = intent.getIntExtra("LOANID", 0);
    }

    private void calculateLoan(int z) {

        long amount = Long.parseLong(amountData.get(z));
        double interest = Double.parseDouble(interestData.get(z));
        int months = Integer.parseInt(numberMonthsData.get(z));

        double beforeTaxes = Double.parseDouble(beforeTax.get(z));
        double monthlyTaxes = Double.parseDouble(monthlyTax.get(z));
        double yearTaxes = Double.parseDouble(yearTax.get(z));
        double afterTaxes = Double.parseDouble(afterTax.get(z));

        this.loan = new Loan(String.valueOf(z), amount, interest, months);
        calcultator = new LoanCalculator();

        if (beforeTaxes != 0 || monthlyTaxes != 0 || yearTaxes != 0 || afterTaxes != 0) {
            this.taxes = new Taxes();
            taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.BEFORE_LOAN, beforeTaxes));
            taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.MONTHLY, monthlyTaxes));
            taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.ANNUAL, yearTaxes));
            taxes.addTax(new Taxes.Entry(ITaxes.TaxType.TAX, ITaxes.TaxValueType.AMOUNT, ITaxes.TaxPeriod.AFTER_LOAN, afterTaxes));

            loan.setTaxes(taxes);
            calcultator.generateSchedule(loan);
            this.aprCalculator = new DefaultAprCalculator();
            double controllAPR = aprCalculator.getAPR(loan);
            if (controllAPR > 50) {
                Toast.makeText(getApplicationContext(), "APR more then 50%", Toast.LENGTH_LONG).show();
                loan.setAnnualPercentageRate(50);
            }else {
                loan.setAnnualPercentageRate(controllAPR);
            }

        } else {
            calcultator.generateSchedule(loan);
            loan.setAnnualPercentageRate(loan.getInterestRate() * 100);
        }
    }

    private void setVariabiles(int z) {
        amountLoan.add(z, String.valueOf(loan.getAmount()));
        monthlyPaymant.add(z, String.format("%.2f", loan.getMonthlyPayment()));
        totalTaxes.add(z, String.format("%.2f", loan.getTotalTaxes()));
        totalInterest.add(z, String.format("%.2f", loan.getTotalInterests()));
        apr.add(z, String.format("%.2f", loan.getAnnualPercentageRate()));
        totalPaymants.add(z, String.format("%.2f", loan.getTotalPayments()));
    }

    //Method to fill arrays with schedule information
    private void scheduleGenerator() {
        ISchedule schedule = loan.getSchedule();
        for (ISchedule.Installment yX : schedule.getInstallments()) {
            this.id.add(String.valueOf(yX.getId()));
            this.interestLoan.add(String.format("%.2f", yX.getInterest()));
            this.principal.add(String.format("%.2f", yX.getPrincipal()));
            this.actualBalance.add(String.format("%.1f", yX.getActualBalance()));
        }
    }

    private void sendDataResultCompare() {
        bundle = new Bundle();
        bundle.putStringArrayList("AMOUNTBUNDLE", (ArrayList<String>) amountLoan);
        bundle.putStringArrayList("INTERESTBUNDE", (ArrayList<String>) totalInterest);
        bundle.putStringArrayList("TAXESBUNDLE", (ArrayList<String>) totalTaxes);
        bundle.putStringArrayList("TOTALPAYMANTS", (ArrayList<String>) totalPaymants);
        bundle.putStringArrayList("MONTHLYPAYMANT", (ArrayList<String>) monthlyPaymant);
        bundle.putStringArrayList("APRBUNDLE", (ArrayList<String>) apr);
        bundle.putInt("ID", x);

    }

    private void sendDataSchedule() {
        bundle = new Bundle();
        bundle.putStringArrayList("IDNUMBER", (ArrayList<String>) id);
        bundle.putStringArrayList("INTEREST", (ArrayList<String>) interestLoan);
        bundle.putStringArrayList("PRINCIPAL", (ArrayList<String>) principal);
        bundle.putStringArrayList("ACTUALBALANCE", (ArrayList<String>) actualBalance);
    }


    private void checkBox() {
        for (int x = 1; x <= amountData.size(); x++) {
            loansCheckBoxList.add("Loan: " + x);
        }
    }

    private void dialogueMakerPDF() {
        checkBox();
        String[] items = new String[loansCheckBoxList.size()];
        items = loansCheckBoxList.toArray(items);
        itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Select loan- PDF");
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
                .setPositiveButton("Generate PDF", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (itemsSelected.size() > 1) {
                            Toast.makeText(getApplicationContext(), "Choose one loan", Toast.LENGTH_LONG).show();
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        } else {
                            setupPDF();
                            try {
                                Toast.makeText(getApplicationContext(), "Creating PDF", Toast.LENGTH_SHORT).show();

                                    setupPDF();
                                    pdfCreate();

                                Toast.makeText(getApplicationContext(), "PDF create in Document directory", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void dialogueMakerEmail() {
        checkBox();
        String[] items = new String[loansCheckBoxList.size()];
        items = loansCheckBoxList.toArray(items);
        itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Select loan- Email");
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
                .setPositiveButton("Generate Email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (itemsSelected.size() > 1) {
                            Toast.makeText(getApplicationContext(), "Choose one loan", Toast.LENGTH_LONG).show();
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        } else {
                            setupPDF();
                            try {
                                emailSend();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void dialogueMakerCahnege() {
        checkBox();
        String[] items = new String[loansCheckBoxList.size()];
        items = loansCheckBoxList.toArray(items);
        itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle("Select loan");
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
                .setPositiveButton("Change loan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (itemsSelected.size() > 1) {
                            Toast.makeText(getApplicationContext(), "Choose one loan", Toast.LENGTH_LONG).show();
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        } else {
                            x = itemsSelected.get(0);
                            clearSchedule();
                            calculateLoan(itemsSelected.get(0));
                            scheduleGenerator();
                            adapter.notifyDataSetChanged();
                            loansCheckBoxList.clear();
                            itemsSelected.clear();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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

    private void setupPDF() {
        PDFBoxResourceLoader.init(getApplicationContext());
        File pdfFile = new File(Environment.getExternalStorageDirectory(), "Documents");
        if (!pdfFile.exists()) {
            pdfFile.mkdir();
        }
    }

    private void pdfCreate() throws IOException {
        myFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "LoanSchedule_" + itemsSelected.get(0) + ".pdf");
        if (myFile.exists()) {
            myFile.delete();
        }
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDFont font = PDType1Font.TIMES_ROMAN;
        PDPageContentStream contentStream;
        PDFBoxResourceLoader.init(getApplicationContext());

        try {
            contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(font, 25);
            contentStream.newLineAtOffset(230, 740);
            contentStream.setLeading(15);
            contentStream.showText("Loan schedule");
            contentStream.newLine();
            contentStream.endText();

            contentStream.beginText();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(font, 20);
            contentStream.newLineAtOffset(50, 700);
            contentStream.setLeading(25);
            contentStream.showText("Loan amount: " + amountLoan.get(itemsSelected.get(0)));
            contentStream.newLine();
            contentStream.showText("Anual percentage rate: " + apr.get(itemsSelected.get(0)) + "%");
            contentStream.newLine();
            contentStream.showText("Monthly payment: " + monthlyPaymant.get(itemsSelected.get(0)));
            contentStream.newLine();
            contentStream.showText("Total interest: " + totalInterest.get(itemsSelected.get(0)));
            if (totalTaxes != null) {
                contentStream.newLine();
                contentStream.showText("Total taxes: " + totalTaxes.get(itemsSelected.get(0)));
            }
            contentStream.newLine();
            contentStream.showText("Total paymants: " + totalPaymants.get(itemsSelected.get(0)));
            contentStream.newLine();
            contentStream.endText();

            contentStream.beginText();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(font, 20);
            contentStream.newLineAtOffset(50, 545);
            contentStream.setLeading(15);
            contentStream.showText("ID");
            contentStream.newLine();
            contentStream.endText();

            contentStream.beginText();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(font, 20);
            contentStream.newLineAtOffset(100, 545);
            contentStream.setLeading(15);
            contentStream.showText("Loan interest");
            contentStream.newLine();
            contentStream.endText();


            contentStream.beginText();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(font, 20);
            contentStream.newLineAtOffset(240, 545);
            contentStream.setLeading(15);
            contentStream.showText("Loan principal");
            contentStream.newLine();
            contentStream.endText();

            contentStream.beginText();
            contentStream.setNonStrokingColor(0, 0, 0);
            contentStream.setFont(font, 20);
            contentStream.newLineAtOffset(400, 545);
            contentStream.setLeading(15);
            contentStream.showText("Actual balance");
            contentStream.newLine();
            contentStream.endText();

            int y = 520;
            for (int xY = 0; xY <= id.size() - 1; xY++) {
                if (xY == 25 || xY == 60 || xY == 95 || xY == 130 || xY == 165 || xY == 200 || xY == 235 || xY == 270 || xY == 305
                        || xY == 340 || xY == 375 || xY == 410 || xY == 445 || xY == 480 || xY == 515 || xY == 550
                        || xY == 585 || xY == 620 || xY == 655 || xY == 690 || xY == 725 || xY == 760 || xY == 795 || xY == 830
                        || xY == 865 || xY == 900 || xY == 935 || xY == 970) {
                    contentStream.close();
                    PDPage pageX = new PDPage();
                    document.addPage(pageX);
                    contentStream = new PDPageContentStream(document, pageX);
                    y = 700;
                }

                contentStream.beginText();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setFont(font, 18);
                contentStream.newLineAtOffset(50, y);
                contentStream.showText(id.get(xY));
                contentStream.endText();
                contentStream.beginText();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setFont(font, 18);
                contentStream.newLineAtOffset(120, y);
                contentStream.showText(interestLoan.get(xY));
                contentStream.endText();
                contentStream.beginText();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setFont(font, 18);
                contentStream.newLineAtOffset(270, y);
                contentStream.showText(principal.get(xY));
                contentStream.endText();
                contentStream.beginText();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setFont(font, 18);
                contentStream.newLineAtOffset(420, y);
                contentStream.showText(actualBalance.get(xY));
                contentStream.endText();
                contentStream.beginText();
                contentStream.setNonStrokingColor(0, 0, 0);
                contentStream.setFont(font, 10);
                contentStream.newLineAtOffset(400, 13);
                contentStream.setLeading(15);
                contentStream.showText("Created from JALC, @broforce42");
                contentStream.newLine();
                contentStream.endText();
                y = y - 18;
            }

            contentStream.close();
            document.save(myFile);
            document.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Email send
    private void emailSend() throws IOException {
        if (myFile == null)
            pdfCreate();
        Uri uri = Uri.fromFile(myFile);
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Loan schedule");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "There is no email client installed", Toast.LENGTH_LONG).show();
        }
    }

    private void clearSchedule() {
        id.clear();
        principal.clear();
        interestLoan.clear();
        actualBalance.clear();
    }

}
