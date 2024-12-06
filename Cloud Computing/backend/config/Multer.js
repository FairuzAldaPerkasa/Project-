import multer from "multer";
import path from "path";

// Konfigurasi penyimpanan file
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    // Folder penyimpanan file
    cb(null, "uploads");
  },
  filename: (req, file, cb) => {
    // Penamaan file dengan timestamp dan nama file aslinya
    const timestamp = Date.now();
    const originalName = file.originalname;
    const uniqueName = `${timestamp}_${originalName}`;
    cb(null, uniqueName);
  },
});

// Filter untuk hanya menerima file gambar
const fileFilter = (req, file, cb) => {
  const fileTypes = /jpeg|jpg|png|gif/;
  const extName = fileTypes.test(path.extname(file.originalname).toLowerCase());
  const mimeType = fileTypes.test(file.mimetype);

  if (extName && mimeType) {
    cb(null, true);
  } else {
    cb(new Error("Only image files are allowed!"), false);
  }
};

// Inisialisasi multer
const upload = multer({
  storage: storage,
  limits: { fileSize: 2 * 1024 * 1024 }, // Maksimum 2 MB
  fileFilter: fileFilter,
});

export default upload;
