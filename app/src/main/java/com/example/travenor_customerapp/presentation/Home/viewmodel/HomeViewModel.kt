package com.example.travenor_customerapp.presentation.Home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travenor_customerapp.R
import com.example.travenor_customerapp.data.model.Destination
import com.example.travenor_customerapp.data.repository.AuthRepository
import com.example.travenor_customerapp.data.repository.DestinationRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val destinationRepository: DestinationRepository,
    val authRepository: AuthRepository
)  : ViewModel() {

val homeUiState: StateFlow<HomeUiState> = destinationRepository.getDestinations()
                                          .map { list-> HomeUiState(destinations = list) }
                                          .stateIn(
                                              scope = viewModelScope,
                                              started = SharingStarted.WhileSubscribed(5_000),
                                              initialValue = HomeUiState(isLoading = true)
                                          )

    private val _destination = MutableStateFlow<Destination>(Destination(
        id = "kolkata_reservoir",
        name = "Kolkata Reservoir",
        location = "Kolkata, India",
        imageRes = R.drawable.kolkata,
        rating = 4.7,
        pricePerPersonUsd = 59,
        description = "A calm getaway with scenic views, local culture, and relaxing spots to explore."
    ))
    val destination: StateFlow<Destination> = _destination


    fun getCurrUserEmail(): String{

        return auth.currentUser?.email ?: "User"
    }

    fun getDestination(destinationId: String){

        viewModelScope.launch {
        destinationRepository.getDestination(destinationId).collect{ destination->
            _destination.value = destination
        }
        }

    }

    fun signOut() {
        authRepository.signOut()
    }


}




data class HomeUiState(
    val isLoading: Boolean = false,
    val destinations: List<Destination> = emptyList(),
    val errorMessage: String? = null
)