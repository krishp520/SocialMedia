import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import "../css/CurrentGroup.css"

const CurrentGroup = () => {
    const [group, setGroup] = useState(null);
    const [members, setMembers] = useState([]);
    const [loading, setLoading] = useState(true);
    const { groupId } = useParams();

    const [interests, setInterests] = useState('');

    const navigate = useNavigate();

    const currentUser = {
        userId: sessionStorage.getItem("userId"),
        firstName: sessionStorage.getItem("firstName"),
        lastName: sessionStorage.getItem("lastName")
    };

    useEffect(() => {
        const fetchGroupDetails = async () => {
            try {
                const response = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}`);
                setGroup(response.data);
                setInterests(response.data.interests || '');
            } catch (error) {
                console.error('Error fetching group details:', error);
            }
        }

        const fetchGroupMembers = async () => {
            try {
                const response = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}/members`);
                setMembers(response.data);
            } catch (error) {
                console.error('Error fetching group members:', error);
            } finally {
                setLoading(false);
            }
        }

        fetchGroupDetails();
        fetchGroupMembers();
    }, [groupId, currentUser.userId, navigate]);

    const updateInterests = async () => {
        try {
            const response = await axios.put(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}/interests`, { interests });
            setGroup({ ...group, interests: response.data });
            alert('Interests updated successfully');
        } catch (error) {
            console.error('Error updating interests:', error);
        }
    };

    const updateIsPrivate = async (isPrivate) => {
        try {
            const response = await axios.put(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}/updateType`, {
                isPrivate: isPrivate
            });
            setGroup({ ...group, isPrivate: response.data.isPrivate });
        } catch (error) {
            console.error('Error changing group type:', error);
        }
    };

    const removeMember = async (memberId) => {
        if (window.confirm('Are you sure you want to remove this member?')) {
            try {
                await axios.delete(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}/members/${memberId}`);
                setMembers(members.filter(member => member.id !== memberId));
                alert('Member has been removed');
            } catch (error) {
                console.error('Error removing member:', error);
            }
        }
    };

    if (loading) return <div>Loading...</div>;

    const isCreator = String(group.createdBy.userId) === String(currentUser.userId);

    return (
        <div className="current-group-container">
            <div className="group-header">
                <button onClick={() => navigate('/groups')} className="backButton">Back to Groups</button>
                <h1>{group.groupName}</h1>
                <div className="group-info">
                    <p>Description: {group.description}</p>
                    <p>Created by: {group.createdBy.firstName} {group.createdBy.lastName}</p>
                </div>
            </div>

            <div className="private-option">
                <label>Group Type</label>
                <select
                    name='isPrivate'
                    value={group.isPrivate}
                    onChange={(e) => updateIsPrivate(e.target.value === 'true')}
                    disabled={!isCreator}
                >
                    <option value={false}>Public</option>
                    <option value={true}>Private</option>
                </select>
            </div>

            <h2>Members</h2>
            <ul className="members-list">
                <li>
                    {group.createdBy.firstName} {group.createdBy.lastName}
                </li>
                {members.map((member) => (
                    member.user && member.user.userId !== group.createdBy.userId && (
                        <li key={member.id}>
                            {member.user.firstName} {member.user.lastName} - Member
                            {isCreator && (
                                <button className="removeButton" onClick={() => removeMember(member.id)}>Remove</button>
                            )}
                        </li>
                    )
                ))}
            </ul>

            <div className="interests-section">
                <h2>Interests</h2>
                {isCreator && (
                    <div>
                        <textarea
                            value={interests}
                            onChange={(e) => setInterests(e.target.value)}
                            placeholder="Replace the group interest"
                        />
                        <button className="updateInterestButton" onClick={updateInterests}>Update Interests</button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default CurrentGroup;
