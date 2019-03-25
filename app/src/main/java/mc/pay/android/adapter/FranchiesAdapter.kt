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
import mc.pay.android.activities.FranchiseActivity
import mc.pay.android.base.RootActivity
import mc.pay.android.base.Utils


open class FranchiesAdapter(context: Context, view:Int, data:ArrayList<JSONObject>,activity: FranchiseActivity) : ArrayAdapter<JSONObject>(context,view, data){

    private lateinit var item: ViewHolder
    var view:Int = view
    var data:ArrayList<JSONObject> = data
    var activity:FranchiseActivity = activity
    var sum = 0
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
        var name = Utils.getString(json,"name")
        var phone = Utils.getString(json,"phone")


        val orders = json.getJSONArray("order")
        if (orders.length() > 0){
            for (i in 0 until orders.length()){
                val order = orders.get(i) as JSONObject
                var price = Utils.getInt(order,"price")
                sum = sum+price

            }
            json.put("sum",sum)

        }



        item.numTV.text = (position+1).toString()
        item.priceTV.text =Utils._comma(sum.toString())
        sum = 0
        item.phoneTV.text = name+"\n"+phone
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
