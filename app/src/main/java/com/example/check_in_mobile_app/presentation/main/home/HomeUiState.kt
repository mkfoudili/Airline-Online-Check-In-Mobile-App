package com.example.check_in_mobile_app.presentation.main.home

import com.example.domain.model.Booking

data class HomeUiState(
    val userName: String = "",
    val activeFlight: Booking? = null,
    val isActiveFlightLoading: Boolean = false,

    val bookingReference: String = "",
    val lastName: String = "",
    val isSearching: Boolean = false,
    val searchResult: Booking? = null,
    val searchError: SearchError? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
) {
    val isCheckInActive: Boolean
        get() = activeFlight?.status?.let { status ->
            status.name == "CHECK_IN_OPEN"
        } ?: false

    val activeFlightDestination: String
        get() = activeFlight?.flight?.let {
            "${it.destinationCity} (${it.destination})"
        } ?: ""
}

sealed class SearchError {
    object NotFound : SearchError()
    object NetworkError : SearchError()
    object EmptyFields : SearchError()
    data class Unknown(val message: String) : SearchError()
}