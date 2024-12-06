import { Sequelize } from "sequelize";
import db from "../config/Database.js";
import Users from "./UserModel.js";
import Posts from "./PostModel.js";

const { DataTypes } = Sequelize;

const Comments = db.define(
  "comments",
  {
    userId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    postId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    content: {
      type: DataTypes.TEXT,
      allowNull: false,
    },
  },
  {
    freezeTableName: true,
  }
);

Users.hasMany(Comments, { foreignKey: "userId" });
Comments.belongsTo(Users, { foreignKey: "userId" });
Posts.hasMany(Comments, { foreignKey: "postId" });
Comments.belongsTo(Posts, { foreignKey: "postId" });

export default Comments;
