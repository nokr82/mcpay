package mc.pay.android.Actions

import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import mc.pay.android.base.HttpClient


/**
 * Created by hooni
 */
object VersionAction {


    fun version_check(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/version/version_check", params, handler)
    }

}