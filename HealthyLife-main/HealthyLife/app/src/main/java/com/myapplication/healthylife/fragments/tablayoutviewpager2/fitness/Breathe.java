package com.myapplication.healthylife.fragments.tablayoutviewpager2.fitness;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentBreatheBinding;
import com.myapplication.healthylife.local.AppPrefs;


public class Breathe extends Fragment {
    FragmentBreatheBinding binding;
    NavController navController;

    CountDownTimer timer;
    boolean isRunning = false;
    SharedPreferences sharedPreferences;
    boolean initTimer = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBreatheBinding.inflate(getLayoutInflater());
        sharedPreferences = AppPrefs.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateTime(60000);
        binding.video.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.breath));
        MediaController ctrl = new MediaController(getContext());
        ctrl.setVisibility(View.GONE);
        binding.video.setMediaController(ctrl);
        binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    countDown(60000);
                    binding.video.setVisibility(View.VISIBLE);
                    binding.video.start();
                    isRunning = true;
                    binding.btn.setText("Cancel");
                    initTimer = true;
                } else {
                    timer.cancel();
                    updateTime(60000);
                    binding.video.stopPlayback();
                    binding.video.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.breath));
                    MediaController ctrl = new MediaController(getContext());
                    ctrl.setVisibility(View.GONE);
                    binding.video.setMediaController(ctrl);
                    binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.setLooping(true);
                        }
                    });
                    binding.video.setVisibility(view.GONE);
                    isRunning = false;
                    binding.btn.setText("Start");
                }
            }
        });
    }

    synchronized private void countDown(long time) {
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long time) {
                updateTime(time);
            }

            @Override
            public void onFinish() {
                binding.video.stopPlayback();
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.custom_dialog_breathe);
                dialog.setCanceledOnTouchOutside(true);
                Button btnOk = dialog.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        navController.navigateUp();
                        dialog.dismiss();
                    }
                });
                Button btnRestart= dialog.findViewById(R.id.btnRestart);
                btnRestart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isRunning=false;
                        updateTime(60000);
                        binding.btn.setText("Start");
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        }.start();

    };

    private void updateTime(long time) {
        int min = (int) time / 60000;
        int sec = (int) time % 60000 / 1000;
        if (sec==58)
            Toast.makeText(getContext(),"Be still and bring your attention to your breath.", Toast.LENGTH_LONG ).show();
        else if (sec==53)
            Toast.makeText(getContext(),"Now inhale...", Toast.LENGTH_SHORT ).show();
        else if (sec==50)
            Toast.makeText(getContext(),"and exhale.", Toast.LENGTH_SHORT ).show();
        String text;
        text = "" + min;
        text += " : ";
        if (sec < 10) {
            text += "0";
        }
        text += sec;
        binding.tvTime.setText(text);


    };

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (initTimer && isRunning)  {
            timer.cancel();
            binding.video.stopPlayback();
        }
    }
}