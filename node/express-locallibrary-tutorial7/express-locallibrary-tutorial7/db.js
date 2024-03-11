var file = "./msg.db";
var sqlite3 = require("sqlite3").verbose();
var db = new sqlite3.Database(file);

db.run(`create table if not exists "message" (
    "id" integer not null,
    "title" text,
    "content" text,
    "lastupdatetime" datetime not null,
    primary key("id" autoincrement)
);`);

db.run(`create table if not exists "users" (
    "id" integer not null,
    "email" text,
    "password" text,
    "EducationalSystem" text,
    "grade" text,
    "createdtime" datetime not null,
    primary key("id" autoincrement)
);`);

module.exports = db;