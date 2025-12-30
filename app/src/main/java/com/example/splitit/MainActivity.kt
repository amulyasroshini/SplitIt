package com.example.splitit

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.view.inputmethod.InputBinding

import android.widget.AdapterView

import androidx.appcompat.app.AppCompatActivity

import android.widget.Button

import android.widget.ImageView

import android.widget.ListView

import android.widget.TextView

import com.example.splitit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var Intro: TextView
    private lateinit var listView: ListView
    private lateinit var icon: ImageView
    private lateinit var text: TextView
    private lateinit var butt: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var userArrayList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Intro = findViewById(R.id.Intro)
        listView = findViewById(R.id.listView)
        icon = findViewById(R.id.imageView)
        text = findViewById(R.id.textView4)
        butt = findViewById(R.id.btnAddGroup)

        val group = arrayOf(
            "Swim Team","Class","Hostel Room"
        )

        val ppl = arrayOf(
            "8","10","5"
        )

        val names =arrayOf(
            "{Alexei,Bruno,Chandana,Dustin,Eleven,Fiona,Grover,Harish}","{Indra,Jane,Krishna,Lea,Mike,Nancy,Ojus,Piya,Qala,Renesa}","{Steve,Tejas,Urvashi,Vrinda,Will}"
        )

        userArrayList= ArrayList()

        for (i in group.indices){
            val user = User(group[i],ppl[i],names[i])
            userArrayList.add(user)
        }
        binding.listView.isClickable = true
        binding.listView.setOnItemClickListener { parent, view, position, id ->

            val group = group[position]
            val ppl = ppl[position]
            val names = names[position]

            val i = Intent(this, Seperate::class.java)
            i.putExtra("group",group)
            i.putExtra("people",ppl)
            i.putExtra("names",names)
            startActivity(i)
        }
        binding.listView.adapter= MyAdapter(this,userArrayList)

        butt.setOnClickListener {
            // 3. Create the Intent
            // "this" refers to the current Context (MainActivity)
            // "SecondActivity::class.java" is the destination
            val intent = Intent(this, inconvenience::class.java)

            // 4. Start the new activity
            startActivity(intent)
        }
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        TODO("Not yet implemented")
    }



    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}

