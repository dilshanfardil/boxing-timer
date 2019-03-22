package lk.avalanche.timer.converter;
/*
 * Developed by Lahiru Muthumal on 3/21/2019.
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TimeStringToLong implements Converter<Long> {
    @Override
    public Long convert(Object value) {
        String str = (String) value;
        String[] strings = str.split(":");
        long min = Long.parseLong(strings[0]), sec = Long.parseLong(strings[1]);
        return min * 60 * 1000 + sec * 1000;
    }
}
