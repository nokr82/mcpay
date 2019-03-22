package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_coupon_history.*
import mc.pay.android.adapter.CouponAdapter
import mc.pay.android.base.RootActivity

class CouponHistoryActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private lateinit var adapter: CouponAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_history)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)


        adapter = CouponAdapter(context, R.layout.item_sale_history, 7)
        listLV.adapter = adapter


        backIV.setOnClickListener {
            finish()
        }

        coupongiftTV.setOnClickListener {
            val intent = Intent(context, MoneyGiftActivity::class.java)
            startActivity(intent)
        }

        couponpayTV.setOnClickListener {
            val intent = Intent(context, CouponPayActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
