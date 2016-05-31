package xyz.donot.quetzal.view.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxDialogFragment
import xyz.donot.quetzal.view.activity.SearchSettingActivity
import java.util.*

class DatePickFragment:RxDialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val  c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val parentActivity= activity
        val minDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -7)
        }

        val  maxDate = c



        val listener= DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            if(parentActivity is SearchSettingActivity) {
                parentActivity.dateSet(year, monthOfYear+1, dayOfMonth,arguments.getBoolean("isFrom",false))
            }

        }

        val dialog=DatePickerDialog(activity, listener,  year, month, day)
        .apply {
            datePicker.maxDate=maxDate.timeInMillis
            datePicker.minDate=minDate.timeInMillis
        }
            return  dialog



    }
}
