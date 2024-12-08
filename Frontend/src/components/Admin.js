import React, { useEffect, useState } from 'react';
import axios from 'axios';
import "../css/Admin.css";
import { useNavigate } from 'react-router-dom';

const Admin = () => {
    const [pendingUsers, setPendingUsers] = useState([]);
    const [approvedUsers, setApprovedUsers] = useState([]);

    const [editRoleUserId, setEditRoleUserId] = useState(null);
    const [newRole, setNewRole] = useState('USER');
    const [isAdmin, setIsAdmin] = useState(false);
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        role: 'USER',
        securityQuestion: '',
        securityAnswer: ''
    });

    const navigate = useNavigate();

    useEffect(() => {
        const role = sessionStorage.getItem("role");

        if (role === 'ADMIN') {
            setIsAdmin(true);
            const fetchUsers = async () => {
                try {
                    const pendingResponse = await axios.get('https://celebrated-harmony-production.up.railway.app/api/admin/pending-approvals');
                    setPendingUsers(pendingResponse.data);
                    const approvedResponse = await axios.get('https://celebrated-harmony-production.up.railway.app/api/admin/approved-users');
                    setApprovedUsers(approvedResponse.data);
                } catch (error) {
                    console.error(error);
                }
            };

            fetchUsers();
        } else {
            alert("You are not an admin.");
            navigate('/');
        }
    }, [navigate]);

    const handleApprove = async (userId) => {
        try {
            await axios.post(`https://celebrated-harmony-production.up.railway.app/api/admin/approve/${userId}`);
            setPendingUsers(prev => prev.filter(user => user.userId !== userId));
            const approvedUser = pendingUsers.find(user => user.userId === userId);
            setApprovedUsers(prev => [...prev, approvedUser]);
        } catch (error) {
            console.error(error);
        }
    };

    const handleReject = async (userId) => {
        try {
            await axios.delete(`https://celebrated-harmony-production.up.railway.app/api/admin/reject/${userId}`);
            setPendingUsers(prev => prev.filter(user => user.userId !== userId));
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (userId) => {
        try {
            await axios.delete(`https://celebrated-harmony-production.up.railway.app/api/admin/delete/${userId}`);
            setApprovedUsers(prev => prev.filter(user => user.userId !== userId));
        } catch (error) {
            console.error(error);
        }
    };

    const handleRoleChange = async (userId) => {
        try {
            await axios.put(`https://celebrated-harmony-production.up.railway.app/api/admin/update-role/${userId}?role=${newRole}`);
            setApprovedUsers(prev => prev.map(user => user.userId === userId ? { ...user, role: newRole } : user));
            setEditRoleUserId(null);
            setNewRole('USER');
        } catch (error) {
            console.error(error);
        }
    };

    const handleFormChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevFormData => ({
            ...prevFormData,
            [name]: value
        }));
    };

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('https://celebrated-harmony-production.up.railway.app/api/admin/create-user', formData);
            alert('User created successfully!');
            setFormData({
                firstName: '',
                lastName: '',
                email: '',
                password: '',
                role: 'USER',
                securityQuestion: '',
                securityAnswer: ''
            });
        } catch (error) {
            console.error(error);
            alert('Error creating user');
        }
    };

    if (!isAdmin) {
        return <p>You are not an admin. You do not have access to this page.</p>;
    }

    return (
        <div className="admin-container">
            <h1>Pending Approvals</h1>
            <div className="user-list">
                {pendingUsers.map(user => (
                    <div className="user-card" key={user.userId}>
                        <div className="user-info">
                            <p><strong>First Name:</strong> {user.firstName}</p>
                            <p><strong>Last Name:</strong> {user.lastName}</p>
                            <p><strong>Email:</strong> {user.email}</p>
                            <p><strong>Role:</strong> {user.role}</p>
                        </div>
                        <div className="actions">
                            <button onClick={() => handleApprove(user.userId)}>Approve</button>
                            <button onClick={() => handleReject(user.userId)}>Reject</button>
                        </div>
                    </div>
                ))}
            </div>
            <h1>User Accounts</h1>
            <div className="user-list">
                {approvedUsers.map(user => (
                    <div className="user-card" key={user.userId}>
                        <div className="user-info">
                            <p><strong>First Name:</strong> {user.firstName}</p>
                            <p><strong>Last Name:</strong> {user.lastName}</p>
                            <p><strong>Email:</strong> {user.email}</p>
                            <p><strong>Role:</strong>
                                <select
                                    value={user.userId === editRoleUserId ? newRole : user.role}
                                    onChange={(e) => setNewRole(e.target.value)}
                                    disabled={user.userId !== editRoleUserId}
                                >
                                    <option value="USER">USER</option>
                                    <option value="ADMIN">ADMIN</option>
                                </select>
                            </p>
                        </div>
                        <div className="actions">
                            {user.userId === editRoleUserId ? (
                                <button onClick={() => handleRoleChange(user.userId)}>Submit</button>
                            ) : (
                                <button onClick={() => {
                                    setEditRoleUserId(user.userId);
                                    setNewRole(user.role);
                                }}>Edit Role</button>
                            )}
                            <button onClick={() => handleDelete(user.userId)}>Delete</button>
                        </div>
                    </div>
                ))}
            </div>
            <h1>Create New User</h1>
            <form onSubmit={handleFormSubmit} className="user-form">
                <div className="form-group">
                    <label htmlFor="firstName">First Name:</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleFormChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last Name:</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleFormChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email ID:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleFormChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleFormChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="role">Role:</label>
                    <select
                        id="role"
                        name="role"
                        value={formData.role}
                        onChange={handleFormChange}
                        required
                    >
                        <option value="USER">USER</option>
                        <option value="ADMIN">ADMIN</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="securityQuestion">Security Question:</label>
                    <input
                        type="text"
                        id="securityQuestion"
                        name="securityQuestion"
                        value={formData.securityQuestion}
                        onChange={handleFormChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="securityAnswer">Security Answer:</label>
                    <input
                        type="text"
                        id="securityAnswer"
                        name="securityAnswer"
                        value={formData.securityAnswer}
                        onChange={handleFormChange}
                        required
                    />
                </div>
                <button type="submit" className="submit-button">Create User</button>
            </form>
        </div>
    );
};

export default Admin;
