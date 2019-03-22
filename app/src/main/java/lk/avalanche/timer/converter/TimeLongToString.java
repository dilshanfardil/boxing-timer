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

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TimeLongToString implements Converter<String> {
    @Override
    public String convert(Object value) {
        Long millisUntilFinished = (Long) value;
        NumberFormat formatter = new DecimalFormat("00");
        long sec = millisUntilFinished / 1000;
        long min = sec / 60;
        min = min % 60;
        sec = sec % 60;
        return formatter.format(min) + ":" + formatter.format(sec);
    }
}
