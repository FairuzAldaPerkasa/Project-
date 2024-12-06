import { getCurrentWeather } from "../services/weatherService.js";
//import { getSafetyPrediction } from "../services/mlService.js";

export async function getWeatherAndSafety(lat, lon) {
  try {
    const weatherData = await getCurrentWeather(lat, lon); // Ambil cuaca
    //const safetyPrediction = await getSafetyPrediction(weatherData.currentWeather); // Kirim ke Flask API
    // console.log("Weather Data:", weatherData);
    // Gabungkan data untuk dikirimkan ke frontend

    const result = {
      temperature: weatherData.temperature,  // Ambil temperature
      humidity: weatherData.humidity,        // Ambil humidity
    };

    console.log("Filtered result:", result);  // Pastikan hanya data yang dibutuhkan yang dikirim
    return result;

    // return {
    //   weather: weatherData.currentWeather,
    //   //prediction: safetyPrediction,
    // };
  } catch (error) {
    console.error("Error fetching weather and safety data:", error.message);
    throw new Error("Unable to fetch combined weather and safety data.");
  }
}
