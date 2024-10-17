import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService';

const ManageQuiz = () => {
    const [quizQuestions, setQuizQuestions] = useState([]);
    const [showAddForm, setShowAddForm] = useState(false);
    const [showUpdateForm, setShowUpdateForm] = useState(false);
    const [newQuestion, setNewQuestion] = useState({
        question_text: '',
        answer_option_1: '',
        answer_option_2: '',
        answer_option_3: '',
        answer_option_4: '',
        correct_answer: '',
    });
    const [isFormModified, setIsFormModified] = useState(false);
    const [loading, setLoading] = useState(true);

    const [showAddQuizBackAlert, setShowAddQuizBackAlert] = useState(false);
    const [addQuizAlertMessage, setAddQuizAlertMessage] = useState('');
    const [addQuizAlertType, setAddQuizAlertType] = useState('success');

    const [updateQuizAlertMessage, setUpdateQuizAlertMessage] = useState('');
    const [updateQuizAlertType, setUpdateQuizAlertType] = useState('success');

    const [deleteConfirmation, setDeleteConfirmation] = useState({ show: false, questionId: null });

    const navigate = useNavigate();

    // Fetching quiz questions from the API 
    const fetchQuizQuestions = async () => {
        try {
            const response = await ApiService.getAllQuizQuestions();
            setQuizQuestions(response.quizQuestionList);
        } catch (error) {
            console.error('Error fetching quiz questions:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchQuizQuestions();
    }, []);

    // Handling input changes for adding a new question
    const handleInputChange = (e, field) => {
        setNewQuestion({ ...newQuestion, [field]: e.target.value });
        setIsFormModified(true);
    };

    // Adding new question 
    const handleAddQuestion = async () => {
        if (
            !newQuestion.question_text ||
            !newQuestion.answer_option_1 ||
            !newQuestion.answer_option_2 ||
            !newQuestion.answer_option_3 ||
            !newQuestion.answer_option_4 ||
            !newQuestion.correct_answer
        ) {
            setAddQuizAlertMessage('Please fill in all fields');
            setAddQuizAlertType('error');
            setTimeout(() => setAddQuizAlertMessage(''), 5000);
            return;
        }

        const correctAnswerNumber = parseInt(newQuestion.correct_answer);
        if (isNaN(correctAnswerNumber) || correctAnswerNumber < 1 || correctAnswerNumber > 4) {
            setAddQuizAlertMessage('Correct answer must be a number between 1 and 4');
            setAddQuizAlertType('error');
            setTimeout(() => setAddQuizAlertMessage(''), 5000);
            return;
        }

        try {
            const questionPayload = {
                questionText: newQuestion.question_text,
                answerOption1: newQuestion.answer_option_1,
                answerOption2: newQuestion.answer_option_2,
                answerOption3: newQuestion.answer_option_3,
                answerOption4: newQuestion.answer_option_4,
                correctAnswer: newQuestion.correct_answer,
            };

            await ApiService.addQuizQuestion(questionPayload);

            setNewQuestion({
                question_text: '',
                answer_option_1: '',
                answer_option_2: '',
                answer_option_3: '',
                answer_option_4: '',
                correct_answer: '',
            });

            setIsFormModified(false);
            setAddQuizAlertMessage('Question added successfully!');
            setAddQuizAlertType('success');

            await fetchQuizQuestions();

            setTimeout(() => setAddQuizAlertMessage(''), 5000);
        } catch (error) {
            console.error('Error adding quiz question:', error);
            setAddQuizAlertMessage('Failed to add the question. Please try again.');
            setAddQuizAlertType('error');
            setTimeout(() => setAddQuizAlertMessage(''), 5000);
        }
    };

    // Handling back button navigation and enabling pop alert message if form is modified
    const handleBackClick = () => {
        if (isFormModified) {
            setShowAddQuizBackAlert(true);
        } else {
            navigate(-1);
        }
    };

    // Confirm back navigation and discard unsaved changes
    const confirmBackNavigation = () => {
        setShowAddForm(false);
        setShowUpdateForm(false);
        setShowAddQuizBackAlert(false);
    };

    // Cancel back navigation to stay on the page
    const cancelBackNavigation = () => {
        setShowAddQuizBackAlert(false);
    };

    // Handling input changes when updating a quiz question
    const handleUpdateInputChange = (e, index, field) => {
        const updatedQuestions = [...quizQuestions];
        updatedQuestions[index][field] = e.target.value;
        setQuizQuestions(updatedQuestions);
    };

    // Updating quiz question logic
    const handleUpdateQuestion = async (index) => {
        const updatedQuestion = quizQuestions[index];

        // Validating fields before sending update
        if (
            !updatedQuestion.questionText ||
            !updatedQuestion.answerOption1 ||
            !updatedQuestion.answerOption2 ||
            !updatedQuestion.answerOption3 ||
            !updatedQuestion.answerOption4 ||
            !updatedQuestion.correctAnswer
        ) {
            setUpdateQuizAlertMessage('Please fill in all fields.');
            setUpdateQuizAlertType('error');
            setTimeout(() => setUpdateQuizAlertMessage(''), 5000);
            return;
        }

        try {
            const updatePayload = {
                id: updatedQuestion.id, 
                questionText: updatedQuestion.questionText,
                answerOption1: updatedQuestion.answerOption1,
                answerOption2: updatedQuestion.answerOption2,
                answerOption3: updatedQuestion.answerOption3,
                answerOption4: updatedQuestion.answerOption4,
                correctAnswer: updatedQuestion.correctAnswer,
            };

            await ApiService.updateQuizQuestion(updatePayload.id, updatePayload);

            // Displaying success message if question is updated successfully
            setUpdateQuizAlertMessage('Question updated successfully!');
            setUpdateQuizAlertType('success');
            setTimeout(() => setUpdateQuizAlertMessage(''), 5000);

        } catch (error) {  // Displaying error message if question isn't updated successfully

            console.error('Error updating question:', error);
            setUpdateQuizAlertMessage('Failed to update the question. Please try again.');
            setUpdateQuizAlertType('error');
            setTimeout(() => setUpdateQuizAlertMessage(''), 5000);
        }
    };

    // Handling confirmation for deleting a question
    const handleDeleteConfirmation = (questionId) => {
        setDeleteConfirmation({ show: true, questionId });
    };

    // Deleting quiz question logic
    const handleDeleteQuestion = async () => {
        const { questionId } = deleteConfirmation;

        try {
            await ApiService.deleteQuizQuestion(questionId); 

            // Removing the question from the quiz after successful deletion
            setQuizQuestions(quizQuestions.filter((question) => question.id !== questionId));
            setDeleteConfirmation({ show: false, questionId: null });

            // Displaying success message after deleting a question
            setUpdateQuizAlertMessage('Question deleted successfully!');
            setUpdateQuizAlertType('success');  
            setTimeout(() => setUpdateQuizAlertMessage(''), 5000);
        } catch (error) {
            console.error('Error deleting question:', error);
            setUpdateQuizAlertMessage('Failed to delete the question. Please try again.');
            setUpdateQuizAlertType('error');
            setTimeout(() => setUpdateQuizAlertMessage(''), 5000);
        }
    };

    if (loading) {
        return <p>Loading quiz questions...</p>;
    }

    return (
        <div className="manage-quiz-page">
            {!showAddForm && !showUpdateForm && (
                <div className="manage-quiz-container">
                    <h1>Manage Quiz</h1>

                    <div className="manage-quiz-buttons">
                        {/* Button to add new quiz */}
                        <button className="add-quiz-button" onClick={() => setShowAddForm(true)}>
                            Add Quiz
                        </button>
                        {/* Back button */}
                        <button className="back-button middle-back-button" onClick={handleBackClick}>
                            Back
                        </button>
                        {/* Button to update or delete quiz */}
                        <button className="update-quiz-button" onClick={() => setShowUpdateForm(true)}>
                            Update/Delete Quiz
                        </button>
                    </div>
                </div>
            )}

            {showAddForm && (
                <div className="add-question-section">
                    <h2>Add New Quiz Question</h2>

                    {showAddQuizBackAlert && (
                        <div className="custom-alert custom-alert-error">
                            <p>You have unsaved changes. Are you sure you want to go back?</p>
                            <div className="delete-confirmation-buttons">
                                <button className="confirm-button" onClick={confirmBackNavigation}>
                                    Confirm
                                </button>
                                <button className="cancel-button" onClick={cancelBackNavigation}>
                                    Cancel
                                </button>
                            </div>
                        </div>
                    )}
                    {addQuizAlertMessage && (
                        <div className={`custom-alert custom-alert-${addQuizAlertType}`}>
                            {addQuizAlertMessage}
                        </div>
                    )}

                    {/* Input fields for adding a new question */}
                    <input
                        type="text"
                        placeholder="Enter question text"
                        value={newQuestion.question_text}
                        onChange={(e) => handleInputChange(e, 'question_text')}
                    />
                    <input
                        type="text"
                        placeholder="Enter answer option 1"
                        value={newQuestion.answer_option_1}
                        onChange={(e) => handleInputChange(e, 'answer_option_1')}
                    />
                    <input
                        type="text"
                        placeholder="Enter answer option 2"
                        value={newQuestion.answer_option_2}
                        onChange={(e) => handleInputChange(e, 'answer_option_2')}
                    />
                    <input
                        type="text"
                        placeholder="Enter answer option 3"
                        value={newQuestion.answer_option_3}
                        onChange={(e) => handleInputChange(e, 'answer_option_3')}
                    />
                    <input
                        type="text"
                        placeholder="Enter answer option 4"
                        value={newQuestion.answer_option_4}
                        onChange={(e) => handleInputChange(e, 'answer_option_4')}
                    />
                    <input
                        type="number"
                        placeholder="Correct answer (1-4)"
                        value={newQuestion.correct_answer}
                        onChange={(e) => handleInputChange(e, 'correct_answer')}
                    />

                    <div className="centered-button">
                        <button onClick={handleAddQuestion}>Add</button>
                    </div>
                    <div className="centered-button">
                        <button className="back-button" onClick={handleBackClick}>Back</button>
                    </div>
                </div>
            )}

            {showUpdateForm && (
                <div className="update-question-section">
                    <h2>Update/Delete Quiz Questions</h2>

                    {updateQuizAlertMessage && (
                        <div className={`custom-alert custom-alert-${updateQuizAlertType}`}>
                            {updateQuizAlertMessage}
                        </div>
                    )}

                    {/* Table to display quiz questions for update/delete */}
                    <div className="table-container">
                        <div className="scrollable-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Question Text</th>
                                        <th>Answer Option 1</th>
                                        <th>Answer Option 2</th>
                                        <th>Answer Option 3</th>
                                        <th>Answer Option 4</th>
                                        <th>Correct Answer</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {quizQuestions.length > 0 ? (
                                        quizQuestions.map((question, index) => (
                                            <tr key={question.id}>
                                                <td>
                                                    <input
                                                        type="text"
                                                        value={question.questionText}
                                                        onChange={(e) => handleUpdateInputChange(e, index, 'questionText')}
                                                    />
                                                </td>
                                                <td>
                                                    <input
                                                        type="text"
                                                        value={question.answerOption1}
                                                        onChange={(e) => handleUpdateInputChange(e, index, 'answerOption1')}
                                                    />
                                                </td>
                                                <td>
                                                    <input
                                                        type="text"
                                                        value={question.answerOption2}
                                                        onChange={(e) => handleUpdateInputChange(e, index, 'answerOption2')}
                                                    />
                                                </td>
                                                <td>
                                                    <input
                                                        type="text"
                                                        value={question.answerOption3}
                                                        onChange={(e) => handleUpdateInputChange(e, index, 'answerOption3')}
                                                    />
                                                </td>
                                                <td>
                                                    <input
                                                        type="text"
                                                        value={question.answerOption4}
                                                        onChange={(e) => handleUpdateInputChange(e, index, 'answerOption4')}
                                                    />
                                                </td>
                                                <td>
                                                    <input
                                                        type="number"
                                                        value={question.correctAnswer}
                                                        onChange={(e) => handleUpdateInputChange(e, index, 'correctAnswer')}
                                                    />
                                                </td>
                                                <td>
                                                    <button className="update-button" onClick={() => handleUpdateQuestion(index)}>
                                                        Update
                                                    </button>
                                                    <button className="delete-button" onClick={() => handleDeleteConfirmation(question.id)}>
                                                        Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        ))
                                    ) : (
                                        <tr>
                                            <td colSpan="7">No quiz questions found</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    {/* Delete confirmation modal */}
                    {deleteConfirmation.show && (
                        <div className="delete-confirmation-modal">
                            <div className="delete-confirmation-content">
                                <p>Are you sure you want to delete this question?</p>
                                <div className="delete-confirmation-actions">
                                    <button className="confirm-delete-button" onClick={handleDeleteQuestion}>Yes, Delete</button>
                                    <button className="cancel-delete-button" onClick={() => setDeleteConfirmation({ show: false, questionId: null })}>Cancel</button>
                                </div>
                            </div>
                        </div>
                    )}

                    <div className="centered-button">
                        <button className="back-button" onClick={handleBackClick}>Back</button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ManageQuiz;