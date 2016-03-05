package xyz.donot.quetzal.view.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import twitter4j.Paging
import xyz.donot.quetzal.util.getMyId
import xyz.donot.quetzal.view.fragment.HomeTimeLine
import xyz.donot.quetzal.view.fragment.MentionTimeLine
import xyz.donot.quetzal.view.fragment.TimeLine

class TimeLinePagerAdapter(val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position){
          0->Factory.home
          1->Factory.mention
          2->Factory.user
          else->throw  IllegalStateException()
        }}


    override fun getPageTitle(position: Int): CharSequence {
        return when(position){
          0->"Home"
          1->"Mention"
          2->"User"
          else->throw  IllegalStateException()
    }}

    override fun getCount(): Int {
        return 3
    }

 private  object Factory{
    val home by  lazy {HomeTimeLine()}
    val user by lazy {object : TimeLine(){
      override fun loadMore() {
        twitterObservable.getUserTimelineAsync(getMyId(), Paging(page)).subscribe {mAdapter.addAll(it)}
      }
    }}
    val mention by lazy { MentionTimeLine() }
  }
  fun  destroyAllItem() {
    for(i in 0..2){
    fm.beginTransaction().remove(this.getItem(i)).commitAllowingStateLoss()
  }}
}

