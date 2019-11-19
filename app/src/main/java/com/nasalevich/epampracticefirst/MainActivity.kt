package com.nasalevich.epampracticefirst

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var signButtons: List<RadioButton>

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
        initClearButton()
        initCalculateButton()
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


    private fun initClearButton() {
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle(R.string.clear_dialog_text)
            .setPositiveButton(
                R.string.clear_dialog_confirm
            ) { _, _ ->
                clearAllFields()
            }
            .setNegativeButton(
                R.string.clear_dialog_cancel
            ) { _, _ ->

            }
            .create()

        clearButton.setOnClickListener {
            dialog.show()
        }
    }

    private fun initCalculateButton() {
        val firstField = field1.text?.toString()
        val secondField = field2.text?.toString()
        val selectedButton = selectedButton

        calculateButton.setOnClickListener {
            when {
                firstField.isNullOrEmpty()
                        || secondField.isNullOrEmpty() -> this@MainActivity.showLongToast(R.string.empty_fields)
                selectedButton == null -> this@MainActivity.showLongToast(R.string.operation_not_found)
                else -> calculate(firstField, secondField, selectedButton)
            }
        }
    }

    private fun clearAllFields() {
        field1.setText(EMPTY)
        field2.setText(EMPTY)

        resultField.text = EMPTY
    }

    private fun calculate(firstField: String, secondField: String, selectedButton: RadioButton){

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
}
