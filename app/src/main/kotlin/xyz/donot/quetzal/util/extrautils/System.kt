package xyz.donot.quetzal.util.extrautils

import android.content.Intent
import android.os.BatteryManager
import android.os.Environment
import android.os.StatFs

fun noFreeSpace(needSize: Long): Boolean {
    val freeSpace = freeSpace()
    return freeSpace < needSize * 3
}

fun freeSpace(): Long {
    val stat = StatFs(Environment.getExternalStorageDirectory().path)
    val blockSize = stat.blockSizeLong
    val availableBlocks = stat.availableBlocksLong
    return availableBlocks * blockSize
}

fun getBatteryLevel(batteryIntent: Intent): Float {
    val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    return level / scale.toFloat()
}

fun getBatteryInfo(batteryIntent: Intent): String {
    val status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
            || status == BatteryManager.BATTERY_STATUS_FULL
    val chargePlug = batteryIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
    val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

    val level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

    val batteryPct = level / scale.toFloat()
    return "Battery Info: isCharging=$isCharging usbCharge=$usbCharge  acCharge=$acCharge  batteryPct=$batteryPct"
}