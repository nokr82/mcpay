package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import android.net.Uri
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.Actions.MemberAction
import mc.pay.android.Actions.OrderAction
import mc.pay.android.R
import mc.pay.android.base.PrefUtils
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder


class MainActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = 3

    val swipepay = "kr.co.firstpayment.app.swipepay"
    val code = "1903200013"
    val memberCode = "SMC031001"
    val passwd = "6619"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        member_id = PrefUtils.getIntPreference(context, "member_id")

        manageLL.setOnClickListener {
            var intent = Intent(context, ManageActivity::class.java)
            startActivity(intent)
        }

        oneTV.setOnClickListener {
            priceText(Utils.getString(oneTV))
        }

        twoTV.setOnClickListener {
            priceText(Utils.getString(twoTV))
        }

        threeTV.setOnClickListener {
            priceText(Utils.getString(threeTV))
        }

        fourTV.setOnClickListener {
            priceText(Utils.getString(fourTV))
        }

        fiveTV.setOnClickListener {
            priceText(Utils.getString(fiveTV))
        }

        sixTV.setOnClickListener {
            priceText(Utils.getString(sixTV))
        }

        sevenTV.setOnClickListener {
            priceText(Utils.getString(sevenTV))
        }

        eightTV.setOnClickListener {
            priceText(Utils.getString(eightTV))
        }

        nineTV.setOnClickListener {
            priceText(Utils.getString(nineTV))
        }

        zeroTV.setOnClickListener {
            priceText(Utils.getString(zeroTV))
        }

        zero2TV.setOnClickListener {
            priceText(Utils.getString(zero2TV))
        }

        zero3TV.setOnClickListener {
            priceText(Utils.getString(zero3TV))
        }

        removeLL.setOnClickListener {
            var price = Utils.getString(priceTV)

            if(price.length == 1) {
                priceTV.text = "0"
                return@setOnClickListener
            }

            price = price.substring(0, price.length - 1)

            priceTV.text = price

        }

        delLL.setOnClickListener {
            priceTV.text = "0"
        }

        confirmLL.setOnClickListener {

            val price = Utils.getLong(priceTV)

            if (price < 1) {
                dlgAlert("금액을 입력해주세요.")
                return@setOnClickListener
            }
            val intent = packageManager.getLaunchIntentForPackage(swipepay)

            if (intent == null) {

                Toast.makeText(context, "스와이프페이를 설치해주세요.", Toast.LENGTH_LONG).show()

                var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$swipepay"))
                startActivity(intent)

            } else {

                var query = ""
                URLEncoder.encode(query, "UTF-8");

                val str = "fpswipepay://setle?crtftCode=${code}&mberCode=${memberCode}&cardCashSe=CARD&delngSe=1&splpc=500&vat=100&admitInfo=order_id=1"

                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
                startActivity(intent)
//                order("CARD")
            }

        }

    }

    fun order(pay_type: String) {
        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("price", Utils.getLong(priceTV))
        params.put("state", 0)
        params.put("pay_type", pay_type)

        OrderAction.order(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        var order_id = Utils.getInt(response, "order_id")

                        val str = "fpswipepay://setle?crtftCode=${code}&mberCode=${memberCode}&cardCashSe=${pay_type}&delngSe=1&splpc=1000&vat=1000&admitInfo=order_id=${order_id}"

                        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
                        startActivity(intent)

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

    fun dlgAlert(contents: String) {
        var intent = Intent(context, DlgCommonActivity::class.java)
        intent.putExtra("contents", contents)
        startActivity(intent)
    }

    fun priceText(addNumber: String) {
        var price = Utils.getLong(priceTV)

        if ((addNumber == "00" || addNumber == "000") && price == 0L) {
            return
        }

        var priceStr = "0"

        if (price == 0L) {
            priceStr = addNumber
        } else if (price >= 9999999999999999) {
            priceStr = 9999999999999999.toString()
        } else {
            priceStr = price.toString() + addNumber
        }
        priceTV.text = Utils._comma(priceStr)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
