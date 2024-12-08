import React from 'react'
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom'
import NavbarWrapper from './components/NavbarWrapper'
import Dashboard from './components/Dashboard'
import Friends from './components/Friends'
import UserManagement from './components/UserManagement'
import FriendRequests from './components/FriendRequests'
import Signup from './components/Signup';
import AdminSignup from './components/AdminSignup';
import Login from './components/Login';
import ForgotPassword from './components/ForgotPassword';
import Admin from './components/Admin';
import { AuthProvider } from './AuthContext';
import PrivateRoute from './PrivateRoute';
import CreateGroup from "./components/CreateGroup";
import Groups from "./components/Groups";
import CurrentGroup from "./components/CurrentGroup";

const App = () => {
  const location = useLocation();
  const noNavbarRoutes = ['/login', '/signup', '/adminsignup', '/reset'];

  return (
    <AuthProvider >
      {!noNavbarRoutes.includes(location.pathname) && <NavbarWrapper />}
      <Routes>
        <Route path="/" element={<PrivateRoute element={Dashboard} />} />
        <Route path='/friends' element={<PrivateRoute element={Friends} />} />
        <Route path='/profile' element={<PrivateRoute element={UserManagement} />} />
        <Route path='/requests' element={<PrivateRoute element={FriendRequests} />} />
        <Route path='/admin' element={<PrivateRoute element={Admin} />} /> 
        <Route path="/groups" element={<PrivateRoute element={Groups} />} />
        <Route path="/groups/:groupId" element={<PrivateRoute element={CurrentGroup} />} />
        <Route path="/create-group" element={<PrivateRoute element={CreateGroup} />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/adminsignup" element={<AdminSignup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/reset" element={<ForgotPassword />} />
      </Routes>
    </AuthProvider>
  )
}

const AppWrapper = () => (
  <Router>
    <App />
  </Router>
);

export default AppWrapper;
