package com.bullsage.android.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullsage.android.data.model.StockInfoResponse
import com.bullsage.android.data.model.StockPriceResponse
import com.bullsage.android.data.network.BullsageApi
import com.bullsage.android.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bullsageApi: BullsageApi,
    private val watchlistRepository: WatchlistRepository
): ViewModel() {
    val ticker = savedStateHandle.get<String>("ticker")

    private val _stockDetails = MutableStateFlow<StockDetails?>(null)
    val stockDetails = _stockDetails.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getStockDetails()
    }

    fun addToWatchlist() {
        viewModelScope.launch {
            val details = _stockDetails.value!!
            watchlistRepository.addToWatchlist(name = details.info.longName, ticker = ticker!!)
            _stockDetails.update {
                it!!.copy(isSaved = true)
            }
        }
    }

    fun removeFromWatchlist() {
        viewModelScope.launch {
            watchlistRepository.removeFromWatchlist(ticker = ticker!!)
            _stockDetails.update {
                it!!.copy(isSaved = false)
            }
        }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }

    private fun getStockDetails() {
        viewModelScope.launch {
            ticker?.let {
                val priceResponse = bullsageApi.getStockPrice(ticker)
                val infoResponse = bullsageApi.getStockInfo(ticker)

                if (priceResponse.isSuccessful && infoResponse.isSuccessful) {
                    _stockDetails.update {
                        StockDetails(
                            price = priceResponse.body()!!,
                            info = infoResponse.body()!!,
                            isSaved = watchlistRepository.isTickerPresent(ticker)
                        )
                    }
                } else {
                    _errorMessage.update { "Something went wrong" }
                }
            }
        }
    }
}

data class StockDetails(
    val price: StockPriceResponse,
    val info: StockInfoResponse,
    val isSaved: Boolean
)