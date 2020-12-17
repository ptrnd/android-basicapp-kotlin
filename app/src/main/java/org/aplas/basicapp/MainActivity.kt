package org.aplas.basicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var startDialog : AlertDialog

    private val dist = Distance()
    private val weight = Weight()
    private val temp = Temperature()
    private var convertBtn: Button? = null
    private var inputTxt: EditText? = null
    private var outputTxt: EditText? = null
    private var unitOri: Spinner? = null
    private var unitConv: Spinner? = null
    private var unitType: RadioGroup? = null
    private var roundBox: CheckBox? = null
    private var formBox: CheckBox? = null
    private var imgView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        convertBtn = findViewById<View>(R.id.convertButton) as Button
        inputTxt = findViewById<View>(R.id.inputText) as EditText
        outputTxt = findViewById<View>(R.id.outputText) as EditText
        unitOri = findViewById<View>(R.id.oriList) as Spinner
        unitConv = findViewById<View>(R.id.convList) as Spinner
        unitType = findViewById<View>(R.id.radioGroup) as RadioGroup
        roundBox = findViewById<View>(R.id.chkRounded) as CheckBox
        formBox = findViewById<View>(R.id.chkFormula) as CheckBox
        imgView = findViewById<View>(R.id.img) as ImageView

        unitType!!.setOnCheckedChangeListener { group, checkedId ->
            inputTxt!!.setText("0")
            outputTxt!!.setText("0")
            val selected = findViewById<View>(checkedId) as RadioButton
            if (selected.text == "Temperature") {
                val adapter: ArrayAdapter<CharSequence>
                adapter = ArrayAdapter.createFromResource(
                        unitType!!.context,
                        R.array.tempList,
                        android.R.layout.simple_spinner_item
                )
                imgView!!.setImageResource(R.drawable.temperature)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                unitOri!!.adapter = adapter
                unitConv!!.adapter = adapter
            } else if (selected.text == "Distance") {
                val adapter: ArrayAdapter<CharSequence>
                adapter = ArrayAdapter.createFromResource(
                        unitType!!.context,
                        R.array.distList,
                        android.R.layout.simple_spinner_item
                )
                imgView!!.setImageResource(R.drawable.distance)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                unitOri!!.adapter = adapter
                unitConv!!.adapter = adapter
            } else {
                val adapter: ArrayAdapter<CharSequence>
                adapter = ArrayAdapter.createFromResource(
                        unitType!!.context,
                        R.array.weightList,
                        android.R.layout.simple_spinner_item
                )
                imgView!!.setImageResource(R.drawable.weight)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                unitOri!!.adapter = adapter
                unitConv!!.adapter = adapter
            }
        }

        convertBtn!!.setOnClickListener { doConvert() }

        oriList!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                doConvert()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }

        convList!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                doConvert()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }
        roundBox!!.setOnCheckedChangeListener { buttonView, isChecked -> doConvert() }
        formBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            (findViewById<View>(R.id.imgFormula) as ImageView).visibility =
                    if (isChecked) View.VISIBLE else View.INVISIBLE
        }

    }

    override fun onStart() {
        super.onStart()

        startDialog = AlertDialog.Builder(this@MainActivity).create()
        startDialog!!.setTitle("Application started")
        startDialog!!.setMessage("This app can use to convert anyunits")
        startDialog!!.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") {
            dialog, which -> dialog.dismiss()
        }
        startDialog!!.show()
    }

    protected fun convertUnit(type: String,
                              oriUnit: String?,
                              convUnit: String?,
                              value: Double): Double {
        return if (type == "Temperature") {
            temp.convert(oriUnit!!, convUnit!!, value)
        } else if (type == "Distance") {
            dist.convert(oriUnit!!, convUnit!!, value)
        } else {
            weight.convert(oriUnit!!, convUnit!!, value)
        }
    }

    protected fun strResult(`val`: Double,
                            rounded: Boolean): String{
        return if (rounded) {
            val f = DecimalFormat("#.##")
            f.format(`val`)
        } else {
            val f = DecimalFormat("#.#####")
            f.format(`val`)
        }
    }

    fun doConvert(){
        val selected = findViewById<View>(unitType!!.checkedRadioButtonId) as RadioButton
        val `val` = inputTxt!!.text.toString().toDouble()
        val res = convertUnit(selected.text.toString(),
                              unitOri!!.selectedItem.toString(),
                              unitConv!!.selectedItem.toString(),
                              `val`)
        outputTxt!!.setText(strResult(res, roundBox!!.isChecked))
    }
}