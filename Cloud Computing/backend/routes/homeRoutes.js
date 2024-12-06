// routes/home.js
import express from "express";
import { getHomeData } from "../controller/HomeController.js";

const router = express.Router();

router.get("/", async (req, res) => {
  const { lat, lon } = req.query;

  if (!lat || !lon) {
    return res.status(400).json({ error: "Latitude and longitude are required." });
  }

  try {
    const homeData = await getHomeData(lat, lon);
    res.json(homeData);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

export default router;
