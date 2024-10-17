import React, { useState } from 'react';
import { NavLink, useNavigate, useLocation } from 'react-router-dom';
import ApiService from '../../service/ApiService';

function Navbar() {
    const isAuthenticated = ApiService.isAuthenticated(); // Checking if the user is authenticated
    const isAdmin = ApiService.isAdmin(); // Checking if the user is an admin
    const isUser = ApiService.isUser(); // Checking if the user is a regular user
    const navigate = useNavigate(); // Hook for navigation
    const location = useLocation(); // Hook to get current route

    const [showAlert, setShowAlert] = useState(false); // State for showing quiz in progress alert
    const [showLogoutModal, setShowLogoutModal] = useState(false); // State for showing logout confirmation modal
    const [pendingNavigation, setPendingNavigation] = useState(null); // Tracking the destination if navigation is pending
    const [isLoggingOut, setIsLoggingOut] = useState(false); // State to track logout progress

    // Handling navigation when user attempts to leave during a quiz
    const handleNavClick = (e, path) => {
        const quizInProgress = localStorage.getItem('quizInProgress') === 'true';
        if (quizInProgress) {
            e.preventDefault(); // Prevent navigation if a quiz is in progress
            setPendingNavigation(path); // Storing the  navigation path
            setShowAlert(true); // Showing alert message asking for confirmation to leave
        } else {
            navigate(path); // Navigating immediately if no quiz is in progress
        }
    };

    // Confirming navigation and clearing the quiz state
    const confirmNavigation = () => {
        localStorage.removeItem('quizInProgress'); // Clear quiz progress
        setShowAlert(false); // Close the alert
        navigate(pendingNavigation); // Proceed with navigation
    };

    // Cancel navigation and stay on the current page
    const cancelNavigation = () => {
        setShowAlert(false); // Close the alert
    };

    // Handling logout process
    const handleLogout = () => {
        const quizInProgress = localStorage.getItem('quizInProgress') === 'true';
        if (quizInProgress) {
            setPendingNavigation('/logout'); // Store logout path if quiz is in progress
            setShowAlert(true); // Show alert asking for confirmation
        } else {
            setShowLogoutModal(true); // Show logout modal if no quiz is in progress
        }
    };

    // Confirming logout and performing the actual logout process
    const confirmLogout = () => {
        setIsLoggingOut(true); // Start the logout process
        setTimeout(() => {
            ApiService.logout(); // Perform logout via API service
            localStorage.removeItem('quizInProgress'); // Clear quiz state if necessary
            navigate('/home'); // Navigate to the home page after logout
            setIsLoggingOut(false); // Reset the logout state
            setShowLogoutModal(false); // Hide the modal
        }, 2000); // Simulate a 2-second delay for user feedback
    };

    // Cancel the logout process
    const cancelLogout = () => {
        setShowLogoutModal(false); // Close the logout modal
    };

    // Checking if the current location is '/profile' or '/edit-profile'
    const isProfilePage = location.pathname === '/profile' || location.pathname === '/edit-profile';

    // Check if the current route is '/admin', '/manage-quiz', or '/manage-users'
    const isAdminPage = location.pathname.includes('/admin') || location.pathname.includes('/manage-users') || location.pathname.includes('/manage-quiz');

    return (
        <div>
            <nav className="navbar">
                <div className="navbar-brand">
                    <NavLink to="/home" onClick={(e) => handleNavClick(e, '/home')}>Quiz Game</NavLink>
                </div>
                <ul className="navbar-ul">
                    <li><NavLink to="/home" onClick={(e) => handleNavClick(e, '/home')}>Home</NavLink></li>
                    {isUser && (
                        <li>
                            <NavLink
                                to="/profile"
                                onClick={(e) => handleNavClick(e, '/profile')}
                                className={isProfilePage ? 'active-link' : ''}
                                style={isProfilePage ? { color: 'white' } : {}}
                            >
                                Profile
                            </NavLink>
                        </li>
                    )}
                    {isAdmin && (
                        <li>
                            <NavLink
                                to="/admin"
                                onClick={(e) => handleNavClick(e, '/admin')}
                                className={isAdminPage ? 'active-link' : ''}
                                style={isAdminPage ? { color: 'white' } : {}}
                            >
                                Admin
                            </NavLink>
                        </li>
                    )}
                    {isAuthenticated && !isAdmin && (
                        <li>
                            <NavLink to="/questions" onClick={(e) => handleNavClick(e, '/questions')}>Game</NavLink>
                        </li>
                    )}
                    {isAuthenticated && isUser && (
                        <li>
                            <NavLink to="/scoreboard" onClick={(e) => handleNavClick(e, '/scoreboard')}>Scoreboard</NavLink>
                        </li>
                    )}
                    {!isAuthenticated && (
                        <li>
                            <NavLink to="/login" onClick={(e) => handleNavClick(e, '/login')}>Login</NavLink>
                        </li>
                    )}
                    {!isAuthenticated && (
                        <li>
                            <NavLink to="/register" onClick={(e) => handleNavClick(e, '/register')}>Register</NavLink>
                        </li>
                    )}
                    {isAuthenticated && (
                        <li className="logout-link" onClick={handleLogout}>
                            Logout
                        </li>
                    )}
                </ul>
            </nav>

            {/* Quiz In Progress Alert */}
            {showAlert && (
                <div className="custom-alert custom-alert-error">
                    <h3>Quiz In Progress</h3>
                    <p>Your quiz is in progress. If you leave now, your score won't be saved. Are you sure you want to leave?</p>
                    <div className="delete-confirmation-buttons">
                        <button className="confirm-button" onClick={confirmNavigation}>Yes, Leave</button>
                        <button className="cancel-button" onClick={cancelNavigation}>No, Stay</button>
                    </div>
                </div>
            )}

            {/* Logout Confirmation Modal */}
            {showLogoutModal && (
                <div className="logout-modal">
                    <div className="logout-modal-content">
                        <h3>{isLoggingOut ? "Logging out..." : "Confirm Logout"}</h3>
                        <p>{isLoggingOut ? "You will be logged out shortly." : "Are you sure you want to log out?"}</p>
                        {!isLoggingOut && (
                            <div className="modal-actions">
                                <button className="confirm-logout-button" onClick={confirmLogout}>Yes, Log Out</button>
                                <button className="cancel-logout-button" onClick={cancelLogout}>Cancel</button>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default Navbar;
