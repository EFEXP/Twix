package xyz.donot.quetzal.view.fragment


import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.trello.rxlifecycle.components.support.RxDialogFragment
import xyz.donot.quetzal.R
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.getTwitterInstance


class HelpFragment : RxDialogFragment() {
    val twitter by lazy { getTwitterInstance() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_help, container, false)
        (   view.findViewById(R.id.text_version)as TextView).text="Quetzal ver.${activity.packageManager
                .getPackageInfo(activity.packageName, PackageManager.GET_ACTIVITIES).versionName}"
        val family=activity.resources.getStringArray(R.array.family)
        TwitterObservable(context,twitter).getRateLimit(*family).subscribe {
            rateLimit->
            var terms=""
            activity.resources.getStringArray(R.array.endpoint).forEach {
                val limit= rateLimit[it]
                terms += "\n $it=${limit?.remaining}/${limit?.limit}"
            }
           (view.findViewById(R.id.text_help)as TextView).text=terms
        }
        return view
    }
}
