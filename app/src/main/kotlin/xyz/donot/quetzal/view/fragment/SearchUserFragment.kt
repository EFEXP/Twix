package xyz.donot.quetzal.view.fragment

import xyz.donot.quetzal.twitter.TwitterObservable

class SearchUserFragment (val query_txt:String):BaseUserFragment(){
  override fun TimelineLoader() {
    TwitterObservable(twitter).getUserSearchAsync(query_txt,page).subscribe {
      mAdapter.add(it)
    }
  }
}
