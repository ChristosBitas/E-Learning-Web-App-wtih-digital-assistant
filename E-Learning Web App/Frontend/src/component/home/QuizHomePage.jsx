import React from "react";

// Home page component 

const QuizHomePage = () => {

    return (
        <div className="home">
            {/* Header section with banner and welcome message */}
            <section>
                <header className="header-banner">
                    <h1>Welcome to E-Learning Quiz Game</h1>
                    <br />
                    <h3>Test your knowledge!</h3>
                    <br />
                </header>
            </section>
        </div>
    );
}

export default QuizHomePage;