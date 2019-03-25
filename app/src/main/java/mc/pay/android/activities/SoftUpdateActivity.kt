package mc.pay.android.activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import mc.pay.android.R
import kotlinx.android.synthetic.main.activity_soft_update.*
import mc.pay.android.Actions.VersionAction
import mc.pay.android.BuildConfig
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils
import org.json.JSONException
import org.json.JSONObject
import android.content.Intent
import android.net.Uri
import android.os.Environment
import java.io.File


class SoftUpdateActivity : RootActivity() {

    private lateinit var context: Context
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mc.pay.android.R.layout.activity_soft_update)
        context = this
        progressDialog = ProgressDialog(context, mc.pay.android.R.style.progressDialogTheme)
        progressDialog!!.setProgressStyle(android.R.style.Widget_DeviceDefault_Light_ProgressBar_Large)
        progressDialog!!.setCancelable(false)

        versionCheck()


        backIV.setOnClickListener {
            finish()
        }

    }




    fun versionCheck() {

        val my_version = BuildConfig.VERSION_NAME
        Log.d("버전",my_version.toString())

        val params = RequestParams()
        params.put("my_version", my_version)
        params.put("device", "A")

        VersionAction.version_check(params, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>?, response: JSONObject?) {
                if (progressDialog != null) {
                    progressDialog!!.dismiss()
                }
                Log.d("버전",response.toString())
                try {
                    val result = response!!.getString("result")

                    if ("ok" == result) {
                        upRL.visibility = View.GONE
                        guideTV.text = "사용가능한 최선 버전으로 이미 업데이트했습니다."

                        versionTV.text = "현재버전 : " + my_version+
                        "\n마지막 업데이트 확인 : "

                    } else if ("false" == result) {

                        val version = response.getJSONObject("version")
                        var android =   Utils.getString(version, "android_version")
                        var created_at = Utils.getString(version, "created_at")

                        guideTV.text = "최신버전이 아닙니다. 업데이트 해주세요."

                        versionTV.text = "현재버전 : " + android+
                                "\n마지막 업데이트 확인 : "
                        upRL.visibility = View.VISIBLE

                        upRL.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(
                                Uri.fromFile(File(Environment.getExternalStorageDirectory().toString() + "/download/" + "app.apk")),
                                "application/vnd.android.package-archive"
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                    } else {
                        Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_LONG).show()
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

                // System.out.println(responseString);

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




    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }
}
