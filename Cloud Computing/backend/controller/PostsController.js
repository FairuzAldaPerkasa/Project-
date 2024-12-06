import Users from "../models/UserModel.js";
import Posts from "../models/PostModel.js";
import upload from "../config/Multer.js";

// Get all posts with user details
export const getPosts = async (req, res) => {
  try {
    const posts = await Posts.findAll({
      include: [
        {
          model: Users,
          attributes: ["name", "profile_photo"],
        },
      ],
      order: [["createdAt", "DESC"]], // Urutkan berdasarkan waktu unggah
    });
    res.json(posts);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// Create a new post
export const createPost = async (req, res) => {
  const uploadMiddleware = upload.single("image");
  uploadMiddleware(req, res, async (err) => {
    if (err) {
      return res.status(400).json({ message: err.message });
    }
    const { userId, content } = req.body;
    const image = req.file ? req.file.filename : null;
    try {
      const post = await Posts.create({ userId, content, image });
      const createdPost = await Posts.findOne({
        where: { id: post.id },
        include: [
          {
            model: Users,
            attributes: ["name", "profile_photo"],
          },
        ],
      });
      res.status(201).json(createdPost);
    } catch (error) {
      res.status(500).json({ message: error.message });
    }
  });
};

// Update a post
export const updatePost = async (req, res) => {
  const { id } = req.params;
  const { content } = req.body;
  const image = req.file ? req.file.filename : req.body.image; // Gunakan gambar baru atau tetap yang lama
  try {
    await Posts.update({ content, image }, { where: { id } });
    const updatedPost = await Posts.findOne({
      where: { id },
      include: [
        {
          model: Users,
          attributes: ["name", "profile_photo"],
        },
      ],
    });
    res.json(updatedPost);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

// Delete a post
export const deletePost = async (req, res) => {
  const { id } = req.params;
  try {
    await Posts.destroy({ where: { id } });
    res.json({ message: "Post deleted successfully" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
