import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from '../src/component/common/NavBar';
import FooterComponent from '../src/component/common/Footer';
import QuizHomePage from '../src/component/home/QuizHomePage';
import LoginPage from '../src/component/auth/LoginPage';
import RegisterPage from '../src/component/auth/RegisterPage';
import UserProfilePage from '../src/component/profile/UserProfilePage';
import EditUserProfilePage from '../src/component/profile/EditUserProfilePage';
import AdminPage from './component/admin/AdminPage';
import { ProtectedRoute, AdminRoute } from './service/AuthRoute';
import Questions from '../src/component/quiz/Questions'; 
import ManageUsersPage from '../src/component/admin/ManageUsersPage';
import ManageQuiz from '../src/component/admin/ManageQuiz';
import Scoreboard from '../src/component/quiz/Scoreboard';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            <Route exact path="/home" element={<QuizHomePage />} />
            <Route exact path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Questions page is protected and has its own layout */}
            <Route path="/questions" element={<ProtectedRoute element={<Questions />} />} />
            <Route path="/scoreboard" element={<ProtectedRoute element={<Scoreboard />} />} />

            <Route path="/profile" element={<ProtectedRoute element={<UserProfilePage />} />} />
            <Route path="/edit-profile" element={<ProtectedRoute element={<EditUserProfilePage />} />} />
            <Route path="/admin" element={<AdminRoute element={<AdminPage />} />} />
            <Route path="/manage-users" element={<AdminRoute element={<ManageUsersPage />} />} />
            <Route path="/manage-quiz" element={<AdminRoute element={<ManageQuiz />} />} />


            <Route path="*" element={<Navigate to="/home" />} />
          </Routes>
        </div>
        <FooterComponent />
      </div>
    </BrowserRouter>
  );
}

export default App;