package com.fly.credit.jiana.js

import android.net.Uri
import android.provider.CalendarContract
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.ApplyInfoBean
import com.fly.credit.jiana.bean.calendarsAuthInfo
import com.fly.credit.jiana.bean.calendarsInfo
import com.fly.credit.jiana.network.NewServiceManage
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.DateTool
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object CalendarsInfoClass {
    fun calendarsInfo(webView: WebView, id: String) {
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.READ_CALENDAR)
                .request(object : OnPermissionCallback{
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
                            GlobalScope.launch(Dispatchers.IO){
                                var calendersInfoBeans =getCalendersList()
                                var calendarsAuthInfo = calendarsAuthInfo()
                                calendarsAuthInfo.create_time = DateTool.getServerTimestamp()
                                calendarsAuthInfo.list = calendersInfoBeans
                                withContext(Dispatchers.Main){
                                    LogUtil.d("日历获取成功 ：$calendarsAuthInfo")
                                    var applyInfoBean = ApplyInfoBean()
                                    applyInfoBean.calendars = calendarsAuthInfo
                                    NewServiceManage.uploadApplyInfo(applyInfoBean,webView,id,Cons.INVOKEFORCREDITBAGCALENDARINFO)
                                }
                            }
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGCALENDARINFO)
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView,id, Cons.INVOKEFORCREDITBAGCALENDARINFO)
                    }
                })
        }
    }

    fun getCalendersList():ArrayList<calendarsInfo>{
        var calendersInfoBeans = ArrayList<calendarsInfo>()
        val eventCursor = MyApplication.application.contentResolver.query(
            Uri.parse("content://com.android.calendar/events"),
            null,
            null,
            null,
            null
        )
        LogUtil.d("日历信息查询：$eventCursor")
        try {
            eventCursor?.let {
                while (it.moveToNext()) {
                    LogUtil.d("日历信息查询：")
                    var id = it.getString(it.getColumnIndexOrThrow("calendar_id"))
                    var eventTitle = it.getString(it.getColumnIndexOrThrow("title"))
                    var startTime = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))

                    var calendersInfoBean = calendarsInfo()
                    if (startTime != null) {
                        calendersInfoBean.create_time =startTime
                    }
                    calendersInfoBean.organizer =it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.ORGANIZER))
                    calendersInfoBean.calendar_id = id
                    calendersInfoBean.event_location =
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION))
                    calendersInfoBean.description = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION))
                    calendersInfoBean.title = eventTitle
                    calendersInfoBean.dt_end =it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTEND))
                    if (startTime != null) {
                        calendersInfoBean.dt_start =  startTime.toLong()
                    }

                    calendersInfoBean.all_day = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY))
                    calendersInfoBean.r_rule =it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.RRULE))
                    calendersInfoBean.duration = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.DURATION))
                    calendersInfoBean.r_date =it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.RDATE))
                    calendersInfoBeans.add(calendersInfoBean)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        } finally {
            eventCursor?.close()
        }
        return calendersInfoBeans
    }
}