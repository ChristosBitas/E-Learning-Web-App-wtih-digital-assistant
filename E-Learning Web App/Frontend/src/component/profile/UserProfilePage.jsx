import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

const UserProfilePage = () => {
    const [user, setUser] = useState(null); // Stores the user profile information
    const [error, setError] = useState(null); // Stores any error messages encountered
    const navigate = useNavigate(); // Hook to navigate between routes

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const response = await ApiService.getLoggedInUserData();
                setUser(response.user); // Setting the user data 
            } catch (error) {
                setError(error.response?.data?.message || error.message);
            }
        };

        fetchUserProfile(); // Triggering the profile data fetch 
    }, []); 

    // Navigating to the Edit Profile page
    const handleEditProfile = () => {
        navigate('/edit-profile'); 
    };

    return (
        <div className="profile-page">
            {/* Displays a welcome message if user data is available */}
            {user && <h2>Welcome, {user.name}</h2>}
            {/* Display an error message if there is one */}
            {error && <p className="error-message">{error}</p>}
            {/* Displays user profile details if available */}
            {user && (
                <div className="profile-details">
                    <h3>My Profile Details</h3>
                    <p><strong>Username:</strong> {user.name}</p>
                    <p><strong>Email:</strong> {user.email}</p>
                    {/* Place the Edit Profile button in the profile details */}
                    <div className="centered-button">
                        <button className="edit-profile-button" onClick={handleEditProfile}>Edit Profile</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserProfilePage;