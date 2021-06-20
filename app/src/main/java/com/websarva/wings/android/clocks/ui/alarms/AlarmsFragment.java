package com.websarva.wings.android.clocks.ui.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.websarva.wings.android.clocks.MainActivity;
import com.websarva.wings.android.clocks.R;
import com.websarva.wings.android.clocks.databinding.FragmentAlarmBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AlarmsFragment extends Fragment {
    private FragmentAlarmBinding binding;

    private TextView noDataTextView;

    private String[] FROM = {"time", "request_code"};
    private int[] TO = {R.id.custom_text};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AlarmViewModel alarmViewModel = new AlarmViewModel(requireContext());

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton actionButton = root.findViewById(R.id.fab);
        actionButton.setOnClickListener(this::execute_fab);

        ListView alarmListView = root.findViewById(R.id.alarmsList);
        noDataTextView = root.findViewById(R.id.noData);
        alarmViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("")){
                    noDataTextView.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    List<Map<String, Object>> alarmsList = gson.fromJson(alarmViewModel.getAlarmText(), new TypeToken<List>(){}.getType());
                    SimpleAdapter adapter = new SimpleAdapter(requireContext(), alarmsList, R.layout.custom_listview, FROM, TO);
                    adapter.notifyDataSetChanged();
                    alarmListView.setAdapter(adapter);
                }else {
                    noDataTextView.setVisibility(View.VISIBLE);
                    noDataTextView.setText(s);
                }
            }
        });

        registerForContextMenu(alarmListView);

        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull @NotNull ContextMenu menu, @NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.alarms_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull @NotNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listItemPosition = info.position;

        int itemID = item.getItemId();

        if (itemID == R.id.delete){
            Gson gson = new Gson();
            SharedPreferences preferences = requireActivity().getSharedPreferences("AlarmsDB", Context.MODE_PRIVATE);
            List<Map<String, Object>> alarmList = gson.fromJson(preferences.getString("alarms_list", ""), new TypeToken<List>(){}.getType());

            Map<String, Object> content = alarmList.get(listItemPosition);
            int requestCode = (int)(double)content.get("request_code");
            Log.d("test2", String.valueOf(requestCode));

            Intent intent = new Intent(requireActivity().getApplicationContext(), AlarmBroadcastReceiver.class);
            PendingIntent pending = PendingIntent.getBroadcast(requireActivity().getApplicationContext(), requestCode, intent, 0);
            AlarmManager am = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
            am.cancel(pending);

            alarmList.remove(listItemPosition);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("alarms_list", gson.toJson(alarmList));
            editor.apply();

            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            requireActivity().finish();
        }

        return super.onContextItemSelected(item);
    }

    private void execute_fab(View view){
        Intent intent = new Intent(requireActivity(), AddAlarmsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}