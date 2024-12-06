// const express = require("express");
// // const { getWeatherByCoordinates } = require("../services/weatherService");
// const { getWeatherData } = require("../services/weatherService");

// const router = express.Router();

// // Rute untuk mendapatkan cuaca berdasarkan koordinat
// router.get("/", async (req, res) => {
//   const { lat, lon } = req.query;

//   // Validasi parameter
//   if (!lat || !lon) {
//     return res.status(400).json({ error: "Latitude and longitude are required." });
//   }

//   try {
//     // const weatherData = await getWeatherByCoordinates(lat, lon);
//     const weatherData = await getWeatherData(lat, lon);
    
//     // const machineLearning = await getClusteringResult(weatherData);
//     res.json(weatherData);
//   } catch (error) {
//     res.status(500).json({ error: error.message });
//   }
// });

// module.exports = router;

import express from "express";
// import { getWeatherByCoordinates } from "../services/weatherService";
import { getCurrentWeather } from "../services/weatherService.js"; // Tambahkan .js jika diperlukan

const router = express.Router();

// Rute untuk mendapatkan cuaca berdasarkan koordinat
router.get("/", async (req, res) => {
  const { lat, lon } = req.query;

  // Validasi parameter
  if (!lat || !lon) {
    return res.status(400).json({ error: "Latitude and longitude are required." });
  }

  try {
    // const weatherData = await getWeatherByCoordinates(lat, lon);
    const weatherData = await getCurrentWeather(lat, lon);

    res.json(weatherData);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

export default router; // Menggunakan default export
