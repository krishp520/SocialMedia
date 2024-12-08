import React, { useContext } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { AuthContext } from '../AuthContext.js';
import "../css/Navbar.css"

const Navbar = () => {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <ul className="nav-links">
        <li><Link to="/">Dashboard</Link></li>
        <li><Link to="/friends">Friends</Link></li>
        <li><Link to="/requests">Requests</Link></li>
        <li><Link to="/admin">Admin</Link></li> 
        <li><Link to="/groups">Groups</Link></li> {/* Add this line */}
        <li><Link to="/profile">Profile</Link></li>
        <li><Link to="/login" onClick={handleLogout}>Logout</Link></li>
      </ul>
    </nav>
  );
}

export default Navbar;
