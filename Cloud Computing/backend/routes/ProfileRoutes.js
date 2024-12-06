
import express from "express";
import {
  getProfile,
  updateProfile,
  uploadProfilePhoto,
  deleteProfilePhoto,
} from "../controller/ProfileController.js";
import upload from "../middleware/upload.js";

const router = express.Router();

router.get("/profile/:id", getProfile); // Get profile
router.put("/profile/:id", updateProfile); // Update name, phone
router.put("/profile/photo/:id", upload.single("profile_photo"), uploadProfilePhoto); // Upload photo
router.delete("/profile/photo/:id", deleteProfilePhoto); // Delete photo

export default router;
