package xyz.donot.quetzal.view.fragment

import twitter4j.Paging

class SeeMyListFragment(val listId:Long): TimeLine() {
  override fun loadMore() {
    twitterObservable.getMyListAsync(listId, Paging(page)).subscribe  {mAdapter.addAll(it)}
  }
}
