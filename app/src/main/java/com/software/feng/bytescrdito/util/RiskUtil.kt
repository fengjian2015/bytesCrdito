package com.software.feng.bytescrdito.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.bluetooth.BluetoothManager
import android.content.*
import android.content.Context.ACTIVITY_SERVICE
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Proxy
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.*
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.provider.*
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.WindowManager
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.gson.Gson
import com.software.feng.bytescrdito.MyApplication
import com.software.feng.bytescrdito.common.Cons
import com.software.feng.bytescrdito.http.NetRequestManage
import com.software.feng.bytescrdito.http.model.risk.*
import com.software.feng.bytescrdito.js.JSCallBack
import com.software.feng.utillibrary.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*


/**
 * Time：2024/5/20
 * Author：feng
 * Description：
 */
object RiskUtil {

    fun tackPhoto(data: Intent, webView: WebView, mId: String) {
        //自拍信息
        GlobalScope.launch(Dispatchers.IO) {
            var file: File? = null
            if (data == null){
                JSCallBack.callBackJsError(Cons.InvokeCreditoTackPhoto,webView, mId,
                    "file null")
                return@launch
            }else{
                val photo: Bitmap? = data.extras?.get("data") as Bitmap
                photo?.let { t->
                    val file = compressBmpToFile(t)
                    if (file != null){
                        LogUtil.d(" onActivityResult file.size"+ file.length())
                        file?.let {
                            NetRequestManage.upImage(webView,mId,file)
                        }
                    }
                }
            }

        }
    }

    fun compressBmpToFile(bmp: Bitmap): File? {
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/shoppingMall/compressImgs/";
        val path: String =
            getInnerImgPath(MyApplication.application) + "/compressImg/"
        val file = File(path + DateUtil.getServerTimestamp().toString() + ".jpg")
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (file.parentFile != null && !file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        val baos = ByteArrayOutputStream()
        var options = 100
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos)
        while (baos.toByteArray().size / 1024 > 700) {
            baos.reset()
            options -= 10
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos)
        }
        try {
            val fos = FileOutputStream(file)
            fos.write(baos.toByteArray())
            fos.flush()
            fos.close()
            //            copyExif(srcPathStr,file.getAbsolutePath());
//            return file.getAbsolutePath();
            return file
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return null
    }

    fun getInnerImgPath(context: Context?): String {
        if (context == null) {
            return ""
        }
        createDir(
            getInnerFilePath(
                context
            ) + "images/"
        )
        return getInnerFilePath(context) + "images/"
    }

    fun getInnerFilePath(context: Context?): String? {
        if (context == null) {
            return ""
        }
        val internalFilePath = context.filesDir.absolutePath
        return if (!internalFilePath.endsWith(File.separator)) {
            internalFilePath + File.separator
        } else internalFilePath
    }

    fun createDir(dirPath: String) {
        var dirPath = dirPath
        if (TextUtils.isEmpty(dirPath)) {
//            Logan.w("dirPath is empty");
            return
        }
        val dir = File(dirPath)
        //文件夹是否已经存在
        if (dir.exists()) {
//            Logan.w("The directory [ " + dirPath + " ] has already exists");
        }
        //不是以路径分隔符"/"结束，则添加路径分隔符"/"
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator
        }

        //判断父目录是否存在
        if (!dir.parentFile.exists()) {
            //父目录不存在 创建父目录
//            Logan.w("creating parent directory...");
            if (!dir.parentFile.mkdirs()) {
//                Logan.w("created parent directory failed...");
            }
        }
        //创建文件夹
        if (dir.mkdirs()) {
//            Logan.w("create directory [ " + dirPath + " ] success");
        }
//        Logan.w("create directory [ " + dirPath + " ] failed");
    }

    fun selectContact(data: Intent, webView: WebView, eventSelectContactId: String) {
        val contactBean = ContactInfo()
        if (data?.data == null) {
            JSCallBack.callBackJsError(Cons.InvokeCreditoSelectContact,webView,eventSelectContactId, "Ninguno")
            return
        }

        val aar = arrayOf("display_name", "data1")
        val cursor = MyApplication.application.contentResolver.query(data?.data!!, aar, null, null, null) ?:return

        while (cursor.moveToNext()){
            contactBean.name= cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            contactBean.mobile = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
        }
        cursor.close()

        if (!contactBean.mobile.isNullOrEmpty()){
            contactBean.mobile = contactBean.mobile!!.replace("-", " ")
            contactBean.mobile = contactBean.mobile!!.replace(" ", "")
        }

        JSCallBack.callBackJsSuccess(Cons.InvokeCreditoSelectContact
            ,webView,eventSelectContactId,Gson().toJson(contactBean))
    }


    fun getCalendersList():ArrayList<CalendarsListBean>{
        var calendersInfoBeans = ArrayList<CalendarsListBean>()
        val eventCursor = MyApplication.application.contentResolver.query(
            Uri.parse("content://com.android.calendar/events"),
            null,
            null,
            null,
            null
        )
        try {
            eventCursor?.let {
                while (it.moveToNext()) {
                    var id = it.getString(it.getColumnIndexOrThrow("calendar_id"))
                    var eventTitle = it.getString(it.getColumnIndexOrThrow("title"))
                    var startTime = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))

                    var calendersInfoBean = CalendarsListBean()
                    if (startTime != null) {
                        calendersInfoBean.create_time =startTime
                    }
                    calendersInfoBean.organizer =it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.ORGANIZER))
                    calendersInfoBean.calendar_id = id
                    calendersInfoBean.event_location =
                        it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION))
                    calendersInfoBean.description = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION))
                    calendersInfoBean.title = eventTitle
                    calendersInfoBean.all_day = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.ALL_DAY))
                    calendersInfoBean.r_rule =it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.RRULE))
                    calendersInfoBean.duration = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.DURATION))
                    calendersInfoBean.r_date =it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.RDATE))
                    calendersInfoBean.dt_end =it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTEND))
                    if (startTime != null) {
                        calendersInfoBean.dt_start =  startTime
                    }
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

    fun getCallLog():List<CalllogListBean>{
        val list = mutableListOf<CalllogListBean>()
        var cursor: Cursor? = null
        try {
            cursor = MyApplication.application.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER)
            cursor?.apply {
                while (moveToNext()){
                    val name = getString(getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                    val phoneNum = getString(getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val callTimestamp = getLong(getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val duration = getInt(getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val callType = getInt(getColumnIndexOrThrow(CallLog.Calls.TYPE))
                    val id = getString(getColumnIndexOrThrow(CallLog.Calls._ID))
                    val callLogDt = CalllogListBean()
                    callLogDt.callName = name
                    callLogDt.callNumber = phoneNum
                    callLogDt.callTime = DateUtil.getTimeFromLongYMDHMS(callTimestamp)
                    callLogDt.callDuration = duration.toString()
                    callLogDt.callType = callType
                    callLogDt.id = id
                    if (callType == 1 ||callType == 3||callType == 5){
                        callLogDt.dial_type ="2"
                    }else{
                        callLogDt.dial_type ="1"
                    }
                    list.add(callLogDt)
                }
            }
            cursor?.close()
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return list

    }

    fun roundDown(v1: Any, scale: Int): String? {
        var result = "0"
        require(scale >= 0) { "The scale must be a positive integer or zero" }
        try {
            val b1 = BigDecimal(v1.toString())
            val b2 = BigDecimal(1)
            result = b1.divide(b2, scale, BigDecimal.ROUND_DOWN).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun toDouble(str: String): Double {
        try {
            return str.toDouble()
        } catch (e: java.lang.Exception) {
        }
        return 0.0
    }

    /**
     * 根据经纬度获取地理位置
     * @param latitude  纬度
     * @param longitude 经度
     * @return [Address]
     */
    fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(MyApplication.application, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 5)
            if (addresses != null) {
                if (addresses.size > 0) return addresses[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }



    fun getLocation(): Location? {
        var location: Location? = null
        val locationManager =
            MyApplication.application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager?.let {
            val providers = locationManager.getProviders(true)
            for (provider in providers) {
                @SuppressLint("MissingPermission")
                val l = locationManager.getLastKnownLocation(provider!!) ?: continue
                if (location == null || l.accuracy < location!!.accuracy) {
                    location = l
                }
            }
        }
        return location
    }

    fun isLocServiceEnable(): Boolean {
        val locationManager = MyApplication.application
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
    }

    fun openLocService() {
        if (!isLocServiceEnable()) {
            val intent = Intent()
            intent.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            MyApplication.application.startActivity(intent)
        }
    }

    fun getSmsInfo(): ArrayList<RiskSmsAllReqBean>? {
        val smsTime: Long = DateUtil.getServerTimestamp() - 365L * 24 * 60 * 60 * 1000
        val arrayList: ArrayList<RiskSmsAllReqBean> = ArrayList<RiskSmsAllReqBean>()
        try {
            LogUtil.d("---" + DateUtil.getTimeFromLongYMDHMS(DateUtil.getServerTimestamp()))
            val cur: Cursor = MyApplication.application.contentResolver.query(
                Uri.parse("content://sms"),
                null,
                Telephony.Sms.DATE + " > " + smsTime,
                null,
                "date desc"
            )!!
            while (cur.moveToNext()) {
                val smsBean = RiskSmsAllReqBean()
                smsBean.sms_id = cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.THREAD_ID)).toString()
                smsBean.is_read = cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.READ)).toString()
                smsBean.update_time = cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                smsBean.address = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                smsBean.content = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))
                smsBean.time = cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                smsBean.type = cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.TYPE))
                smsBean.phone = smsBean.address
                smsBean.sms_status = cur.getInt(cur.getColumnIndexOrThrow(Telephony.Sms.STATUS)).toString()
                arrayList.add(smsBean)
            }
            cur.close()
            getContactName(arrayList)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return arrayList
    }

    private fun getContactName(beans: ArrayList<RiskSmsAllReqBean>) {
        try {
            val allContacts: ArrayList<ContactInfo> = getContactInfoModelsName()
            for (bean in beans) {
                for (contactBean in allContacts) {
                    if (bean.address.equals(contactBean.phone)) {
                        bean.contactor_name = contactBean.name
                        continue
                    }
                }
                if (TextUtils.isEmpty(bean.contactor_name)) {
                    bean.contactor_name=bean.address
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getContactInfoModelsName(): ArrayList<ContactInfo> {
        val contactInfoModels: ArrayList<ContactInfo> = ArrayList<ContactInfo>()
        try {
            contactInfoModels.clear()
            val cursor: Cursor? = MyApplication.application.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val contactInfoModel = ContactInfo()
                    contactInfoModel.name =
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                            )
                        )

                    contactInfoModel.phone=
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            ))
                    contactInfoModels.add(contactInfoModel)
                }
            }
            cursor?.close()
            return contactInfoModels
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return contactInfoModels
    }

    fun getInstallationInfos() :ArrayList<AppListRiskAppsAllReqBean>{
        var installationInfos = ArrayList<AppListRiskAppsAllReqBean>()
        try {
            val allApps: List<PackageInfo>? =
                MyApplication.application.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
            allApps?.let {
                for (appInfo in allApps){
                    var installationInfoBean = AppListRiskAppsAllReqBean()
                    installationInfoBean.app_name = appInfo.applicationInfo.loadLabel(MyApplication.application.packageManager).toString()
                    installationInfoBean.version_name = appInfo.versionName
                    installationInfoBean.package_name = appInfo.packageName
                    installationInfoBean.version_code = appInfo.versionCode.toString()
                    installationInfoBean.first_install_time = appInfo.firstInstallTime.toString()
                    installationInfoBean.last_update_time = appInfo.lastUpdateTime.toString()
                    installationInfoBean.is_system = if ((appInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0){
                        //非系统
                        "1"
                    }else{
                        "2"
                    }
                    if (installationInfoBean.is_system.equals("2")){
                        installationInfoBean.is_pre_install = "1"
                    }else if ("00:00:00".equals(DateUtil.getTimeFromLongHMS(appInfo.firstInstallTime))){
                        installationInfoBean.is_pre_install = "1"
                    }else{
                        installationInfoBean.is_pre_install = "0"
                    }
                    installationInfos.add(installationInfoBean)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        return installationInfos
    }

    fun getDeviceRiskBatteryInfoReqBean(): DeviceRiskBatteryInfoReqBean {
        val deviceRiskBatteryInfoReqBean = DeviceRiskBatteryInfoReqBean()
        deviceRiskBatteryInfoReqBean.is_usb_charge = Cons.KEY_BATTERY_IS_USB
        deviceRiskBatteryInfoReqBean.is_ac_charge = Cons.KEY_BATTERY_IS_AC
        deviceRiskBatteryInfoReqBean.batteryPercentage = Cons.KEY_BATTERY_LEVEL
        deviceRiskBatteryInfoReqBean.battery_temper = Cons.KEY_BATTERY_TEMPER.toString()
        deviceRiskBatteryInfoReqBean.battery_health = Cons.KEY_BATTERY_HEALTH.toString()
        deviceRiskBatteryInfoReqBean.batteryStatus =Cons.KEY_BATTERY_STATUS.toString()
        deviceRiskBatteryInfoReqBean.power_time =DateUtil.getServerTimestamp()
        deviceRiskBatteryInfoReqBean.device_id = getAndroidID()
        deviceRiskBatteryInfoReqBean.voltage = Cons.KEY_BATTERY_VOLTAGE.toString()
        deviceRiskBatteryInfoReqBean.battery_capacity =getBatteryCapacity().toString()
        return deviceRiskBatteryInfoReqBean
    }

    fun getBatteryCapacity(): Float {
        val batteryIntent: Intent? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MyApplication.application.registerReceiver(
                    null,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )
            } else {
                MyApplication.application.registerReceiver(
                    null,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )
            }

        val level: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return if (level != -1 && scale != -1) {
            (level.toFloat() / scale.toFloat()) * getBatteryCapacityFromHealth(
                batteryIntent?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            )
        } else {
            0.0f
        }
    }

    private fun getBatteryCapacityFromHealth(health: Int?): Float {
        return when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> 1000.0f // 默认值，可以根据需要调整
            else -> 0.0f // 默认值，可以根据需要调整
        }
    }

    fun getDeviceRiskHardwareReqBean() : DeviceRiskHardwareReqBean {
        val deviceRiskHardwareReqBean =DeviceRiskHardwareReqBean ()
        deviceRiskHardwareReqBean.device_model =Build.MODEL
        deviceRiskHardwareReqBean.imei = getIMEI()
        deviceRiskHardwareReqBean.sys_version = Build.VERSION.SDK_INT.toString()
        deviceRiskHardwareReqBean.screenResolution= getScreenResolution()
        deviceRiskHardwareReqBean.manufacturerName = Build.BRAND
        deviceRiskHardwareReqBean.screenxpx =getScreenResolutionWidth()
        deviceRiskHardwareReqBean.screenypx =getScreenResolutionHeight()
        deviceRiskHardwareReqBean.is_xposed_or_root =if (hasXposed()) "true" else "false"
        deviceRiskHardwareReqBean.cpu_frequency =getCPUMaxFreq()
        deviceRiskHardwareReqBean.physical_size =getPhysicalSize()
        deviceRiskHardwareReqBean.cpu_model = MACUtil.getCpuName()
        deviceRiskHardwareReqBean.keyboard =onEvaluateInputViewShown()
        deviceRiskHardwareReqBean.device_no =getGAID()
        return deviceRiskHardwareReqBean

    }

    fun onEvaluateInputViewShown(): String? {
        val config: Configuration = MyApplication.application.resources.configuration
        if (config.keyboard == Configuration.KEYBOARD_NOKEYS) {
            return "1"
        } else if (config.keyboard == Configuration.KEYBOARD_UNDEFINED) {
            return "0"
        } else if (config.keyboard == Configuration.KEYBOARD_QWERTY) {
            return "2"
        } else if (config.keyboard == Configuration.KEYBOARD_12KEY) {
            return "3"
        }
        return "0"
    }


    fun getPhysicalSize(): String? {
        val metrics = DisplayMetrics()
        com.software.feng.bytescrdito.util.ActivityManager.getCurrentActivity()?.windowManager
            ?.defaultDisplay?.getMetrics(metrics)
        val screenWidthInInches = metrics.widthPixels / metrics.xdpi
        val screenHeightInInches = metrics.heightPixels / metrics.ydpi
        return "$screenHeightInInches * $screenWidthInInches"
    }


    fun getCPUMaxFreq(): String? {
        return try {
            val br =
                BufferedReader(FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"))
            val maxFreq = br.readLine()
            br.close()
            maxFreq
        } catch (e: IOException) {
            e.printStackTrace()
            "N/A"
        }
    }

    fun hasXposed(): Boolean {
        val packageManager = MyApplication.application.packageManager
        val appliacationInfoList =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (item in appliacationInfoList) {
            if (item.packageName == "de.robv.android.xposed.installer") {
                return true
            }
        }
        return false
    }

    fun getScreenResolutionHeight(): String? {
        val windowManager: WindowManager =
            (com.software.feng.bytescrdito.util.ActivityManager.getCurrentActivity()?.window
                ?.windowManager ?: null) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val height = metrics.heightPixels
        return height.toString() + ""
    }

    fun getScreenResolutionWidth(): String? {
        val windowManager: WindowManager =
            (com.software.feng.bytescrdito.util.ActivityManager.getCurrentActivity()?.window
                ?.windowManager ?: null) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val width = metrics.widthPixels
        return width.toString() + ""
    }

    fun getScreenResolution(): String? {
        val windowManager: WindowManager =
            (com.software.feng.bytescrdito.util.ActivityManager.getCurrentActivity()?.window
                ?.windowManager ?: null) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        return "$height * $width"
    }

    @SuppressLint("MissingPermission")
    fun getIMEI(): String? {
        var IMEI: String?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEI = getAndroidID()
        } else {
            IMEI = (MyApplication.application
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = getAndroidID()
            }
        }
        return IMEI
    }

    fun getDeviceRiskDeviceGeneralReqBean() : DeviceRiskDeviceGeneralReqBean {
        val deviceRiskDeviceGeneralReqBean = DeviceRiskDeviceGeneralReqBean()
        deviceRiskDeviceGeneralReqBean.phone_type =getPhoneType()
        deviceRiskDeviceGeneralReqBean.language = MyApplication.application.resources.configuration.locale.language
        deviceRiskDeviceGeneralReqBean.locale_display_language =getLocaleDisplayLanguage()
        deviceRiskDeviceGeneralReqBean.network_operator_name =getOperatorName()
        deviceRiskDeviceGeneralReqBean.locale_iso_3_country=MyApplication.application.resources.configuration.locale.isO3Country
        deviceRiskDeviceGeneralReqBean.locale_iso_3_language =MyApplication.application.resources.configuration.locale.isO3Language
        deviceRiskDeviceGeneralReqBean.last_boot_time =(System.currentTimeMillis() - SystemClock.elapsedRealtime()).toString()
        deviceRiskDeviceGeneralReqBean.is_simulator =if (isDeviceRooted()) "1" else "0"
        deviceRiskDeviceGeneralReqBean.locale_display_name = Locale.getDefault().displayName
        return deviceRiskDeviceGeneralReqBean
    }

    fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
            "/system/sbin/", "/usr/bin/", "/vendor/bin/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    fun getOperatorName(): String? {
        val systemService = MyApplication.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return systemService.simOperatorName
    }

    fun getLocaleDisplayLanguage(): String? {
        val language = Locale.getDefault().language
        return if (language.contains("zh")) {
            "中文"
        } else if (language.contains("en")) {
            "英文"
        } else {
            language
        }
    }

    fun getPhoneType(): String? {
        val systemService = MyApplication.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            ?: return "0"
        return when (systemService.phoneType) {
            TelephonyManager.PHONE_TYPE_GSM -> "1"
            TelephonyManager.PHONE_TYPE_CDMA -> "3"
            TelephonyManager.PHONE_TYPE_SIP -> "4"
            else -> "0"
        }
    }

    @SuppressLint("MissingPermission")
    fun getDeviceRiskWifiReqBeanList(): ArrayList<DeviceRiskWifiReqBean>{
        val wifiList: ArrayList<DeviceRiskWifiReqBean> = ArrayList()
        try {
            val wifiManager =
                MyApplication.application.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val scanWifiList = wifiManager.scanResults
            val curWifi = getWifiName()
            scanWifiList?.let {
                for (i in scanWifiList.indices) {
                    var wifiListBean = DeviceRiskWifiReqBean()
                    val scanResult = scanWifiList[i]
                    if (!TextUtils.isEmpty(scanResult.SSID)) {
                        wifiListBean.wifi_name = scanResult.SSID
                        wifiListBean.bssid = scanResult.BSSID
                        wifiListBean.type = if (curWifi.equals(scanResult.SSID)) 1 else 2
                        wifiListBean.mac = "02:00:00:00"
                        wifiList.add(wifiListBean)
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return wifiList
    }

    fun getSensorsList(): ArrayList<DeviceSensorsBean> {
        var sensorListBeans = ArrayList<DeviceSensorsBean>()
        val sensorList = getSensorList()
        for (sensor in sensorList) {
            var sensorListBean = DeviceSensorsBean()
            sensorListBean.type = sensor.type
            sensorListBean.name = sensor.name
            sensorListBean.version = sensor.version.toString()
            sensorListBean.vendor = sensor.vendor
            sensorListBean.max_range = sensor.maximumRange.toString()
            sensorListBean.min_delay = sensor.minDelay.toString()
            sensorListBean.power = sensor.power.toString()
            sensorListBean.resolution = DecimalFormat("#.#######").format(sensor.resolution)
            sensorListBeans.add(sensorListBean)
        }
        return sensorListBeans
    }

    fun getSensorList(): List<Sensor> {
        val sm =
            MyApplication.application.getSystemService(Context.SENSOR_SERVICE) as SensorManager //获取系统的传感器服务并创建实例
        return sm.getSensorList(Sensor.TYPE_ALL) //获取传感器的集合
    }

    fun app_free_memory(): Float{
        val activityManager = MyApplication.application.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        //最大分配内存
        //最大分配内存
        val memory = activityManager!!.memoryClass
        println("memory: $memory")
        //最大分配内存获取方法2
        //最大分配内存获取方法2
        val maxMemory = (Runtime.getRuntime().maxMemory() * 1.0).toFloat()
        //当前分配的总内存
        //当前分配的总内存
        val totalMemory = (Runtime.getRuntime().totalMemory() * 1.0 ).toFloat()
        //剩余内存
        //剩余内存
        val freeMemory = (Runtime.getRuntime().freeMemory() * 1.0 ).toFloat()
        return totalMemory - freeMemory
    }

    fun getAvailableSize() : Float{
        val activityManager = MyApplication.application.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        //最大分配内存
        //最大分配内存
        val memory = activityManager!!.memoryClass
        println("memory: $memory")
        //最大分配内存获取方法2
        //最大分配内存获取方法2
        val maxMemory = (Runtime.getRuntime().maxMemory() * 1.0).toFloat()
        //当前分配的总内存
        //当前分配的总内存
        val totalMemory = (Runtime.getRuntime().totalMemory() * 1.0 ).toFloat()
        //剩余内存
        //剩余内存
        val freeMemory = (Runtime.getRuntime().freeMemory() * 1.0 ).toFloat()
        println("maxMemory: $maxMemory")
        println("totalMemory: $totalMemory")
        println("freeMemory: $freeMemory")
        return freeMemory
    }

    fun appMaxMemory(): Double {
        val activityManager = MyApplication.application.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        //最大分配内存
        //最大分配内存
        val memory = activityManager!!.memoryClass
        println("memory: $memory")
        //最大分配内存获取方法2
        //最大分配内存获取方法2
        return (Runtime.getRuntime().maxMemory() * 1.0 )
    }

    fun readSDCardMemoryUseSize() : Long{
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            val blockSize = sf.blockSize.toLong()
            val blockCount = sf.blockCount.toLong()
            val availCount = sf.availableBlocks.toLong()
            Log.d(
                "",
                "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB"
            )
            Log.d("", "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB")
            return blockSize * blockCount - availCount * blockSize
        }
        return 0
    }

    fun readSDCardMemoryUsableSize() : Long{
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            val blockSize = sf.blockSize.toLong()
            val blockCount = sf.blockCount.toLong()
            val availCount = sf.availableBlocks.toLong()
            Log.d(
                "",
                "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB"
            )
            Log.d("", "可用的block数目：:" + availCount + ",剩余空间:" + availCount * blockSize / 1024 + "KB")
            return availCount * blockSize
        }
        return 0
    }

    fun getTotalRam(): String? {
        return queryWithStorageManager(MyApplication.application)
            ?.get(0)
    }
    fun getTotalMemory(): Long {
        val str1 = "/proc/meminfo" // 系统内存信息文件
        val str2: String
        val arrayOfString: Array<String>
        var initial_memory: Long = 0
        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader = BufferedReader(localFileReader, 8192)
            str2 = localBufferedReader.readLine() // 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+").toTypedArray()
            for (num in arrayOfString) {
                Log.i(str2, num + "\t")
            }
            // 获得系统总内存，单位是KB
            val i = Integer.valueOf(arrayOfString[1]).toInt()
            //int值乘以1024转换为long类型
            initial_memory = i.toLong() * 1024
            localBufferedReader.close()
        } catch (e: IOException) {
        }
        return initial_memory // Byte转换为KB或者MB，内存大小规格化
    }

    fun isVpn(): String? {
        val systemService = MyApplication.application
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = systemService.getNetworkInfo(ConnectivityManager.TYPE_VPN)
            ?: return "false"
        return if (networkInfo.isConnected) "true" else "false"
    }

    fun stringToInt(i: String): Int {
        try {
            return i.toInt()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 0
    }

    fun isProxy(): String? {
        try {
            val proxyAddress: String
            val proxyPort: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                proxyAddress = System.getProperty("http.proxyHost")
                var property = System.getProperty("http.proxyPort")
                if (TextUtils.isEmpty(property)) property = "-1"
                proxyPort = stringToInt(property)
            } else {
                proxyAddress = Proxy.getHost(MyApplication.application)
                proxyPort = Proxy.getPort(MyApplication.application)
            }
            return if (!TextUtils.isEmpty(proxyAddress) && proxyPort != -1) "true" else "false"
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return "false"
    }

    fun isUsbDebug(): String? {
        return if (Settings.Secure.getInt(
                MyApplication.application.contentResolver, Settings.Secure.ADB_ENABLED, 0
            ) > 0
        ) "true" else "false"
    }

    fun getAvailMemory(): Long {
        val am = MyApplication.application
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        // mi.availMem; 当前系统的可用内存
        return mi.availMem // 将获取的内存大小规格化
    }

    fun getAvailableSpace(): String? {
        return queryWithStorageManager(MyApplication.application)?.get(1)
    }

    /**
     * 获取内存大小和剩余空间
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi", "SoonBlockedPrivateApi")
    private fun queryWithStorageManager(context: Context): Array<String>? {
        var size = arrayOf("0", "0")
        //5.0 查外置存储
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val version = Build.VERSION.SDK_INT
        if (version < Build.VERSION_CODES.M) { //小于6.0
            try {
                val getVolumeList = StorageManager::class.java.getDeclaredMethod("getVolumeList")
                val volumeList: Array<StorageVolume>? =
                    getVolumeList.invoke(storageManager) as Array<StorageVolume>
                var totalSize: Long = 0
                var availableSize: Long = 0
                if (volumeList != null) {
                    var getPathFile: Method? = null
                    for (volume: StorageVolume in volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.javaClass.getDeclaredMethod("getPathFile")
                        }
                        val file = getPathFile!!.invoke(volume) as File
                        totalSize += file.totalSpace
                        availableSize += file.usableSpace
                    }
                }
                size[0] = totalSize.toString() + ""
                size[1] = availableSize.toString() + ""
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        } else {
            try {
                val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes") //6.0
                val getVolumeInfo = getVolumes.invoke(storageManager) as List<Any>
                var total = 0L
                var used = 0L
                var systemSize = 0L
                for (obj: Any in getVolumeInfo) {
                    val getType = obj.javaClass.getField("type")
                    val type = getType.getInt(obj)
                    if (type == 1) { //TYPE_PRIVATE
                        var totalSize = 0L
                        //获取内置内存总大小
                        if (version >= Build.VERSION_CODES.O) { //8.0
                            val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                            val fsUuid = getFsUuid.invoke(obj)
                            var fs : String?= null
                            if(fsUuid == null){
                                fs = null
                            }else{
                                fs =  fsUuid.toString()
                            }
                            totalSize = getTotalSize(
                                context,
                                fs
                            ) //8.0 以后使用
                        } else if (version >= Build.VERSION_CODES.N_MR1) { //7.1.1
                            val getPrimaryStorageSize =
                                StorageManager::class.java.getMethod("getPrimaryStorageSize") //5.0 6.0 7.0没有
                            totalSize = getPrimaryStorageSize.invoke(storageManager) as Long
                        }
                        val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                        val readable = isMountedReadable.invoke(obj) as Boolean
                        if (readable) {
                            val file = obj.javaClass.getDeclaredMethod("getPath")
                            val f = file.invoke(obj) as File
                            if (totalSize == 0L) {
                                totalSize = f.totalSpace
                            }
                            systemSize = totalSize - f.totalSpace
                            used += totalSize - f.freeSpace
                            total += totalSize
                        }
                    } else if (type == 0) { //TYPE_PUBLIC
                        //外置存储
                        val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                        val readable = isMountedReadable.invoke(obj) as Boolean
                        if (readable) {
                            val file = obj.javaClass.getDeclaredMethod("getPath")
                            val f = file.invoke(obj) as File
                            used += f.totalSpace - f.freeSpace
                            total += f.totalSpace
                        }
                    } else if (type == 2) { //TYPE_EMULATED
                    }
                }
                size[0] = (total + systemSize).toString() + ""
                size[1] = (total - used).toString() + ""
                LogUtil.d(
                    "总内存 total = " + total + "\n已用 used(with system) = " + used + "可用 available = "
                            + (total - used) + "系统大小：" + systemSize
                )
            } catch (e: SecurityException) {
                Log.e(ContentValues.TAG, "缺少权限：permission.PACKAGE_USAGE_STATS")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                size = queryWithStatFs(size)
            }
        }
        return size
    }

    private fun queryWithStatFs(strings: Array<String>): Array<String> {
        val statFs = StatFs(Environment.getExternalStorageDirectory().path)
        //存储块
        val blockCount = statFs.blockCount.toLong()
        //块大小
        val blockSize = statFs.blockSize.toLong()
        //可用块数量
        val availableCount = statFs.availableBlocks.toLong()
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        strings[0] = (blockSize * blockCount).toString() + ""
        strings[1] = (blockSize * availableCount).toString() + ""
        return strings
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTotalSize(context: Context, fsUuid: String?): Long {
        return try {
            val id: UUID
            id = if (fsUuid == null) {
                StorageManager.UUID_DEFAULT
            } else {
                UUID.fromString(fsUuid)
            }
            val stats = context.getSystemService(
                StorageStatsManager::class.java
            )
            stats.getTotalBytes(id)
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
            -1
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            -1
        } catch (e: NullPointerException) {
            e.printStackTrace()
            -1
        } catch (e: IOException) {
            e.printStackTrace()
            -1
        }
    }

    fun getVersionName(): String {
        var versionName = ""
        try {
            versionName =
                MyApplication.application.packageManager.getPackageInfo(MyApplication.application.packageName, 0).versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    fun getVersionCode(): Int {
        var code = 0
        try {
            code = MyApplication.application.packageManager.getPackageInfo(MyApplication.application.packageName, 0).versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return code
    }

    fun  isInsertExpansionCard(): String{
        return if (isExternalStorageAvailable()){
            "1"
        } else{
            "0"
        }
    }

    fun isExternalStorageAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isExternalStorageAvailableQ()
        } else {
            isExternalStorageAvailableLegacy()
        }
    }

    fun isExternalStorageAvailableQ(): Boolean {
        val externalDirs = MyApplication.application.getExternalFilesDirs(null)
        for (externalDir in externalDirs) {
            if (externalDir != null && Environment.MEDIA_MOUNTED == Environment.getStorageState(externalDir)) {
                return true
            }
        }
        return false
    }

    fun isExternalStorageAvailableLegacy(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun wifiHotspot(): String{
        return if (isHotSpotApOpen2()){
            "1"
        }else{
            "2"
        }
    }

    //获取热点是否打开方式2
    @SuppressLint("WifiManagerLeak")
    fun isHotSpotApOpen2(): Boolean {
        var isAPEnable = false
        try {
            val wifiManager = MyApplication.application.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val method: Method = wifiManager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.setAccessible(true)
            isAPEnable = method.invoke(wifiManager) as Boolean
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isAPEnable
    }

    fun isDualcardStandby(): String{
        return if (isDualSimSupported()){
            "1"
        } else{
            "0"
        }
    }

    @SuppressLint("MissingPermission")
    fun isDualSimSupported(): Boolean {
        val telephonyManager =
            MyApplication.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 在API Level 22及以上，可以使用SubscriptionManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val subscriptionManager =
                MyApplication.application.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            val activeSubscriptionInfoList: List<SubscriptionInfo> =
                subscriptionManager.activeSubscriptionInfoList ?: emptyList()

            return activeSubscriptionInfoList.size >= 2
        }

        // 在API Level 22以下，一些制造商可能提供特定的方法来检查
        // 例如：Samsung设备上可以使用 telephonyManager.getPhoneCount() 来判断双卡支持

        // 默认返回false
        return false
    }

    fun getBluetoothEnable():Int{
        try {
                val manager = MyApplication.application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val adapter = manager.adapter
                if (adapter != null){
                    if (adapter.isEnabled){
                        return 1
                    } else{
                        return 2
                    }
                }

        }catch (e:Exception){
            e.printStackTrace()
        }

        return 0
    }


    fun isFlightModeEnabled(): String {
        val state = Settings.Global.getInt(MyApplication.application.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0) != 0

        return if (state){
            "1"
        } else{
            "0"
        }

    }

    fun getSimOperator(): String {
        val telephonyManager = MyApplication.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simOperator
    }

    fun getNetworkOperatorCountryCode(): String {
        try {
            val telephonyManager = MyApplication.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simOperator = telephonyManager.simOperator

            // 获取 MCC 部分
            val mcc = getCountryCodeFromMCC(simOperator)

            // 返回国家编号
            return mcc
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ""
    }

    fun getCountryCodeFromMCC(mcc: String): String {
        // MCC 格式通常为三位数字
        if (mcc.length >= 3) {
            return mcc.substring(0, 3)
        }
        return ""
    }

    fun getBackCameraResolution(): String {
        val cameraManager = MyApplication.application.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    val streamConfigurationMap =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

                    val sizes: Array<Size>? = streamConfigurationMap?.getOutputSizes(android.graphics.ImageFormat.JPEG)
                    val largestSize = sizes?.maxByOrNull { it.width * it.height }

                    return if (largestSize != null) {
                        "${largestSize.width}x${largestSize.height}"
                    } else {
                        ""
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun getPhoneMode(): Int {
        val audioManager = MyApplication.application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> 1 // 普通模式
            AudioManager.RINGER_MODE_VIBRATE -> 2 // 振动模式
            AudioManager.RINGER_MODE_SILENT -> 3 // 静音模式
            else -> 0 // 未知
        }
    }

    fun getSimCardType(): String {
        val telephonyManager = MyApplication.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val operator = telephonyManager.simOperator

        return when (operator) {
            "46000", "46002", "46007" -> "China Mobile"
            "46001", "46006", "46009" -> "China Unicom"
            "46003", "46005", "46011" -> "China Telecom"
            else -> "Other"
        }
    }

    fun getScreenBrightness(): Int {
        val contentResolver: ContentResolver = MyApplication.application.contentResolver
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
    }

    fun getFrontCameraResolution(): String {
        val cameraManager = MyApplication.application.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            // 获取可用摄像头列表
            val cameraIdList = cameraManager.cameraIdList

            for (cameraId in cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)

                // 判断摄像头是否为前置摄像头
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    // 获取前置摄像头分辨率
                    val streamConfigurationMap =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    val sizes = streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)


                    // 选择分辨率，这里选择第一个分辨率作为示例
                    val selectedSize = sizes?.get(0)

                    return "${selectedSize?.width}x${selectedSize?.height}"
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        return ""
    }

    fun checkSDK(): Int {
        val sdCardExist = (Environment.getExternalStorageState()
                == Environment.MEDIA_MOUNTED) //判断sd卡是否存在
        return if (sdCardExist) {
            1
        } else 0
    }

    @SuppressLint("MissingPermission")
    fun getNetworkState1(): String {
        val connManager =
            MyApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: // 为空则认为无网络
                return "无网络" // 获取网络服务
        // 获取网络类型，如果为空，返回无网络
        val activeNetInfo = connManager.activeNetworkInfo
        if (activeNetInfo == null || !activeNetInfo.isAvailable) {
            return "无网络"
        }
        // 判断是否为WIFI
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (null != wifiInfo) {
            val state = wifiInfo.state
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return "wifi"
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        val telephonyManager =
            MyApplication.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkType = telephonyManager.networkType
        return when (networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
            TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
            TelephonyManager.NETWORK_TYPE_LTE -> "4G"
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            else -> "手机流量"
        }
    }

    fun getCarrierName(): String {
        val telephonyManager = MyApplication.application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // 获取运营商名称
        return when (telephonyManager.simState) {
            TelephonyManager.SIM_STATE_READY -> {
                // SIM卡准备就绪
                telephonyManager.networkOperatorName ?: "Unknown"
            }
            else -> {
                // SIM卡未就绪或不可用
                "Not Available"
            }
        }
    }

    fun getGAID(): String? {
        var gaid = ""
        var adInfo: AdvertisingIdClient.Info? = null
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                MyApplication.application.applicationContext
            )
        } catch (e: IOException) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).
            Log.e("getGAID", "IOException")
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Google Play services is not available entirely.
            Log.e("getGAID", "GooglePlayServicesNotAvailableException")
        } catch (e: java.lang.Exception) {
            Log.e("getGAID", "Exception:$e")
            // Encountered a recoverable error connecting to Google Play services.
        }
        if (adInfo != null) {
            gaid = adInfo.id.toString()
        }
        return gaid
    }

    // 获取当前热点最新的信号强度
    fun getCurrentNetworkRssi(): String? {
        val wifiManager = MyApplication.application.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        val wifiInfo = wifiManager.connectionInfo ?: return ""
        return wifiInfo.rssi.toString()
    }

    fun getSensorCount(): String? {
        val sm = MyApplication.application
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager //获取系统的传感器服务并创建实例
        val list = sm.getSensorList(Sensor.TYPE_ALL) //获取传感器的集合
        return if (list != null) {
            list.size.toString() + ""
        } else 0.toString() + ""
    }

    fun getImagesExternalFiles(): ArrayList<File>? {
        val fileArrayList = ArrayList<File>()
        try {
            getFiles(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA
            )?.let {
                fileArrayList.addAll(
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }

    fun getDeviceName(): String? {
        return Settings.Secure.getString(
            MyApplication.application.contentResolver,
            "bluetooth_name"
        )
    }


    fun getTimeZone(): String? {
        val aDefault = TimeZone.getDefault()
        return aDefault.getDisplayName(false, TimeZone.SHORT)
    }

    fun getWifiList(): List<String>? {
        val wifiManager = MyApplication.application.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        val scanWifiList = wifiManager.scanResults
        val wifiList: MutableList<String> = ArrayList()
        return if (scanWifiList != null && scanWifiList.size > 0) {
            for (i in scanWifiList.indices) {
                val scanResult = scanWifiList[i]
                if (!TextUtils.isEmpty(scanResult.SSID) && !wifiList.contains(scanResult.SSID)) {
                    wifiList.add(scanResult.SSID)
                }
            }
            wifiList
        } else {
            Log.e(ContentValues.TAG, "非常遗憾搜索到wifi")
            wifiList
        }
    }


    @SuppressLint("HardwareIds")
    fun getAndroidID(): String? {
        val id = Settings.Secure.getString(
            MyApplication.application.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return if ("9774d56d682e549c" == id) "" else id ?: ""
    }

    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    fun getNetworkState(): Int {
        val connManager = MyApplication.application
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            ?: return 0
        // 若不是WIFI，则去判断是2G、3G、4G网
        val telephonyManager = MyApplication.application
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkType
    }

    fun getAudioExternalFiles(): ArrayList<File>? {
        val fileArrayList = ArrayList<File>()
        try {
            getFiles(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Audio.Media.DATA
            )?.let {
                fileArrayList.addAll(
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }

    fun getVideoInternalFiles(): ArrayList<File>? {
        val fileArrayList = ArrayList<File>()
        try {
            getFiles(
                MediaStore.Video.Media.INTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA
            )?.let {
                fileArrayList.addAll(
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }


    fun getImagesInternalFiles(): ArrayList<File>? {
        val fileArrayList = ArrayList<File>()
        try {
            getFiles(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                MediaStore.Images.Media.DATA
            )?.let {
                fileArrayList.addAll(
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }

    fun getAudioInternalFiles(): ArrayList<File>? {
        val fileArrayList = ArrayList<File>()
        try {
            getFiles(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                MediaStore.Audio.Media.DATA
            )?.let {
                fileArrayList.addAll(
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }


    fun getKernelVersion(): String? {
        try {
            return System.getProperty("os.version")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getTimeZoneId(): String? {
        val aDefault = TimeZone.getDefault()
        return aDefault.id
    }

    fun getDownloadFiles(): java.util.ArrayList<File> {
        val fileArrayList = java.util.ArrayList<File>()
        try {
            getFiles(
                MediaStore.Files.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                ), MediaStore.Files.FileColumns.DATA
            )?.let {
                fileArrayList.addAll(
                    it
                )
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }

    @SuppressLint("MissingPermission")
    fun getWifiName(): String? {
        val wifiManager = MyApplication.application.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        val info = wifiManager.connectionInfo
        return if (info.ssid == null) "" else info.ssid
    }

    fun getIMEI1(): String? {
        val IMEI: String
        try {
            IMEI = (MyApplication.application
                .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
            return IMEI
        } catch (ignored: java.lang.Exception) {
        }
        return null
    }

    fun getWifiInfo(): String? {
        val wifiManager = MyApplication.application.applicationContext.getSystemService(
            Context.WIFI_SERVICE
        ) as WifiManager
        val info = wifiManager.connectionInfo
        return if (info.bssid == null) "" else info.bssid
    }

    fun getVideoExternalFiles(): ArrayList<File> {
        val fileArrayList = ArrayList<File>()
        try {
            getFiles(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Video.Media.DATA
            )?.let {
                fileArrayList.addAll(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }

    private fun getFiles(volume: Uri, columnName: String): java.util.ArrayList<File>? {
        val fileArrayList = java.util.ArrayList<File>()
        try {
            val cursor: Cursor? = MyApplication.application.contentResolver
                .query(volume, null, null, null, null)
            if (cursor != null) {
                val columnIndexOrThrow = cursor.getColumnIndexOrThrow(columnName)
                while (cursor.moveToNext()) {
                    val path = cursor.getString(columnIndexOrThrow)
                    val i = path.lastIndexOf(".")
                    if (i == -1) {
                        continue
                    }
                    val i1 = path.lastIndexOf(File.separator)
                    if (i1 == -1) {
                        continue
                    }
                    val file = File(path)
                    fileArrayList.add(file)
                }
                cursor.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return fileArrayList
    }
}