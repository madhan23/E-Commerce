import React from "react";
import { AiFillInstagram, AiFillFacebook } from "react-icons/ai";
import "./Footer.scss";
const Footer = () => {
  return (
    <div className='footer'>
      <small>
        &copy; Copyright {new Date().getFullYear()}. All Rights Reserved
      </small>
      <div className='footer__icon-section'>
        <p className='footer__icon'>
          <AiFillInstagram />
        </p>
        <p className='footer__icon'>
          <AiFillFacebook />
        </p>
      </div>
    </div>
  );
};

export default Footer;
