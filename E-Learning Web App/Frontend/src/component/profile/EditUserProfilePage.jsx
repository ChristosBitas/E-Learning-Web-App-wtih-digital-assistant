import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

const EditUserProfilePage = () => {
    const [user, setUser] = useState({ name: '', email: '', password: '' });
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [success, setSuccess] = useState(false); // Track if profile update is successful
    const [showModal, setShowModal] = useState(false); // Control visibility of the success modal
    const navigate = useNavigate(); // Hook for navigation

    // Fetching user data 
    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                setLoading(true); // Set loading state while fetching data
                const response = await ApiService.getLoggedInUserData();
                setUser(response.user); // Set the user data from API response
            } catch (error) {
                setError(error.message); // Handle errors during fetch
            } finally {
                setLoading(false); // Stop loading once data is fetched
            }
        };
        fetchUserProfile();
    }, []);

    // Handling submit form user for profile update
    const handleUpdateProfile = async (e) => {
        e.preventDefault();
        setSuccess(false); // Reset success state before update
        try {
            await ApiService.updateUserProfile(user); // Calling API in order to update user profile
            setSuccess(true); // Successful update
            setShowModal(true); // Show modal on success
        } catch (error) {
            setError(error.message); // Handling errors when updating user information
        }
    };

    // Handling form field after changes
    const handleChange = (e) => {
        setUser({ ...user, [e.target.name]: e.target.value }); // Updating user informations with input values
    };

    // Redirecting to login page after success profile update and logout
    useEffect(() => {
        if (success) {
            const timer = setTimeout(async () => {
                setShowModal(false); // Hide the success modal
                await ApiService.logout(); // Log out the user
                navigate('/login'); // Redirecting to login page
            }, 3000); // 3-second delay before logout and redirect
            return () => clearTimeout(timer); // Clean up the timer
        }
    }, [success, navigate]);

    if (loading) {
        return <p>Loading profile...</p>; // Displaying loading label while fetching profile
    }

    return (
        <div className="edit-profile-page-container">
            {/* Display error message */}
            {error && <p className="error-message">{error}</p>}

            {/* Edit user profile */}
            {user && (
                <form onSubmit={handleUpdateProfile} className="edit-profile-form">
                    <h2>Edit Profile</h2>
                    <div className="form-group">
                        <label htmlFor="name">Name:</label>
                        <input
                            type="text"
                            name="name"
                            value={user.name}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input
                            type="email"
                            name="email"
                            value={user.email}
                            onChange={handleChange}
                            className="form-control"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password:</label>
                        <input
                            type="password"
                            name="password"
                            value={user.password}
                            onChange={handleChange}
                            className="form-control"
                        />
                    </div>
                    <button type="submit" className="update-profile-button">
                        Update Profile
                    </button>
                </form>
            )}

            {/* Success Modal for profile update */}
            {showModal && (
                <div className="modal">
                    <div className="modal-content">
                        <p>Profile updated successfully! <br />Logging out...</p>
                    </div>
                </div>
            )}
        </div>
    );
};

export default EditUserProfilePage;