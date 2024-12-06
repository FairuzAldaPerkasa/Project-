import axios from "axios";

export async function getSafetyPrediction(weatherData) {
  const url = "http://your-flask-api-url/predict"; // URL Flask API
  try {
    const response = await axios.post(url, {
      temperature: weatherData.temperature,
      humidity: weatherData.humidity,
      weather: weatherData.weather,
      wind_speed: weatherData.wind_speed,
      pressure: weatherData.pressure,
    });

    return response.data; // Hasil prediksi dari Flask API
  } catch (error) {
    console.error("Error fetching safety prediction:", error.message);
    throw new Error("Unable to fetch safety prediction.");
  }
}
