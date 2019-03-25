package mc.pay.android.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_coupon_history.*
import mc.pay.android.Actions.BankAction
import mc.pay.android.adapter.CouponAdapter
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject

class CouponHistoryActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    var adapterData:ArrayList<JSONObject> = ArrayList<JSONObject>()
    private lateinit var adapter: CouponAdapter

    var first_day = ""
    var last_day = ""

    var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_history)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)


        adapter = CouponAdapter(context, R.layout.item_coupon_history, adapterData)
        listLV.adapter = adapter


        backIV.setOnClickListener {
            finish()
        }
        calLL.setOnClickListener {
            datedlg()
        }
        cal2LL.setOnClickListener {
            datedlg2()
        }
        coupongiftTV.setOnClickListener {
            val intent = Intent(context, MoneyGiftActivity::class.java)
            startActivity(intent)
        }

        couponpayTV.setOnClickListener {
            val intent = Intent(context, CouponPayActivity::class.java)
            startActivity(intent)
        }
        coupon_history()

    }



    fun datedlg() {
        var day = Utils.todayStr()
        var days =    day.split("-")
        DatePickerDialog(context, dateSetListener, days[0].toInt(), days[1].toInt()-1, days[2].toInt()).show()
    }

    fun datedlg2() {
        var day = Utils.todayStr()
        var days =    day.split("-")
        DatePickerDialog(context, dateSetListener2,days[0].toInt(), days[1].toInt(), days[2].toInt()).show()
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)

        firstTV.text = msg
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        first_day = msg
        coupon_history()

    }
    private val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)
        val end_msg = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth)
        endTV.text = msg
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        last_day = msg
        coupon_history()
    }


    //은행정보보
    fun coupon_history() {

        val params = RequestParams()
        params.put("member_id", PrefUtils.getIntPreference(context,"member_id"))
        params.put("type",type)
        params.put("first_day",first_day)
        params.put("last_day",last_day)

        BankAction.pay_history(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    print("result : $response")
                    if ("ok" == result) {
                        val orders = response.getJSONArray("orders")
                        val sum = response.getInt("sum")
                        sumTV.text = sum.toString()+"원"
                        adapterData.clear()
                        if (orders.length() > 0){
                            for (i in 0 until orders.length()){
                                val order = orders.get(i) as JSONObject
                                adapterData.add(order)
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
