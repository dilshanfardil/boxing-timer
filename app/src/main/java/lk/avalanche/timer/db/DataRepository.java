package lk.avalanche.timer.db;
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

import android.content.Context;

import java.util.List;

import androidx.lifecycle.LiveData;
import lk.avalanche.timer.db.Entity.Data;
import lk.avalanche.timer.db.dao.DataDao;

public class DataRepository {
    private Context context;
    private AppDatabase APP_App_DATABASE_INSTANCE;
    private DataDao dataDao;

    public DataRepository(Context context) {
        APP_App_DATABASE_INSTANCE = AppDatabase.getDatabase(context);
        dataDao=APP_App_DATABASE_INSTANCE.dataDao();
        this.context=context;
    }

    public void insertData(Data data){
        APP_App_DATABASE_INSTANCE.dataDao().insert(data);
    }

    public LiveData<Data> getData(){
        LiveData<Data> data = dataDao.getData(1);
        return data;
    }

    public Data getInitialData(){
        List<Data> all = dataDao.getAll();
        Data initialData = dataDao.getInitialData(1);
        return initialData;
    }

    public void updateData(Data data){
        dataDao.updateData(data);
    }
}
