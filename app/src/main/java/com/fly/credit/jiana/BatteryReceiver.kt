package com.fly.credit.jiana

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.Cons.KEY_BATTERY_VOLTAGE
import com.fly.credit.jiana.util.MMKVCacheUtil
import java.util.*

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class BatteryReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent == null) return
        val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val plugged: Int = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        val health: Int = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val voltage: Int = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        val temperature: Int = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10
        try {
            MMKVCacheUtil.putInt(KEY_BATTERY_VOLTAGE,voltage)
            val batteryTotal: Float = (intent.extras?.getInt("scale")?.toFloat() ?: 0) as Float
            val level: Float = (intent.extras?.getInt("level")?.toFloat() ?: 0) as Float
            if (level != null && batteryTotal != null) {
                MMKVCacheUtil.putString(Cons.KEY_BATTERY_LEVEL, String.format(Locale.getDefault(), "%.2f", level / batteryTotal)
                )
                if (MyApplication.open_power === -1) {
                    MyApplication.open_power = (level / batteryTotal * 100).toInt()
                }
                MyApplication.complete_apply_power = (level / batteryTotal * 100).toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
            MMKVCacheUtil.putInt(Cons.KEY_BATTERY_IS_USB, 1)
        } else {
            MMKVCacheUtil.putInt(Cons.KEY_BATTERY_IS_USB, 0)
        }
        if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
            MMKVCacheUtil.putInt(Cons.KEY_BATTERY_IS_AC, 1)
        } else {
            MMKVCacheUtil.putInt(Cons.KEY_BATTERY_IS_AC, 0)
        }
        MMKVCacheUtil.putInt(Cons.KEY_BATTERY_STATUS,status)
        MMKVCacheUtil.putInt(Cons.KEY_BATTERY_HEALTH,health)
        MMKVCacheUtil.putInt(Cons.KEY_BATTERY_TEMPER,temperature)
    }
}