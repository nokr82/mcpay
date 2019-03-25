package mc.pay.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_bank_choice.*
import kotlinx.android.synthetic.main.activity_edit_info.*
import mc.pay.android.Actions.BankAction
import mc.pay.android.Actions.MemberAction
import mc.pay.android.adapter.BankAdapter
import mc.pay.android.adapter.CouponAdapter
import mc.pay.android.adapter.SaleAdapter
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject

class BankChoiceActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private lateinit var adapter: BankAdapter
    var adapterData:ArrayList<JSONObject> = ArrayList<JSONObject>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_choice)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        loadbank()
        adapter = BankAdapter(context, R.layout.item_bank, adapterData)
        listLV.adapter = adapter

        listLV.setOnItemClickListener { parent, view, position, id ->
            var data = adapterData.get(position)
            val bank = Utils.getString(data, "bank")
            var intent = Intent()
            intent.putExtra("bank", bank)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


        closeIV.setOnClickListener {
            finish()
        }





    }

    //은행정보보
    fun loadbank() {
        val params = RequestParams()

        BankAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    print("result : $response")

                    if ("ok" == result) {
                        val banks = response.getJSONArray("bank")
                        adapterData.clear()
                        if (banks.length() > 0){
                            for (i in 0 until banks.length()){
                                val bank = banks.get(i) as JSONObject
                                adapterData.add(bank)
                            }
                        }
                        adapter.notifyDataSetChanged()


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



    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
