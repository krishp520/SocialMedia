import React from 'react';
import { useLocation } from 'react-router-dom';
import Navbar from './Navbar';

const NavbarWrapper = () => {
    const location = useLocation();
    const hideNavbarPaths = ['/login', '/signup'];

    return !hideNavbarPaths.includes(location.pathname) ? <Navbar /> : null;
};
export default NavbarWrapper;