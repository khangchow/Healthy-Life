package com.myapplication.healthylife.utils;

import com.myapplication.healthylife.model.Stat;

import java.util.ArrayList;

public class StatUtils {
    public static void removeStats()    {
        DatabaseUtils.removeStats();
    }

    public static void addStat(Stat stat)   {
        DatabaseUtils.addStat(stat);
    }

    public static ArrayList<Stat> getStatList()    {
        return DatabaseUtils.getStatList();
    }
}
