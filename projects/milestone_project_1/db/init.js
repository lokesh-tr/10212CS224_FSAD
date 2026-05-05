const mysql = require('mysql2/promise');
const fs = require('fs');
const path = require('path');

async function initializeDB() {
    console.log("Connecting to MySQL...");
    try {
        const connection = await mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'root',
            multipleStatements: true // Allows running multiple queries at once
        });

        console.log("Reading schema.sql...");
        const schema = fs.readFileSync(path.join(__dirname, 'schema.sql'), 'utf8');
        
        console.log("Executing schema...");
        await connection.query(schema);
        
        console.log("Database schema initialized successfully!");
        await connection.end();
    } catch (err) {
        console.error("Error initializing database:", err);
    }
}

initializeDB();
