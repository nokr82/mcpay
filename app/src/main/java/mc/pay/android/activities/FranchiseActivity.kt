package mc.pay.android.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_franchisee.*
import mc.pay.android.Actions.BankAction.pay_history
import mc.pay.android.Actions.FranchiesAction
import mc.pay.android.adapter.FranchiesAdapter
import mc.pay.android.base.PrefUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject

class FranchiseActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var adapterData:ArrayList<JSONObject> = ArrayList<JSONObject>()
    lateinit var adapter: FranchiesAdapter


    var type = 1
    var first_day = ""
    var last_day = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_franchisee)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)


        adapter = FranchiesAdapter(context, R.layout.item_franchise_history, adapterData,this)
        listLV.adapter = adapter


        cancelTV.setOnClickListener {
            type = 2
            index()
            setmenu()
            cancelTV.setBackgroundColor(Color.parseColor("#f0ba2f"))
            cancelTV.setTextColor(Color.parseColor("#000000"))
        }
        payTV.setOnClickListener {
            type = 1
            index()
            setmenu()
            payTV.setBackgroundColor(Color.parseColor("#f0ba2f"))
            payTV.setTextColor(Color.parseColor("#000000"))
        }


        calLL.setOnClickListener {
            datedlg()
        }
        cal2LL.setOnClickListener {
            datedlg2()
        }


        backIV.setOnClickListener {
            finish()
        }
        index()
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
        index()

    }
    private val dateSetListener2 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        val msg = String.format("%d.%d.%d", year, monthOfYear + 1, dayOfMonth)
        val end_msg = String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth)
        endTV.text = msg
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        last_day = msg
        index()
    }


    fun setmenu(){
        cancelTV.setBackgroundResource(R.drawable.background_border_strock_926f4a)
        payTV.setBackgroundResource(R.drawable.background_border_strock_926f4a)
        cancelTV.setTextColor(Color.parseColor("#926f4a"))
        payTV.setTextColor(Color.parseColor("#926f4a"))
    }
    //가맹점정보
    fun index() {

        val params = RequestParams()
        params.put("member_id", PrefUtils.getIntPreference(context,"member_id"))
        params.put("first_day",first_day)
        params.put("last_day",last_day)
        params.put("type",type)
        FranchiesAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")
                    print("result : $response")
                    if ("ok" == result) {
                        val franchies = response.getJSONArray("franchies")
                        adapterData.clear()

                        var r_sum = 0
                        if (franchies.length() > 0){
                            for (i in 0 until franchies.length()){
                                val fran = franchies.get(i) as JSONObject
                                var sum = Utils.getInt(fran,"sum")
                                adapterData.add(fran)
                                r_sum = r_sum+sum
                            }
                        }
                        adapter.notifyDataSetChanged()


                        sumTV.text = Utils._comma(r_sum.toString())+"원"
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
