package com.fly.credit.jiana.util

import java.io.File

/**
 * Time：2024/3/13
 * Author：feng
 * Description：
 */
object Cons {
    var eventTackPhotoId = ""
    val KEY_AF_CHANNEL ="afChannel"

    const val BaseUrl = "https://api.ghcreditbag.com"
    const val ENCRYPT_KEY_TEST = "Kb6icVNdgHOwqirX"
    const val VECTOR_TEST ="0000000000000000"


    const val TACK_PHOTO = 1010
    val KEY_SERVICE_TIME ="ServicesTimes"

    val INSTALL_REFERRER_RESPONSE_JSON ="INSTALL_REFERRER_RESPONSE_JSON"
    val KEY_DIFFERENCE_TIME ="DifTimes"
    val KEY_USER_INFO="myInfo"

    val KEY_AUTHORIZATION ="Authorization"
    val KEY_TOKEN ="token"

    val INSTALL_REFERRER_RESPONSE_JSON_ERROR = "INSTALL_REFERRER_RESPONSE_JSON_ERROR"

    const val KEY_PROTOCAL_1 ="PROTOCAL_1"
    const val KEY_PROTOCAL_2 ="PROTOCAL_2"
    const val KEY_BATTERY_STATUS = "KEY_BATTERY_STATUS"
    const val KEY_BATTERY_IS_USB = "KEY_BATTERY_IS_USB"
    const val KEY_BATTERY_IS_AC = "KEY_BATTERY_IS_AC"
    const val KEY_BATTERY_LEVEL = "KEY_BATTERY_LEVEL"
    const val KEY_BATTERY_HEALTH = "KEY_BATTERY_HEALTH"
    const val KEY_BATTERY_TEMPER = "KEY_BATTERY_TEMPER"
    const val KEY_BATTERY_VOLTAGE = "KEY_BATTERY_VOLTAGE"

    const val SELECT_CONTACTS_CONTRACT = 1011

    const val KEY_PUBLIC_IP = "KEY_PUBLIC_IP"

    //事件回调
    const val INVOKEFORCREDITBAGUSERINFO ="invokeForCreditBagUserInfo"
    const val INVOKEFORCREDITBAGCOPYCLIP ="invokeForCreditBagCopyClip"
    const val INVOKEFORCREDITBAGSIGNOUT ="invokeForCreditBagSignOut"
    const val INVOKEFORCREDITBAGDEVICEINFO ="invokeForCreditBagDeviceInfo"
    const val INVOKEFORCREDITBAGINSTALLATIONINFO ="invokeForCreditBagInstallationInfo"
    const val INVOKEFORCREDITBAGSMSINFO ="invokeForCreditBagSmsInfo"
    const val INVOKEFORCREDITBAGLOCATIONINFO ="invokeForCreditBagLocationInfo"
    const val INVOKEFORCREDITBAGCALLLOG ="invokeForCreditBagCallLog"
    const val INVOKEFORCREDITBAGCALENDARINFO ="invokeForCreditBagCalendarInfo"
    const val INVOKEFORCREDITBAGSELECTCONTACT ="invokeForCreditBagSelectContact"
    const val INVOKEFORCREDITBAGAPPSFLYER ="invokeForCreditBagAppsFlyer"
    const val INVOKEFORCREDITBAGTACKPHOTO ="invokeForCreditBagTackPhoto"
    const val INVOKEFORCREDITBAGFORWARDOUTSIDE ="invokeForCreditBagForwardOutside"
    const val INVOKEFORCREDITBAGOPENBROWSER ="invokeForCreditBagOpenBrowser"
    const val INVOKEFORCREDITBAGAPPSERVICETIME ="invokeForCreditBagAppServiceTime"
}