import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../../service/ApiService'; 

const Questions = () => {
    const [currentQuestion, setCurrentQuestion] = useState(0); // Tracking the current question index
    const [showScore, setShowScore] = useState(false); // Tracking whether the score should be displayed
    const [score, setScore] = useState(0); // Tracking the user's score
    const [timeLeft, setTimeLeft] = useState(300); // Setting timer for each question (5 minutes)
    const [questions, setQuestions] = useState([]); // Storing the list of quiz questions
    const [loading, setLoading] = useState(true); // Tracking loading state while fetching questions
    const navigate = useNavigate(); // Hook for navigation

    // Setting quiz progress flag 
    useEffect(() => {
        localStorage.setItem('quizInProgress', 'true');
        return () => localStorage.removeItem('quizInProgress'); // Clear the flag when component unmounts
    }, []);

    // Saving the user's score after the quiz ends
    const saveUserScore = async () => {
        try {
            const response = await ApiService.saveUserScore(score); // Saving the total score to the database
            console.log('Score saved:', response);
            localStorage.removeItem('quizInProgress'); // Clearing quiz progress flag after saving score
        } catch (error) {
            console.error('Error saving the score:', error);
        }
    };

    // Triggering saving user score when the quiz ends
    useEffect(() => {
        if (showScore) {
            saveUserScore(); // Saving user score if quiz is complete
        }
    }, [showScore]);

    // Fetching quiz questions from the API 
    useEffect(() => {
        const fetchQuestions = async () => {
            try {
                const response = await ApiService.getAllQuizQuestions(); 
                const formattedQuestions = response.quizQuestionList.map(q => ({
                    questionText: q.questionText,
                    answerOptions: [
                        { answerText: q.answerOption1, isCorrect: q.correctAnswer === 1 },
                        { answerText: q.answerOption2, isCorrect: q.correctAnswer === 2 },
                        { answerText: q.answerOption3, isCorrect: q.correctAnswer === 3 },
                        { answerText: q.answerOption4, isCorrect: q.correctAnswer === 4 },
                    ]
                }));
                setQuestions(formattedQuestions); // Setting the quiz questions
                setLoading(false); // Turning off the loading state
            } catch (error) {
                console.error('Error fetching questions:', error);
            }
        };
        fetchQuestions();
    }, []);

    // Handling the quiz timer
    useEffect(() => {
        if (showScore || timeLeft <= 0) return;

        const timerId = setInterval(() => {
            setTimeLeft(prevTime => {
                if (prevTime <= 1) {
                    clearInterval(timerId); // Clearing quiz timer when time runs out
                    setShowScore(true); // Showing the user score when time runs out
                    return 0;
                }
                return prevTime - 1; // Decreasing quiz time by 1 second
            });
        }, 1000);

        return () => clearInterval(timerId); // Cleaning up the quiz timer 
    }, [timeLeft, showScore]);

    // Handling answer selection and move to the next question
    const handleAnswerOptionClick = (isCorrect) => {
        if (isCorrect) {
            setScore(prevScore => prevScore + 50); // Add 50 points for each correct user answer
        }
        const nextQuestion = currentQuestion + 1;
        if (nextQuestion < questions.length) {
            setCurrentQuestion(nextQuestion); // Moving to the next question
            setTimeLeft(300); // Reseting quiz time for the next question
        } else {
            setShowScore(true); // End quiz and showing quiz score
        }
    };

    // Restarting the quiz and reset all states
    const restartQuiz = () => {
        setCurrentQuestion(0); // Reseting quiz question index
        setScore(0); // Reseting quiz score
        setShowScore(false); // Hiding quiz score
        setTimeLeft(300); // Reseting quiz timer
    };

    // Formating the quiz time left in minutes and seconds
    const formatTime = () => {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        return `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    };

    // Setting Botpress AI Assistant (I have deleted my AI Assistant because i didn't use anymore the Botpress platform)
    useEffect(() => {
        const script1 = document.createElement('script');
        script1.src = ""; // link for AI Assistant 
        script1.async = true;
        document.body.appendChild(script1);

        const script2 = document.createElement('script');
        script2.src = ""; // link for AI Assistant 
        script2.async = true;
        document.body.appendChild(script2);

        return () => {
            document.body.removeChild(script1);
            document.body.removeChild(script2);
        };
    }, []);

    if (loading) {
        return <div className="questions-page-container">Loading questions...</div>; // Displaying loading while fetching quiz questions
    }

    return (
        <div className="questions-page-container">
            <div className="app">
                {showScore ? (
                    <div className="score-section">
                        <p className="score-text">Score: {score} points</p>
                        <div className="end-buttons">
                            <button className="home-button" onClick={() => navigate('/home')}>Home</button>
                            <button className="restart-button" onClick={restartQuiz}>Restart quiz</button>
                            <button className="scoreboard-button" onClick={() => navigate('/scoreboard')}>Scoreboard</button>
                        </div>
                    </div>
                ) : (
                    <>
                        <div className="question-count-timer">
                            <div className="question-count">
                                <span>Question {currentQuestion + 1}</span>/{questions.length}
                            </div>
                            <div className="timer">
                                <span className="time_left_txt">Time Left:</span>
                                <span className="timer_sec">{formatTime()}</span>
                            </div>
                        </div>
                        <div className="question-section">
                            <div className="question-text">{questions[currentQuestion].questionText}</div>
                        </div>
                        <div className="answer-section">
                            {questions[currentQuestion].answerOptions.map((answerOption, index) => (
                                <button key={index} onClick={() => handleAnswerOptionClick(answerOption.isCorrect)}>
                                    {answerOption.answerText}
                                </button>
                            ))}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default Questions;