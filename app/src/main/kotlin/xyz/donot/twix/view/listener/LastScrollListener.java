package xyz.donot.twix.view.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class  LastScrollListener extends RecyclerView.OnScrollListener {

  int pastVisiblesItems, visibleItemCount, totalItemCount;

  private boolean loading = true;
  private int current_page = 1;
  private LinearLayoutManager mLinearLayoutManager;

  public  LastScrollListener(LinearLayoutManager linearLayoutManager) {
    this.mLinearLayoutManager = linearLayoutManager;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if(dy > 0) //check for scroll down
    {
      visibleItemCount = mLinearLayoutManager.getChildCount();
      totalItemCount =mLinearLayoutManager.getItemCount();
      pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

      if (loading)
      {
        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
        {
          loading = false;
         onLoadMore(current_page);
          current_page++;
        }
      }
    }
  }


  public abstract void onLoadMore(int current_page);
}
