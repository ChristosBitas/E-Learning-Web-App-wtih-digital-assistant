import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import ApiService from "../../service/ApiService";

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [showAlert, setShowAlert] = useState(false); 
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from?.pathname || '/home'; // User navigating to home page after success login

    // Handling login form for submit
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Checking if both email and password are provided
        if (!email || !password) {
            setError('Please fill in all fields.');
            setShowAlert(true); // Show custom alert if fields are missing
            setTimeout(() => setShowAlert(false), 5000); // Hide alert after 5 seconds
            return;
        }

        // Attempting login with the API
        try {
            const response = await ApiService.loginUser({ email, password });
            if (response.statusCode === 200) {
                // Saving token and role in localStorage on successful login
                localStorage.setItem('token', response.token);
                localStorage.setItem('role', response.role);
                // Navigating to the intended page or home on successful login
                navigate(from, { replace: true });
            }
        } catch (error) {
            // Displaying error message if login fails
            setError(error.response?.data?.message || error.message);
            setShowAlert(true); 
            setTimeout(() => setShowAlert(false), 5000); // Hiding alert message after 5 seconds
        }
    };

    return (
        <div className="auth-container">
            <h2>Login</h2>

            {/* Custom Alert Modal for displaying errors */}
            {showAlert && (
                <div className="custom-alert custom-alert-error">
                    <p>{error}</p>
                    <button onClick={() => setShowAlert(false)} className="alert-close-btn">Close</button>
                </div>
            )}

            {/* Login Form */}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Email:</label>
                    <div className="input-container">
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                </div>
                <div className="form-group">
                    <label>Password:</label>
                    <div className="input-container">
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                </div>
                <button type="submit" className="submit-button">Login</button>
            </form>

            {/* Link to register if user doesn't have an account */}
            <p className="register-link">
                Don't have an account? <a href="/register">Register</a>
            </p>
        </div>
    );
}

export default LoginPage;