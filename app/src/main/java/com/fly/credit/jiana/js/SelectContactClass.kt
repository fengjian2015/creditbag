package com.fly.credit.jiana.js

import android.content.Intent
import android.provider.ContactsContract
import android.webkit.WebView
import com.fly.credit.jiana.util.ActivityManager
import com.fly.credit.jiana.util.Cons
import com.fly.credit.jiana.web.AndroidCallBackJS
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */
object SelectContactClass {
    fun selectContact(webView: WebView, id: String, eventSelectContactId: String) {
        ActivityManager.getCurrentActivity()?.let {
            XXPermissions.with(it)
                .permission(Permission.READ_CONTACTS)
                .permission(Permission.GET_ACCOUNTS)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                        if (allGranted){
//                            eventSelectContactId = id
//                            ActivityManager.getCurrentActivity()?.startActivityForResult(
//                                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
//                                Cons.SELECT_CONTACTS_CONTRACT
//                            )
                        }else{
                            AndroidCallBackJS.callbackJsErrorPermissions(webView, id, Cons.INVOKEFORCREDITBAGSELECTCONTACT)
                        }
                    }

                    override fun onDenied(
                        permissions: MutableList<String>,
                        doNotAskAgain: Boolean
                    ) {
                        AndroidCallBackJS.callbackJsErrorPermissions(webView, id, Cons.INVOKEFORCREDITBAGSELECTCONTACT)
                    }
                })
        }
    }
}