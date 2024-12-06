// controllers/homeController.js
import { getCurrentWeather } from "../services/weatherService.js";
import { getPosts } from "../controller/PostsController.js";

const getDayName = (date) => {
  const days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  return days[date.getDay()];
};

export const getHomeData = async (lat, lon) => {
  try {
    // Dapatkan data cuaca
    const weatherData = await getCurrentWeather(lat, lon);
    
    console.log("Fetching posts...");
    // Dapatkan data postingan
    const posts = await getPosts();
    console.log("Posts fetched successfully:", posts);
    // Dapatkan tanggal dan hari sekarang
    const currentDate = new Date();
    const dayName = getDayName(currentDate);
    const formattedDate = currentDate.toISOString().split("T")[0];

    // Gabungkan data
    return {
      weather: {
        location: weatherData.location,
        humidity: weatherData.humidity,
        wind_speed: weatherData.wind_speed,
        pressure: weatherData.pressure,
        temperature: weatherData.temperature,
      },
      date: {
        day: dayName,
        formattedDate,
      },
      posts,
    };
  } catch (error) {
    throw new Error("Unable to fetch home data.");
  }
};
