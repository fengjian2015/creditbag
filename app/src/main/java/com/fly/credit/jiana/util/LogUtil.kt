package com.fly.credit.jiana.util

import android.util.Log
import java.util.*

object LogUtil {
    private var LOG = true

    @JvmStatic
    fun setDebug(debug: Boolean) {
        LOG = debug
    }

    @JvmStatic
    fun d(mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.d(getTag(), it) }
        }
    }


    @JvmStatic
    fun i(mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.i(getTag(), it) }
        }
    }


    @JvmStatic
    fun f(mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.d(getTag(), it) }
        }
    }

    @JvmStatic
    fun f(tag: String?,mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.d(tag, it) }
        }
    }

    @JvmStatic
    fun v(mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.d(getTag(), it) }
        }
    }

    @JvmStatic
    fun e(mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.e(getTag(), it) }
        }
    }

    @JvmStatic
    fun w(mess: String?) {
        if (LOG) {
            mess?.let { buildMessage(it) }?.let { Log.w(getTag(), it) }
        }
    }

    private fun getTag(): String? {
        val trace = Throwable().fillInStackTrace()
            .stackTrace
        var callingClass = ""
        for (i in 2 until trace.size) {
            val clazz: Class<*> = trace[i].javaClass
            if (clazz != LogUtil::class.java) {
                callingClass = trace[i].className
                callingClass = callingClass.substring(
                    callingClass
                        .lastIndexOf('.') + 1
                )
                break
            }
        }
        return callingClass
    }

    private fun buildMessage(msg: String): String? {
        val trace = Throwable().fillInStackTrace()
            .stackTrace
        var caller: String? = ""
        for (i in 2 until trace.size) {
            val clazz: Class<*> = trace[i].javaClass
            if (clazz != LogUtil::class.java) {
                caller = trace[i].methodName
                break
            }
        }
        return String.format(
            Locale.US, "[%d] %s: %s", Thread.currentThread()
                .id, caller, msg
        )
    }
}