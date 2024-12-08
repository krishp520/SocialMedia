import React, { useState, useEffect } from 'react'
import axios from 'axios'
import "../css/FriendRequests.css"

const FriendRequests = () => {
  const [requests, setRequests] = useState([])
  const userId = sessionStorage.getItem("userId") // Assume the current user ID is 2

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const response = await axios.get(
          `https://celebrated-harmony-production.up.railway.app/api/requests/${userId}`
        )
        setRequests(response.data)
      } catch (error) {
        console.error(error)
      }
    }

    fetchRequests()
  }, [userId])

  const handleRequestAction = async (requestId, action) => {
    try {
      await axios.patch(
        `https://celebrated-harmony-production.up.railway.app/api/requests/${userId}/${requestId}?action=${action}`
      )
      setRequests(requests.filter(request => request.requestId !== requestId))
    } catch (error) {
      console.error(error)
    }
  }

  return (
    <div className="friend-request-container">
      <h1 >
        Friend Requests
      </h1>
      <ul className="list">
        {requests.map(request => (
          <li className="list-item" key={request.requestId}>
            <span className="list-item-content">
              {`From: ${request.sender.firstName} ${request.sender.lastName}`}
            </span>
            <button
              onClick={() => handleRequestAction(request.requestId, 'accepted')}
            >
              Accept
            </button>
            <button
              onClick={() => handleRequestAction(request.requestId, 'rejected')}
            >
              Reject
            </button>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default FriendRequests
