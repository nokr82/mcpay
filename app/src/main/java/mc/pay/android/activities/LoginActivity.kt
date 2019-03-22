package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import mc.pay.android.base.RootActivity
import kotlinx.android.synthetic.main.activity_login.*
import mc.pay.android.Actions.LoginAction
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var autoLogin = false

    var remember_id = false

    var re_login_id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)
        remember_id = intent.getBooleanExtra("remember_id",false)
        if (remember_id){
            re_login_id = intent.getStringExtra("login_id")
            emailET.setText(re_login_id)
            rememberIV.setImageResource(R.mipmap.check)
        }else{
            rememberIV.setImageResource(0)
        }
        if (autoLogin){
            autoIV.setImageResource(R.mipmap.check)
        }else{
            autoIV.setImageResource(0)
        }

        loginTV.setOnClickListener {
            login()
        }


        autoLL.setOnClickListener {
            if (!autoLogin){
                autoLogin = true
                autoIV.setImageResource(R.mipmap.check)
            }else{
                autoLogin = false
                autoIV.setImageResource(0)
            }

        }

        rememberLL.setOnClickListener {
            if (!remember_id){
                remember_id = true
                rememberIV.setImageResource(R.mipmap.check)
            }else{
                remember_id = false
                rememberIV.setImageResource(0)
            }


        }


    }



    fun login() {

        var email = Utils.getString(emailET)
        var passwd = Utils.getString(pwET)

        if (email == ""){
            Toast.makeText(context,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
            return
        }else if (passwd ==""){
            Toast.makeText(context,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
            return
        }

        val params = RequestParams()
        params.put("email", email)
        params.put("passwd", passwd)

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
                        PrefUtils.setPreference(context, "login_id", Utils.getString(data, "login_id"))
                        PrefUtils.setPreference(context, "email", Utils.getString(data, "email"))
                        PrefUtils.setPreference(context, "passwd", Utils.getString(data, "passwd"))
                        PrefUtils.setPreference(context, "company_num", Utils.getString(data, "company_num"))
                        PrefUtils.setPreference(context, "autoLogin", autoLogin)
                        PrefUtils.setPreference(context, "remember_id", remember_id)
                        Utils.hideKeyboard(context)

                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Toast.makeText(context, "일치하는 회원이 존재하지 않습니다.", Toast.LENGTH_LONG).show()
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
                throwable.printStackTrace()
                error()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>?, throwable: Throwable, errorResponse: JSONArray?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
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


    override fun onDestroy() {
        super.onDestroy()
    }

}
