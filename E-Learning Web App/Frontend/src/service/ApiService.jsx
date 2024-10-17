import axios from "axios";

export default class ApiService {

    // Base URL for the API endpoints
    static BASE_URL = "http://localhost:8080";

    // Method to generate headers for authorized API requests
    static getHeader() {
        const token = localStorage.getItem("token"); // Retrieve JWT token from local storage
        return {
            Authorization: `Bearer ${token}`, // Including the token in the Authorization header
            "Content-Type": "application/json" // Indicating that the request body will be JSON
        };
    }

    /** AUTHENTICATION METHODS **/

    // Registers a new user by sending the registration data via POST request
    static async registerUser(registration) {
        const response = await axios.post(`${this.BASE_URL}/auth/register`, registration);
        return response.data; // Return the API response data
    }

    // Logging a user by sending login credentials (email, password) via POST request
    static async loginUser(loginDetails) {
        const response = await axios.post(`${this.BASE_URL}/auth/login`, loginDetails);
        return response.data; // Return the API response data
    }

    /** USER MANAGEMENT METHODS **/

    // Retrieves a list of all users, requires an authenticated request
    static async getAllUsers() {
        const response = await axios.get(`${this.BASE_URL}/users/all`, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the API response data
    }

    // Retrieving a specific user's details by their user ID
    static async getUser(userId) {
        const response = await axios.get(`${this.BASE_URL}/users/get-user-by-id/${userId}`, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the API response data
    }

    // Retrieving the logged-in user's details using the JWT token
    static async getLoggedInUserData() {
        const response = await axios.get(`${this.BASE_URL}/users/get-logged-in-user-data`, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the API response data
    }

    // Deleting a user by user ID, requires authorization
    static async deleteUser(userId) {
        const response = await axios.delete(`${this.BASE_URL}/users/delete-user-by-id/${userId}`, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the API response data
    }
    
    // Updating User profile
    static async updateUserProfile(updatedUser) {
        const response = await axios.put(`${this.BASE_URL}/users/update-profile`, updatedUser, {
            headers: this.getHeader(), // Pass authorization headers
        });
        return response.data;
    }

    /** AUTHENTICATION STATUS METHODS **/

    // Logs out the user by clearing the token and role from local storage
    static logout() {
        localStorage.removeItem('token'); // Remove the JWT token
        localStorage.removeItem('role'); // Remove the user's role
    }

    // Checking if the user is authenticated by verifying the existence of a JWT token
    static isAuthenticated() {
        const token = localStorage.getItem('token'); // Check for a stored token
        return !!token; // Return true if token exists, false otherwise
    }

    // Checking if the logged-in user is an admin
    static isAdmin() {
        const role = localStorage.getItem('role'); // Retrieve the user's role from local storage
        return role === 'ADMIN'; // Return true if the role is 'ADMIN'
    }

    // Checking if the logged-in user is a regular user
    static isUser() {
        const role = localStorage.getItem('role'); // Retrieve the user's role from local storage
        return role === 'USER'; // Return true if the role is 'USER'
    }
   
    /** QUIZ QUESTION METHODS **/

    // Fetching all quiz questions
    static async getAllQuizQuestions() {
        const response = await axios.get(`${this.BASE_URL}/api/quiz-questions/getAllQuizzes`, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the list of quiz questions
    }

    // Adding a new quiz question
    static async addQuizQuestion(newQuestion) {
        const response = await axios.post(`${this.BASE_URL}/api/quiz-questions/addQuiz`, newQuestion, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the added quiz question
    }

    // Updating an existing quiz question by its ID
    static async updateQuizQuestion(questionId, updatedQuestion) {
        const response = await axios.put(`${this.BASE_URL}/api/quiz-questions/updateQuiz/${questionId}`, updatedQuestion, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the updated quiz question
    }

    // Delete a quiz question by its ID
    static async deleteQuizQuestion(questionId) {
        const response = await axios.delete(`${this.BASE_URL}/api/quiz-questions/deleteQuiz/${questionId}`, {
            headers: this.getHeader() // Pass authorization headers
        });
        return response.data; // Return the response after deleting the question
    }

    // Saving users quiz score
    static async saveUserScore(score) {
        const response = await axios.put(`${this.BASE_URL}/users/save-score`, { user_score: score }, {
            headers: this.getHeader(), // Pass authorization headers
        });
        return response.data;
    }
}