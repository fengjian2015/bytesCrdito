package com.software.feng.bytescrdito.observer;

public class ObserverType {
    public static final String INIT_LOCATION_LISTENER = "INIT_LOCATION_LISTENER";

    public static final String XXXXXXXXXXX = "observer.XXX.XXXXXXXXXXX";
    //接收到会员信息页面通知 关闭加油页面
    public static final String MEMBERDETAILS_OILPAY_CLOSE = "observer.memberdetails.oilpay.close";
    //接收到会员信息页面通知 关闭会员充值页面
    public static final String MEMBERDETAILS_MEMBERRECHARGE_CLOSE = "observer.memberdetails.memberrecharge.close";
    //接收到会员信息页面通知 关闭快速消费页面
    public static final String MEMBERDETAILS_FAST_CLOSE = "observer.memberdetails.fast.close";
    //接收到会员信息页面通知 关闭商品消费页面
    public static final String MEMBERDETAILS_PRODUCT_CLOSE = "observer.memberdetails.product.close";
    //接收到会员信息页面通知 关闭充次续次页面
    public static final String MEMBERDETAILS_COUNT_CLOSE = "observer.memberdetails.count.close";
    //接收到会员信息页面通知 关闭积分兑换页面
    public static final String MEMBERDETAILS_REDEEM_CLOSE = "observer.memberdetails.redeem.close";
    //接收到会员信息页面通知 关闭积分变动页面
    public static final String MEMBERDETAILS_POINTCHANGE_CLOSE = "observer.memberdetails.pointchange.close";
    //接收到会员信息页面通知 关闭预约页面
    public static final String MEMBERDETAILS_SUBSCRIBEADD_CLOSE = "observer.memberdetails.subscribeadd.close";
    //接收到会员信息页面通知 关闭计次消费
    public static final String MEMBERDETAILS_TIMECONSUME_CLOSE = "observer.memberdetails.timeconsume.close";
    //接收到会员信息页面通知 关闭房台开单
    public static final String MEMBERDETAILS_ROOMOPEN_CLOSE = "observer.memberdetails.roomopen.close";

    //油品消费成功重置油品消费页面数据
    public static final String PAYSUCCESS_OILPAY_RESET = "observer.paysuccess.oilpay.reset";
    //充值或者充次成功后 若会员信息页面存在 先finish再打开 涉及到数据刷新的问题
    public static final String PAYSUCCESS_MEMBERDETAILS_CLOSE = "observer.paysuccess.memberdetails.close";
    //充值成功关闭充值
    public static final String PAYSUCCESS_MEMBERRECHARGE_CLOSE = "observer.paysuccess.memberrecharge.close";
    //充次成功关闭充次
    public static final String PAYSUCCESS_MEMBERCOUNT_CLOSE = "observer.paysuccess.membercount.close";
    //房台开单成功回调
    public static final String PAYSUCCESS_ROOMDILL_SUCCESS = "observer.paysuccess.roomdill.success";
    //会员密码验证成功通知新增预约页面
    public static final String MEMBERPASSWORD_VERIFY_FORSUBSCRIBEADD = "observer.memberpassword.verify.forsubscribeadd";
    //会员密码验证成功通知预付款弹窗
    public static final String MEMBERPASSWORD_VERIFY_FORYFKDIALOG = "observer.memberpassword.verify.foryfkdialog";
    //会员密码验证成功通知新增预约页面
    //撤销预付款 计次卡、余额后 DillNowFrag需要做的一些操作
    public static final String DILLNOWFRAG_SUBSCRIBERECORD_REVOKESUCCESS = "observer.dillnowfrag.subscriberecord.revokesuccess";
    //撤销预付款 计次卡、余额后 SubscribeAddEditActivity需要做的一些操作
    public static final String SUBSCRIBEEDITACT_SUBSCRIBERECORD_REVOKESUCCESS = "observer.subscribeeditact.subscriberecord.revokesuccess";
    //预约预付款退还方式选择完毕通知预约单退款并取消预约
    public static final String SUBSCRIBE_REBACKPAYTYPES_SELECTED_SUCCESS = "observer.subscribe.rebackpaytypes.selected.success";
    //房台订单列表取消预约选择完毕通知房台订单列表退款并取消预约
    public static final String ROOMACT_REBACKPAYTYPES_SELECTED_SUCCESS = "observer.roomact.rebackpaytypes.selected.success";
    //房台详情取消预约选择完毕通知房台详情退款并取消预约
    public static final String ROOMDETAILS_REBACKPAYTYPES_SELECTED_SUCCESS = "observer.roomdetails.rebackpaytypes.selected.success";
    //房台列表数据需要刷新(包括房台列表及详情)
    public static final String ROOMACTLIST_REFRESH = "observer.roomactlist.refresh";
    //开单弹窗输入成功后回传数据到房台开单页面
    public static final String HANDANDWATERCODEDIALOG_FORROOMDILLOPEN = "observer.handwatercode.dialog.forroomdillopen";
    //开单弹窗输入成功后回传数据到支付窗口
    public static final String HANDANDWATERCODEDIALOG_FORYFKDIALOG = "observer.handwatercode.dialog.foryfkdialog";
    //非预付款时的房台挂单成功
    public static final String ROOMREST_NOADVANCE_SUCCESS = "observer.roomrest.noadvance.success";
    //预付款时的房台挂单成功通知预付款添加窗口进行支付
    public static final String ROOMREST_ADVANCE_FORDIALOGPAY = "observer.roomrest.advance.fordialogpay";
    //付款码扫描后将扫描结果传递给预付款添加窗口DIALOG
    public static final String FULLSCREEN_PAYCODE_SACN_SUCCESS = "observer.fullscreen.paycode.scan.success";
    //预付款追加成功时通知房台开单及房台列表页面刷新
    public static final String ADVANCEAMOUNT_ADDED_REFRESHROOMDATA = "observer.advanceamount.added.refreshroomdata";
    //预付款撤销成功时通知房台开单及房台列表页面刷新
    public static final String ADVANCEAMOUNT_REVOKED_REFRESHROOMDATA = "observer.advanceamount.revoked.refreshroomdata";
    //预约订单列表开单过来挂单成功，需要通知预约订单列表刷新
    public static final String ROOMOPEN_RERESTED_REFRESHSUBSCRIBERDATA = "observer.roomopen.rerested.refreshsubscribedata";
    //结算页面退还预付款退款方式选择成功后回传数据到结算页面
    public static final String CONSUME_REBACKPAYMENTS_SELECTED = "observer.consume.rebackpayments.selected";
    //房台明细数据刷新
    public static final String ROOMDETAILS_REFRESH = "observer.roomdetails.refresh";
    //根据接口获取到自动匹配的员工
    public static final String STAFF_AOTUMATCHED_RESULT = "observer.staff.automatched.result";
    //企业安全评分请求成功回调
    public static final String COMPANY_SECURITY_SECOND_ENTER = "observer.company.security.second.enter";
    //企业安全评分数据获取
    public static final String COMPANY_SECURITY_SECOND_INFO = "observer.company.security.second.info";
    //油品消费选择或改变赠品时通知消费页面同步赠品数据
    public static final String OILPAYACT_GIVEN_CHANGE = "observer.oilpayact.given.change";
}
