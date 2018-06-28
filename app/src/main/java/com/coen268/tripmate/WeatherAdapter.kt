package com.coen268.tripmate
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView






class WeatherAdapter(private var activity: Activity, private var items: ArrayList<Weather>): BaseAdapter() {
    private class ViewHolder(row: View?) {
        var titleView: TextView = row?.findViewById<View>(R.id.txtTitle) as TextView
        var authorView: TextView = row?.findViewById<View>(R.id.txtAuthor) as TextView
        var dateView: TextView = row?.findViewById<View>(R.id.txtDate) as TextView



    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder:ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_view, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var Event = items[position]
        viewHolder.titleView?.text = Event.title
        viewHolder.authorView?.text = Event.author
        viewHolder.dateView?.text = Event.date


        //viewHolder.txtPlace?.text=Event.

        return view as View
    }

    override fun getItem(i: Int): Weather {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }




}