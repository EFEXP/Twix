package xyz.donot.twix.view.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.donot.twix.R;


public class TweetTapDialog extends DialogFragment {


  public TweetTapDialog() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v=inflater.inflate(R.layout.fragment_tweet_tap_dialog, container, false);


    return v;
  }

}
