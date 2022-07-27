import React from "react";
import Cart from "../pages/Cart/Cart";
import { Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { logout } from "../redux/features/UserSlice";
import { toggleCart, clearCart } from "../redux/features/CartSlice";
import { AiOutlineShopping } from "react-icons/ai";
import { FcShop } from "react-icons/fc";
import "./Header.scss";
const Header = () => {
  const {
    userDetails: { username },
  } = useSelector((state) => state.user);

  const { showCart, cartQty } = useSelector((state) => state.cart);
  const dispatch = useDispatch();

  const handleLogout = () => {
    dispatch(clearCart());
    dispatch(logout());
  };
  return (
    <div className='navbar'>
      <Link to='/'>
        <div className='navbar__logo'>
          <FcShop />
          <p>Trendz</p>
        </div>
      </Link>

      <div className='navbar__menu'>
        <div className='navbar__menu__user'>
          {username ? (
            <div className='navbar__menu__user-info'>
              <span>{username[0].toUpperCase()}</span>
              <span
                className='navbar__menu__user-logout'
                onClick={handleLogout}
              >
                Logout
              </span>
            </div>
          ) : (
            <Link to={"login"}>
              <span className='navbar__menu__user-signIn'>Sign in</span>
            </Link>
          )}
        </div>
        <div className='navbar__menu__cart'>
          <button
            type='button'
            className='navbar__menu__cart-icon'
            onClick={() => dispatch(toggleCart())}
          >
            <AiOutlineShopping />
            <span className='navbar__menu__cart--qty'>{cartQty}</span>
          </button>
          {showCart && <Cart />}
        </div>
      </div>
    </div>
  );
};

export default Header;
