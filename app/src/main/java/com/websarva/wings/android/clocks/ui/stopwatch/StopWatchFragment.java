package com.websarva.wings.android.clocks.ui.stopwatch;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.websarva.wings.android.clocks.R;
import com.websarva.wings.android.clocks.databinding.FragmentStopwatchBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StopWatchFragment extends Fragment{

    private FragmentStopwatchBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Timer timer;

    private TextView timerText;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);
    private TextView rap_text;

    private long delay, period;
    private int count, rap_count;

    private String rap_time = "";

    private Button start_button;
    private Button rap_button;

    private boolean cancel_flag = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStopwatchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        delay = 0;
        count = 0;
        rap_count = 1;
        period = 10;

        start_button = root.findViewById(R.id.start_button);
        start_button.setOnClickListener(this::start_execute);

        rap_button = root.findViewById(R.id.rap_button);
        rap_button.setVisibility(View.GONE);
        rap_button.setOnClickListener(this::rap_execute);

        timerText = root.findViewById(R.id.timer);
        timerText.setText(dateFormat.format(0));
        rap_text = root.findViewById(R.id.rap_time);

        return root;
    }

    private void start_execute(View view){
        if (!cancel_flag){
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            count++;
                            timerText.setText(dateFormat.format(count*period));
                        }
                    });
                }
            }, delay, period);

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_state3, null);
            start_button.setBackground(drawable);
            start_button.setText(getString(R.string.cancel));

            rap_button.setVisibility(View.VISIBLE);
            rap_button.setText(getString(R.string.rap));
            cancel_flag = true;
        }else {
            timer.cancel();

            Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_state, null);
            start_button.setBackground(drawable);
            start_button.setText(getString(R.string.restart));

            rap_button.setText(getString(R.string.reset));
            cancel_flag = false;
        }
    }

    private void rap_execute(View view){
        if (!cancel_flag){
            count = 0;
            timer = null;
            timerText.setText(dateFormat.format(0));
            start_button.setText(getString(R.string.start));
            rap_time = "";
            rap_count = 1;
            rap_button.setVisibility(View.GONE);
        }else {
            rap_time += rap_count + "　　　" + dateFormat.format(count * period) + "\n";
            rap_count++;
        }
        rap_text.setText(rap_time);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}