package mc.pay.android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONException
import org.json.JSONObject


open class CouponAdapter(context: Context, view: Int, data: Int): ArrayAdapter<JSONObject>(context, view, data) {

    private lateinit var item: ViewHolder
    private var data:Int = data
    private var view: Int = view

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = View.inflate(context, view, null)
            item = ViewHolder(convertView!!)
            convertView!!.tag = item
        } else {
            item = convertView.tag as ViewHolder
            if (item == null) {
                convertView = View.inflate(context, view, null)
                item = ViewHolder(convertView!!)
                convertView!!.tag = item
            }
        }

        try {



        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return convertView
    }


    class ViewHolder(v: View) {


        init {


        }
    }

}