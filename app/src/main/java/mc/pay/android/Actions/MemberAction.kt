package mc.pay.android.Actions

import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import mc.pay.android.base.HttpClient


/**
 * Created by hooni
 */
object MemberAction {

    // 회원 페이지
    fun my_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_info.json", params, handler)
    }
    //닉네임변경
    fun edit_info(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/edit_info.json", params, handler)
    }

    // 회원 페이지
    fun my_page_index(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/my_page_index.json", params, handler)
    }

    //사업자 쿠폰목록
    fun company_page(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/company_page.json", params, handler)
    }
    //회원탈퇴
    fun secession(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/secession.json", params, handler)
    }

    fun cirtySchoolEmail(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/cirty_school_email.json", params, handler)
    }

    fun uploadSchoolId(params: RequestParams, handler: JsonHttpResponseHandler) {
        HttpClient.post("/member/upload_school_id.json", params, handler)
    }
}