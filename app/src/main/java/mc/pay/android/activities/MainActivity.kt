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
import mc.pay.android.R


class MainActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    var member_id = 3

    val swipepay = "kr.co.firstpayment.app.swipepay"
    val code = "1903200013"
    val storeId = "SMC031001"
    val passwd = "6619"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

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

            }

            val str = "fpswipepay://setle?crtftCode=344ddssdddfff&mberCode=0000001&cardCashSe=CARD&denlgSe=1&splpc=1000&vat=100&admitInfo=order_no=2334443…"
        }

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
