package mc.pay.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_bank_send.*
import mc.pay.android.base.RootActivity

class BankSendActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private val SELECT_BANK = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_send)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        backIV.setOnClickListener {
            finish()
        }

        selTV.setOnClickListener {
            var intent = Intent(context, BankChoiceActivity::class.java)
            startActivityForResult(intent,SELECT_BANK)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                SELECT_BANK -> {
                       bankTV.text  = data!!.getStringExtra("bank")
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
