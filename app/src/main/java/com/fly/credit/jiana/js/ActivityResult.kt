package com.fly.credit.jiana.js

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.provider.ContactsContract
import android.webkit.WebView
import com.fly.credit.jiana.MyApplication
import com.fly.credit.jiana.bean.ContactInfo
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.util.FileUtil1
import com.fly.credit.jiana.util.LogUtil
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object ActivityResult {
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent,
        webView: WebView,
        eventSelectContactId: String
    ){
        if (requestCode == Cons.SELECT_CONTACTS_CONTRACT) {
            if (resultCode == Activity.RESULT_OK) {
                selectContact(data,webView,eventSelectContactId)
            }
        }else if (requestCode == Cons.TACK_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                tackPhoto(data,webView)
            } else {
                AndroidCallBackJS.callbackJsErrorOther(
                    webView,
                    Cons.eventTackPhotoId,
                    Cons.INVOKEFORCREDITBAGTACKPHOTO,
                    "Ninguno."
                )
            }
        }
    }

    fun tackPhoto(data: Intent, webView: WebView) {
        //自拍信息
//                LoadingUtil.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            var file: File? = null
            if (data == null){
                AndroidCallBackJS.callbackJsErrorOther(
                    webView,
                    Cons.eventTackPhotoId,
                    Cons.INVOKEFORCREDITBAGTACKPHOTO,
                    "file null")
                return@launch
            }else{
                val photo: Bitmap? = data.extras?.get("data") as Bitmap
                photo?.let { t->
                    val file = FileUtil1.compressBmpToFile(t)
                    if (file != null){
                        LogUtil.d(" onActivityResult file.size"+ file.length())
                        file?.let {
                            UpImageClass.upImage(webView,Cons.eventTackPhotoId,file)
                        }
                    }
                }
            }

        }
    }

    fun selectContact(data: Intent, webView: WebView, eventSelectContactId: String) {
        val contactBean = ContactInfo()
        if (data?.data == null) {
            AndroidCallBackJS.callbackJsErrorOther(
                webView,
                eventSelectContactId,
                Cons.INVOKEFORCREDITBAGSELECTCONTACT,
                "Ninguno"
            )
            return
        }
        var cursor: Cursor? = null
        var phone: Cursor? = null
        try {
            val reContentResolverol: ContentResolver =
                MyApplication.application.contentResolver
            cursor = reContentResolverol.query(data.data!!, null, null, null, null)
            cursor?.let {
                it.moveToFirst()
                contactBean.name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                // 条件为联系人ID
                val contactId =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
                phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null
                )
                phone?.let { p ->
                    while (p.moveToNext()) {
                        contactBean.mobile =
                            p.getString(p.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            phone?.close()
            cursor?.close()
        }
        AndroidCallBackJS.callBackJsSuccess(
            webView,
            eventSelectContactId,
            Cons.INVOKEFORCREDITBAGSELECTCONTACT,
            Gson().toJson(contactBean)
        )
    }
}