package com.websarva.wings.android.clocks.ui.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.websarva.wings.android.clocks.R;
import com.websarva.wings.android.clocks.databinding.FragmentTimerBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimerFragment extends Fragment {

    private FragmentTimerBinding binding;

    private TextView timerText, colonText;
    private EditText minuteEdit, secondEdit;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.JAPAN);
    private Button startButton, stopButton;
    private ProgressBar progressBar;

    private boolean cancel_flag = false, reset_flag = true;
    private long countNumber, interval, millisUntilFinished_main;
    private final long[] val = {0};

    private CountDown countDown;

    private NotificationManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        countNumber = 6000;
        interval = 10;

        startButton = root.findViewById(R.id.start_button);
        startButton.setOnClickListener(this::start_execute);

        stopButton = root.findViewById(R.id.stop_button);
        stopButton.setVisibility(View.GONE);
        stopButton.setOnClickListener(this::stop_execute);

        timerText = root.findViewById(R.id.timer);
        timerText.setText(dateFormat.format(0));
        timerText.setVisibility(View.GONE);

        minuteEdit = root.findViewById(R.id.minute);
        minuteEdit.setText(getString(R.string.zero_time));

        colonText = root.findViewById(R.id.colon);

        secondEdit = root.findViewById(R.id.second);
        secondEdit.setText(getString(R.string.zero_time));

        progressBar = root.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(getString(R.string.notification_description_timer));
            channel.enableVibration(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), null);
            channel.setShowBadge(true);
            manager.createNotificationChannel(channel);
        }

        return root;
    }

    private void start_execute(View view){
        if (!cancel_flag){
            boolean error_flag = false;
            if (reset_flag){
                int minute = Integer.parseInt(minuteEdit.getText().toString());
                int second = Integer.parseInt(secondEdit.getText().toString());

                if ((minute < 0 || second < 0) || (minute == 0 && second == 0)){
                    error_flag = true;
                    Toast.makeText(requireContext(), "不正な値です", Toast.LENGTH_SHORT).show();
                }else {
                    countNumber = (minute * 60 + second) * 1000;
                    countDown = new CountDown(countNumber, interval);

                    minuteEdit.setVisibility(View.GONE);
                    colonText.setVisibility(View.GONE);
                    secondEdit.setVisibility(View.GONE);
                    timerText.setVisibility(View.VISIBLE);

                    reset_flag = false;
                }
            }
            if (!error_flag){
                countDown.start();

                progressBar.setVisibility(View.VISIBLE);

                stopButton.setVisibility(View.VISIBLE);

                setDrawable(getString(R.string.cancel), cancel_flag);
                cancel_flag = true;
            }
        }else {
            countDown.cancel();

            setDrawable(getString(R.string.restart), cancel_flag);

            countDown = new CountDown(millisUntilFinished_main, interval);
            cancel_flag = false;
        }
    }

    private void stop_execute(View view){
        countDown.cancel();
        resetMode();

        progressBar.setVisibility(View.GONE);
        val[0] = 0;
        progressBar.setProgress((int) val[0]);
    }

    private void setDrawable(final String text, final boolean flag){
        Drawable drawable;
        if (flag){
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_state, null);
        }else {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_state3, null);
        }
        startButton.setBackground(drawable);
        startButton.setText(text);
    }

    private void resetMode(){
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_state, null);
        startButton.setBackground(drawable);
        startButton.setText(getString(R.string.start));

        timerText.setVisibility(View.GONE);
        timerText.setText(dateFormat.format(0));
        minuteEdit.setText(getString(R.string.zero_time));
        minuteEdit.setVisibility(View.VISIBLE);
        colonText.setVisibility(View.VISIBLE);
        secondEdit.setText(getString(R.string.zero_time));
        secondEdit.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.GONE);

        cancel_flag = false;
        reset_flag = true;
    }

    private class CountDown extends CountDownTimer{

        CountDown(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            timerText.setText(dateFormat.format(millisUntilFinished));
            millisUntilFinished_main = millisUntilFinished;

         //   debug_count++;
            val[0] += 6039000 / (countNumber / (interval + 6));
            progressBar.setProgress((int) val[0]);
        }

        @Override
        public void onFinish() {
            resetMode();

            val[0] = 100;
            progressBar.setProgress((int) val[0]);
            progressBar.setVisibility(View.GONE);
            val[0] = 0;
            progressBar.setProgress((int) val[0]);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), getString(R.string.notification_channel_name));
                builder.setSmallIcon(android.R.drawable.ic_dialog_info);
                builder.setContentTitle(getString(R.string.app_name));
                builder.setContentText(getString(R.string.notification_description_timer));
                builder.setAutoCancel(true);

                Notification notification = builder.build();

                manager.notify(0, notification);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}