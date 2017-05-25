package broforce42.cz.jalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //Text views loan basic info
    private static EditText amount;
    private static EditText interest;
    private static EditText months;

    //Switch
    private Switch taxesPlus;

    //Taxes input layouts
    private TextInputLayout downPaymantInput;
    private TextInputLayout beforeTaxesInput;
    private TextInputLayout monthlyTaxesInput;
    private TextInputLayout anualTaxesInput;
    private TextInputLayout afterTaxesInput;

    //Taxes text views
    private EditText downPaymentTaxes;
    private EditText beforeTaxes;
    private EditText monthlyTaxes;
    private EditText anualTaxes;
    private EditText afterTaxes;

    //Variabiles
    private static List<String> amountData = new ArrayList<>();
    private static List<String> interestData = new ArrayList<>();
    private static List<String> numberMonthsData = new ArrayList<>();
    private static List<String> beforeTax = new ArrayList<>();
    private static List<String> afterTax = new ArrayList<>();
    private static List<String> monthlyTax = new ArrayList<>();
    private static List<String> yearTax = new ArrayList<>();

    //Controll to set List
    private static int x = 0;

    //Floating action bar
    private FloatingActionButton fab;

    private Toolbar toolbar;

    //Dialogue about
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        amount = (EditText) findViewById(R.id.loan_amount);
        interest = (EditText) findViewById(R.id.interest_rate);
        months = (EditText) findViewById(R.id.months_number);

        taxesPlus = (Switch) findViewById(R.id.taxes_plus);

        downPaymantInput = (TextInputLayout) findViewById(R.id.deposit_textfield);
        downPaymantInput.setVisibility(TextInputLayout.INVISIBLE);
        beforeTaxesInput = (TextInputLayout) findViewById(R.id.before_tax_text_field);
        beforeTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
        monthlyTaxesInput = (TextInputLayout) findViewById(R.id.monthly_tax_text_field);
        monthlyTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
        anualTaxesInput = (TextInputLayout) findViewById(R.id.anual_tax_text_field);
        anualTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
        afterTaxesInput = (TextInputLayout) findViewById(R.id.after_tax_text_field);
        afterTaxesInput.setVisibility(TextInputLayout.INVISIBLE);

        amount.addTextChangedListener(onTextChangedListener(amount));

        downPaymentTaxes = (EditText) findViewById(R.id.deposit);
        beforeTaxes = (EditText) findViewById(R.id.before_tax);
        monthlyTaxes = (EditText) findViewById(R.id.monthly_tax);
        anualTaxes = (EditText) findViewById(R.id.anual_tax);
        afterTaxes = (EditText) findViewById(R.id.after_tax);

        downPaymentTaxes.addTextChangedListener(onTextChangedListener(downPaymentTaxes));
        beforeTaxes.addTextChangedListener(onTextChangedListener(beforeTaxes));
        monthlyTaxes.addTextChangedListener(onTextChangedListener(monthlyTaxes));
        anualTaxes.addTextChangedListener(onTextChangedListener(anualTaxes));
        afterTaxes.addTextChangedListener(onTextChangedListener(afterTaxes));

        fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);

        initToolBar();
        clickTaxPlus();
        actionButton();
    }

    //On resume activity
    @Override
    public void onResume() {
        super.onResume();
    }

    private void actionButton() {
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setData();
            }
        });
    }

    public void initToolBar() {
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                dialogueMaker();
                return true;
            case R.id.menu_help:
                helpDialogue();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setData() {
        if (amount.length() < 1 || interest.length() < 1 || months.length() < 1) {
            Toast.makeText(getApplicationContext(), R.string.complete_settings, Toast.LENGTH_LONG).show();
        } else if (Long.parseLong(amount.getText().toString().replaceAll(",", "")) == 0 ||
                Double.parseDouble(interest.getText().toString()) == 0 ||
                Long.parseLong(months.getText().toString()) == 0
                ) {
            Toast.makeText(getApplicationContext(), R.string.complete_settings, Toast.LENGTH_LONG).show();
        } else {
            interestData.add(x, interest.getText().toString());
            numberMonthsData.add(x, months.getText().toString());

            if (!downPaymentTaxes.getText().toString().isEmpty()) {
                long amountDown = 0;
                amountDown = Long.parseLong(amount.getText().toString().replaceAll(",", "")) - Long.parseLong(downPaymentTaxes.getText().toString().replaceAll(",", ""));
                amountData.add(x, String.valueOf(amountDown));
            } else {
                amountData.add(x, amount.getText().toString().replaceAll(",", ""));
            }
            if (!monthlyTaxes.getText().toString().isEmpty()) {
                monthlyTax.add(x, monthlyTaxes.getText().toString().replaceAll(",", ""));
            } else {
                monthlyTax.add(x, "0");
            }
            if (!beforeTaxes.getText().toString().isEmpty()) {
                beforeTax.add(x, beforeTaxes.getText().toString().replaceAll(",", ""));
            } else {
                beforeTax.add(x, "0");
            }
            if (!anualTaxes.getText().toString().isEmpty()) {
                yearTax.add(x, anualTaxes.getText().toString().replaceAll(",", ""));
            } else {
                yearTax.add(x, "0");
            }
            if (!afterTaxes.getText().toString().isEmpty()) {
                afterTax.add(x, afterTaxes.getText().toString().replaceAll(",", ""));
            } else {
                afterTax.add(x, "0");
            }
            setIntent();
            x++;
        }
    }

    private void setIntent() {
        Intent intentResult = new Intent(getApplicationContext(), ResultActivity.class);
        intentResult.putStringArrayListExtra("AMOUNT", (ArrayList<String>) amountData);
        intentResult.putStringArrayListExtra("INTEREST", (ArrayList<String>) interestData);
        intentResult.putStringArrayListExtra("MONTHS", (ArrayList<String>) numberMonthsData);
        intentResult.putStringArrayListExtra("MONTHLYT", (ArrayList<String>) monthlyTax);
        intentResult.putStringArrayListExtra("BEFORET", (ArrayList<String>) beforeTax);
        intentResult.putStringArrayListExtra("YEART", (ArrayList<String>) yearTax);
        intentResult.putStringArrayListExtra("AFTERT", (ArrayList<String>) afterTax);
        intentResult.putExtra("LOANID", x);
        startActivity(intentResult);
    }

    //Function to expand taxes
    private void clickTaxPlus() {
        taxesPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taxesVisibility();

            }
        });
    }

    //clear taxes
    private void clearTaxes() {
        downPaymentTaxes.setText(null);
        beforeTaxes.setText(null);
        monthlyTaxes.setText(null);
        anualTaxes.setText(null);
        afterTaxes.setText(null);
    }

    //Taxes visibility
    private void taxesVisibility() {
        if (taxesPlus.isChecked() == true) {
            downPaymantInput.setVisibility(TextInputLayout.VISIBLE);
            beforeTaxesInput.setVisibility(TextInputLayout.VISIBLE);
            monthlyTaxesInput.setVisibility(TextInputLayout.VISIBLE);
            anualTaxesInput.setVisibility(TextInputLayout.VISIBLE);
            afterTaxesInput.setVisibility(TextInputLayout.VISIBLE);
        } else {
            downPaymantInput.setVisibility(TextInputLayout.INVISIBLE);
            beforeTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
            monthlyTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
            anualTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
            afterTaxesInput.setVisibility(TextInputLayout.INVISIBLE);
            clearTaxes();
        }
    }

    private TextWatcher onTextChangedListener(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                amount.removeTextChangedListener(this);
                beforeTaxes.removeTextChangedListener(this);
                afterTaxes.removeTextChangedListener(this);
                monthlyTaxes.removeTextChangedListener(this);
                anualTaxes.removeTextChangedListener(this);
                downPaymentTaxes.removeTextChangedListener(this);
                try {
                    String originalString = editable.toString();
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (originalString.contains(".")) {
                        originalString = originalString.replaceAll(".", "");
                    }
                    longval = Long.parseLong(originalString);
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
                    formatter.applyPattern("##,###,###");
                    String formattedString = formatter.format(longval);
                    editText.setText(formattedString);
                    editText.setSelection(editText.getText().length());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                editText.addTextChangedListener(this);
            }
        };
    }

    private void dialogueMaker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(R.string.about_jalc);
        builder.setMessage(" Just another loan calcultator\n Created by broforce42 studio\n Web page: www.broforce42.com\n Email: broforce42@broforce42.com");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void helpDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        View hView = getLayoutInflater().inflate(R.layout.dialog_help, null);
        builder.setView(hView);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
