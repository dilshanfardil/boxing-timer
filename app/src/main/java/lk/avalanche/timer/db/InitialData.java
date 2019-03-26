package lk.avalanche.timer.db;
/*
 * Developed by Lahiru Muthumal on 3/25/2019.
 * Last modified $file.lastModified.
 *
 * (C) Copyright 2019.year avalanche.lk (Pvt) Limited.
 * All Rights Reserved.
 *
 * These materials are unpublished, proprietary, confidential source code of
 * avalanche.lk (Pvt) Limited and constitute a TRADE SECRET
 * of avalanche.lk (Pvt) Limited.
 *
 * avalanche.lk (Pvt) Limited retains all title to and intellectual
 * property rights in these materials.
 *
 */

import java.util.ArrayList;
import java.util.List;

import lk.avalanche.timer.db.Entity.Sound;

public class InitialData {
    public static List<Sound> sounds = new ArrayList<>();

    static {
        sounds.add(new Sound("bell_0", true));
        sounds.add(new Sound("bell_1", true));
        sounds.add(new Sound("bell_2", true));
        sounds.add(new Sound("bell_3", true));
        sounds.add(new Sound("bell_4", true));
        sounds.add(new Sound("bell_5", true));
        sounds.add(new Sound("bell_6", true));
        sounds.add(new Sound("bell_7", true));
        sounds.add(new Sound("bell_8", true));
        sounds.add(new Sound("bell_9", true));
        sounds.add(new Sound("bell_10", true));
        sounds.add(new Sound("bell_11", true));
        sounds.add(new Sound("bell_12", true));
        sounds.add(new Sound("bell_13", true));
        sounds.add(new Sound("bell_14", true));
        sounds.add(new Sound("count_down_1", false));
        sounds.add(new Sound("count_down_2", false));
        sounds.add(new Sound("count_down_3", false));
        sounds.add(new Sound("count_down_4", false));
        sounds.add(new Sound("count_down_6", false));
        sounds.add(new Sound("count_down_7", false));
        sounds.add(new Sound("count_down_8", false));
        sounds.add(new Sound("count_down_9", false));
        sounds.add(new Sound("count_down_10", false));
        sounds.add(new Sound("count_down_11", false));
        sounds.add(new Sound("count_down_12", false));
        sounds.add(new Sound("count_down_13", false));
        sounds.add(new Sound("count_down_14", false));
        sounds.add(new Sound("count_down_15", false));
        sounds.add(new Sound("count_down_16", false));
        sounds.add(new Sound("count_down_17", false));
        sounds.add(new Sound("count_down_18", false));
        sounds.add(new Sound("count_down_19", false));
        sounds.add(new Sound("count_down_20", false));
        sounds.add(new Sound("count_down_21", false));

    }
}
