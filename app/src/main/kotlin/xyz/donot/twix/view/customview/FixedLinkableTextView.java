package xyz.donot.twix.view.customview;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.donot.twix.view.customview.simplelinkabletext.Link;
import xyz.donot.twix.view.customview.simplelinkabletext.LinkModifier;


public class FixedLinkableTextView extends TextView {

  private List<Link> mLinks = new ArrayList<>();

  private LinkModifier mLinkModifier;

  public FixedLinkableTextView(Context context) {
    super(context);

    init();
  }

  public FixedLinkableTextView(Context context, AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public FixedLinkableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    init();
  }

  private void init() {
    setMovementMethod(LinkMovementMethod.getInstance());

    mLinkModifier = new LinkModifier(LinkModifier.ViewType.TEXT_VIEW);
  }

  public FixedLinkableTextView setText(String text) {
    super.setText(text);
    mLinkModifier.setText(text);
    return this;
  }

  public FixedLinkableTextView addLink(Link link) {
    mLinks.add(link);

    mLinkModifier.setLinks(mLinks);

    return this;
  }

  public FixedLinkableTextView addLinks(List<Link> links) {
    mLinks.addAll(links);

    mLinkModifier.setLinks(mLinks);

    return this;
  }

  public List<Link> getFoundLinks() {
    return mLinkModifier.getFoundLinks();
  }

  public FixedLinkableTextView build() {
    mLinkModifier.build();
    if (mLinkModifier.getSpannable()!=null){
      setText(mLinkModifier.getSpannable());
    }else{
      setText(mLinkModifier.getText());
    }
    return this;
  }
}
