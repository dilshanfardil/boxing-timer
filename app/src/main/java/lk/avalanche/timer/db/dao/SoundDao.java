package lk.avalanche.timer.db.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import lk.avalanche.timer.db.Entity.Sound;

/*
 * Developed by Lahiru Muthumal on 3/22/2019.
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
@Dao
public interface SoundDao {
    @Insert
    void insert(Sound sound);

    @Insert
    void insert(List<Sound> sound);


    @Query("SELECT * FROM sound WHERE type=:type")
    List<Sound> getByType(boolean type);

}
