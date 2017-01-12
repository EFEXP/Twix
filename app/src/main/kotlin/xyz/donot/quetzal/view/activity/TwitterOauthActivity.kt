package xyz.donot.quetzal.view.activity


import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_twitter_oauth.*
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder
import xyz.donot.quetzal.R
import xyz.donot.quetzal.model.realm.DBAccount
import xyz.donot.quetzal.twitter.TwitterObservable
import xyz.donot.quetzal.util.bindToLifecycle
import xyz.donot.quetzal.util.getSerialized
import xyz.donot.quetzal.util.safeTry
import xyz.donot.quetzal.util.showSnackbar

class TwitterOauthActivity : RxAppCompatActivity() {
    val tInstance: Twitter =   TwitterFactory().instance
    val builder= ConfigurationBuilder()
    var requestToken:RequestToken?=null
    var waitDialog:ProgressDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twitter_oauth)
        showProgress()
        builder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key))
        builder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret))
        builder.setTweetModeExtended(true)
        tInstance.setOAuthConsumer(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret))
        safeTry(this@TwitterOauthActivity) {
            requestToken= tInstance.getOAuthRequestToken(getString(R.string.twitter_callback_url))
        }.bindToLifecycle(this@TwitterOauthActivity)
        .subscribe {
            web_view.loadUrl(requestToken!!.authorizationURL)
            web_view.setWebViewClient(object :WebViewClient(){
                //認証
                fun getAccessToken(uri: Uri){
                    val  verifier = uri.getQueryParameter("oauth_verifier")
                    safeTry(this@TwitterOauthActivity){ tInstance.getOAuthAccessToken(requestToken,verifier)}
                    .subscribe {
                        builder.setOAuthAccessToken(it.token)
                        builder.setOAuthAccessTokenSecret(it.tokenSecret)
                        saveToken(TwitterFactory(builder.build()).instance) }
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    waitDialog?.dismiss()
                }

                @TargetApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (request.url.toString().startsWith(getString(R.string.twitter_callback_url))) {
                        view.stopLoading()
                        getAccessToken(Uri.parse(request.url.toString()))
                        return true
                    }
                    return true
                }

                @SuppressWarnings("deprecation")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.startsWith(getString(R.string.twitter_callback_url))) {
                        view.stopLoading()
                        getAccessToken(Uri.parse(url))
                        return true
                    }
                    return false
                }
            })
        }
    }

    fun saveToken(x: Twitter) {
     safeTry(this@TwitterOauthActivity){
         val fire=  FirebaseAnalytics.getInstance(this@TwitterOauthActivity)
         val params =Bundle()
         params.putString(FirebaseAnalytics.Param.VALUE, x.screenName)
         fire.logEvent(FirebaseAnalytics.Event.LOGIN, params)
         fire.logEvent("Start App",   Bundle().apply { putString("name",x.screenName) })
        Realm.getDefaultInstance().use {
            realm->
            if(! realm.where(DBAccount::class.java).equalTo("id",x.id).isValid){
                showSnackbar( oauth_activity_coordinator,R.string.description_already_added_account)
                 }
            //Twitterインスタンス保存
            realm.executeTransaction {
                if(realm.where(DBAccount::class.java).equalTo("isMain", true).findFirst()!=null){
                    it.where(DBAccount::class.java).equalTo("isMain", true).findFirst().isMain=false}
                if(it.where(DBAccount::class.java).equalTo("id", x.id).findFirst()==null){
                    it.createObject(DBAccount::class.java,x.id).apply {
                        isMain = true
                        twitter = x.getSerialized()} }
            }

        }
        //Userインスタンス
        TwitterObservable(this@TwitterOauthActivity, x)
                .showUser(x.id)
                .subscribe(
                        { user_->
                            Realm.getDefaultInstance()
                                    .use {
                                        realm->
                                        realm.executeTransaction{
                                            it.where(DBAccount::class.java).equalTo("id",x.id)
                                                    .findFirst()
                                                    .apply { user=user_.getSerialized()}
                                        }
                                    }
                        }
                        ,{it.printStackTrace()})


     }
        .subscribe {
            startActivity(Intent(this@TwitterOauthActivity, MainActivity::class.java))
            finish()
        }

    }
    private fun showProgress() {
        waitDialog = ProgressDialog(this)
        waitDialog?.setMessage("まずはアカウントの認証をしましょう！")
        waitDialog?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        waitDialog?.show()
    }


}