import React, { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import '../css/Groups.css'

const Groups = () => {
    const [allGroups, setAllGroups] = useState([])
    const [userGroups, setUserGroups] = useState([])
    const [currSearching, setCurrSearching] = useState('')
    const [filter, setFilter] = useState('all')
    const [selectedGroup, setSelectedGroup] = useState(null)
    const [groupMembers, setGroupMembers] = useState([])

    const navigate = useNavigate()

    const currentUser = {
        userId: sessionStorage.getItem("userId"),
        firstName: sessionStorage.getItem("firstName"),
        lastName: sessionStorage.getItem("lastName")
    }

    useEffect(() => {
        const fetchAllGroups = async () => {
            try {
                const Response = await axios.get('https://celebrated-harmony-production.up.railway.app/api/groups/all')
                setAllGroups(Response.data)

                const userGroupsResponse = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/groups/user/${currentUser.userId}`)
                setUserGroups(userGroupsResponse.data)
            } catch (error) {
                console.error('Error fetching groups data:', error)
            }
        }

        fetchAllGroups()
    }, [currentUser.userId, navigate])

    const handleJoinRequest = async groupId => {
        try {
            await axios.post(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}/join?userId=${currentUser.userId}`)
            
            // Update group data after joining
            const updatedAllGroups = await axios.get('https://celebrated-harmony-production.up.railway.app/api/groups/all')
            setAllGroups(updatedAllGroups.data)

            const updatedUserGroups = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/groups/user/${currentUser.userId}`)
            setUserGroups(updatedUserGroups.data)

            alert('Joined the group successfully!')
        } catch (error) {
            console.error('Error joining group:', error)
            alert('Oops, failed to join')
        }
    }

    const handleCurrSearchingChange = e => {
        setCurrSearching(e.target.value)
    }

    const handleFilterChange = e => {
        setFilter(e.target.value)
    }

    const isUserInCurrGroup = (group) => {
        return userGroups.some(currGroup => currGroup.groupId === group.groupId)
    }

    const isJoinButtonVisible = (group) => {
        return !group.isPrivate && !isUserInCurrGroup(group)
    }

    const fetchGroupDetails = async (groupId) => {
        try {
            const groupResponse = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}`)
            setSelectedGroup(groupResponse.data)

            const membersResponse = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/groups/${groupId}/members`)
            setGroupMembers(membersResponse.data)
        } catch (error) {
            console.error('Error fetching group details:', error)
        }
    }

    const filtering = allGroups.filter(group => {
        const matchGroupName = group.groupName.toLowerCase().includes(currSearching.toLowerCase())
        const matchGroupInterests = group.interests.toLowerCase().includes(currSearching.toLowerCase())
        const resultForSearch = matchGroupName || matchGroupInterests

        switch (filter) {
            case 'MyGroups':
                return resultForSearch && isUserInCurrGroup(group)
            case 'Public':
                return resultForSearch && !group.isPrivate
            case 'Private':
                return resultForSearch && group.isPrivate
            default:
                return resultForSearch
        }
    })

    return (
        <div className="groups-container">
            <h1>Groups</h1>
            <div className="search-bar">
                <input
                    type="text"
                    placeholder="Search new groups what you want"
                    value={currSearching}
                    onChange={handleCurrSearchingChange}
                    className="search-input"
                />
                <select value={filter} onChange={handleFilterChange} className="filter">
                    <option value="All">All Groups</option>
                    <option value="MyGroups">My Groups</option>
                    <option value="Public">Public Groups</option>
                    <option value="Private">Private Groups</option>
                </select>
            </div>
            <Link to="/create-group" className="createGroupButton">
                Create A New Group
            </Link>
            <div>
                {!selectedGroup ? (
                    filtering.map(group => (
                        <div className="group-item" key={group.groupId}>
                            <h2>
                                <button
                                    onClick={() => fetchGroupDetails(group.groupId)}
                                    className="group-name"
                                >
                                    {group.groupName}
                                </button>
                            </h2>
                            <p>Created by: {group.createdBy.firstName} {group.createdBy.lastName}</p>
                            <p>Interests: {group.interests}</p>
                            {isJoinButtonVisible(group) && (
                                <button
                                    onClick={() => handleJoinRequest(group.groupId)}
                                    className="joinButton"
                                >
                                    Join
                                </button>
                            )}
                        </div>
                    ))
                ) : (
                    <div className="group-details">
                        <h2>{selectedGroup.groupName}</h2>
                        <p>Created by: {selectedGroup.createdBy.firstName} {selectedGroup.createdBy.lastName}</p>
                        <p>Interests: {selectedGroup.interests}</p>
                        <h3>Members:</h3>
                        <ul>
                            {groupMembers.map(member => (
                                <li key={member.user.userId}>
                                    {member.user.firstName} {member.user.lastName}
                                </li>
                            ))}
                        </ul>
                        {isUserInCurrGroup(selectedGroup) ? (
                            <p>You are a member of this group.</p>
                        ) : (
                            !selectedGroup.isPrivate && (
                                <button onClick={() => handleJoinRequest(selectedGroup.groupId)} className="joinButton">
                                    Join Group
                                </button>
                            )
                        )}
                        <button onClick={() => setSelectedGroup(null)} className="backButton">
                            Back to Groups
                        </button>
                    </div>
                )}
            </div>
        </div>
    )
}

export default Groups
