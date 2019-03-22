package mc.pay.android.Actions

import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import mc.pay.android.base.HttpClient

/**
 * Created by hooni
 */
object OrderAction {

    fun order(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/order/order", params, handler)
    }

}