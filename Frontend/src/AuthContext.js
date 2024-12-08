import React, { createContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(sessionStorage.getItem('userId'));
    const navigate = useNavigate();

    const login = (data) => {
        setIsAuthenticated(true);
        console.log(data);
        sessionStorage.setItem("userId", data.userId);
        sessionStorage.setItem("firstName", data.firstName)
        sessionStorage.setItem("lastName", data.lastName)

        navigate('/');
    };

    const logout = () => {
        setIsAuthenticated(false);
        navigate('/login');
        sessionStorage.removeItem("userId");
        sessionStorage.removeItem("firstName")
        sessionStorage.removeItem("lastName")
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
