package com.nasalevich.epampracticefirst

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val DIALOG_KEY = "dialog"
private const val RESULT_KEY = "RESULT_SHOWN"

class MainActivity : AppCompatActivity(), MessageDialog.MessageDialogCallback {

    private lateinit var signButtons: List<RadioButton>

    private val handler = Handler(Looper.getMainLooper())

    private val selectedButton: RadioButton?
        get() {
            return signButtons.firstOrNull {
                it.isChecked
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSignButtons()
        initFloatValuesCheckBox()
        initSignedValuesCheckBox()
        initCalculateButton()

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RESULT_KEY)) {
                resultField.text = savedInstanceState.getCharSequence(RESULT_KEY)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.also {
            it.putCharSequence(RESULT_KEY, resultField.text)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.menu_clear -> {
            MessageDialog().show(supportFragmentManager, DIALOG_KEY)

            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    private fun initSignButtons() {
        signButtons = arrayListOf(
            plusRadioButton,
            minusRadioButton,
            divideRadioButton,
            multiplyRadioButton
        )

        signButtons.forEach {
            it.initClickListener()
        }
    }

    private fun initFloatValuesCheckBox() {
        floatValuesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (signedValuesCheckBox.isChecked) {
                    field1.inputType = FLOAT_VALUES or SIGNED_VALUES
                    field2.inputType = FLOAT_VALUES or SIGNED_VALUES
                } else {
                    field1.inputType = FLOAT_VALUES
                    field2.inputType = FLOAT_VALUES
                }
            } else {
                if (signedValuesCheckBox.isChecked) {
                    field1.inputType = SIGNED_VALUES
                    field2.inputType = SIGNED_VALUES
                } else {
                    field1.inputType = NUMBER_VALUES
                    field2.inputType = NUMBER_VALUES
                }
                field1.removeSign(DOT)
                field2.removeSign(DOT)
            }
        }
    }

    private fun initSignedValuesCheckBox() {
        signedValuesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (floatValuesCheckBox.isChecked) {
                    field1.inputType = FLOAT_VALUES or SIGNED_VALUES
                    field2.inputType = FLOAT_VALUES or SIGNED_VALUES
                } else {
                    field1.inputType = SIGNED_VALUES
                    field2.inputType = SIGNED_VALUES
                }
            } else {
                if (floatValuesCheckBox.isChecked) {
                    field1.inputType = FLOAT_VALUES
                    field2.inputType = FLOAT_VALUES
                } else {
                    field1.inputType = NUMBER_VALUES
                    field2.inputType = NUMBER_VALUES
                }
                field1.removeSign(MINUS)
                field2.removeSign(MINUS)
            }
        }
    }

    private fun initCalculateButton() {
        calculateButton.setOnClickListener {
            val firstField = field1.text?.toString()
            val secondField = field2.text?.toString()
            val selectedButton = selectedButton

            when {
                firstField.isNullOrBlank()
                        || secondField.isNullOrBlank() -> showLongToast(R.string.empty_fields)
                selectedButton == null -> showLongToast(R.string.operation_not_found)
                else -> calculate(firstField.toDouble(), secondField.toDouble(), selectedButton)
            }
        }
    }

    private fun clearAllFields() {
        field1.setText(EMPTY)
        field2.setText(EMPTY)

        resultField.text = EMPTY
    }

    private fun calculate(firstField: Double, secondField: Double, selectedButton: RadioButton) {
        val result = when (selectedButton.text) {
            getString(R.string.plus) -> sum(firstField, secondField)
            getString(R.string.minus) -> subtraction(firstField, secondField)
            getString(R.string.divide) -> division(firstField, secondField)
            getString(R.string.multiply) -> multiplication(firstField, secondField)
            else -> EMPTY
        }

        if (result == INFINITY) {
            divisionByZeroProtocol()
        } else {
            resultField.text = result
        }
    }

    private fun EditText.removeSign(sign: String) {
        when (sign) {
            DOT -> {
                val str = text
                val indexOfDot = str.indexOf(sign)

                if (indexOfDot != -1) {
                    setText(str.substring(0, indexOfDot))
                }
            }

            MINUS -> setText(text.toString().replaceFirst(sign, EMPTY))
        }
    }

    private fun RadioButton.initClickListener() {
        this.setOnClickListener { buttonView ->
            signButtons.forEach {
                if (it.id != buttonView.id) {
                    it.isChecked = false
                }
            }
        }
    }

    private fun divisionByZeroProtocol() {
        showLongToast(R.string.division_by_zero)

        Thread {
            Thread.sleep(1000)
            handler.post {
                finishAffinity()
            }
        }.start()
    }

    override fun onPositiveClick() {
        clearAllFields()
    }
}