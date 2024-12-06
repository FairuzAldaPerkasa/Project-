'use strict';

/** @type {import('sequelize-cli').Migration} */
module.exports = {
  async up (queryInterface, Sequelize) {
    /**
     * Add altering commands here.
     *
     * Example:
    */
    await queryInterface.createTable('users', {
      id: {
        type: Sequelize.INTEGER,
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,  // Set as primary key
      },
      name:{
          type:Sequelize.STRING
      },
      phone:{
          type:Sequelize.STRING
      },
      password:{
          type:Sequelize.STRING
      },
      confPassword:{
          type:Sequelize.STRING
      },
      profile_photo:{
        type:Sequelize.STRING,
        allowNull: true
      },
      refresh_token:{
          type:Sequelize.TEXT
      },
      createdAt: {
        type: Sequelize.DATE,
        allowNull: false,
        defaultValue: Sequelize.NOW,
      },
      updatedAt: {
        type: Sequelize.DATE,
        allowNull: false,
        defaultValue: Sequelize.NOW,
      }});
  },

  async down (queryInterface, Sequelize) {
    /**
     * Add reverting commands here.
     *
     * Example:
    */
    await queryInterface.dropTable('users');
  }
};
