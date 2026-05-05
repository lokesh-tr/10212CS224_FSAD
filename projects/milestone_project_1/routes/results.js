const express = require('express');
const pool = require('../db/database');
const { authenticateToken, requireRole } = require('../middleware/auth');

const router = express.Router();

// Submit exam answers (Student only)
router.post('/submit', authenticateToken, requireRole('student'), async (req, res) => {
    const { exam_id, answers } = req.body;
    // answers format: { question_id: 'A', question_id2: 'C' }

    if (!exam_id || !answers) {
        return res.status(400).json({ error: 'exam_id and answers are required' });
    }

    try {
        // Fetch correct answers for the exam
        const [questions] = await pool.query('SELECT id, correct_option FROM questions WHERE exam_id = ?', [exam_id]);

        let score = 0;
        let totalQuestions = questions.length;

        if (totalQuestions === 0) {
            return res.status(400).json({ error: 'This exam has no questions' });
        }

        // Calculate score
        for (let q of questions) {
            if (answers[q.id] === q.correct_option) {
                score++;
            }
        }

        // Save result
        await pool.query(
            'INSERT INTO results (user_id, exam_id, score, total_questions) VALUES (?, ?, ?, ?)',
            [req.user.id, exam_id, score, totalQuestions]
        );

        res.json({ message: 'Exam submitted successfully', score, totalQuestions });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error submitting exam' });
    }
});

// Get user's past scores
router.get('/my', authenticateToken, async (req, res) => {
    try {
        const [results] = await pool.query(
            `SELECT r.id, r.score, r.total_questions, r.submitted_at, e.title 
             FROM results r 
             JOIN exams e ON r.exam_id = e.id 
             WHERE r.user_id = ? 
             ORDER BY r.submitted_at DESC`,
            [req.user.id]
        );
        res.json(results);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error retrieving results' });
    }
});

// Get all scores (Admin only)
router.get('/all', authenticateToken, requireRole('admin'), async (req, res) => {
    try {
        const [results] = await pool.query(
            `SELECT r.id, r.score, r.total_questions, r.submitted_at, e.title AS exam_title, u.username 
             FROM results r 
             JOIN exams e ON r.exam_id = e.id 
             JOIN users u ON r.user_id = u.id 
             ORDER BY r.submitted_at DESC`
        );
        res.json(results);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error retrieving all results' });
    }
});

module.exports = router;
