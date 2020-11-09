package com.example.huuquang.qrcode.util;

import android.view.View;
import android.view.ViewGroup;

public class FragmentUtil {
    public static int INCOME_FRAGMENT = 0;
    public static int OUTCOME_FRAGMENT = 1;
    public static int REPORT_FRAGMENT = 2;
    public static int TODO_FRAGMENT = 3;
    public static int SETTING_FRAGMENT = 4;
    public static int CHAT_FRAGMENT = 5;

    /**
     * Enables/Disables all child views in a view group.
     *
     * @param view the view group
     * @param enabled <code>true</code> to enable, <code>false</code> to disable
     * the views.
     */
    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }
}
