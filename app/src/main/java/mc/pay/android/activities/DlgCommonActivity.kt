package mc.pay.android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dlg_common.*
import mc.pay.android.R
import mc.pay.android.base.RootActivity

class DlgCommonActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_common)

        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        var contents = intent.getStringExtra("contents")
        var cancel = intent.getBooleanExtra("cancel", false)

        if (cancel) {
           cancelTV.visibility = View.VISIBLE
        }

        contentsTV.text = contents

        cancelTV.setOnClickListener {
            finish()
        }

        doneTV.setOnClickListener {
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
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
