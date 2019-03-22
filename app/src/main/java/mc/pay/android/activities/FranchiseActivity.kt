package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_franchisee.*
import mc.pay.android.adapter.CouponAdapter
import mc.pay.android.base.RootActivity

class FranchiseActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    private lateinit var adapter: CouponAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_franchisee)
        context = this
        progressDialog = ProgressDialog(context, R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)


        adapter = CouponAdapter(context, R.layout.item_franchise_history, 12)
        listLV.adapter = adapter


        calLL.setOnClickListener {

        }
        cal2LL.setOnClickListener {

        }


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
