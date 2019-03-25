package mc.pay.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_bank_send.*
import mc.pay.android.Actions.BankAction
import mc.pay.android.Actions.MemberAction
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject

class BankSendActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    val SELECT_BANK = 300

    var member_id = -1
    var bank_id = -1
    var my_money = 0
    var amount = 0
    var my_code = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_send)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        idTV.text = PrefUtils.getStringPreference(context, "login_id")

        backIV.setOnClickListener {
            finish()
        }

        bankTV.setOnClickListener {
            var intent = Intent(context, BankChoiceActivity::class.java)
            startActivityForResult(intent, SELECT_BANK)
        }

        doneLL.setOnClickListener {

            val passwd = Utils.getString(passwdET)
            val accountNum = Utils.getString(accountNumET)
            val amount = Utils.getInt(amountET)
            val accountHolder = Utils.getString(accountHolderET)
            val code = Utils.getInt(codeET)

            if (passwd.count() < 1) {
                Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (bank_id < 1) {
                Toast.makeText(context, "입금 은행을 선택해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(accountNum.count() < 1) {
                Toast.makeText(context, "계좌번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(accountHolder.count() < 1) {
                Toast.makeText(context, "예금주를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(amount < 1) {
                Toast.makeText(context, "이체하실 금액을 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(code < 1) {
                Toast.makeText(context, "인증번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (passwd != PrefUtils.getStringPreference(context, "passwd")) {
                Toast.makeText(context, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (code != my_code) {
                Toast.makeText(context, "인증번호를 다시 확인해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (amount > this.amount) {
                Toast.makeText(context, "이체 금액을 확인해주세요.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (amount <= 2000) {
                Toast.makeText(context, "이체 수수료는 2000원입니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            transfer()

        }

        saveLL.setOnClickListener {
            if(saveIV.visibility == View.VISIBLE) {
                saveIV.visibility = View.GONE
            } else {
                saveIV.visibility = View.VISIBLE
            }
        }

        loadData()

    }

    override fun finish() {
        super.finish()

        Utils.hideKeyboard(context)
    }

    fun transfer(){
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("bank_id", bank_id)
        params.put("account_num", Utils.getString(accountNumET))
        params.put("account_holder", Utils.getString(accountHolderET))
        params.put("amount", Utils.getInt(amountET))
        params.put("save_info", if(saveIV.visibility == View.VISIBLE) "Y" else "N")

        BankAction.transfer(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        Toast.makeText(context, "이체 되었습니다", Toast.LENGTH_LONG).show()

                        finish()

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

                        my_money = Utils.getInt(response, "my_money")
                        amount = Utils.getInt(response, "amount")

                        val member = response.getJSONObject("member")
                        my_code = Utils.getInt(member, "code")

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                SELECT_BANK -> {

                    if (data != null) {
                        bank_id = data.getIntExtra("bank_id", -1)
                        bankNameTV.text = data.getStringExtra("bank_name")
                    }

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
