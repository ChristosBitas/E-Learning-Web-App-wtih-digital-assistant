import React, { useState, useEffect } from 'react';
import ApiService from '../../service/ApiService';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: ''
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [loading, setLoading] = useState(false);

    // Handling input form input changes and updating formData state
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Checking if all fields are filled
    const validateForm = () => {
        const { name, email, password } = formData;
        return name && email && password;
    };

    // Handling submit function for user registration
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) {
            setErrorMessage('Please fill all the fields.');
            setTimeout(() => setErrorMessage(''), 5000); // Clear error message after 5 seconds
            return;
        }

        setLoading(true);

        try {
            const response = await ApiService.registerUser(formData);

            if (response.statusCode === 200) { // Reset form and show success message
                setFormData({
                    name: '',
                    email: '',
                    password: ''
                });
                setSuccessMessage('User registered successfully');
                setShowModal(true); // Show success modal
            }
        } catch (error) {
            // Handling error in registration
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000); // Clear error message after 5 seconds
        } finally {
            setLoading(false); // Stop loading state
        }
    };

    // Automatically redirecting to login page after showing success message
    useEffect(() => {
        if (showModal) {
            const timer = setTimeout(() => {
                setShowModal(false);
                navigate('/login'); // Navigating to login page
            }, 3000);
            return () => clearTimeout(timer); // Clear timer on cleanup
        }
    }, [showModal, navigate]);

    return (
        <div className="auth-container">
            {loading && <p>Loading...</p>} {/* Showing loading message after API call */}
            {errorMessage && <p className="custom-alert custom-alert-error">{errorMessage}</p>} {/* Display error message if any */}
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit}>
                {/* Name Input */}
                <div className="form-group">
                    <label>Name:</label>
                    <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
                </div>
                {/* Email Input */}
                <div className="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={formData.email} onChange={handleInputChange} required />
                </div>
                {/* Password Input */}
                <div className="form-group">
                    <label>Password:</label>
                    <input type="password" name="password" value={formData.password} onChange={handleInputChange} required />
                </div>
                <button type="submit" disabled={loading}>Register</button> {/* Disable button when loading */}
            </form>

            <p className="register-link">
                Already have an account? <a href="/login">Login</a> {/* Link to login page */}
            </p>

            {/* Modal for displaying success message */}
            {showModal && (
                <div className="modal">
                    <div className="custom-alert custom-alert-success"> {/* Apply success alert class here */}
                        <p>{successMessage}</p>
                    </div>
                </div>
            )}
        </div>
    );
}

export default RegisterPage;
