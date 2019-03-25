package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_money_gift.*
import mc.pay.android.Actions.MemberAction
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject

class MoneyGiftActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = -1
    var amount = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_gift)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        backIV.setOnClickListener {
            finish()
        }

        giftRL.setOnClickListener {
            val passwd = Utils.getString(passwdET)
            val phone = Utils.getString(phoneET)
            val login_id = Utils.getString(loginIdET)
            val gift_money = Utils.getInt(giftMoneyET)

            if (passwd.count() < 1) {
                Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (phone.count() < 1) {
                Toast.makeText(context, "받는 사람의 전화번호를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (login_id.count() < 1) {
                Toast.makeText(context, "받는 사람의 아이디를 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (gift_money < 1) {
                Toast.makeText(context, "선물 금액을 입력해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (gift_money > amount) {
                Toast.makeText(context, "선물 금액을 확인해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            giftMoney()

        }

        loadData()

    }

    fun loadData(){
        val params = RequestParams()
        params.put("member_id", member_id)

        MemberAction.my_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        amount = Utils.getInt(response, "amount")

                        val member = response.getJSONObject("member")

                        balanceTV.text = Utils.thousand(amount)
                        millisecondsTV.text = Utils.getString(member, "milliseconds")

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

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
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

    fun giftMoney(){
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("login_id", Utils.getString(loginIdET))
        params.put("phone", Utils.getString(phoneET))
        params.put("gift_money", Utils.getString(giftMoneyET))

        MemberAction.gift_money(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        Toast.makeText(context, "머니를 선물하였습니다.", Toast.LENGTH_LONG).show()

                        finish()

                    } else if ("no_member" == result) {
                        Toast.makeText(context, "받는 회원이 존재하지 않습니다. 아이디를 다시 확인해주세요.", Toast.LENGTH_LONG).show()
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

            override fun onFailure(statusCode: Int, headers: Array<Header>?, responseString: String?, throwable: Throwable) {
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

    override fun finish() {
        super.finish()

        Utils.hideKeyboard(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
