package com.example.netplix.ui.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.netplix.R
import com.example.netplix.databinding.FragmentLoginBinding
import com.example.netplix.di.ValidationModule
import com.example.netplix.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var validationModule: ValidationModule
) : ViewModel() {
    private var isFormValidMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var emailValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var emailObservable: Observable<String>
    private var passwordValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var passwordObservable: Observable<String>
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        isFormValidMutableLiveData.value = false
    }

    fun initValidation(binding: FragmentLoginBinding) {
        initEmailValidation(binding)
        initPasswordValidation(binding)
        emailSubscribe()
        passwordSubscribe()
        combineAll(binding)
    }

    private fun initPasswordValidation(binding: FragmentLoginBinding) {
        passwordObservable =
            validationModule.getEditTextObservable(binding.passwordTextInputLayout)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initEmailValidation(binding: FragmentLoginBinding) {
        emailObservable =
            validationModule.getEditTextObservable(binding.emailTextInputLayout)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    fun emailSubscribe() {
        compositeDisposable.add(emailObservable.subscribe { it ->
            emailValidMutableLiveData.value = it
        })
    }

    fun passwordSubscribe() {
        compositeDisposable.add(passwordObservable.subscribe { it ->
            passwordValidMutableLiveData.value = it
        })
    }

    fun combineAll(binding: FragmentLoginBinding) {
        compositeDisposable.add(
            Observable.combineLatest(emailObservable, passwordObservable) { email, password ->
                return@combineLatest validationModule.validateWithRegex(
                    binding.emailTextInputLayout,
                    Patterns.EMAIL_ADDRESS.pattern(),
                    binding.root.context.getString(R.string.enter_a_valid_email)
                ) && validationModule.validateWithRegex(
                    binding.passwordTextInputLayout,
                    Constants._8CHAR_PASSWORD_REGEX,
                    binding.root.context.getString(R.string.enter_a_valid_password)
                ) && !email.isNullOrEmpty() && !password.isNullOrEmpty()
            }.subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                isFormValidMutableLiveData.postValue(it)
            })
    }

    fun isValidForm() = isFormValidMutableLiveData
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}