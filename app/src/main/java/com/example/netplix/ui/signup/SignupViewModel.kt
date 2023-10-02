package com.example.netplix.ui.signup

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.netplix.R
import com.example.netplix.databinding.FragmentSignUpBinding
import com.example.netplix.di.ValidationModule
import com.example.netplix.utils.Constants
import com.example.netplix.utils.Constants.Companion.PHONE_REGEX
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val validationModule: ValidationModule
) : ViewModel() {
    private var isFormValidMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private var emailValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var emailObservable: Observable<String>
    private var passwordValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var passwordObservable: Observable<String>
    private var firstNameValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var firstNameObservable: Observable<String>
    private var lastNameValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var lastNameObservable: Observable<String>
    private var genderValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var genderObservable: Observable<String>
    private var phoneValidMutableLiveData: MutableLiveData<String> = MutableLiveData()
    private lateinit var phoneObservable: Observable<String>
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        isFormValidMutableLiveData.value = false
    }

    fun initValidation(binding: FragmentSignUpBinding) {
        initEmailValidation(binding)
        initPasswordValidation(binding)
        initFirstNameValidation(binding)
        initLastNameValidation(binding)
        initPhoneValidation(binding)
        initGenderValidation(binding)
        emailSubscribe()
        genderSubscribe()
        phoneSubscribe()
        lastNameSubscribe()
        firstNameSubscribe()
        combineAll(binding)
    }

    private fun initGenderValidation(binding: FragmentSignUpBinding) {
        genderObservable =
            validationModule.getEditTextObservable(binding.textInputLayout)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initPhoneValidation(binding: FragmentSignUpBinding) {
        phoneObservable = validationModule.getEditTextObservable(binding.phoneTextInputLayout)
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initLastNameValidation(binding: FragmentSignUpBinding) {
        lastNameObservable =
            validationModule.getEditTextObservable(binding.secondNameTextInputLayout)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initFirstNameValidation(binding: FragmentSignUpBinding) {
        firstNameObservable =
            validationModule.getEditTextObservable(binding.nameTextInputLayout)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initPasswordValidation(binding: FragmentSignUpBinding) {
        passwordObservable =
            validationModule.getEditTextObservable(binding.passwordTextInputLayout)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    private fun initEmailValidation(binding: FragmentSignUpBinding) {
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

    private fun firstNameSubscribe() {
        compositeDisposable.add(firstNameObservable.subscribe { it ->
            firstNameValidMutableLiveData.value = it
        })
    }

    private fun lastNameSubscribe() {
        compositeDisposable.add(lastNameObservable.subscribe { it ->
            lastNameValidMutableLiveData.value = it
        })
    }

    private fun phoneSubscribe() {
        compositeDisposable.add(phoneObservable.subscribe { it ->
            phoneValidMutableLiveData.value = it
        })
    }

    private fun genderSubscribe() {
        compositeDisposable.add(genderObservable.subscribe { it ->
            genderValidMutableLiveData.value = it
        })
    }

    fun combineAll(binding: FragmentSignUpBinding) {
        compositeDisposable.add(
            Observable.combineLatest(
                emailObservable,
                passwordObservable,
                firstNameObservable,
                lastNameObservable,
                genderObservable,
                phoneObservable
            ) { email, password, firstName, last, gender, phone ->
                return@combineLatest isValidEntries(binding, email, password, gender, phone, firstName, last)
            }.subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribe {
                isFormValidMutableLiveData.postValue(it)
            })
    }

    private fun isValidEntries(
        binding: FragmentSignUpBinding,
        email: String?,
        password: String?,
        gender: String?,
        phone: String?,
        firstName: String?,
        last: String?
    ) = (validationModule.validateWithRegex(
        binding.emailTextInputLayout,
        Patterns.EMAIL_ADDRESS.pattern(),
        binding.root.context.getString(R.string.enter_a_valid_email)
    ) && validationModule.validateWithRegex(
        binding.passwordTextInputLayout,
        Constants.PASSWORD_REGEX,
        binding.root.context.getString(R.string.enter_a_valid_password)
    )
            && validationModule.validateEmpty(binding.nameTextInputLayout)
            && validationModule.validateEmpty(binding.secondNameTextInputLayout)
            && validationModule.validateEmpty(binding.textInputLayout)
            && validationModule.validateWithRegex(
        binding.phoneTextInputLayout,
        PHONE_REGEX,
        binding.root.context.getString(R.string.enter_a_valid_egyptian_phone_no)
    ) && isNotEmptyFields(email, password, gender, phone, firstName, last))

    private fun isNotEmptyFields(
        email: String?,
        password: String?,
        gender: String?,
        phone: String?,
        firstName: String?,
        last: String?
    ) =
        (!email.isNullOrEmpty()
                && !password.isNullOrEmpty()
                && !gender.isNullOrEmpty()
                && !phone.isNullOrEmpty()
                && !last.isNullOrEmpty()
                && !firstName.isNullOrEmpty())

    fun isValidForm() = isFormValidMutableLiveData
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}