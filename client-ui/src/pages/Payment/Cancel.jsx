import React from "react";
import { useEffect } from "react";
import { updateOrder } from "../../api/Order";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { removeCartItem } from "../../redux/features/CartSlice";
import { BsBagCheckFill } from "react-icons/bs";
import "./Cancel.scss";

const Cancel = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const items = useSelector((state) => state.cart.cartItems);
  useEffect(() => {
    const orderInfo = {
      orderId: localStorage.getItem("orderId"),
      paymentId: null,
    };
    updateOrder(orderInfo);
    items.forEach((item) => dispatch(removeCartItem(item.id)));
  }, [items, dispatch]);
  return (
    <div className='cancel'>
      <div className='cancel__card-section'>
        <p className='cancel__card-section__icon'>
          <BsBagCheckFill />
        </p>
        <h2>Payment Canceled </h2>
        <button type='button' className='btn' onClick={() => navigate("/")}>
          Continue Shopping
        </button>
      </div>
    </div>
  );
};

export default Cancel;
