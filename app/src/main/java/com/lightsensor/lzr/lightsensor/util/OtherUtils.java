package com.lightsensor.lzr.lightsensor.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lizheren on 2019/1/15.
 */

public class OtherUtils {
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(calendar.getTime());
    }

    /**
     * 格式化保留2位有效数字【四舍五入】，末位有0仍然显示
     *
     * @param d
     * @return
     */
    public static String formatToSave2All(double d) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "0.00";
    }
}
