package com.example.splitit

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.zip.Inflater

class MyAdapter(private val context : Activity, private val arrayList : ArrayList<User>) : ArrayAdapter<User>(context,R.layout.list_item,arrayList){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item, null)
        val grpName : TextView = view.findViewById(R.id.grpName)
        val nameOfPpl : TextView = view.findViewById(R.id.nameOfPpl)
        val noOfPpl : TextView = view.findViewById(R.id.noPpl)

        grpName.text = arrayList[position].group
        noOfPpl.text = arrayList[position].ppl
        nameOfPpl.text = arrayList[position].names



        return view
    }
}