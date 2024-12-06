import express from "express";
import { getWeatherAndSafety } from "../controller/ResultController.js";

const router = express.Router();

router.get("/result", async (req, res) => {
  const { lat, lon } = req.query;

  try {
    const result = await getWeatherAndSafety(lat, lon);
    res.json(result); // Kirimkan hasil ke frontend
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

export default router;
