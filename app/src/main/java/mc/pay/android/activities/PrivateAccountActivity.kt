package mc.pay.android.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_private_account.*
import mc.pay.android.Actions.BankAction
import mc.pay.android.adapter.PrivateAdapter
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject

class PrivateAccountActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var adapterData:ArrayList<JSONObject> = ArrayList<JSONObject>()
    private lateinit var adapter: PrivateAdapter

    var first_day = ""
    var last_day = ""

    var type = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_account)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)


        adapter = PrivateAdapter(context, R.layout.item_coupon_history, adapterData)
        listLV.adapter = adapter



        calLL.setOnClickListener {
            datedlg()
        }
        cal2LL.setOnClickListener {
            datedlg2()
        }
        useTV.setOnClickListener {
            type = 2
            private_history()
            setmenu()
            useTV.setBackgroundColor(Color.parseColor("#f0ba2f"))
            useTV.setTextColor(Color.parseColor("#000000"))
            titleTV.text = "출금총액"

        }
        unuseTV.setOnClickListener {
            type = 1
            private_history()
            setmenu()
            unuseTV.setBackgroundColor(Color.parseColor("#f0ba2f"))
            unuseTV.setTextColor(Color.parseColor("#000000"))
            titleTV.text = "입금총액"

        }

        backIV.setOnClickListener {
            finish()
        }

        giftTV.setOnClickListener {
            var intent = Intent(context, MoneyGiftActivity::class.java)
            startActivity(intent)
        }

        accountTV.setOnClickListener {
            var intent = Intent(context, BankSendActivity::class.java)
            startActivity(intent)
        }

        private_history()

    }

    fun setmenu(){
        useTV.setBackgroundResource(R.drawable.background_border_strock_926f4a)
        unuseTV.setBackgroundResource(R.drawable.background_border_strock_926f4a)
        useTV.setTextColor(Color.parseColor("#926f4a"))
        unuseTV.setTextColor(Color.parseColor("#926f4a"))
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
        private_history()

    }
    private val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)
        val end_msg = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth)
        endTV.text = msg
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        last_day = msg
        private_history()
    }
    fun private_history() {

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
                        adapterData.clear()
                        if (orders.length() > 0){
                            for (i in 0 until orders.length()){
                                val order = orders.get(i) as JSONObject

                                adapterData.add(order)
                            }
                            var dd_sum =0
                            var sum = 0
                            if (type ==1){
                                 sum =  adapterData[0].getInt("balance")
                                sumTV.text =Utils._comma(sum.toString())
                            }else{
                                for (i in 0 until adapterData.size){
                                  var d_sum =   adapterData[i].getInt("money")
                                    dd_sum = dd_sum +d_sum
                                }
                                sumTV.text =Utils._comma(dd_sum.toString())
                            }
                            var r_sum = response.getInt("p_sum") - response.getInt("d_sum")
                            leftTV.text = Utils._comma(r_sum.toString())
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
