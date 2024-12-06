import dotenv from 'dotenv';
dotenv.config();

import {Sequelize} from "sequelize";

const db = new Sequelize(
    process.env.DB_NAME,
    process.env.DB_USER,
    process.env.DB_PASSWORD,
    {
        host: process.env.DB_HOST,
        dialect: 'mysql',
    }
)

// import {Sequelize} from "sequelize";

// const db = new Sequelize('auth_db','root','',{
//     host: "localhost",
//     dialect: "mysql"
// });

export default db;



// export default db;