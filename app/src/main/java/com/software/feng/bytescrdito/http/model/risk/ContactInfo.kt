package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/3/14
 * Author：feng
 * Description：
 */
class ContactInfo : java.io.Serializable {
    //string	手机号分组
    var groups: String? = ""

    //名字
    var name: String? = ""

    //电话
    var mobile: String? = ""

    //string	通讯录来源
    var source: String? = ""

    //最后一次使用次数
    var last_used_times: Long = 0

    //电话
    var phone: String? = ""

    //上次更新时间
    var last_update_times: Long = 0

    //联系次数
    var contact_times = 0

    //上次联系时间
    var last_contact_time: Long = 0

    //新增
    //通讯录获取时间
    var create_time: Long = 0

    var contact_id //临时id
            : String? = ""

    override fun toString(): String {
        return "ContactInfo(groups=$groups, name=$name, mobile=$mobile, source=$source, last_used_times=$last_used_times, phone=$phone, last_update_times=$last_update_times, contact_times=$contact_times, last_contact_time=$last_contact_time, create_time=$create_time, contact_id=$contact_id)"
    }

}