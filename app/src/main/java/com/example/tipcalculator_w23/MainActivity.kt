package com.example.tipcalculator_w23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.tipcalculator_w23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher,
    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{
    private lateinit var binding: ActivityMainBinding
    private var tipPercent = 0.5f
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var myList: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        arrayAdapter.add("1 Person")
        arrayAdapter.add("2 People")
        arrayAdapter.add("3 People")
        arrayAdapter.add("4 People")
        for(i in 5..100){
            if(i == 1){
                arrayAdapter.add(i.toString() + " Person")
            }
            else {
                arrayAdapter.add(i.toString() + " People")
            }
        }

        val numPeopleSpinner: Spinner = binding.numPeopleSpinner
        numPeopleSpinner.adapter = arrayAdapter
        numPeopleSpinner.setOnItemSelectedListener(this)

        myList = ArrayAdapter(this, android.R.layout.select_dialog_multichoice)

        myList.add("IOT1009 midterm")
        myList.add("IOT1009 lab 4")
        myList.add("IOT1009 code review 2")
        val listView = binding.listView
        listView.adapter = myList
        listView.onItemClickListener = this

        //findViewById<Button>(R.id.dec_button).setOnClickListener(this)
        binding.decButton.setOnClickListener(this)

        //val incButton = findViewById<Button>(R.id.inc_button)
        val incButton = binding.incButton
        incButton.setOnClickListener(this)

        updateDisplay()

//        findViewById<EditText>(R.id.bill_amount_et).setOnClickListener { view ->
//            Log.i("CLICK", "Edit Text")
//        }
        binding.billAmountEt.setOnClickListener {
            Log.i("CLICK", "Edit Text")
        }

        binding.billAmountEt.addTextChangedListener(this)

        findViewById<SeekBar>(R.id.tip_perecent_sb).setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        tipPercent = progress / 100f
                        updateDisplay()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // nothing to do
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // nothing to do
                }

            }
        )
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        Log.i("onClick", "A button was pressed")

        myList.add(binding.billAmountEt.text.toString())

        when (v?.id) {
            R.id.dec_button -> tipPercent -= 0.05f
            R.id.inc_button -> tipPercent += 0.05f
            else -> Log.e("onClick", "Something went wrong")
        }
        updateDisplay()
    }

    fun updateDisplay() {
        val billAmountString = binding.billAmountEt.text.toString()
        var billAmount = 0f
        if (billAmountString.isNotBlank()) {
            billAmount = billAmountString.toFloat()
        }

        binding.tipPercentTv.text = (tipPercent * 100).toInt().toString() + "%"
        binding.tipPerecentSb.progress = (tipPercent * 100).toInt()
        when (binding.numPeopleGroup.checkedRadioButtonId) {
            R.id.one_person -> Log.i("RadioButton", "One Person")
            R.id.two_people -> Log.i("RadioButton", "Two People")
        }


        val totalAmount = billAmount * (1 + tipPercent)
        binding.totalAmountTV.text = totalAmount.toString()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //TODO("Not yet implemented")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //TODO("Not yet implemented")
    }

    override fun afterTextChanged(s: Editable?) {
        updateDisplay()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //Log.i("Spinner", arrayAdapter.getItem(position).toString())
        if (arrayAdapter.getItem(position).toString().equals("1 Person")){
            binding.perPersonLL.visibility = View.GONE
        }
        else {
            binding.perPersonLL.visibility = View.VISIBLE
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // TODO("Not yet implemented")
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.i("View: ", view.toString())
        if(view is CheckedTextView){
            val checkbox: CheckedTextView = view
            checkbox.toggle()
        }
    }

}