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

    fun detail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/order/detail", params, handler)
    }

    fun cancel(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/order/cancel", params, handler)
    }

    fun done(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/order/done", params, handler)
    }

}