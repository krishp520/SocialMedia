import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import "../css/CreateGroup.css"

const CreateGroup = () => {
    const [groupName, setGroupName] = useState('');
    const [description, setDescription] = useState('');
    const [interests, setInterests] = useState('');
    const [isPrivate, setIsPrivate] = useState('public');
    const navigate = useNavigate();

    const currentUser = {
        userId: sessionStorage.getItem("userId"),
        firstName: sessionStorage.getItem("firstName"),
        lastName: sessionStorage.getItem("lastName")
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
    
        const groupData = {
            groupName,
            description,
            interests,
            isPrivate: isPrivate === 'private'
        };
    
        try {
            await axios.post(`https://celebrated-harmony-production.up.railway.app/api/groups/create/${currentUser.userId}`, groupData);
    
            // Display confirmation dialog
            const userChoice = window.confirm('Successfully created! Click OK to go to the groups page or Cancel to create another group.');
    
            if (userChoice) {
                // Redirect to the newly created group's page
                navigate(`/groups`);
            } else {
                // Reset form fields to allow user to create another group
                setGroupName('');
                setDescription('');
                setInterests('');
                setIsPrivate('public');
            }
        } catch (error) {
            alert('Failed to create a new group');
        }
    };

    return (
        <div className="create-group">
            <h2>Create New Group</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="groupName">Group Name:</label>
                    <input
                        type="text"
                        id="groupName"
                        value={groupName}
                        onChange={(e) => setGroupName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description:</label>
                    <textarea
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                </div>
                <div className="form-group-type">
                    <label htmlFor="groupType">Group Type:</label>
                    <select
                        id="groupType"
                        value={isPrivate}
                        onChange={(e) => setIsPrivate(e.target.value)}
                    >
                        <option value="public">Public</option>
                        <option value="private">Private</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="interests">Interests (comma separated):</label>
                    <input
                        type="text"
                        id="interests"
                        value={interests}
                        onChange={(e) => setInterests(e.target.value)}
                    />
                </div>
                <button type="submit">Create</button>
            </form>
        </div>
    );
};

export default CreateGroup;