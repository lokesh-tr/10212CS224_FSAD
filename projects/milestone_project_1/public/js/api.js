const API_BASE = 'http://localhost:3000/api';

const api = {
    getToken() {
        return localStorage.getItem('token');
    },
    setToken(token, role, username) {
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
        localStorage.setItem('username', username);
    },
    logout() {
        localStorage.clear();
        window.location.href = '/index.html';
    },
    async request(endpoint, method = 'GET', body = null) {
        const headers = { 'Content-Type': 'application/json' };
        const token = this.getToken();
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const options = { method, headers };
        if (body) {
            options.body = JSON.stringify(body);
        }

        const response = await fetch(`${API_BASE}${endpoint}`, options);
        let data;
        try {
            data = await response.json();
        } catch (e) {
            data = { error: 'Invalid server response' };
        }

        if (!response.ok) {
            throw new Error(data.error || 'API Request Failed');
        }
        return data;
    },
    checkAuth(requiredRole = null) {
        const token = this.getToken();
        const role = localStorage.getItem('role');
        if (!token) {
            window.location.href = '/login.html';
            return false;
        }
        if (requiredRole && role !== requiredRole) {
            alert('Access Denied');
            window.location.href = role === 'admin' ? '/admin.html' : '/student.html';
            return false;
        }
        return true;
    },
    setupNavbar() {
        const username = localStorage.getItem('username');
        const role = localStorage.getItem('role');
        const userDisplay = document.getElementById('userDisplay');
        const logoutBtn = document.getElementById('logoutBtn');
        const navLinks = document.getElementById('navLinks');

        if (userDisplay && username) {
            userDisplay.textContent = `Hello, ${username} (${role})`;
        }

        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => {
                this.logout();
            });
        }
    }
};
