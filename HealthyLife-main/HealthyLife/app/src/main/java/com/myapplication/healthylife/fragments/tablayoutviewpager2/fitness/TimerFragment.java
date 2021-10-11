package com.myapplication.healthylife.fragments.tablayoutviewpager2.fitness;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;

import com.google.gson.Gson;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentTimerBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Exercise;
import com.myapplication.healthylife.model.Timer;
import com.myapplication.healthylife.model.User;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

import kotlin.jvm.Synchronized;

public class TimerFragment extends Fragment{
    FragmentTimerBinding binding;
    DatabaseHelper db;
    NavController navController;

    private ArrayList<Exercise> list;
    private ArrayList<Timer> listTimer;
    int i = 0;

    CountDownTimer timer;

    boolean isRunning = false;
    boolean initTimer = false;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        binding = FragmentTimerBinding.inflate(getLayoutInflater());
        list = db.getRecommendedExerciseList();
        listTimer = convert(list);
//        listTimer = new ArrayList<>();
//        listTimer.add(new Timer("Test1", "Test1", 5000, list.get(0).getVideo()));
//        listTimer.add(new Timer("Test2", "Test2", 5000, list.get(1).getVideo()));

        sharedPreferences = AppPrefs.getInstance(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvName.setText(listTimer.get(i).getName());
        binding.tvStatus.setText(listTimer.get(i).getStatus());
        updateTime(listTimer.get(i).getTime());

        binding.video.setVideoURI(Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+listTimer.get(i).getVideo()));
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
                    countDown(listTimer);
                    binding.video.setVisibility(View.VISIBLE);
                    binding.video.start();
                    isRunning = true;
                    binding.btn.setText("Cancel");
                    initTimer = true;
                }else  {
                    timer.cancel();
                    updateTime(listTimer.get(i).getTime());
                    binding.video.stopPlayback();
                    binding.video.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + listTimer.get(i).getVideo()));
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

    synchronized private void countDown(ArrayList<Timer> listTimer)    {
        timer = new CountDownTimer(listTimer.get(i).getTime(), 1000) {
            @Override
            public void onTick(long l) {
                updateTime(l);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFinish() {
                if (++i < listTimer.size()) {
                    binding.tvName.setText(listTimer.get(i).getName());
                    binding.tvStatus.setText(listTimer.get(i).getStatus());
                    binding.video.setVideoURI(Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+listTimer.get(i).getVideo()));
                    binding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.setLooping(true);
                        }
                    });
                    countDown(listTimer);
                }else   {
                    String data = sharedPreferences.getString("user", null);
                    User user = new Gson().fromJson(data, User.class);

                    binding.video.stopPlayback();
                    ArrayList<Exercise> exercises = db.getExerciseList();
                    for (int i = 0; i < 5; i++) {
                        Log.d("DEBUG", String.valueOf(exercises.get(i).isFinished()));
                        if (!exercises.get(i).isFinished()) {
                            exercises.get(i).setFinished(true);
                            exercises.get(i).setProgress(exercises.get(i).getProgress()+1);
                            user.setCaloFitness(user.getCaloFitness()+exercises.get(i).getcaloSet());
                        }
                    }
                    sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();
                    db.deleteAllExercises();
                    for (Exercise ex: exercises
                         ) {
                        db.addExercise(ex);
                    }

                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.custom_dialog_congratz);
                    dialog.setCanceledOnTouchOutside(true);
                    Button btnOk = dialog.findViewById(R.id.btnOk);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            navController.navigateUp();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                }
            }
        }.start();
    }

    private void updateTime(long time)   {
        int min = (int)time/60000;
        int sec = (int)time%60000/1000;

        String text;
        text = ""+min;
        text += " : ";
        if (sec < 10)   {
            text += "0";
        }
        text += sec;
        binding.tvTime.setText(text);
    }

    private ArrayList<Timer> convert (ArrayList<Exercise> list) {
        ArrayList<Timer> returnList = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            String name = list.get(i).getName();
            String status = "";
            long time = 0;
            int video = list.get(i).getVideo();
            boolean isBreak = false;

            for (int j = 1; j <= (list.get(i).getnumSet() * 2) - 1; ++j) {
                if (!isBreak) {
                    status = "Set " + Math.round(j / 2.0);
                    time = list.get(i).getDurationSet() * 1000;
                    isBreak = true;
                }
                else {
                    status = "Break " + (j / 2) + "-" + ((j / 2) + 1);
                    time = list.get(i).getbreakSet() * 1000;
                    isBreak = false;
                }
                returnList.add(new Timer(name, status, time, video));
            }

            name = "Next Exercise";
            status = "Breathe...";
            time = list.get(i).getbreakEx() * 1000;
            video = R.raw.breath;
            returnList.add(new Timer(name, status, time, video));
        }

        return returnList;
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "onDestroy: timer");
        if (initTimer && isRunning)  {
            timer.cancel();
            binding.video.stopPlayback();
        }
    }
}