import { Sequelize } from "sequelize";
import db from "../config/Database.js";
import Users from "./UserModel.js";

const { DataTypes } = Sequelize;

const Posts = db.define(
  "posts",
  {
    content: { type: DataTypes.TEXT, allowNull: false 
    },
    image: { type: DataTypes.STRING 
    },
    likes: { type: DataTypes.INTEGER, defaultValue: 0 },
  },
  {
    freezeTableName: true,
    timestamps: true, // Mengaktifkan otomatis createdAt dan updatedAt
  }
);

Users.hasMany(Posts, { foreignKey: "userId" });
Posts.belongsTo(Users, { foreignKey: "userId" });

export default Posts;
