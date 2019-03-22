package mc.pay.android.activities

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
import kotlinx.android.synthetic.main.activity_edit_info.*
import mc.pay.android.Actions.MemberAction
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream

class EditInfoActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null
    var email = ""
    var passwd = ""
    var name = ""
    var company_num = -1
    var login_id = ""
    var print_type = -1
    var phone = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)
        loadInfo()

        changeTV.setOnClickListener {
            var con_passwd = Utils.getString(passwdET)
            if (passwd == con_passwd){
                val intent = Intent(context, DlgAccountChangeActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(context,"비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show()
            }

        }


        backIV.setOnClickListener {
            finish()
        }

        editLL.setOnClickListener {
            edit_profile()
        }


    }

    //회원정보
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
                        email =  Utils.getString(member, "email")
                        name = Utils.getString(member,"name")
                        login_id =  Utils.getString(member, "login_id")
                        passwd = Utils.getString(member,"passwd")
                        company_num =  Utils.getInt(member, "company_num")
                        phone = Utils.getInt(member,"phone")

                        phoneET.setText(phone.toString())
                        passwdET.setText(passwd)
                        nameET.setText(name)
                        login_idET.setText(login_id)
                        company_numET.setText(company_num.toString())
                        emailET.setText(email)


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

    fun edit_profile() {
         name = Utils.getString(nameET)
         phone = Utils.getInt(phoneET)
        login_id = Utils.getString(login_idET)
        company_num = Utils.getInt(company_numET)

        if (phone.toString().length != 11){
            Toast.makeText(context,"핸드폰번호가 올바르지가 않습니다",Toast.LENGTH_SHORT).show()
            return
        }
        if (company_num.toString().length < 1){
            Toast.makeText(context,"사업자번호가 올바르지가 않습니다",Toast.LENGTH_SHORT).show()
            return
        }
        if (name.length < 2){
            Toast.makeText(context,"가맹점명이 올바르지가 않습니다",Toast.LENGTH_SHORT).show()
            return
        }
        if (login_id.length < 3){
            Toast.makeText(context,"아이디가 올바르지가 않습니다",Toast.LENGTH_SHORT).show()
            return
        }


        val params = RequestParams()
        params.put("member_id", PrefUtils.getIntPreference(context, "member_id"))
        params.put("name", name)
        params.put("phone", phone)
        params.put("email", email)
        params.put("login_id", login_id)
        params.put("passwd", passwd)
        params.put("company_num", company_num)
        params.put("print_type", print_type)

        MemberAction.update_info(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        Toast.makeText(context, "변경되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()

                    } else {

                        Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
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

//                 System.out.println(responseString);

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
//                println("--------$errorResponse")
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
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
