package mc.pay.android.activities

import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.Actions.LoginAction
import mc.pay.android.R
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class IntroActivity : RootActivity() {

    protected var _splashTime = 2000 // time to display the splash screen in ms
    private val _active = true
    private var splashThread: Thread? = null

    private var progressDialog: ProgressDialog? = null

    private var context: Context? = null

    private var posting_id:String = ""
    private var chatting_member_id:String = ""
    private var is_push:Boolean = false

    val SHOW_DLG = 301

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        this.context = this
        progressDialog = ProgressDialog(context)



        splashThread = object : Thread() {
            override fun run() {
                try {
                    var waited = 0
                    while (waited < _splashTime && _active) {
                        Thread.sleep(100)
                        waited += 100
                    }
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {
                    stopIntro()
                }
            }
        }
        (splashThread as Thread).start()

    }

    private fun stopIntro() {

        val autoLogin = PrefUtils.getBooleanPreference(context, "autoLogin")
        val remember_id = PrefUtils.getBooleanPreference(context, "remember_id")
        val login_id = PrefUtils.getStringPreference(context,"login_id")




        if (!autoLogin) {
            PrefUtils.clear(context)
            val intent = Intent(context, LoginActivity::class.java)
            if (remember_id){
                intent.putExtra("login_id",login_id)
                intent.putExtra("remember_id",remember_id)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            PrefUtils.clear(context)
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
//            handler.sendEmptyMessage(0)
        }

    }

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
//            login()
        }
    }

    private fun login() {

        val params = RequestParams()
        params.put("email", PrefUtils.getStringPreference(context,"login_id"))
        params.put("passwd", PrefUtils.getStringPreference(context,"passwd"))

        LoginAction.login(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val data = response.getJSONObject("member")

                        PrefUtils.setPreference(context, "member_id", Utils.getInt(data, "id"))
                        PrefUtils.setPreference(context, "name", Utils.getString(data, "name"))
                        PrefUtils.setPreference(context, "email", Utils.getString(data, "email"))
                        PrefUtils.setPreference(context, "passwd", Utils.getString(data, "passwd"))
                        PrefUtils.setPreference(context, "company_num", Utils.getString(data, "company_num"))
                        PrefUtils.setPreference(context, "autoLogin", true)

                        Utils.hideKeyboard(context)

                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONArray?) {
                super.onSuccess(statusCode, headers, response)
            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // print(errorResponse)

                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // print(errorResponse)

                throwable.printStackTrace()
                error()
            }

            override fun onStart() {
                // show dialog
                if (progressDialog != null) {

                    progressDialog!!.show()
                }
            }

            override fun onFinish() {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
            }
        })


    }


}
