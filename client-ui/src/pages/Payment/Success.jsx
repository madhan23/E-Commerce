import React, { useEffect } from "react";
import { updateOrder } from "../../api/Order";
import { useNavigate } from "react-router-dom";
import { fireWorks } from "../../util/utils";
import { useDispatch, useSelector } from "react-redux";
import { removeCartItem } from "../../redux/features/CartSlice";
import { BsBagCheckFill } from "react-icons/bs";
import "./Success.scss";

const Success = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const items = useSelector((state) => state.cart.cartItems);
  useEffect(() => {
    const orderInfo = {
      orderId: localStorage.getItem("orderId"),
      paymentId: localStorage.getItem("pid"),
    };
    updateOrder(orderInfo);
    items.forEach((item) => dispatch(removeCartItem(item.id)));
    fireWorks();
  }, [items, dispatch]);

  return (
    <div className='success'>
      <div className='success__card-section'>
        <p className='success__card-section__icon'>
          <BsBagCheckFill />
        </p>
        <h2>Thank you for your Order !</h2>
        <p className='email-msg'>Check your email inbox for the receipt.</p>
        <p className='description'>
          if you have any questions,please email{" "}
          <a className='email' href='mailto:order@example.com'>
            order@example.com
          </a>
        </p>

        <button type='button' className='btn' onClick={() => navigate("/")}>
          Continue Shopping
        </button>
      </div>
    </div>
  );
};

export default Success;
