// Footer component that displays the current year dynamically

const FooterComponent = () => {
    return (
        <footer>
            <span className="footer">
                E-Learning Web App | All Rights Reserved &copy; {new Date().getFullYear()}
            </span>
        </footer>
    );
};

export default FooterComponent;