package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_manage.*
import mc.pay.android.base.RootActivity

class ManageActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        backIV.setOnClickListener {
            finish()
        }

        franLL.setOnClickListener {
            var intent = Intent(context, FranchiseActivity::class.java)
            startActivity(intent)
        }
        editLL.setOnClickListener {
            var intent = Intent(context, EditInfoActivity::class.java)
            startActivity(intent)
        }
        payLL.setOnClickListener {
            var intent = Intent(context, SaleHistoryActivity::class.java)
            startActivity(intent)
        }
        noticeLL.setOnClickListener {
            var intent = Intent(context, DlgNoticeActivity::class.java)
            startActivity(intent)
        }
        softLL.setOnClickListener {
            var intent = Intent(context, SoftUpdateActivity::class.java)
            startActivity(intent)
        }
        autoIV.setOnClickListener {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }


    }
}
