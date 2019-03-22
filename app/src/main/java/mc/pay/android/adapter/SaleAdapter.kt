package mc.pay.android.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.nostra13.universalimageloader.core.ImageLoader
import org.json.JSONObject
import mc.pay.android.R
import mc.pay.android.base.Utils


open class SaleAdapter(context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context,view, data){

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
        var price = Utils.getInt(json,"price")
        var pay_type = Utils.getString(json,"pay_type")
        var created_at = Utils.getString(json,"created_at")
        var confm_no = Utils.getString(json,"confm_no")
        item.numTV.text = position.toString()
        item.createdTV.text = created_at
        item.priceTV.text = Utils._comma(price.toString())
        item.acceptTV.text = confm_no.toString()
        item.paytypeTV.text = pay_type






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
        var numTV= v.findViewById<View>(R.id.numTV) as TextView
        var createdTV= v.findViewById<View>(R.id.createdTV) as TextView
        var priceTV= v.findViewById<View>(R.id.priceTV) as TextView
        var acceptTV= v.findViewById<View>(R.id.acceptTV) as TextView
        var paytypeTV= v.findViewById<View>(R.id.paytypeTV) as TextView

    }



}
