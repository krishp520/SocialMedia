import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../css/Signup.css';

const SignUp = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    securityQuestion: '',
    securityAnswer: '',
    role: 'USER'
  });

  const [errors, setErrors] = useState({});
  const [serverError, setServerError] = useState('');
  const navigate = useNavigate();

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
      formErrors.email = 'Email must end with @dal.ca';
    }

    // Password validation (example: at least 8 characters, one uppercase, one lowercase, one number)
    if (formData.password) {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
      if (!passwordRegex.test(formData.password)) {
        formErrors.password = 'Password must be at least 8 characters, include an uppercase letter, a lowercase letter, and a number';
      }
    }

    // Confirm password validation
    if (formData.password !== formData.confirmPassword) {
      formErrors.confirmPassword = 'Passwords do not match';
    }

    setErrors(formErrors);
    return Object.keys(formErrors).length === 0;
  };

  const isValidEmail = (email) => {
    // Email validation to ensure it ends with @dal.ca
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email) && email.endsWith('@dal.ca');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      try {
        const response = await axios.post('http://localhost:8080/api/users/signup', formData);

        if (response.status === 200) {
          console.log(response.data);
          // Redirect to login page
          navigate('/login');
        } else {
          setServerError('Account creation failed. Please try again.');
        }
      } catch (error) {
        console.error(error);
        if (error.response && error.response.data) {
          setServerError(error.response.data.message || 'Account creation failed. Please try again.');
        } else {
          setServerError('Account creation failed.');
        }
      }
    }
  };

  return (
    <div className="SignUp">
      <h2>Sign Up</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            required
          />
          {errors.firstName && <span className="error">{errors.firstName}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="lastName">Last Name:</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            required
          />
          {errors.lastName && <span className="error">{errors.lastName}</span>}
        </div>
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
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          {errors.password && <span className="error">{errors.password}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="confirmPassword">Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />
          {errors.confirmPassword && <span className="error">{errors.confirmPassword}</span>}
        </div>
        <div className="form-group">
          <label htmlFor="securityQuestion">Security Question:</label>
          <input
            type="text"
            id="securityQuestion"
            name="securityQuestion"
            value={formData.securityQuestion}
            onChange={handleChange}
            required
          />
          {errors.securityQuestion && <span className="error">{errors.securityQuestion}</span>}
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
        {serverError && <p className="error">{serverError}</p>}
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
};

export default SignUp;
