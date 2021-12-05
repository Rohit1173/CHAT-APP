package com.example.basicsocket

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load


class adapter(val context: Context, val list: ArrayList<message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    class ViewHolder1(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txt: TextView = itemview.findViewById(R.id.textview)
        val myname:TextView = itemview.findViewById(R.id.myname)
    }

    class ViewHolder2(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val send_msg: TextView = itemview.findViewById(R.id.sendmsg)
    }
    class ViewHolder3(itemview: View):RecyclerView.ViewHolder(itemview){
        val join_text: TextView = itemview.findViewById(R.id.join_text)
    }
    class ViewHolder4(itemview: View):RecyclerView.ViewHolder(itemview){
        val out_image:ImageView=itemview.findViewById(R.id.outimage)
    }
    class ViewHolder5(itemview: View):RecyclerView.ViewHolder(itemview){
        val img_name:TextView=itemview.findViewById(R.id.imgname)
        val in_image:ImageView =itemview.findViewById(R.id.in_img)
    }


    override fun getItemViewType(position: Int): Int {
        val items = list[position]
        return (items.viewType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.incoming, parent, false)
            return ViewHolder1(view)
        } else if(viewType==2){
            view = layoutInflater.inflate(R.layout.outgoing, parent, false)
            return ViewHolder2(view)

        }
        else if(viewType==3){
            view = layoutInflater.inflate(R.layout.join, parent, false)
            return ViewHolder3(view)

        }
        else if(viewType==4){
            view = layoutInflater.inflate(R.layout.outgoing_image, parent, false)
            return ViewHolder4(view)
        }
        else{
            view = layoutInflater.inflate(R.layout.incoming_image, parent, false)
            return ViewHolder5(view)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if(getItemViewType(position)==1){
            (holder as ViewHolder1).txt.text = item.chat
            holder.myname.text =item.username
        }
        else if(getItemViewType(position)==2){
            (holder as ViewHolder2).send_msg.text =item.chat
        }
        else if(getItemViewType(position)==3){
            (holder as ViewHolder3).join_text.text =item.chat
        }
        else if(getItemViewType(position)==4){
            (holder as ViewHolder4).out_image.load(item.chat.toUri())
        }
        else{
            (holder as ViewHolder5).in_image.load(item.chat.toUri())
            holder.img_name.text=item.username
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }


}