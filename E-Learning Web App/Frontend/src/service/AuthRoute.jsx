import React from 'react';
import { Navigate, useLocation } from 'react-router-dom'; // Import necessary functions from react-router-dom
import ApiService from './ApiService'; // Import the authentication service for checking user roles

// ProtectedRoute component: Used to protect routes that require the user to be authenticated
export const ProtectedRoute = ({ element: Component }) => {
  const location = useLocation(); // Get the current location using the useLocation hook from react-router-dom

  // If the user is authenticated, render the component, otherwise redirect to the login page
  return ApiService.isAuthenticated() ? (
    Component // Render the component if authenticated
  ) : (
    // Redirect to the login page, passing the current location in state so the user can be redirected back after logging in
    <Navigate to="/login" replace state={{ from: location }} />
  );
};

// AdminRoute component: Used to protect routes that require the user to have admin privileges
export const AdminRoute = ({ element: Component }) => {
  const location = useLocation(); // Get the current location using the useLocation hook from react-router-dom

  // If the user is an admin, render the component, otherwise redirect to the login page
  return ApiService.isAdmin() ? (
    Component // Render the component if the user has an admin role
  ) : (
    // Redirect to the login page, passing the current location in state for possible redirection after login
    <Navigate to="/login" replace state={{ from: location }} />
  );
};