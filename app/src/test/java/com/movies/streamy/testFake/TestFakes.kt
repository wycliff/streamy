package com.movies.streamy.testFake

import com.movies.streamy.model.dataSource.network.data.response.City
import com.movies.streamy.model.dataSource.network.data.response.Coord
import com.movies.streamy.model.dataSource.network.data.response.CurrentWeather
import com.movies.streamy.model.dataSource.network.data.response.FiveDayWeather


fun getFakeCurrentWeatherResponse() = CurrentWeather(
    cod = 200,
    coord = getFakeCoordEntity(),
    dt = 1691960024,
    id = 184622,
    main = getFakeMainWeather(),
    name = "Nakuru",
    timezone = 10800,
    weather = null,
    date = "2023-08-22 12:00:00"

)

fun getFakeCoordEntity() = Coord(
    lat = -0.269,
    lon = 36.006
)

fun getFakeMainWeather() = Main(
    humidity = 75,
    pressure = 1017,
    seaLevel = 1017,
    temp = 14.15,
    tempMax = 14.15,
    tempMin = 14.15
)

fun getFiveDayWeatherResponse() = FiveDayWeather(
    city = getFakeCity(),
    cnt = 40,
    cod = 200,
    list = null,
    message = 0
)

fun getFakeCity() = City(
    coord = getFakeCoordEntity(),
    country = "IT",
    id = 3163858,
    name = "Zocca",
    population = 4593,
    sunrise = 1692678412,
    sunset = 1692727897,
    timezone = 7200
)
