package com.shiliu.dragon.utils.mobile;

import java.util.regex.Pattern;

/**
 * @author ouyangchao
 * @createTime
 * @description
 */
public class MobileUtils {
    private static final String CHINA = "86";
    private static final String UA = "44";
    public static final String MOBILE = "mobile";

    public static boolean isValid(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        } else {
            return getMobileCalibrator(mobile).isValid();
        }
    }

    private static MobileCalibrator getMobileCalibrator(String mobile) {
        if (mobile.startsWith(UA)) {
            return new UAMobileCalibrator(mobile);
        } else {
            //默认国内号码
            return new ChinaMobileCalibrator(mobile);
        }
    }

}

abstract class MobileCalibrator {
    protected String mobile;

    abstract boolean isValid();

    public MobileCalibrator(String mobile) {
        this.mobile = mobile;
    }
}

/**
 * 大中化区号码校验规则
 */
class ChinaMobileCalibrator extends MobileCalibrator {
    public static final Pattern CHINA_MOBILE_COMPILE = Pattern.compile("^[8][6][1]\\d{10}$");

    public ChinaMobileCalibrator(String mobile) {
        super(mobile);
    }


    @Override
    public boolean isValid() {
        if (!CHINA_MOBILE_COMPILE.matcher(mobile).matches()) {
            return false;
        }
        return true;
    }
}

/**
 * 英国号码校验规则
 */
class UAMobileCalibrator extends MobileCalibrator {
    public static final Pattern CHINA_MOBILE_COMPILE = Pattern.compile("^[4][4]\\d{10}$");

    public UAMobileCalibrator(String mobile) {
        super(mobile);
    }

    @Override
    public boolean isValid() {
        if (!CHINA_MOBILE_COMPILE.matcher(mobile).matches()) {
            return false;
        }
        return true;
    }
}
