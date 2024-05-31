package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class RiskSmsAllReqBean : java.io.Serializable {
    //短信id(20230825新增)
    var sms_id: String? = ""

    //对方名称
    var contactor_name: String? = ""
    //新增
    //是否已读（0-未读，  1-已读）
    var is_read: String? = ""

    //update_time到毫秒的时间戳
    var update_time: Long? = 0

    //临时记录查询到的电话号码
    var address
            : String? = ""
    //短信内容
    var content: String? = ""
    //发送时间
    var time: Long? = 0
    //类型：1:'Receive',2:'Send',3:'ReceiveDraft',5:'SendDraft'
    var type = 0
    //对方手机号
    var phone: String? = ""
    //短信状态：  -1表示接收，  0-complete ，32-pending ，64- failed(20230825新增)
    var sms_status: String? = ""

    override fun toString(): String {
        return "RiskSmsAllReqBean(phone=$phone, content=$content, time=$time, type=$type, contactor_name=$contactor_name, is_read=$is_read, sms_id=$sms_id, sms_status=$sms_status, update_time=$update_time, address=$address)"
    }
}