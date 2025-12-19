package com.example.travenor_customerapp.data.repository

import com.example.travenor_customerapp.R
import com.example.travenor_customerapp.data.model.Destination
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class DestinationRepository @Inject constructor() {

    private val destinations: List<Destination> = listOf(
        Destination(
            id = "kolkata_reservoir",
            name = "Kolkata Reservoir",
            location = "Kolkata, India",
            imageRes = R.drawable.kolkata,
            rating = 4.7,
            pricePerPersonUsd = 59,
            description = "A calm getaway with scenic views, local culture, and relaxing spots to explore.Theh daanc ajac aicna caicnac iacna cj cajjca caica c aciajc a"
        ),
        Destination(
            id = "mumbai_marine_drive",
            name = "Marine Drive Sunset",
            location = "Mumbai, India",
            imageRes = R.drawable.mumbai,
            rating = 4.6,
            pricePerPersonUsd = 72,
            description = "A breezy coastline experience with city vibes, sunsets, and iconic waterfront walks.Theh daanc ajac aicna caicnac iacna cj cajjca caica c aciajc a"
        ),
        Destination(
            id = "delhi_india_gate",
            name = "India Gate Walk",
            location = "Delhi, India",
            imageRes = R.drawable.delhi,
            rating = 4.5,
            pricePerPersonUsd = 49,
            description = "A classic city experience around landmarks, food streets, and vibrant evening scenes. Theh daanc ajac aicna caicnac iacna cj cajjca caica c aciajc a"
        ),
        Destination(
            id = "bengaluru_cubbon_park",
            name = "Cubbon Park Escape",
            location = "Bengaluru, India",
            imageRes = R.drawable.banglore,
            rating = 4.4,
            pricePerPersonUsd = 55,
            description = "A green retreat with peaceful trails, fresh air, and laid-back city exploration.Theh daanc ajac aicna caicnac iacna cj cajjca caica c aciajc a"
        )
    )


    fun getDestinations(): Flow<List<Destination>> = flowOf(destinations)

    fun getDestination(destinationId: String): Flow<Destination> {
        return getDestinations().map { list -> list.first { it.id == destinationId } }
    }

}