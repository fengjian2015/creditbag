package com.fly.credit.jiana.bean

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class calllogInfo : java.io.Serializable {
    //通话姓名，若没有标记姓名则传空字符
    var callName: String? = ""

    //通话号码
    var callNumber: String? = ""

    //通话时长
    var callDuration: String? = ""

    //拨打时间
    var callTime: String? = ""

    /**
     * 通话类型：1、呼入(Incoming)，2、呼出(Outgoing)，3、未接通(Missed)，4、语音邮箱(Voicemail)
     * 5、拒接（Rejected），6、占线(Blocked)，7、外部应答(AnsweredExternally)
     */
    var callType = 0

    //新增
    //通话记录id
    var id: String? = ""
    override fun toString(): String {
        return "calllogInfo(callName=$callName, callNumber=$callNumber, callDuration=$callDuration, callTime=$callTime, callType=$callType, id=$id)"
    }

}