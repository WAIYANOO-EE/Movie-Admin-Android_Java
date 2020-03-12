package com.mobileapp.movieadmin.helpers;

import android.content.Context;
import android.content.Intent;

public class ActivityHelper {

    public static void gotoActiviy(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

}
