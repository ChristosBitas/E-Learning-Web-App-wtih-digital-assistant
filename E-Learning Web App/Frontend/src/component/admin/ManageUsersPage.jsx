import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService'; 

const ManageUsersPage = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [deleteMessage, setDeleteMessage] = useState('');
    const [showMessage, setShowMessage] = useState(false);
    const [confirmDelete, setConfirmDelete] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);
    const [alertClass, setAlertClass] = useState(''); 

    const navigate = useNavigate(); // Hook for navigation

    // Fetching the list of users with the role != admin role 
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await ApiService.getAllUsers();
                const filteredUsers = response.userList.filter(user => user.role !== 'ADMIN');
                setUsers(filteredUsers); // Setting users without admins
                setLoading(false);
            } catch (error) {
                console.error('Error fetching users:', error);
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    // Deleting user 
    const handleDeleteUser = async () => {
        if (userToDelete) {
            try {
                // API call to delete the user from DB
                await ApiService.deleteUser(userToDelete.id);
                // Updating the user list 
                setUsers(users.filter(user => user.id !== userToDelete.id));
                setDeleteMessage(`User "${userToDelete.name}" has been deleted successfully.`);
                setAlertClass('custom-alert-success'); // Success alert class
            } catch (error) {
                // Display message if it an error
                setDeleteMessage('Error deleting user. Please try again.');
                setAlertClass('custom-alert-error'); // Error alert class
                console.error('Error deleting user:', error);
            }
            // Displaying the alert message and reset confirmation state
            setShowMessage(true);
            setTimeout(() => setShowMessage(false), 5000);
            setConfirmDelete(false);
        }
    };

    // Displaying delete confirmation message 
    const openDeleteConfirmation = (user) => {
        setUserToDelete(user); // Set the user to delete
        setConfirmDelete(true); // Show delete confirmation modal
    };

    // Cancel the delete action
    const cancelDelete = () => {
        setConfirmDelete(false); // Hide delete confirmation modal
        setUserToDelete(null); // Clear userToDelete state
    };

    // Navigating back to the previous page for the back button
    const handleBackClick = () => {
        navigate(-1); 
    };

    if (loading) {
        return <p>Loading users...</p>; // Displaying loading label while users are being fetched
    }

    return (
        <div className="manage-users-page">
            <h1>Manage Users</h1>
            <br />

            {/* Displaying success or error messages after deletion */}
            {showMessage && (
                <div className={`custom-alert ${alertClass}`}>
                    {deleteMessage}
                </div>
            )}

            {/* Users table */}
            <div className="table-container2">
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.length > 0 ? (
                            users.map((user) => (
                                <tr key={user.id}>
                                    <td>{user.name}</td>
                                    <td>{user.email}</td>
                                    <td>
                                        <button
                                            className="delete-button"
                                            onClick={() => openDeleteConfirmation(user)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="3">No users found</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>

            {/* Delete confirmation modal */}
            {confirmDelete && (
                <div className="delete-confirmation-modal">
                    <div className="delete-confirmation-content">
                        <p>Are you sure you want to delete the user "{userToDelete?.name}"?</p>
                        <div className="delete-confirmation-actions">
                            <button onClick={handleDeleteUser} className="confirm-delete-button">Yes, Delete</button>
                            <button onClick={cancelDelete} className="cancel-delete-button">Cancel</button>
                        </div>
                    </div>
                </div>
            )}

            {/* Back button to navigate back to the previous page */}
            <div className="centered-button">
                <button className="back-button" onClick={handleBackClick}>
                    Back
                </button>
            </div>
        </div>
    );
};

export default ManageUsersPage;
