package com.petworld.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.petworld.model.PetWorld

class ListViewModel(application: Application) : AndroidViewModel(application) {
    val petLD = MutableLiveData<ArrayList<PetWorld>>()
    val petLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun refresh() {
        loadingLD.value = true
        petLoadErrorLD.value = false
        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/uts_anmp/berita.json"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                val sType = object : TypeToken<List<PetWorld>>() { }.type
                val result = Gson().fromJson<List<PetWorld>>(it, sType)
                petLD.value = result as ArrayList<PetWorld>?
                loadingLD.value = false

                Log.d("showvoley", result.toString())

            },
            {
                Log.d("showvoley", it.toString())
                petLoadErrorLD.value = false
                loadingLD.value = false
            })
        stringRequest.tag = TAG
        queue?.add(stringRequest)

        petLoadErrorLD.value = false
        loadingLD.value = true
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}