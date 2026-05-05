const express = require('express');
const pool = require('../db/database');
const { authenticateToken, requireRole } = require('../middleware/auth');

const router = express.Router();

// Get all exams (Auth required)
router.get('/', authenticateToken, async (req, res) => {
    try {
        const [exams] = await pool.query('SELECT * FROM exams ORDER BY created_at DESC');
        res.json(exams);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error retrieving exams' });
    }
});

// Get exam details with questions
router.get('/:id', authenticateToken, async (req, res) => {
    try {
        const [examRows] = await pool.query('SELECT * FROM exams WHERE id = ?', [req.params.id]);
        if (examRows.length === 0) return res.status(404).json({ error: 'Exam not found' });

        const exam = examRows[0];

        // If admin, send correct options. If student, omit correct options.
        let questionsQuery = 'SELECT id, exam_id, question_text, option_a, option_b, option_c, option_d FROM questions WHERE exam_id = ?';
        if (req.user.role === 'admin') {
            questionsQuery = 'SELECT * FROM questions WHERE exam_id = ?';
        }

        const [questions] = await pool.query(questionsQuery, [req.params.id]);
        res.json({ exam, questions });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error retrieving exam details' });
    }
});

// Create new exam (Admin only)
router.post('/', authenticateToken, requireRole('admin'), async (req, res) => {
    const { title, description } = req.body;
    if (!title) return res.status(400).json({ error: 'Title is required' });

    try {
        const [result] = await pool.query('INSERT INTO exams (title, description) VALUES (?, ?)', [title, description]);
        res.status(201).json({ id: result.insertId, title, description });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error creating exam' });
    }
});

// Add question to exam (Admin only)
router.post('/:id/questions', authenticateToken, requireRole('admin'), async (req, res) => {
    const { question_text, option_a, option_b, option_c, option_d, correct_option } = req.body;
    const exam_id = req.params.id;

    if (!question_text || !option_a || !option_b || !option_c || !option_d || !correct_option) {
        return res.status(400).json({ error: 'All fields are required' });
    }

    try {
        const [result] = await pool.query(
            `INSERT INTO questions (exam_id, question_text, option_a, option_b, option_c, option_d, correct_option) 
             VALUES (?, ?, ?, ?, ?, ?, ?)`,
            [exam_id, question_text, option_a, option_b, option_c, option_d, correct_option]
        );
        res.status(201).json({ id: result.insertId, message: 'Question added successfully' });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Server error adding question' });
    }
});

module.exports = router;
