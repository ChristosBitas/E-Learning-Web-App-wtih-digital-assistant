import React, { useEffect, useState } from 'react';
import ApiService from '../../service/ApiService'; 

const Scoreboard = () => {
    const [scoreboard, setScoreboard] = useState([]); // State to store the list of users and their scores
    const [loading, setLoading] = useState(true); // State to track loading status
    const [error, setError] = useState(false); // State to track if there was an error fetching data

    // Fetching users and their quiz scores 
    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await ApiService.getAllUsers(); // Fetching all users from the API
                if (response && Array.isArray(response.userList) && response.userList.length > 0) {
                    const filteredUsers = response.userList
                        .filter(user => user.role !== 'ADMIN' && user.userScore > 0) // Filter out admins and users with 0 score in order not to display 
                        .sort((a, b) => b.userScore - a.userScore); // Sort users by score in descending order

                    setScoreboard(filteredUsers); // Setting the filtered and sorted users in the scoreboard state
                } else {
                    setScoreboard([]); // In case there is no user, scoreboard will be an empty array
                }
                setLoading(false); // Stop loading once data is fetched
            } catch (error) {
                console.error('Error fetching users:', error); // Handling error if fetching fails
                setError(true); // Set error state to true
                setLoading(false); // Stop loading even if there is an error
            }
        };

        fetchUsers(); // Call the fetch function
    }, []);

    //  Loading label while data is being fetched
    if (loading) {
        return <div className="scoreboard-container">Loading scoreboard...</div>;
    }

    return (
        <div className="scoreboard-container">
            <h1 className="scoreboard-title">Scoreboard</h1>
            <div className="scoreboard-table-container"> {/* Scrollable container for the scoreboard table */}
                <table className="scoreboard-table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Score</th>
                        </tr>
                    </thead>
                    <tbody>
                        {/* Displaying message if there is an error or no data available */}
                        {error || scoreboard.length === 0 ? (
                            <tr>
                                <td colSpan="2">No data available</td>
                            </tr>
                        ) : (
                            // Mapping over the scoreboard and display each user's name and score
                            scoreboard.map((user, index) => (
                                <tr key={index}>
                                    <td>{user.name}</td>
                                    <td>{user.userScore}</td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Scoreboard;