package lk.avalanche.timer.db.dao;
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

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import lk.avalanche.timer.db.Entity.Data;
@Dao
public interface DataDao {
    @Insert
    void insert(Data data);

    @Query("SELECT * FROM data WHERE id=:Id")
    LiveData<Data> getData(int Id);

    @Query("SELECT * FROM data WHERE id=:Id")
    Data getInitialData(int Id);

    @Update
    void updateData(Data data);

    @Query("SELECT * FROM data")
    List<Data> getAll();
}
