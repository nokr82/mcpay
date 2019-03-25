package mc.pay.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_bank_choice.*
import mc.pay.android.Actions.BankAction
import mc.pay.android.adapter.BankAdapter
import mc.pay.android.base.HolidayUtil
import mc.pay.android.base.HolidayUtils
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class BankChoiceActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private lateinit var adapter: BankAdapter
    var adapterData:ArrayList<JSONObject> = ArrayList<JSONObject>()

    var sdf = SimpleDateFormat("yyyy-MM-dd")
    var sdf1 = SimpleDateFormat("HH:mm")

    @RequiresApi(Build.VERSION_CODES.N)
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

            val bank = adapterData[position]

            val bank_name = Utils.getString(bank, "bank")

            val date = Date()

            var date_time = sdf1.parse(sdf1.format(date))

            var open = false

            if (HolidayUtil.isHoliday(SimpleDateFormat("yyyyMMdd").format(date))) {

                if (bank_name == "BOA은행") {
                    // 휴일 공동망 접속불가 (타행불가)

                    open = false

                } else {

                    val holiday_time = Utils.getString(bank, "holiday_time")

                    if (holiday_time != "") {
                        val holiday_times = holiday_time.split("~")

                        val holiday_start_dt = sdf1.parse(holiday_times[0])
                        val holiday_end_dt = sdf1.parse(holiday_times[1])

                        if(holiday_start_dt < date_time && holiday_end_dt > date_time) {
                            open = true
                        } else {
                            open = false
                        }

                    } else {
                        open = true
                    }

                }

            } else {

                val week_time = Utils.getString(bank, "week_time")

                if (week_time != "") {
                    val week_times = week_time.split("~")

                    val week_start_dt = sdf1.parse(week_times[0])
                    val week_end_dt = sdf1.parse(week_times[1])

                    if(week_start_dt < date_time && week_end_dt > date_time) {
                        open = true
                    } else {
                        open = false
                    }
                } else {
                    open = true
                }
            }

            val cal = Calendar.getInstance()
            cal.time = date

            val week_of_month = cal.get(Calendar.WEEK_OF_MONTH)
            val day_of_week = cal.get(Calendar.DAY_OF_WEEK)

            if ("국민은행" == bank_name) {

                val start_time = sdf1.parse("00:00")
                val end_time = sdf1.parse("07:00")

                // 매월 셋째주 일요일 00:00 ~ 07:00 점검
                if(week_of_month == 3 && day_of_week == 1 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("외환은행" == bank_name) {

                val start_time = sdf1.parse("01:00")
                val end_time = sdf1.parse("01:30")

                // 매월 셋째주 월요일 01:00 ~ 01:30 점검
                if(week_of_month == 3 && day_of_week == 2 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("농협" == bank_name) {

                val start_time = sdf1.parse("00:00")
                val end_time = sdf1.parse("04:00")

                // 매월 셋째주 월요일 01:00 ~ 01:30 점검
                if(week_of_month == 3 && day_of_week == 2 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("우리은행" == bank_name) {

                val start_time = sdf1.parse("23:45")
                val end_time = sdf1.parse("00:45")

                val start_time1 = sdf1.parse("02:00")
                val end_time2 = sdf1.parse("05:00")

                // 매주 금요일 23:45 ~ 00:45 점검
                if(day_of_week == 6 && start_time < date_time && end_time > date_time) {
                    open = false
                } else if (week_of_month == 2 && day_of_week == 1 && start_time1 < date_time && end_time2 > date_time) {
                    // 매월 둘째주 일요일 02:00 ~ 05:00 정기 작업
                    open = false
                }

            } else if ("한국씨티은행" == bank_name) {
                val start_time = sdf1.parse("00:00")
                val end_time = sdf1.parse("05:00")

                // 매일 00:00 ~ 05:00 간헐적 점검
                if(start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("광주은행" == bank_name) {

                val start_time = sdf1.parse("02:00")
                val end_time = sdf1.parse("06:00")

                // 매월 둘째주 일요일 02:00 ~ 06:00 간헐적 점검
                if(week_of_month == 2 && day_of_week == 1 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("제주은행" == bank_name) {

                val start_time = sdf1.parse("04:30")
                val end_time = sdf1.parse("05:00")

                // 매주 일요일, 월요일, 목요일 04:30 ~ 05:00 간헐적 점검
                if((day_of_week == 1 || day_of_week == 2 || day_of_week == 5) && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("전북은행" == bank_name) {

                val start_time = sdf1.parse("00:00")
                val end_time = sdf1.parse("03:00")

                // 매월 둘째주 토요일 00:00 ~ 03:00
                if(week_of_month == 2 && day_of_week == 7 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("경남은행" == bank_name) {

                val start_time = sdf1.parse("00:00")
                val end_time = sdf1.parse("00:40")

                // 매월 둘째주 일요일 00:00 ~ 00:40
                if(week_of_month == 2 && day_of_week == 1 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("우체국" == bank_name) {

                val start_time = sdf1.parse("04:00")
                val end_time = sdf1.parse("05:00")

                // 매일 04:00 ~ 05:00
                if(start_time < date_time && end_time > date_time) {
                    open = false
                }

            } else if ("하나은행" == bank_name) {

                val start_time = sdf1.parse("01:00")
                val end_time = sdf1.parse("01:30")

                // 매월 셋째주 월요일 01:00 ~ 01:30
                if(week_of_month == 3 && day_of_week == 2 && start_time < date_time && end_time > date_time) {
                    open = false
                }

            }

            if (open) {

                var bank_id = Utils.getInt(bank, "id")

                println("bank_id:::::::::::::::::::::::::::::${bank_id}")
                println("bank_name:::::::::::::::::::::::::::::${bank_name}")

                var intent = Intent()
                intent.putExtra("bank_id", bank_id)
                intent.putExtra("bank_name", bank_name)
                setResult(Activity.RESULT_OK, intent)

                finish()

            } else {
                Toast.makeText(context, "서비스 이용 시간이 아닙니다.", Toast.LENGTH_LONG).show()
            }

        }

        closeIV.setOnClickListener {
            finish()
        }

    }

    //은행정보
    fun loadbank() {
        val params = RequestParams()

        BankAction.index(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        adapterData.clear()

                        val banks = response.getJSONArray("bank")

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



    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
