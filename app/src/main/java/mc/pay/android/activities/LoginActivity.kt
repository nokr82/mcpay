package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import mc.pay.android.R
import mc.pay.android.base.RootActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        loginTV.setOnClickListener {
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }




    }





    override fun onDestroy() {
        super.onDestroy()
    }

}
