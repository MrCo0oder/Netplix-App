package com.example.netplix.di

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.example.netplix.models.MovieModel
import com.example.netplix.models.TvModel
import com.example.netplix.utils.Constants
import com.example.netplix.utils.Constants.Companion.FEMALE_AVATAR
import com.example.netplix.utils.Constants.Companion.MALE_AVATAR
import com.example.netplix.utils.Constants.Companion.MOVIES_LIST
import com.example.netplix.utils.Constants.Companion.NETPLIX_USERS
import com.example.netplix.utils.Constants.Companion.TV_LIST
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule @Inject constructor() {
    private var auth: FirebaseAuth
    private var db: FirebaseFirestore
    private var activity: Activity? = null


    init {
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
    }

    fun init(activity: Activity) {
        this.activity = activity
    }

    @Singleton
    fun getAuth() = auth

    @Singleton
    fun getDb() = db

    fun signUp(
        email: String,
        password: String,
        firstName: String,
        secondName: String,
        gender: String,
        phone: String,
        callback: (FirebaseUser?, Boolean, String) -> Unit
    ) {
        activity?.let {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(it) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.i(
                        this.javaClass.simpleName,
                        "User info= ${user?.email}/displayName ${user?.displayName}/uid ${user?.uid} /isEmailVerified ${user?.isEmailVerified}"
                    )
                    updateUserInfo(
                        firstName, secondName, gender, phone, email, user?.uid.toString()
                    ) { isSuccessful, message ->
                        if (isSuccessful) {
                            callback(user, true, "")
                        } else {
                            callback(user, false, message)
                        }
                    }
                } else {
                    callback(null, false, task.exception?.message + "")
                }
            }
        }
    }

    private fun updateUserInfo(
        firstName: String,
        secondName: String,
        gender: String,
        phone: String,
        email: String,
        uId: String,
        callback: (Boolean, String) -> Unit
    ) {
        db.collection(NETPLIX_USERS).document(uId)
            .set(userInfoMap(firstName, secondName, gender, phone, email))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "")
                } else callback(false, task.exception?.message + "")
            }
    }
    fun updateProfileImage(
        url: String,
        callback: (Boolean, String) -> Unit
    ) {
        db.collection(NETPLIX_USERS).document(auth.uid.toString())
            .update("photoUrl",url)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "")
                } else callback(false, task.exception?.message + "")
            }
    }

    private fun userInfoMap(
        firstName: String, secondName: String, gender: String, phone: String, email: String
    ) = mapOf(
        "firstName" to firstName,
        "secondName" to secondName,
        "gender" to gender,
        "phone" to phone,
        "email" to email,
        "photoUrl" to if (gender == "Male") Constants.MALE_AVATAR else Constants.FEMALE_AVATAR
    )

    fun login(
        email: String, password: String, callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }

    fun logOut() {
        auth.signOut()
    }

    fun sendEmailVerification(callback: (Boolean, String) -> Unit) {
        auth.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Email sent.")
            } else {
                callback(false, task.exception?.message.toString())
            }
        }
    }

    fun deleteAccount(callback: (Boolean, String) -> Unit) {
        auth.currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Account Deleted.")
            } else callback(false, task.exception?.message.toString())
        }
    }

    fun resetPassword(emailAddress: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, "Email sent.")
            } else callback(false, task.exception?.message.toString())
        }
    }

    fun addMovieToList(movieModel: MovieModel, callback: (Boolean, String) -> Unit) {
        db.collection(NETPLIX_USERS).document(auth.uid.toString()).collection(MOVIES_LIST)
            .document(movieModel.id.toString()).set(movieModel).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Added")
                } else {
                    callback(false, task.exception?.message.toString())
                }
            }
    }

    fun getMoviesList(callback: (MutableList<MovieModel>) -> Unit) {
        var tempList = mutableListOf<MovieModel>()
        db.collection(NETPLIX_USERS).document(auth.uid.toString()).collection(MOVIES_LIST).get()
            .addOnSuccessListener {
                if (!it?.isEmpty!!) {
                    it.forEach {
                        tempList.add(it.toObject(MovieModel::class.java))
                    }
                    callback(tempList.toMutableList())
                } else callback(mutableListOf())
            }

    }

    fun getShowsList(callback: (MutableList<TvModel>) -> Unit) {
        var tempList = mutableListOf<TvModel>()
        db.collection(NETPLIX_USERS).document(auth.uid.toString()).collection(TV_LIST).get()
            .addOnSuccessListener {
                if (!it?.isEmpty!!) {
                    it.forEach {
                        tempList.add(it.toObject(TvModel::class.java))
                    }
                    callback(tempList.toMutableList())
                } else callback(mutableListOf())
            }

    }


    fun addTvShowToList(tvModel: TvModel, callback: (Boolean, String) -> Unit) {
        db.collection(NETPLIX_USERS)
            .document(auth.uid.toString())
            .collection(Constants.TV_LIST)
            .document(tvModel.id.toString())
            .set(tvModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Added")
                } else {
                    callback(false, task.exception?.message.toString())
                }
            }
    }


    fun getUserInfo(callback: (task: Task<DocumentSnapshot>) -> Unit) {
        db.collection(NETPLIX_USERS).document(auth.uid.toString()).get()
            .addOnCompleteListener { task ->
                callback(task)
            }
    }

    fun removeMovie(id: String, callback: (task: Task<Void>) -> Unit) {
        db.collection(NETPLIX_USERS).document(auth.uid.toString()).collection(MOVIES_LIST)
            .document(id).delete().addOnCompleteListener { task ->
                callback(task)
            }
    }

    fun removeTvShow(id: String, callback: (task: Task<Void>) -> Unit) {
        db.collection(NETPLIX_USERS).document(auth.uid.toString())
            .collection(TV_LIST).document(id)
            .delete()
            .addOnCompleteListener { task ->
                callback(task)
            }
    }
    fun getUserPic(url: String, callback: (task: Task<Uri>) -> Unit) {
        activity?.let {
            FirebaseStorage.getInstance()
                .getReferenceFromUrl(url)
                .downloadUrl.addOnCompleteListener(
                    it
                ) { task ->
                    callback(task)
                }
        }
    }

    fun isFavMovie(id: String, callback: (Boolean, String) -> Unit) {
        db.collection(NETPLIX_USERS).document(auth.uid.toString()).collection(MOVIES_LIST)
            .document(id).get().addOnSuccessListener { task ->
                if (task.exists()) {
                    callback(true, "Founded.")
                } else {
                    callback(false, "Not Found.")
                }
            }
    }

    fun isFavTvShow(id: String, callback: (Boolean, String) -> Unit) {
        db.collection(NETPLIX_USERS)
            .document(auth.uid.toString())
            .collection(TV_LIST)
            .document(id)
            .get()
            .addOnSuccessListener { task ->
                if (task.exists()) {
                    callback(true, "Founded.")
                } else {
                    callback(false, "Not Found.")
                }
            }
    }

    fun destroy() {
        activity = null
    }
}