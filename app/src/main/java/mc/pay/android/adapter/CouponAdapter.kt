package mc.pay.android.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import mc.pay.android.base.Utils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*






open class CouponAdapter(context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context,view, data){

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data

    override fun getView(position: Int, convertView: View?, parent : ViewGroup?): View {

        lateinit var retView: View

        if (convertView == null) {
            retView = View.inflate(context, view, null)
            item = ViewHolder(retView)
            retView.tag = item
        } else {
            retView = convertView
            item = convertView.tag as ViewHolder
            if (item == null) {
                retView = View.inflate(context, view, null)
                item = ViewHolder(retView)
                retView.tag = item
            }
        }

        var json = data.get(position)

        Log.d("쿠폰",json.toString())

        var coupon_id = Utils.getInt(json,"id")
        var price = Utils.getInt(json,"price")
        var created_at = Utils.getString(json,"created_at")
        var milliseconds = Utils.getString(json,"milliseconds")

        val format = SimpleDateFormat("yyyy.MM.dd")
        val date = format.parse(created_at.replace("-","."))

        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR,1)


        var click = Utils.getString(json,"click")

        item.numTV.text = (position+1).toString()
        item.priceTV.text = Utils._comma(price.toString())
        item.limitTV.text = created_at.replace("-",".").substring(0,10)+"\n"+format.format(cal.time)
        item.couponnumTV.text = milliseconds
        if (click == "N"){
            item.itemLL.setBackgroundColor(Color.parseColor("#000000"))
        }else{
            item.itemLL.setBackgroundColor(Color.parseColor("#70c8a27a"))
        }

        return retView

    }

    override fun getItem(position: Int): JSONObject {

        return data.get(position)
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getCount(): Int {

        return data.count()
    }

    fun removeItem(position: Int){
        data.removeAt(position)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) {
        var itemLL =v.findViewById<View>(mc.pay.android.R.id.itemLL) as LinearLayout
        var numTV= v.findViewById<View>(mc.pay.android.R.id.numTV) as TextView
        var priceTV= v.findViewById<View>(mc.pay.android.R.id.priceTV) as TextView
        var limitTV= v.findViewById<View>(mc.pay.android.R.id.limitTV) as TextView
        var couponnumTV= v.findViewById<View>(mc.pay.android.R.id.couponnumTV) as TextView

    }



}
