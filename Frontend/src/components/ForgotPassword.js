import React, { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom'; // Updated to useNavigate for programmatic navigation
import '../css/ForgotPassword.css';

const ForgotPassword = () => {
  const navigate = useNavigate(); // Hook for navigation

  const [formData, setFormData] = useState({
    email: '',
    securityAnswer: '',
    newPassword: '',
    confirmNewPassword: ''
  });

  const [errors, setErrors] = useState({});
  const [resetSuccess, setResetSuccess] = useState(false);
  const [serverError, setServerError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const validateForm = () => {
    let formErrors = {};

    // Check if all fields are filled
    for (let key in formData) {
      if (!formData[key]) {
        formErrors[key] = 'This field is required';
      }
    }

    // Email validation
    if (formData.email && !isValidEmail(formData.email)) {
      formErrors.email = 'Invalid email format';
    }

    // Password validation (example: at least 8 characters, one uppercase, one lowercase, one number)
    if (formData.newPassword) {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
      if (!passwordRegex.test(formData.newPassword)) {
        formErrors.newPassword = 'Password must be at least 8 characters, include an uppercase letter, a lowercase letter, and a number';
      }
    }

    // Confirm password validation
    if (formData.newPassword !== formData.confirmNewPassword) {
      formErrors.confirmNewPassword = 'Passwords do not match';
    }

    setErrors(formErrors);
    return Object.keys(formErrors).length === 0;
  };

  const isValidEmail = (email) => {
    // Basic email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      try {
        const response = await axios.post('https://celebrated-harmony-production.up.railway.app/api/users/reset-password', {
          email: formData.email,
          securityAnswer: formData.securityAnswer,
          password: formData.newPassword
        });
        console.log(response.data);
        setResetSuccess(true); // Assuming backend response indicates success
        navigate('/login'); // Redirect to login page on successful password reset
      } catch (error) {
        if (error.response && error.response.data && error.response.data.message) {
          setServerError(error.response.data.message);
        } else {
          setServerError('An error occurred. Please try again later.');
        }
      }
    }
  };

  return (
    <div className="ForgotPassword">
      <h2>Forgot Password</h2>
      {!resetSuccess ? (
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="email">Email:</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
            {errors.email && <span className="error">{errors.email}</span>}
          </div>
          <div className="form-group">
            <label htmlFor="securityAnswer">Security Answer:</label>
            <input
              type="text"
              id="securityAnswer"
              name="securityAnswer"
              value={formData.securityAnswer}
              onChange={handleChange}
              required
            />
            {errors.securityAnswer && <span className="error">{errors.securityAnswer}</span>}
          </div>
          <div className="form-group">
            <label htmlFor="newPassword">New Password:</label>
            <input
              type="password"
              id="newPassword"
              name="newPassword"
              value={formData.newPassword}
              onChange={handleChange}
              required
            />
            {errors.newPassword && <span className="error">{errors.newPassword}</span>}
          </div>
          <div className="form-group">
            <label htmlFor="confirmNewPassword">Confirm New Password:</label>
            <input
              type="password"
              id="confirmNewPassword"
              name="confirmNewPassword"
              value={formData.confirmNewPassword}
              onChange={handleChange}
              required
            />
            {errors.confirmNewPassword && <span className="error">{errors.confirmNewPassword}</span>}
          </div>
          {serverError && <p className="error">{serverError}</p>}
          <button type="submit">Reset Password</button>
        </form>
      ) : (
        <p>Password reset successful. You can now <Link to="/login">login</Link> with your new password.</p>
      )}
    </div>
  );
};

export default ForgotPassword;
