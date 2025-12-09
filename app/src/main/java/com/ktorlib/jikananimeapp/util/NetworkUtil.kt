package com.ktorlib.jikananimeapp.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {

    fun isOnline(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        return cm.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}