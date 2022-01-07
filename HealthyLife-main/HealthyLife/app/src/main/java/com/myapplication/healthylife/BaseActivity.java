package com.myapplication.healthylife;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.model.User;
import com.myapplication.healthylife.utils.KeyboardUtils;

public class BaseActivity extends AppCompatActivity {
    long startClickTime;
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = AppPrefs.getInstance(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            startClickTime = System.currentTimeMillis();

        } else if (ev.getAction() == MotionEvent.ACTION_UP) {

            if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                View view = getCurrentFocus();

                if (!(view instanceof EditTextV2)) {
                    return super.dispatchTouchEvent(ev);
                }

                Rect outRect = new Rect();

                view.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)ev.getX(), (int)ev.getY())) {
                    KeyboardUtils.hideKeyboard(view);

                    view.clearFocus();
                }
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public static User getUserData() {
        String data = sharedPreferences.getString("user", null);
        return new Gson().fromJson(data, User.class);
    }

    public static void updateUserData(User user) {
        sharedPreferences.edit().putString("user", new Gson().toJson(user, User.class)).commit();
    }
}
