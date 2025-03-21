package com.example.windy.data.remote

class FakeWeatherRemoteDataSource(val currentResponse: CurrentWeatherResponse, val fiveDayResponse: FiveDayThreeHourResponse): IWeatherRemoteDataSource {
    override suspend fun getCurrentWeatherRemote(
        lat: String,
        lon: String,
        units: String
    ): CurrentWeatherResponse? = currentResponse

    override suspend fun getFiveDayThreeHourWeatherRemote(
        lat: String,
        lon: String,
        units: String
    ): FiveDayThreeHourResponse? = fiveDayResponse
}