package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_private_account.*
import mc.pay.android.base.RootActivity

class PrivateAccountActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_account)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)


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

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
