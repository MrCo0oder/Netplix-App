package com.example.netplix.di

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.regex.Pattern
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class ValidationModule @Inject constructor() {
    fun getEditTextObservable(editText: TextInputLayout): Observable<String> {
        val subject: PublishSubject<String> = PublishSubject.create()
        editText.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                text: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                subject.onNext(text.toString())
            }

            override fun afterTextChanged(text: Editable) {
            }
        })
        return subject
    }

    fun validateEmpty(
        textInputLayout: TextInputLayout,
        validateMessage: String? = "required",
        isRequiredFocus: Boolean = true
    ): Boolean {
        if (textInputLayout.editText!!.text.trim().isEmpty()) {
            if (isRequiredFocus) textInputLayout.editText!!.requestFocus()
            textInputLayout.error = validateMessage
            return false
        }
        textInputLayout.error = null
        return true
    }

    fun validateWithRegex(
        textInputLayout: TextInputLayout,
        regex: String,
        validateMessage: String = "required",
        isRequiredFocus: Boolean = true
    ): Boolean {
        val pattern = Pattern.compile(regex)
        if (validateEmpty(textInputLayout, validateMessage, false))
            if (!pattern.matcher(textInputLayout.editText!!.text.toString()).matches()) {
                if (isRequiredFocus) textInputLayout.editText!!.requestFocus()
                textInputLayout.error = validateMessage
                textInputLayout.endIconDrawable = null
                return false
            }
        return true
    }
}