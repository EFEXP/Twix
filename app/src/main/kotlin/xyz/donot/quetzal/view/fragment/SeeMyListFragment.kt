package xyz.donot.quetzal.view.fragment

import twitter4j.Paging

class SeeMyListFragment(): TimeLine() {

  override fun loadMore() {
    twitterObservable.getMyListAsync(    arguments.getLong("listId"), Paging(page)).subscribe  {mAdapter.addAll(it)}
  }
}
