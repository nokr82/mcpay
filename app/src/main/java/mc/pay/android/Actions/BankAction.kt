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
    //닉네임변경
    fun update_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/update_info", params, handler)
    }

    // 회원 페이지
    fun my_page_index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/my_page_index", params, handler)
    }

    //사업자 쿠폰목록
    fun company_page(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/company_page", params, handler)
    }
    //회원탈퇴
    fun secession(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/secession", params, handler)
    }

    fun cirtySchoolEmail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/cirty_school_email", params, handler)
    }

    fun uploadSchoolId(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/api/member/upload_school_id", params, handler)
    }
}