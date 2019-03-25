package mc.pay.android.Actions

import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import mc.pay.android.base.HttpClient


/**
 * Created by hooni
 */
object BankAction {

    fun pay_history(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/bank/pay_history", params, handler)
    }
    fun index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/bank/index", params, handler)
    }
    fun order_history(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/bank/order_history", params, handler)
    }
    fun transfer(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/bank/transfer", params, handler)
    }

    fun coupon_history(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/bank/coupon_history", params, handler)
    }
}