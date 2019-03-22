package lk.avalanche.timer.db;

import android.content.Context;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import lk.avalanche.timer.db.Entity.Data;
import lk.avalanche.timer.db.Entity.Sound;
import lk.avalanche.timer.db.dao.DataDao;
import lk.avalanche.timer.db.dao.SoundDao;

/*
 * Developed by Lahiru Muthumal on 3/20/2019.
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
@androidx.room.Database(entities = {Data.class, Sound.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();

    public abstract SoundDao soundDao();
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "data_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppDatabase database = getDatabase(context);
                                            database.dataDao().insert(
                                                    new Data(180000l,
                                                            120000l,
                                                            (long) 10,
                                                            "countdown",
                                                            "bell"));
                                            SoundDao soundDao = database.soundDao();
                                            soundDao.insert(new Sound("bell", true));
                                            soundDao.insert(new Sound("countdown", true));
                                            soundDao.insert(new Sound("bell", false));
                                            soundDao.insert(new Sound("countdown", false));

                                        }
                                    });

                                }
                            })
                            .build();

                }
            }
        }
        return INSTANCE;
    }


}
