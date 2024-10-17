import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from '../../service/ApiService'; // ApiService to handle API calls

const AdminPage = () => {
    const [adminName, setAdminName] = useState('');
    const navigate = useNavigate();

    // Fetching logged-in admin's data 
    useEffect(() => {
        const fetchAdminName = async () => {
            try {
                const response = await ApiService.getLoggedInUserData();
                setAdminName(response.user.name); // Setting the admin's name from the API response
            } catch (error) {
                console.error('Error fetching admin details:', error.message);
            }
        };
        fetchAdminName();
    }, []);

    return (
        <div className="admin-page">
            <h1 className="welcome-message">Welcome, {adminName}</h1>
            <div className="admin-actions">
                {/* Button to navigate to manage users page */}
                <button className="admin-button" onClick={() => navigate('/manage-users')}>
                    Manage Users
                </button>

                {/* Button to navigate to manage quiz page */}
                <button className="admin-button" onClick={() => navigate('/manage-quiz')}>
                    Manage Quiz
                </button>
            </div>
            <br />
            {/* Back button to return to the home page */}
            <div className="back-button-container">
                <button className="back-button" onClick={() => navigate("/home")}>
                    Back
                </button>
            </div>
        </div>
    );
}

export default AdminPage;