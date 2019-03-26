package mc.pay.android.activities

import android.app.Activity
import android.app.AlertDialog
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
import mc.pay.android.Actions.OrderAction
import mc.pay.android.R
import mc.pay.android.base.PrefUtils
import org.json.JSONException
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = 3

    val swipepay = "kr.co.firstpayment.app.swipepay"
    val code = "699a9d6ec4aed1cd547bfdb31e7a56c5bc59a31abd8b1fe12c0390ed4ef7d896"
    val memberCode = "1903200013"
    val passwd = "6619"

    var pay_type = "CARD"

    var cardCashSe = ""
    var delngSe = ""
    var setleSuccesAt = "X"
    var setleMssage = ""
    var setleSe = ""
    var rciptNo = ""
    var confmNo = ""
    var confmDe = ""
    var confmTime = ""
    var splpc = ""
    var vat = ""
    var cardNo = ""
    var instlmtMonth = ""
    var issuCmpnyCode = ""
    var issuCmpnyNm = ""
    var puchasCmpnyCode = ""
    var puchasCmpnyNm = ""
    var type = ""

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

        couponLL.setOnClickListener {
            var intent = Intent(context, CouponPayActivity::class.java)
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

        cardLL.setOnClickListener {
            if (pay_type == "CARD") {
                pay_type = "CASH"
                cardLL.setBackgroundResource(R.drawable.background_border_strock_7b593e)
            } else {
                pay_type = "CARD"
                cardLL.setBackgroundResource(R.drawable.background_2d0e6b_border_strock_7b593e)
            }
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
                order()
            }

        }

        cancelLL.setOnClickListener {
            val order_id = PrefUtils.getIntPreference(context, "order_id")

            if (order_id < 1) {
                Toast.makeText(context, "이미 취소되었습니다.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            orderDetail(order_id)

        }

        shemeCheck()

    }

    fun shemeCheck() {
        var data_string = intent!!.dataString

        if (data_string != null) {

            data_string = URLDecoder.decode(data_string, "UTF-8")

            println("data_string::::::::::::::::::::::::$data_string")

            val str = data_string.split("?")

            if(str.count() == 2) {
                val sheme = str[0]
                val dataStr = str[1]

                var order_id = "-1"
                var payment_id = "-1"

                var datas = dataStr.split("&")

                for (i in 0 until datas.size) {
                    var data = datas[i].split("=")

                    if (data.count() == 2) {
                        val key = data[0]
                        val value = data[1]

                        if (key == "cardCashSe"){
                            cardCashSe = value
                        } else if (key == "setleSuccesAt") {
                            setleSuccesAt = value
                        } else if (key == "setleMssage") {
                            setleMssage = value
                        } else if (key == "setleSe") {
                            setleSe = value
                        } else if (key == "rciptNo") {
                            rciptNo = value
                        } else if (key == "confmNo") {
                            confmNo = value
                        } else if (key == "confmDe") {
                            confmDe = value
                        } else if (key == "confmTime") {
                            confmTime = value
                        } else if (key == "splpc") {
                            splpc = value
                        } else if (key == "vat") {
                            vat = value
                        } else if (key == "cardNo") {
                            cardNo = value
                        } else if (key == "instlmtMonth") {
                            instlmtMonth = value
                        } else if (key == "issuCmpnyCode") {
                            issuCmpnyCode = value
                        } else if (key == "issuCmpnyNm") {
                            issuCmpnyNm = value
                        } else if (key == "puchasCmpnyCode") {
                            puchasCmpnyCode = value
                        } else if (key == "puchasCmpnyNm") {
                            puchasCmpnyNm = value
                        } else if (key == "admitInfo") {

                            val parameters = value.split("_-_")

                            for (j in 0 until parameters.count()) {
                                val param = parameters[j].split("__")

                                if (param.count() == 2) {
                                    val param_key = param[0]
                                    val param_val = param[1]

                                    if (param_key == "order_id") {
                                        order_id = param_val
                                    } else if (param_key == "type") {
                                        type = param_val
                                    } else if (param_key == "payment_id") {
                                        payment_id = param_val
                                    }

                                }

                            }

                        }

                    }
                }

                if((sheme.contains("ok_by_card") || sheme.contains("ok_by_cash")) && order_id.toInt() > 0) {

                    if (setleSuccesAt == "O") {
                        orderDone(order_id, 1)
                    } else {
                        Toast.makeText(context, setleMssage, Toast.LENGTH_LONG).show()
                    }

                } else if ((sheme.contains("cancelled_by_card") || sheme.contains("cancelled_by_cash")) && order_id.toInt() > 0 && payment_id.toInt() > 0) {

                    if (setleSuccesAt == "O") {
                        orderCancel(order_id.toInt(), 2, payment_id.toInt())
                    }

                }

            }


        }
    }

    fun order() {

        val price = Utils.getLong(priceTV)

        val params = RequestParams()
        params.put("member_id", member_id)
        params.put("price", price)
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

                        var query = "order_id__${order_id}_-_type__pay"
                        query = URLEncoder.encode(query, "UTF-8");


                        var callback = ""
                        if (pay_type == "CASH") {
                            callback = "mc_pay://ok_by_cash"
                        } else {
                            callback = "mc_pay://ok_by_card"
                        }

                        val str = "fpswipepay://setle?crtftCode=${code}&mberCode=${memberCode}&cardCashSe=${pay_type}&delngSe=1&splpc=${price}&vat=0&callbackAppUrl=${callback}&admitInfo=${query}"

                        println("str::::::::::::::::::::$str")

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

                 System.out.println(responseString);

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

    fun orderDetail(order_id: Int) {

        val params = RequestParams()
        params.put("order_id", order_id)

        OrderAction.detail(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        val order = response.getJSONObject("order")
                        var payments = order.getJSONArray("payments")

                        var order_id = Utils.getInt(order, "id")

                        if (payments.length() > 0) {
                            val donePayments = payments[0] as JSONObject
                            var payment_id = Utils.getString(donePayments, "id")
                            var pay_type = Utils.getString(donePayments, "card_cash_se")
                            var splpc = Utils.getString(donePayments, "splpc")
                            var vat = Utils.getString(donePayments, "vat")
                            var rcipt_no = Utils.getString(order, "rcipt_no")

                            var query = "order_id__${order_id}_-_type__cancel_-_payment_id__${payment_id}"
                            query = URLEncoder.encode(query, "UTF-8");

                            val str = "fpswipepay://setle?crtftCode=${code}&mberCode=${memberCode}&cardCashSe=${pay_type}&denlgSe=0&splpc=${splpc}&vat=${vat}&admitInfo=${query}&rciptNo=${rcipt_no}"

                            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
                            startActivity(intent)
                        }

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

                 System.out.println(responseString);

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

    fun orderCancel(order_id: Int, state: Int, payment_id: Int) {

        val params = RequestParams()
        params.put("order_id", order_id)
        params.put("state", state)
        params.put("payment_id", payment_id)
        params.put("card_cash_se", cardCashSe)
        params.put("setle_mssage", setleMssage)
        params.put("setle_se", setleSe)
        params.put("rcipt_no", rciptNo)
        params.put("confm_de", confmDe)
        params.put("confm_time", confmTime)
        params.put("splpc", splpc)
        params.put("vat", vat)
        params.put("card_no", cardNo)
        params.put("instlmt_month", instlmtMonth)
        params.put("issu_cmpny_code", issuCmpnyCode)
        params.put("issu_cmpny_nm", issuCmpnyNm)
        params.put("puchas_cmpny_code", puchasCmpnyCode)
        params.put("puchas_cmpny_nm", puchasCmpnyNm)

        OrderAction.cancel(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        PrefUtils.removePreference(context, "order_id")

                        Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_LONG).show()
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

                 System.out.println(responseString);

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

    fun orderDone(order_id: String, state: Int) {
        val params = RequestParams()
        params.put("order_id", order_id)
        params.put("state", state)
        params.put("card_cash_se", cardCashSe)
        params.put("setle_mssage", setleMssage)
        params.put("setle_se", setleSe)
        params.put("confm_no", confmNo)
        params.put("rcipt_no", rciptNo)
        params.put("confm_de", confmDe)
        params.put("confm_time", confmTime)
        params.put("splpc", splpc)
        params.put("vat", vat)
        params.put("card_no", cardNo)
        params.put("instlmt_month", instlmtMonth)
        params.put("issu_cmpny_code", issuCmpnyCode)
        params.put("issu_cmpny_nm", issuCmpnyNm)
        params.put("puchas_cmpny_code", puchasCmpnyCode)
        params.put("puchas_cmpny_nm", puchasCmpnyNm)

        OrderAction.done(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }

                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {

                        PrefUtils.setPreference(context, "order_id", order_id)

                        if (state == 1) {
                            Toast.makeText(context, "결제가 완료 되었습니다.", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, setleMssage, Toast.LENGTH_LONG).show()
                        }

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

                 System.out.println(responseString);

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
