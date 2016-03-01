package xyz.donot.quetzal.util


import twitter4j.ExtendedMediaEntity
import twitter4j.MediaEntity
import java.util.*


class MediaUtil {
    fun getVideoURL(mediaEntities: Array<MediaEntity>, extendedMediaEntities: Array<ExtendedMediaEntity>): String? {
        if (mediaEntities.size > 0) {
            val mediaEntity = mediaEntities[0]
            val url = mediaEntity.mediaURLHttps
            if (url.matches(PIC_TWITTER_GIF.toRegex())) {
                return url.replace(PIC_TWITTER_GIF_URL_1, PIC_TWITTER_GIF_REPLACE_1).replace(PIC_TWITTER_GIF_URL_2, PIC_TWITTER_GIF_REPLACE_2)
            }
        }
        // MP4 Video
        if (extendedMediaEntities.size > 0) {
            for (entity in extendedMediaEntities) {
                val variants = entity.videoVariants
                if (variants != null && variants.size > 0) {
                    val videoMap = TreeMap<Int, String>()
                    val bitrateList = ArrayList<Int>()
                    for (variant in variants) {
                        val contentType = variant.contentType
                        val videoUrl = variant.url
                        val bitrate = variant.bitrate
                        if (contentType == "video/mp4") {
                            bitrateList.add(bitrate)
                            videoMap.put(bitrate, videoUrl)
                        }
                    }
                    Collections.sort(bitrateList)
                    return videoMap[bitrateList[bitrateList.size - 1]]
                }
            }
        }
        return null
    }

    companion object {
        // pic.twitter.com
        val PIC_TWITTER = "https?://pbs\\.twimg\\.com/media/[a-zA-Z0-9_\\-]+\\.\\w+"
        val PIC_TWITTER_THUMB_ADD = ":thumb"
        val PIC_TWITTER_GIF = "https?://pbs\\.twimg\\.com/tweet_video_thumb/[a-zA-Z0-9_\\-]+\\.png"
        val PIC_TWITTER_GIF_URL_1 = "tweet_video_thumb"
        val PIC_TWITTER_GIF_URL_2 = "png"
        val PIC_TWITTER_GIF_REPLACE_1 = "tweet_video"
        val PIC_TWITTER_GIF_REPLACE_2 = "mp4"

        // twitpic
        val TWITPIC = "https?://twitpic\\.com/\\w+"
        val TWITPIC_URL = "https?://twitpic\\.com/"
        val TWITPIC_REPLACE = "https://twitpic.com/show/large/"
        val TWITPIC_ADD = ".jpg"
        val TWITPIC_THUMB_REPLACE = "https://twitpic.com/show/mini/"

        // yfrog
        val YFROG = "https?://yfrog\\.com/\\w+"
        val YFROG_ADD = ":medium"
        val YFROG_THUMB_ADD = ":small"

        // Instagram
        val INSTAGRAM = "https?://instagram\\.com/p/[a-zA-Z0-9_\\-/]+"
        val INSTAGRAM_ADD = "media/?size=l"
        val INSTAGRAM_THUMB_ADD = "media/?size=t"

        // ついっぷるフォト
        val TWIPPLE = "https?://p\\.twipple\\.jp/\\w+"
        val TWIPPLE_URL = "https?://p\\.twipple\\.jp/"
        val TWIPPLE_REPLACE = "http://p.twpl.jp/show/orig/"
        val TWIPPLE_THUMB_REPLACE = "http://p.twpl.jp/show/thumb/"

        // imgur
        val IMGUR = "https?://imgur\\.com/\\w+"
        val IMGUR_URL = "https?://imgur\\.com/"
        val IMGUR_REPLACE = "http://i.imgur.com/"
        val IMGUR_ADD = ".jpg"
        val IMGUR_THUMB_ADD = "s.jpg"

        // img.ly
        val IMG_LY = "https?://img\\.ly/\\w+"
        val IMG_LY_URL = "https?://img\\.ly/"
        val IMG_LY_REPLACE = "http://img.ly/show/large/"
        val IMG_LY_THUMB_REPLACE = "http://img.ly/show/thumb/"

        // via.me
        val VIA_ME = "https?://via\\.me/[a-zA-Z0-9_\\-]+"

        // flickr
        val FLICKR = "https?://www\\.flickr\\.com/photos/[a-zA-Z0-9_@\\-/]+"
        val FLICKR_SHORT = "https?://flic\\.kr/\\w/\\w+"
    }
}

fun isMediaURL(url: String): Boolean {
  return url.matches(MediaUtil.PIC_TWITTER.toRegex())
    || url.matches(MediaUtil.TWITPIC.toRegex())
    || url.matches(MediaUtil.YFROG.toRegex())
    || url.matches(MediaUtil.INSTAGRAM.toRegex())
    || url.matches(MediaUtil.TWIPPLE.toRegex())
    || url.matches(MediaUtil.IMGUR.toRegex())
    || url.matches(MediaUtil.IMG_LY.toRegex())
    || url.matches(MediaUtil.VIA_ME.toRegex())
    || url.matches(MediaUtil.FLICKR.toRegex())
    || url.matches(MediaUtil.FLICKR_SHORT.toRegex())
    || url.endsWith(".jpg")
    || url.endsWith(".jpeg")
    || url.endsWith(".png")
    || url.endsWith(".gif")
    || url.endsWith(".mp4")
}
fun getThumbUrl(url: String): String? {
  if (url.matches(MediaUtil.PIC_TWITTER.toRegex())) {
    return url + MediaUtil.PIC_TWITTER_THUMB_ADD

  } else if (url.matches(MediaUtil.TWITPIC.toRegex())) {
    return url.replace(MediaUtil.TWITPIC_URL.toRegex(), MediaUtil.TWITPIC_THUMB_REPLACE) + MediaUtil.TWITPIC_ADD

  } else if (url.matches(MediaUtil.YFROG.toRegex())) {
    return url + MediaUtil.YFROG_THUMB_ADD

  } else if (url.matches(MediaUtil.INSTAGRAM.toRegex())) {
    if (url.endsWith("/")) {
      return url + MediaUtil.INSTAGRAM_THUMB_ADD
    } else {
      return url + "/" + MediaUtil.INSTAGRAM_THUMB_ADD
    }

  } else if (url.matches(MediaUtil.TWIPPLE.toRegex())) {
    return url.replace(MediaUtil.TWIPPLE_URL.toRegex(), MediaUtil.TWIPPLE_THUMB_REPLACE)

  } else if (url.matches(MediaUtil.IMGUR.toRegex())) {
    return url.replace(MediaUtil.IMGUR_URL.toRegex(), MediaUtil.IMGUR_REPLACE) + MediaUtil.IMGUR_THUMB_ADD

  } else if (url.matches(MediaUtil.IMG_LY.toRegex())) {
    return url.replace(MediaUtil.IMG_LY_URL.toRegex(), MediaUtil.IMG_LY_THUMB_REPLACE)

  } else if (url.matches(MediaUtil.VIA_ME.toRegex())) {
    return url

  } else if (url.matches(MediaUtil.FLICKR.toRegex()) || url.matches(MediaUtil.FLICKR_SHORT.toRegex())) {
    return url

  } else if (url.endsWith(".jpg")
    || url.endsWith(".jpeg")
    || url.endsWith(".png")
    || url.endsWith(".gif")) {

    return url

  } else {
    return null
  }
}
