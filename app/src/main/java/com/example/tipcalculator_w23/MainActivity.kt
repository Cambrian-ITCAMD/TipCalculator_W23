package com.example.tipcalculator_w23

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.preference.PreferenceManager
import com.example.tipcalculator_w23.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnClickListener, TextWatcher,
    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{
    private lateinit var binding: ActivityMainBinding
    private var tipPercent = 0.5f
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var myList: ArrayAdapter<String>
    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)

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

    override fun onResume() {
        super.onResume()

        //var myValue = sharedPrefs.getBoolean("key", default)
//        var switchValue = sharedPrefs.getBoolean("switch_preference_1", false)
//        Toast.makeText(this, switchValue.toString(), Toast.LENGTH_SHORT).show()
////
//        var listValue = sharedPrefs.getString("list_preference_1", "")
//        Toast.makeText(this, "Text:" + listValue, Toast.LENGTH_SHORT).show()

        //var billAmount = sharedPrefs.getString("bill_amount_string", "")
        binding.billAmountEt.setText(sharedPrefs.getString("bill_amount_string", ""))
        tipPercent = sharedPrefs.getFloat("tip_percent", 0.2f)
        val numPeople = sharedPrefs.getInt("numPeople", 12)
        binding.numPeopleSpinner.setSelection(12, true)
        updateDisplay()
    }

    override fun onPause() {
        val editor = sharedPrefs.edit()
        //sharedPrefs.getType("key", defaultValue)

        if(sharedPrefs.getBoolean("switch_preference_1", false)) {
            editor.putString("bill_amount_string", binding.billAmountEt.text.toString())
            editor.putFloat("tip_percent", tipPercent)
            editor.putInt("numPeople", binding.numPeopleSpinner.selectedItemPosition)
        }
        else{
            editor.clear()
            editor.putBoolean("switch_preference_1", false)
        }
        //editor.commit()
        editor.apply()

        super.onPause()
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("ItemID", item.itemId.toString())
        when(item.itemId){
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.menu_print -> {
                Toast.makeText(this, "You clicked print", Toast.LENGTH_LONG).show()
            }
            R.id.menu_share -> {
                Toast.makeText(this, "You clicked share", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_about -> {
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_reset -> {
                Toast.makeText(this, "You clicked reset", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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

        val roundingOption = sharedPrefs.getString("list_preference_1", "")

        var tipAmount = billAmount * tipPercent
        if(roundingOption.equals(getString(R.string.round_tip_value))){
            try {
                tipAmount = tipAmount.roundToInt().toFloat()
            } catch (_:Exception) {}

            if(billAmount != 0f)
                tipPercent = tipAmount / billAmount
        }


        var subtotalAmount = billAmount * (1 + tipPercent)
        if(roundingOption.equals(getString(R.string.round_total_value))){
            try {
                subtotalAmount = subtotalAmount.roundToInt().toFloat()
            } catch (_:Exception) {}

            tipAmount = subtotalAmount - billAmount
            if(billAmount != 0f)
                tipPercent = tipAmount / billAmount
        }
        val actualNumPeople = binding.numPeopleSpinner.selectedItemPosition + 1
        val totalAmount = subtotalAmount / actualNumPeople

        val currency = NumberFormat.getCurrencyInstance()
        val percent = NumberFormat.getPercentInstance()

        binding.tipPercentTv.text = percent.format(tipPercent)
        binding.tipAmountTv.setText(currency.format(tipAmount))
        binding.subAmountTv.text = currency.format(subtotalAmount)
        binding.totalAmountTV.text = currency.format(totalAmount)
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
        updateDisplay()
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