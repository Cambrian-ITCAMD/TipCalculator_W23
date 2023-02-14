package com.example.tipcalculator_w23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import com.example.tipcalculator_w23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    private var tipPercent = 0.5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //findViewById<Button>(R.id.dec_button).setOnClickListener(this)
        binding.decButton.setOnClickListener(this)

        //val incButton = findViewById<Button>(R.id.inc_button)
        val incButton = binding.incButton
        incButton.setOnClickListener(this)

        updateDisplay()

//        findViewById<EditText>(R.id.bill_amount_et).setOnClickListener { view ->
//            Log.i("CLICK", "Edit Text")
//        }
        binding.billAmountEt.setOnClickListener { view ->
            Log.i("CLICK", "Edit Text")
        }

        findViewById<SeekBar>(R.id.tip_perecent_sb).setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener{

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

        when(v?.id) {
            R.id.dec_button -> tipPercent -= 0.05f
            R.id.inc_button -> tipPercent += 0.05f
            else -> Log.e("onClick", "Something went wrong")
        }
        updateDisplay()
    }

    fun updateDisplay(){
        binding.tipPercentTv.text = (tipPercent * 100).toInt().toString() + "%"
        binding.tipPerecentSb.progress = (tipPercent * 100).toInt()
        when(binding.numPeopleGroup.checkedRadioButtonId) {
            R.id.one_person -> Log.i("RadioButton", "One Person")
            R.id.two_people -> Log.i("RadioButton", "Two People")
        }
    }
}