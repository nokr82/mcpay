package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_coupon_pay.*
import mc.pay.android.base.RootActivity

class CouponPayActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_pay)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        backIV.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
