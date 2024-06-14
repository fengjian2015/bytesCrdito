package pers.spj.kotlin.common

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.Serializable


inline fun <reified T : Activity> Activity.start(vararg pairs: Pair<String, Any>) {
    val intent = Intent(this, T::class.java)
    if (pairs.isNotEmpty()) {
        pairs.forEach {
            when (val value = it.second) {
                is Array<*> -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is Int -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Serializable -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                else -> throw IllegalArgumentException("Type Error : ${value.javaClass.canonicalName}")
            }
        }
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.start() {
    startActivity(Intent(this, T::class.java))
}

fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.isNetworkConnected(): Boolean {
    return networkConnected(this)
}
fun Activity.isNetworkConnected(): Boolean {
    return networkConnected(this)
}
fun Service.isNetworkConnected(): Boolean {
    return networkConnected(this)
}
fun networkConnected(context: Context?): Boolean {
    val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = cm.activeNetwork
    if (network != null) {
        val nc = cm.getNetworkCapabilities(network)
        if (nc != null) {
            if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { //WIFI
                return true
            } else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { //移动数据
                return true
            }
        }
    }
    return false
}


fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
    )
}

fun Int.dpToPx(context: Context): Int {
    return (this.toFloat().dpToPx(context) + 0.5f).toInt()
}

fun <T : Comparable<T>> bubbleSort(arr: Array<T>) {
    val n = arr.size
    for (i in 0 until n - 1) {
        for (j in 0 until n - i - 1) {
            if (arr[j] > arr[j + 1]) {
                val temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
            }
        }
    }
}

fun <T : Comparable<T>> quickSort(arr: Array<T>, low: Int, high: Int) {
    if (low < high) {
        val pivotIndex = partition(arr, low, high)
        quickSort(arr, low, pivotIndex - 1)
        quickSort(arr, pivotIndex + 1, high)
    }
}

private fun <T : Comparable<T>> partition(arr: Array<T>, low: Int, high: Int): Int {
    val pivot = arr[high]
    var i = low - 1
    for (j in low until high) {
        if (arr[j] < pivot) {
            i++
            val temp = arr[i]
            arr[i] = arr[j]
            arr[j] = temp
        }
    }
    val temp = arr[i + 1]
    arr[i + 1] = arr[high]
    arr[high] = temp
    return i + 1
}

inline fun <reified T : Any> Array<T>.toMutableList(): MutableList<T> {
    return mutableListOf(*this)
}

inline fun <reified T : Service> Context.startService() {
    val intent = Intent(this, T::class.java)
    ContextCompat.startForegroundService(this, intent)
}

inline fun <reified T : Service> Context.stopService() {
    val intent = Intent(this, T::class.java)
    stopService(intent)
}
fun String.lastChar(): Char {
    return get(length - 1)
}
fun String?.firstChar(): Char? {
    return this?.get(0)
}

fun Toast.showMsg(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
fun Activity.showToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
fun Service.showToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
fun Fragment.showToast(msg: String){
    Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
}
fun <T> List<T>.joinToString(): String {
    val result = StringBuilder()
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(", ")
        result.append(element)
    }
    return result.toString()
}
fun List<Int>.sum(): Int {
    var sum: Int = 0
    for (element in this) {
        sum += element
    }
    return sum
}

fun List<Double>.sum(): Double {
    var sum: Double = 0.0
    for (element in this) {
        sum += element
    }
    return sum
}
fun List<Long>.sum(): Long {
    var sum: Long = 0L
    for (element in this) {
        sum += element
    }
    return sum
}
fun List<Float>.sum(): Float {
    var sum: Float = 0F
    for (element in this) {
        sum += element
    }
    return sum
}
