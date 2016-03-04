package xyz.donot.quetzal.view.fragment

class SeeMyListFragment(val listId:Long): TimeLine() {
  override fun loadMore() {
    twitterObservable.getMyListAsync(listId,paging).subscribe  {mAdapter.addAll(it)}
  }
}
