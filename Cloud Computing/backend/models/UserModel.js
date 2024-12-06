import {Sequelize} from "sequelize";
import db from "../config/Database.js";

const { DataTypes } = Sequelize;

const Users = db.define('users',{
    name:{
        type:DataTypes.STRING
    },
    phone:{
        type:DataTypes.STRING
    },
    password:{
        type:DataTypes.STRING
    },
    confPassword:{
        type:DataTypes.STRING
    },
    profile_photo:{
        type:DataTypes.STRING,
        allowNull: true
    },
    refresh_token:{
        type:DataTypes.TEXT
    }
},{
    freezeTableName:true
});

export default Users;