package xyz.donot.quetzal.view.fragment

import twitter4j.Paging

class SeeMyListFragment : TimeLineFragment() {

  override fun loadMore() {
    twitterObservable.getMyListAsync(
            arguments.getLong("listId"), Paging(page))
            .subscribe  {
                if (it.isNotEmpty()) {
                mAdapter.addAll(it)
              }
              else{
                load.onNext(false)
                empty.onNext(true)
              }
         }
  }
}
