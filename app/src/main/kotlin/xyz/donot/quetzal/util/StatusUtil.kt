package xyz.donot.quetzal.util


import twitter4j.Status
import java.util.regex.Pattern

    fun getClientName(source: String): String {
        val tokens = source.split("[<>]".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
        if (tokens.size > 1) {
            return tokens[2]
        } else {
            return tokens[0]
        }
    }



    fun getExpandedText(status: Status): String {
        var text = status.text
        for (url in status.urlEntities) {
            val p = Pattern.compile(url.url)
            val m = p.matcher(text)
            text = m.replaceAll(url.expandedURL)
        }
        return text
    }






