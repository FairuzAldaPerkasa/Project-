import express from "express";
import dotenv from "dotenv";
import { fileURLToPath } from 'url';
import path from 'path';  // Menambahkan import untuk path module
import cookieParser from "cookie-parser";
import cors from "cors";
import db from "./config/Database.js";
import Posts from "./models/PostModel.js";
import router from "./routes/index.js";
import ProfileRoutes from "./routes/ProfileRoutes.js";
import postRoutes from "./routes/PostsRoutes.js"; 
// require("dotenv").config();
// 
// 
import homeRoutes from "./routes/homeRoutes.js";
import weatherRoutes from "./routes/weather.js";
import ResultRoutes from "./routes/ResultRoutes.js";



//import Users from "./models/UserModel.js";
dotenv.config();
const app = express();
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// try{
//     await db.authenticate();
//     console.log('Database connected..');
//     //await Users.sync();//membuat table otomatis jika belom ada
// }catch (error) {
//     console.error(error);
// }
app.use((req, res, next) => {
    console.log(`${req.method} ${req.url}`);
    next();
  });
app.use(cors({credentials:true, origin:'http://localhost:3000'}));
app.use(cookieParser());
app.use(express.json());

app.use("/uploads", express.static(path.join(__dirname, "./uploads")));
// Menggunakan ProfileRoutes
app.use("/weather", weatherRoutes);
app.use(ProfileRoutes);
app.use(router);
app.use(postRoutes);
app.use("/home", homeRoutes); 
app.use('/api', ResultRoutes);

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});


