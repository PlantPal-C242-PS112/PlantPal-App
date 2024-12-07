package com.android.plantpal.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.android.plantpal.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomFieldText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val maxCharacterCount = 200
    private val minCharacterCount = 20
    private val textInputLayout: TextInputLayout
    private val editTextField: TextInputEditText
    private val characterCounter: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.character_counter_ques, this, true)

        textInputLayout = findViewById(R.id.textInputLayout)
        editTextField = findViewById(R.id.editTextFieldQues)
        characterCounter = findViewById(R.id.characterCounter)

        editTextField.addTextChangedListener { text ->
            val currentLength = text?.length ?: 0
            characterCounter.text = "$currentLength/$maxCharacterCount"

            textInputLayout.error = if (currentLength < minCharacterCount) {
                context.getString(R.string.input_too_short)
            } else {
                null
            }

            if (currentLength > maxCharacterCount) {
                editTextField.setText(text?.substring(0, maxCharacterCount))
                editTextField.setSelection(maxCharacterCount)
            }

            characterCounter.setTextColor(
                if (currentLength == maxCharacterCount) {
                    context.getColor(R.color.red)
                } else {
                    context.getColor(R.color.black)
                }
            )
        }
    }
    fun getText(): String {
        return editTextField.text.toString()
    }
}
