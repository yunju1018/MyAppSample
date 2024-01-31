package com.example.myappsample.chipAnimation;

import android.text.TextUtils;


import com.example.myappsample.R;

import androidx.annotation.StringRes;

public enum eUserGrade {
    NONE,   // 초기값
    USER_GRADE_WELCOME("00"),   // Welcome 등급
    USER_GRADE_GREEN("10"),     // Green 등급
    USER_GRADE_GOLD("20"),      // Gold 등급
    ;

    public final String grade;

    eUserGrade() {
        this.grade = "";
    }

    eUserGrade(String code) {
        this.grade = code;
    }

    public static eUserGrade fromString(String value) {
        if (!TextUtils.isEmpty(value)) {
            for (eUserGrade userGrade : eUserGrade.values()) {
                if (userGrade.grade.equalsIgnoreCase(value)) {
                    return userGrade;
                }
            }
        }

        return NONE;
    }

    @StringRes
    public int getLevelStrResId() {
        switch (this) {
            case USER_GRADE_GOLD:
                return R.string.user_grade_gold;

            case USER_GRADE_GREEN:
                return R.string.user_grade_green;

            case USER_GRADE_WELCOME:
                return R.string.user_grade_welcome;

            case NONE:
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return "{ " + "name : " + name() + ", grade : " + grade + " }";
    }
}
