package com.keliya.chickson.travelguide
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView




/**
 * Created by bett on 8/21/17.
 */
class PlaceListAdapter(private var activity: Activity, private var items: ArrayList<Places>): BaseAdapter() {
//    var items = ArrayList<UserDto>()
//    var activity: Activity? = null

//    init {
//        this.activity = activity
//        this.items = items
//    }

    private class ViewHolder(row: View?) {
        var txtName: TextView? = null
        var txtComment: TextView? = null
        var txtPlace:TextView?=null

        init {
            this.txtName = row?.findViewById<TextView>(R.id.txtName)
            this.txtComment = row?.findViewById<TextView>(R.id.txtComment)
            this.txtPlace=row?.findViewById<TextView>(R.id.txtPlace)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.loc_list, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var userDto = items[position]
        viewHolder.txtName?.text = userDto.name
        viewHolder.txtComment?.text = userDto.comment
        viewHolder.txtPlace?.text=userDto.place

        return view as View
    }

    override fun getItem(i: Int): Places {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }


}

