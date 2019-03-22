package mc.pay.android.Actions

import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import mc.pay.android.base.HttpClient


object LoginAction {

    fun login(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/login/login", params, handler)
    }

    fun validUser(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/login/valid_user", params, handler)
    }

}