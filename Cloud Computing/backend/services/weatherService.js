import axios from "axios";



export async function getCurrentWeather(lat, lon) {
  const API_KEY = process.env.API_KEY;
  const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;

  try {
    const response = await axios.get(url);
    return {
      location: response.data.name,
      temperature: response.data.main.temp,
      temp_max: response.data.main.temp_max,
      temp_min: response.data.main.temp_min,
      humidity: response.data.main.humidity,
      weather: response.data.weather[0].description,
      wind_speed: response.data.wind.speed,
      pressure: response.data.main.pressure,
    };
  } catch (error) {
    console.error("Error fetching current weather:", error.message);
    throw new Error("Unable to fetch current weather data.");
  }
}

// export async function getWeatherForecast(lat, lon) {
//   const API_KEY = process.env.API_KEY;
//   const url = `https://api.openweathermap.org/data/2.5/forecast?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;

//   try {
//     const response = await axios.get(url);
//     const forecastData = response.data.list;

//     // Menghitung total curah hujan dalam satu hari (misalnya untuk 24 jam)
//     let totalRain = 0;
//     forecastData.forEach(item => {
//       if (item.rain) {
//         totalRain += item.rain["3h"] || 0;
//       }
//     });

//     // Mengembalikan data cuaca ramalan dengan total curah hujan dalam satu hari
//     const result = forecastData.map(item => ({
//       date: item.dt_txt,
//       temperature: item.main.temp,
//       humidity: item.main.humidity,
//       weather: item.weather[0].description,
//       rain: item.rain ? item.rain["3h"] : 0,  // Curah hujan per 3 jam
//     }));

//     return {
//       forecastData: result,
//       totalRain,  // Total curah hujan dalam 24 jam
//     };
//   } catch (error) {
//     console.error("Error fetching weather forecast:", error.message);
//     throw new Error("Unable to fetch weather forecast.");
//   }
// }

//  async function getWeatherData(lat, lon) {
//   try {
//     const currentWeather = await getCurrentWeather(lat, lon);
//     //const weatherForecast = await getWeatherForecast(lat, lon);

//     return {
//       currentWeather,
//       //weatherForecast,
//     };
//   } catch (error) {
//     console.error("Error fetching weather data:", error.message);
//     throw new Error("Unable to fetch weather data.");
//   }
// }


//  async function getClusteringResult(weatherData) {
//   const url = `MANA API ML NYA ????`;

//   // try {
//   //   // const response = await axios.get(url);
//   //   console.log(response.data)
//   //   return {
//   //     clustering: response.data.clustering ,
//   //   };
//   // } catch (error) {
//   //   console.error("Error fetching weather data:", error.message);
//   //   throw new Error("Unable to fetch weather data.");
//   // }
// }

// // module.exports = { getWeatherByCoordinates, getClusteringResult };
// // module.exports = { getWeatherData, getClusteringResult };

// export { getWeatherData, getClusteringResult };
