package com.myapplication.healthylife.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
    public static Boolean validateString(String str)   {
        return !str.isEmpty() && Pattern.matches("^[a-zA-Z_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâã" +
                "èéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ\" " +
                "+ \"ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉ" +
                "ịọỏốồổỗộớờởỡợ\" + \"ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\\\s]+$", str);
    }

    public static Boolean validateFloat(String num) {
        return !num.isEmpty() && Pattern.matches("([0-9]*[.])?[0-9]+", num);
    }

    public static Boolean validateHeight(String height) {
        return !height.isEmpty()
                && ValidatorUtils.validateFloat(height)
                && (Float.valueOf(height) >= 10  && Float.valueOf(height) <= 300);
    }

    public static Boolean validateWeight(String weight) {
        return !weight.isEmpty()
                && ValidatorUtils.validateFloat(weight)
                && (Float.valueOf(weight) >= 1 && Float.valueOf(weight) <= 600);
    }
}
