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


open class FranchiesAdapter(context: Context, view:Int, data:ArrayList<JSONObject>) : ArrayAdapter<JSONObject>(context,view, data){

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
        var company_name = Utils.getString(json,"company_name")
        var phone = Utils.getString(json,"phone")
        item.numTV.text = (position+1).toString()
        item.priceTV.text =""
        item.phoneTV.text = company_name+"\n"+phone
        item.grouppriceTV.text = ""







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
        var grouppriceTV= v.findViewById<View>(R.id.grouppriceTV) as TextView
        var priceTV= v.findViewById<View>(R.id.priceTV) as TextView
        var phoneTV= v.findViewById<View>(R.id.phoneTV) as TextView

    }



}
