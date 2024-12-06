import fs from "fs";
import path from "path";

const defaultProfilePhoto = "/uploads/default-photo.jpg"; // Path ke foto default

export const getProfile = async (req, res) => {
    const { id } = req.params;
    // Fetch user data from database (sesuaikan dengan model Anda)
    const user = { id, name: "John Doe", phone: "123456789", profile_photo: defaultProfilePhoto };
    res.json(user);
};

export const updateProfile = async (req, res) => {
    const { id } = req.params;
    const { name, phone } = req.body;
    // Update data user di database (sesuaikan dengan model Anda)
    res.json({ id, name, phone, message: "Profile updated successfully" });
};

export const uploadProfilePhoto = async (req, res) => {
    const { id } = req.params;
    const file = req.file;

    if (!file) {
        return res.status(400).json({ message: "No file uploaded" });
    }

    // Update path file foto di database (sesuaikan dengan model Anda)
    const filePath = `/uploads/${file.filename}`;
    res.json({ id, profile_photo: filePath, message: "Photo uploaded successfully" });
};

export const deleteProfilePhoto = async (req, res) => {
    const { id } = req.params;
    // Hapus file foto lama jika ada (sesuaikan dengan database Anda)
    const filePath = path.join(process.cwd(), "uploads", "old-photo.jpg"); // Ganti dengan nama file sebenarnya
    if (fs.existsSync(filePath)) {
        fs.unlinkSync(filePath);
    }
    res.json({ id, message: "Photo deleted successfully" });
};
