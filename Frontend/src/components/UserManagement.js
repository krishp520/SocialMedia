import React, { useState, useEffect } from 'react'
import axios from 'axios'
import "../css/UserManagement.css"
const UserManagement = () => {
  const [user, setUser] = useState({
    email: '',
    firstName: '',
    lastName: '',
    accountStatus: 'activated', // Default status
    status: 'available', // Default status
    securityQuestion: '',
    securityAnswer: ''
  })
  const [userId] = useState(sessionStorage.getItem("userId"));
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await axios.get(`https://celebrated-harmony-production.up.railway.app/api/users/${userId}`)
        setUser(response.data)
      } catch (error) {
        console.error("Error fetching user data:",error.response ? error.response.data : error.message)
      }
    }

    fetchUser()
  }, [userId])

  const handleChange = e => {
    const { name, value } = e.target
    setUser({ ...user, [name]: value })
  }

  const handleSubmit = async () => {
    try {
      await axios.patch(`https://celebrated-harmony-production.up.railway.app/api/users/${userId}`, user)
      alert('User updated successfully!')
    } catch (error) {
      console.error(error)
    }
  }

  return (
    <div className="mangement-container">
      <h1 >
        {user.firstName}'s Profile
      </h1>
      <form className="form">
        <div className="context">
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={user.email}
            onChange={handleChange}
          />
        </div>

        <div className="context">
          <label>First Name</label>
          <input
            type="text"
            name="firstName"
            value={user.firstName}
            onChange={handleChange}
          />
        </div>

        <div className="context">
          <label>Last Name</label>
          <input
            type="text"
            name="lastName"
            value={user.lastName}
            onChange={handleChange}
          />
        </div>
        <div className="context">
          <label>Security Question</label>
          <input
            type="text"
            name="securityQuestion"
            value={user.securityQuestion}
            onChange={handleChange}
          />
        </div>
        <div className="context">
          <label>Security Answer</label>
          <input
            type="text"
            name="securityAnswer"
            value={user.securityAnswer}
            onChange={handleChange}
          />
        </div>

        <div className="context">
          <label>Account Status</label>
          <select
            name="accountStatus"
            value={user.accountStatus}
            onChange={handleChange}
          >
            <option value='activated'>Activated</option>
            <option value='deactivated'>Deactivated</option>
          </select>
        </div>

        <div className="context">
          <label>Status</label>
          <select name='status' value={user.status} onChange={handleChange}>
            <option value='available'>Available</option>
            <option value='busy'>Busy</option>
            <option value='away'>Away</option>
          </select>
        </div>

        <button className="update-button" onClick={handleSubmit}>
          Update
        </button>
      </form>
    </div>
  )
}

export default UserManagement
