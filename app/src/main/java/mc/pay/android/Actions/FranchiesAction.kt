package mc.pay.android.Actions

import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import mc.pay.android.base.HttpClient


/**
 * Created by hooni
 */
object FranchiesAction {


    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/franchies/index", params, handler)
    }

}