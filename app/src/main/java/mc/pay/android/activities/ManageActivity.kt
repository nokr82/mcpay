package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_manage.*
import mc.pay.android.Actions.MemberAction
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject
import android.net.Uri


class ManageActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var autoLogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mc.pay.android.R.layout.activity_manage)
        context = this
        progressDialog = ProgressDialog(context, mc.pay.android.R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)
        loadInfo()
        autoLogin = PrefUtils.getBooleanPreference(context, "autoLogin")

        if (autoLogin){
            autoIV.setImageResource(mc.pay.android.R.mipmap.check)
        }else{
            autoIV.setImageResource(0)
        }

        accountLL.setOnClickListener {
            var intent = Intent(context, PrivateAccountActivity::class.java)
            startActivity(intent)
        }


        callTV.setOnClickListener {
            val tt = Intent(Intent.ACTION_DIAL, Uri.parse("tel:0221246625"))
            startActivity(tt)

        }


        backIV.setOnClickListener {
            finish()
        }

        logoutTV.setOnClickListener {
            var intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        franLL.setOnClickListener {
            var intent = Intent(context, FranchiseActivity::class.java)
            startActivity(intent)
        }
        editLL.setOnClickListener {
            var intent = Intent(context, EditInfoActivity::class.java)
            startActivity(intent)
        }
        payLL.setOnClickListener {
            var intent = Intent(context, SaleHistoryActivity::class.java)
            startActivity(intent)
        }
        noticeLL.setOnClickListener {
            var intent = Intent(context, DlgNoticeActivity::class.java)
            startActivity(intent)
        }
        softLL.setOnClickListener {
            var intent = Intent(context, SoftUpdateActivity::class.java)
            startActivity(intent)
        }
        autoIV.setOnClickListener {
            if (autoLogin){
                autoLogin = false
                PrefUtils.setPreference(context, "autoLogin", autoLogin)
                autoIV.setImageResource(0)
            }else{
                autoLogin = true
                PrefUtils.setPreference(context, "autoLogin", autoLogin)
                autoIV.setImageResource(mc.pay.android.R.mipmap.check)
            }
        }

    }

    fun loadInfo() {
        val params = RequestParams()
        params.put("member_id", PrefUtils.getIntPreference(context, "member_id"))

        MemberAction.my_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    print("result : $response")

                    if ("ok" == result) {

                        var member = response.getJSONObject("member")
                        val name = Utils.getString(member,"name")
                        nameTV.text = name



                    } else {
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseString: String?) {

                // System.out.println(responseString);
            }

            private fun error() {
                Utils.alert(context, "조회중 장애가 발생하였습니다.")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                responseString: String?,
                throwable: Throwable
            ) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                // System.out.println(responseString);

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


    override fun onResume() {
        super.onResume()
        loadInfo()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
