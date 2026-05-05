const mysql = require('mysql2/promise');
const bcrypt = require('bcrypt');

async function seedDB() {
    console.log("Connecting to MySQL...");
    try {
        const connection = await mysql.createConnection({
            host: 'localhost',
            user: 'root',
            password: 'root',
            database: 'online_exam_system'
        });

        console.log("Clearing old data...");
        await connection.query('SET FOREIGN_KEY_CHECKS = 0');
        await connection.query('TRUNCATE TABLE results');
        await connection.query('TRUNCATE TABLE questions');
        await connection.query('TRUNCATE TABLE exams');
        await connection.query('TRUNCATE TABLE users');
        await connection.query('SET FOREIGN_KEY_CHECKS = 1');

        console.log("Seeding users...");
        const adminHash = await bcrypt.hash('admin123', 10);
        const studentHash = await bcrypt.hash('student123', 10);

        await connection.query(
            `INSERT INTO users (username, password_hash, role) VALUES 
            ('admin', ?, 'admin'), 
            ('student1', ?, 'student')`,
            [adminHash, studentHash]
        );

        console.log("Seeding exams...");
        const [phyRes] = await connection.query(
            `INSERT INTO exams (title, description) VALUES ('Physics Midterm', 'A comprehensive test covering kinematics and dynamics.')`
        );
        const [csRes] = await connection.query(
            `INSERT INTO exams (title, description) VALUES ('Computer Science Finals', 'Data structures and algorithms.')`
        );

        const phyExamId = phyRes.insertId;
        const csExamId = csRes.insertId;

        console.log("Seeding questions...");
        await connection.query(
            `INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES 
            (?, 'What is the SI unit of force?', 'Joule', 'Newton', 'Watt', 'Pascal', 'B'),
            (?, 'Who formulated the laws of motion?', 'Einstein', 'Newton', 'Galileo', 'Bohr', 'B'),
            (?, 'The acceleration due to gravity on Earth is approximately?', '9.8 m/s²', '10.5 m/s²', '8.9 m/s²', '9.0 m/s²', 'A')`,
            [phyExamId, phyExamId, phyExamId]
        );

        await connection.query(
            `INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES 
            (?, 'Which data structure uses LIFO?', 'Queue', 'Array', 'Stack', 'Tree', 'C'),
            (?, 'What is the time complexity of binary search?', 'O(n)', 'O(n log n)', 'O(1)', 'O(log n)', 'D')`,
            [csExamId, csExamId]
        );

        console.log("Database seeded successfully!");
        await connection.end();
    } catch (err) {
        console.error("Error seeding database:", err);
    }
}

seedDB();
