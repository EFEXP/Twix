package xyz.donot.twix.view.fragment

import xyz.donot.twix.twitter.TwitterObservable

class SearchUserFragment (val query_txt:String):BaseUserFragment(){
  override fun TimelineLoader() {
    TwitterObservable(twitter).getUserSearchAsync(query_txt,page).subscribe {
      mAdapter.add(it)
    }
  }
}
