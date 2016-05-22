package xyz.donot.quetzal.util.extrautils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

private val uiHandler = Handler(Looper.getMainLooper())

/**
 * Executes the provided code immediately on the UI Thread
 *
 */
 fun mainThread(runnable: () -> Unit) {
  uiHandler.post(runnable)
}

/**
 * Executes the provided code immediately on a background thread
 *
 */
 fun async(runnable: () -> Unit) {
  Thread(runnable).start()
}

/**
 * Executes the provided code immediately on a background thread that will be submitted to the
 * provided ExecutorService
 *
 * @param onBackgroundJob Interface that wraps the code to execute
 * @param executor        Will queue the provided code
 */
 fun async(runnable: () -> Unit, executor: ExecutorService): Future<out Any?> {
  return executor.submit(runnable)
}
