package com.example.travenor_customerapp.presentation.Details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travenor_customerapp.data.repository.BookingRepository
import com.example.travenor_ownerapp.core.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed interface CustomerRequestUi {
    data object Idle : CustomerRequestUi
    data object Creating : CustomerRequestUi
    data object Waiting : CustomerRequestUi
    data object Accepted : CustomerRequestUi
    data object Rejected : CustomerRequestUi
    data class Error(val message: String) : CustomerRequestUi
}


@HiltViewModel
class DetailViewModel @Inject constructor(private val bookingRepository: BookingRepository): ViewModel() {


    private val _ui = MutableStateFlow<CustomerRequestUi>(CustomerRequestUi.Idle)
    val ui: StateFlow<CustomerRequestUi> = _ui.asStateFlow()

    private val _btnEnabled = MutableStateFlow(true)
    val btnEnabled: StateFlow<Boolean> = _btnEnabled.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private var listenJob: Job? = null
    private var currentRequestId: String? = null

    fun book(cityId: String,customerId: String){

        if (_loading.value) return

        _loading.value = true
        _btnEnabled.value = false
        _ui.value = CustomerRequestUi.Creating

        viewModelScope.launch {

            try {

                val doc = bookingRepository.createRequest(cityId = cityId, customerId = customerId).await()

                currentRequestId = doc.id

                startObservingRequest(currentRequestId)


            }catch (e: Exception){

                _loading.value = false
                _btnEnabled.value = true
                _ui.value = CustomerRequestUi.Error(e.message ?: "Unknown error")
            }

        }

    }

    private fun startObservingRequest(currentRequestId: String?) {

        listenJob?.cancel()

        if (currentRequestId==null) {
            _ui.value= CustomerRequestUi.Error("id is null")
            return
        }

        listenJob = viewModelScope.launch {

            bookingRepository.listenToRequest(currentRequestId).collect { bookingRequest ->

                _loading.value = false

                if (bookingRequest==null){
                    _ui.value = CustomerRequestUi.Error("req is null")
                    return@collect
                }

                when(bookingRequest.status){

                    Status.PENDING -> {
                        _ui.value = CustomerRequestUi.Waiting
                    }

                    Status.ACCEPTED -> {
                        _ui.value = CustomerRequestUi.Accepted
                    }

                    Status.REJECTED -> {
                        _ui.value = CustomerRequestUi.Rejected
                    }

                    else -> {
                        _ui.value = CustomerRequestUi.Error("Unknown  stats : "+{bookingRequest.status})
                    }

                }


            }

        }

    }

    fun onDispose() {
        listenJob?.cancel()
        listenJob = null
        currentRequestId = null
        _loading.value = false
        _btnEnabled.value = true
        _ui.value = CustomerRequestUi.Idle
    }

}