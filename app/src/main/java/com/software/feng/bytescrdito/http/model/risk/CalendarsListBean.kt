package com.software.feng.bytescrdito.http.model.risk

/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
class CalendarsListBean : java.io.Serializable {
    //事件创建时间格式毫秒时间戳
    var create_time: Long = 0

    //事件所属日历的id
    var calendar_id: String? = ""

    //事件组织者（所有者）的电子邮件
    var organizer: String? = ""

    //事件的发生地
    var event_location: String? = ""

    //事件描述
    var description: String? = ""

    //主题
    var title: String? = ""

    //事件结束事件 格式毫秒时间戳
    var dt_end: Long = 0

    //事件开始时间 格式毫秒时间戳
    var dt_start: Long = 0

    //值为1 表示此事件占用一整天，值为0表示它是常规事件，可 在一天内的任何时间开始和结束
    var all_day: String? = ""

    //事件的重复发生规则格式
    var r_rule: String? = ""

    //RFC5545格式的事件持续时间。例如：值为“PT1H”表示事件 应持续一小时，值为‘P2W’
    var duration: String? = ""

    //事件的重复发生日期  yyyy-MM-dd
    var r_date: String? = null
    override fun toString(): String {
        return "CalendarsListBean(create_time=$create_time, calendar_id=$calendar_id, organizer=$organizer, event_location=$event_location, description=$description, title=$title, dt_end=$dt_end, dt_start=$dt_start, all_day=$all_day, r_rule=$r_rule, duration=$duration, r_date=$r_date)"
    }

}