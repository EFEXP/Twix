package xyz.donot.twix.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import twitter4j.ExtendedMediaEntity;
import twitter4j.MediaEntity;

public class MediaUtil {
      /*
        _URL     : 置換前文字列
        _REPLACE : 置換後文字列
        _ADD     : 末尾に追加
        _THUMB   : サムネイル
    */

  // pic.twitter.com
  public static final String PIC_TWITTER = "https?://pbs\\.twimg\\.com/media/[a-zA-Z0-9_\\-]+\\.\\w+";
  public static final String PIC_TWITTER_THUMB_ADD = ":thumb";
  public static final String PIC_TWITTER_GIF = "https?://pbs\\.twimg\\.com/tweet_video_thumb/[a-zA-Z0-9_\\-]+\\.png";
  public static final String PIC_TWITTER_GIF_URL_1 = "tweet_video_thumb";
  public static final String PIC_TWITTER_GIF_URL_2 = "png";
  public static final String PIC_TWITTER_GIF_REPLACE_1 = "tweet_video";
  public static final String PIC_TWITTER_GIF_REPLACE_2 = "mp4";

  // twitpic
  public static final String TWITPIC = "https?://twitpic\\.com/\\w+";
  public static final String TWITPIC_URL = "https?://twitpic\\.com/";
  public static final String TWITPIC_REPLACE = "https://twitpic.com/show/large/";
  public static final String TWITPIC_ADD = ".jpg";
  public static final String TWITPIC_THUMB_REPLACE = "https://twitpic.com/show/mini/";

  // yfrog
  public static final String YFROG = "https?://yfrog\\.com/\\w+";
  public static final String YFROG_ADD = ":medium";
  public static final String YFROG_THUMB_ADD = ":small";

  // Instagram
  public static final String INSTAGRAM = "https?://instagram\\.com/p/[a-zA-Z0-9_\\-/]+";
  public static final String INSTAGRAM_ADD = "media/?size=l";
  public static final String INSTAGRAM_THUMB_ADD = "media/?size=t";

  // ついっぷるフォト
  public static final String TWIPPLE = "https?://p\\.twipple\\.jp/\\w+";
  public static final String TWIPPLE_URL = "https?://p\\.twipple\\.jp/";
  public static final String TWIPPLE_REPLACE = "http://p.twpl.jp/show/orig/";
  public static final String TWIPPLE_THUMB_REPLACE = "http://p.twpl.jp/show/thumb/";

  // imgur
  public static final String IMGUR = "https?://imgur\\.com/\\w+";
  public static final String IMGUR_URL = "https?://imgur\\.com/";
  public static final String IMGUR_REPLACE = "http://i.imgur.com/";
  public static final String IMGUR_ADD = ".jpg";
  public static final String IMGUR_THUMB_ADD = "s.jpg";

  // img.ly
  public static final String IMG_LY = "https?://img\\.ly/\\w+";
  public static final String IMG_LY_URL = "https?://img\\.ly/";
  public static final String IMG_LY_REPLACE = "http://img.ly/show/large/";
  public static final String IMG_LY_THUMB_REPLACE = "http://img.ly/show/thumb/";

  // via.me
  public static final String VIA_ME = "https?://via\\.me/[a-zA-Z0-9_\\-]+";

  // flickr
  public static final String FLICKR = "https?://www\\.flickr\\.com/photos/[a-zA-Z0-9_@\\-/]+";
  public static final String FLICKR_SHORT = "https?://flic\\.kr/\\w/\\w+";

  public boolean isMediaURL(String url) {
    return
      (url.matches(PIC_TWITTER)
        || url.matches(TWITPIC)
        || url.matches(YFROG)
        || url.matches(INSTAGRAM)
        || url.matches(TWIPPLE)
        || url.matches(IMGUR)
        || url.matches(IMG_LY)
        || url.matches(VIA_ME)
        || url.matches(FLICKR)
        || url.matches(FLICKR_SHORT)
        || url.endsWith(".jpg")
        || url.endsWith(".jpeg")
        || url.endsWith(".png")
        || url.endsWith(".gif")
        || url.endsWith(".mp4"));
  }


  public String getThumbUrl(String url){
    if (url.matches(PIC_TWITTER)) {
      return url + PIC_TWITTER_THUMB_ADD;

    } else if (url.matches(TWITPIC)) {
      return url.replaceAll(TWITPIC_URL, TWITPIC_THUMB_REPLACE) + TWITPIC_ADD;

    } else if (url.matches(YFROG)) {
      return url + YFROG_THUMB_ADD;

    } else if (url.matches(INSTAGRAM)) {
      if (url.endsWith("/")) {
        return url + INSTAGRAM_THUMB_ADD;
      } else {
        return url + "/" + INSTAGRAM_THUMB_ADD;
      }

    } else if (url.matches(TWIPPLE)) {
      return url.replaceAll(TWIPPLE_URL, TWIPPLE_THUMB_REPLACE);

    } else if (url.matches(IMGUR)) {
      return url.replaceAll(IMGUR_URL, IMGUR_REPLACE) + IMGUR_THUMB_ADD;

    } else if (url.matches(IMG_LY)) {
      return url.replaceAll(IMG_LY_URL, IMG_LY_THUMB_REPLACE);

    } else if (url.matches(VIA_ME)) {
      return url;

    } else if (url.matches(FLICKR) || url.matches(FLICKR_SHORT)) {
      return url;

    } else if (url.endsWith(".jpg")
      || url.endsWith(".jpeg")
      || url.endsWith(".png")
      || url.endsWith(".gif")) {

      return url;

    } else {
      return null;
    }
  }

  public String getVideoURL(MediaEntity[] mediaEntities, ExtendedMediaEntity[] extendedMediaEntities) {

    // Animation GIF
    if (mediaEntities.length > 0) {
      MediaEntity mediaEntity = mediaEntities[0];
      String url = mediaEntity.getMediaURLHttps();
      if (url.matches(PIC_TWITTER_GIF)) {
        return url.replace(PIC_TWITTER_GIF_URL_1, PIC_TWITTER_GIF_REPLACE_1)
          .replace(PIC_TWITTER_GIF_URL_2, PIC_TWITTER_GIF_REPLACE_2);
      }
    }

    // MP4 Video
    if (extendedMediaEntities.length > 0) {
      for (ExtendedMediaEntity entity : extendedMediaEntities) {
        ExtendedMediaEntity.Variant[] variants = entity.getVideoVariants();
        if (variants != null && variants.length > 0) {
          Map<Integer, String> videoMap = new TreeMap<>();
          List<Integer> bitrateList = new ArrayList<>();
          for (ExtendedMediaEntity.Variant variant : variants) {
            String contentType = variant.getContentType();
            String videoUrl = variant.getUrl();
            int bitrate = variant.getBitrate();
            if (contentType.equals("video/mp4")) {
              bitrateList.add(bitrate);
              videoMap.put(bitrate, videoUrl);
            }
          }
          Collections.sort(bitrateList);
          return videoMap.get(bitrateList.get(bitrateList.size()-1));
        }
      }
    }
    return null;
  }
}
