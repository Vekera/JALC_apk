package broforce42.cz.jalc;

import android.content.pm.ActivityInfo;
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

import java.util.ArrayList;
import java.util.List;


public class ScheduleFragment extends Fragment {

    //List of schedule information
    private List<String> id = new ArrayList<String>();
    private List<String> interest = new ArrayList<String>();
    private List<String> principal = new ArrayList<String>();
    private List<String> actualBalance = new ArrayList<String>();

    //Schedule layout
    private TableLayout scheduleLayout;

    //View to find views
    private View rootView;

    public ScheduleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        scheduleLayout = (TableLayout) rootView.findViewById(R.id.schedule_table);
        bundleSchedule();
        schedule();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        return rootView;
    }

    private void schedule() {
        int x = 1;
        scheduleLayout.isStretchAllColumns();
        TableRow row = new TableRow(getActivity());
        TextView idX = new TextView(getActivity());
        idX.setTypeface(null, Typeface.BOLD);
        idX.setText(R.string.mon);
        idX.setBackgroundResource(R.color.colorPrimaryDark);
        idX.setPadding(30, 5, 0, 5);
        idX.setGravity(Gravity.CENTER);
        idX.setTextColor(Color.WHITE);
        row.addView(idX);
        TextView interestX = new TextView(getActivity());
        interestX.setTypeface(null, Typeface.BOLD);
        interestX.setText(R.string.intx);
        interestX.setBackgroundResource(R.color.colorPrimaryDark);
        interestX.setPadding(30, 5, 0, 5);
        interestX.setGravity(Gravity.CENTER);
        interestX.setTextColor(Color.WHITE);
        row.addView(interestX);
        TextView principalX = new TextView(getActivity());
        principalX.setTypeface(null, Typeface.BOLD);
        principalX.setText(R.string.pr);
        principalX.setBackgroundResource(R.color.colorPrimaryDark);
        principalX.setPadding(35, 5, 0, 5);
        principalX.setGravity(Gravity.CENTER);
        principalX.setTextColor(Color.WHITE);
        row.addView(principalX);
        TextView actualBalanceX = new TextView(getActivity());
        actualBalanceX.setTypeface(null, Typeface.BOLD);
        actualBalanceX.setText(R.string.ab);
        actualBalanceX.setBackgroundResource(R.color.colorPrimaryDark);
        actualBalanceX.setPadding(35, 5, 30, 5);
        actualBalanceX.setGravity(Gravity.CENTER);
        actualBalanceX.setTextColor(Color.WHITE);
        row.addView(actualBalanceX);
        scheduleLayout.addView(row);
        for (int i = 0; i < id.size(); i++) {
            TableRow scheduleRow = new TableRow(getActivity());
            String idXY = id.get(i);
            String interestXY = interest.get(i);
            String principalXY = principal.get(i);
            String actualXY = actualBalance.get(i);

            TextView idRow = new TextView(getActivity());
            idRow.setText(idXY);
            idRow.setGravity(Gravity.CENTER);
            idRow.setPadding(20, 0, 0, 0);

            TextView interestRow = new TextView(getActivity());
            interestRow.setText(interestXY);
            interestRow.setPadding(30, 0, 0, 0);
            interestRow.setGravity(Gravity.CENTER);

            TextView principalRow = new TextView(getActivity());
            principalRow.setText(principalXY);
            principalRow.setPadding(40, 0, 0, 0);
            principalRow.setGravity(Gravity.CENTER);

            TextView actualRow = new TextView(getActivity());
            actualRow.setText(actualXY);
            actualRow.setPadding(40, 0, 30, 0);
            actualRow.setGravity(Gravity.CENTER);

            if (x % 2 == 0) {
                idRow.setBackgroundResource(R.color.colorPrimaryDark);
                interestRow.setBackgroundResource(R.color.colorPrimaryDark);
                principalRow.setBackgroundResource(R.color.colorPrimaryDark);
                actualRow.setBackgroundResource(R.color.colorPrimaryDark);
                idRow.setTextColor(Color.WHITE);
                interestRow.setTextColor(Color.WHITE);
                principalRow.setTextColor(Color.WHITE);
                actualRow.setTextColor(Color.WHITE);
            }
            x++;
            scheduleRow.addView(idRow);
            scheduleRow.addView(interestRow);
            scheduleRow.addView(principalRow);
            scheduleRow.addView(actualRow);
            scheduleLayout.addView(scheduleRow);
        }
    }

    //Schedule intent to get form result activity
    private void bundleSchedule() {
        id = this.getArguments().getStringArrayList("IDNUMBER");
        interest = this.getArguments().getStringArrayList("INTEREST");
        principal = this.getArguments().getStringArrayList("PRINCIPAL");
        actualBalance = this.getArguments().getStringArrayList("ACTUALBALANCE");
    }
}
