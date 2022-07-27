import React, { useEffect } from "react";
import { createOrder } from "../../api/Order";
import { useSelector, useDispatch } from "react-redux";
import {
  toggleCart,
  removeCartItem,
  toggleCartQtyItem,
  getCartData,
} from "../../redux/features/CartSlice";
import {
  AiOutlineMinus,
  AiOutlinePlus,
  AiOutlineLeft,
  AiOutlineShopping,
} from "react-icons/ai";
import { TiDeleteOutline } from "react-icons/ti";
import { toast } from "react-hot-toast";
import "./Cart.scss";

const Cart = () => {
  const dispatch = useDispatch();
  const { cartItems, total, cartQty } = useSelector((state) => state.cart);
  const { username, token } = useSelector((state) => state.user.userDetails);
  useEffect(() => {
    if (username) dispatch(getCartData());
  }, [dispatch, username]);
  const handlePayment = () => {
    toast.loading("Redirecting...", { duration: 1000 });
    createOrder(cartItems, token);
  };
  return (
    <div className='cart'>
      <div className='cart__section'>
        <button
          type='button'
          className='cart__section__icon'
          onClick={() => dispatch(toggleCart())}
        >
          <AiOutlineLeft />
          <span>
            Your Cart (
            <span className='cart__section--items'>{cartQty} items</span>)
          </span>
        </button>

        {cartItems.length < 1 && (
          <div className='cart__section--empty'>
            <AiOutlineShopping size={150} />
            <h3>Your Shopping bag is empty</h3>

            <button
              type='button'
              className='cart__btn'
              onClick={() => dispatch(toggleCart())}
            >
              Continue Shopping
            </button>
          </div>
        )}

        <div className='cart-product'>
          {cartItems.length > 0 &&
            cartItems.map((item, index) => (
              <div className='cart-product__info' key={item.pid}>
                <img
                  src={item?.image}
                  className='cart-product__image'
                  alt={item.title}
                />
                <div className='cart-product__desc'>
                  <p>{item.title}</p>
                  <span>
                    <strong>&#x20b9;</strong> {item.price}
                  </span>
                  <div>
                    <strong>color</strong>
                    <span style={{ marginLeft: "10px", fontSize: "13px" }}>
                      {item.color}
                    </span>
                  </div>
                  <div>
                    <strong>size</strong>
                    <span style={{ marginLeft: "10px", fontSize: "13px" }}>
                      {item.size}
                    </span>
                  </div>
                  <div className='cart-product__quantity'>
                    <div className='cart-product__quantity__desc'>
                      <button
                        className='decrement-btn'
                        onClick={() =>
                          dispatch(
                            toggleCartQtyItem({ type: "DEC", pid: item.pid })
                          )
                        }
                        disabled={item.quantity > 1 ? false : true}
                      >
                        <AiOutlineMinus />
                      </button>
                      <span className='qty-value'>{item.quantity} </span>
                      <button
                        className='increment-btn'
                        onClick={() =>
                          dispatch(
                            toggleCartQtyItem({ type: "INC", pid: item.pid })
                          )
                        }
                      >
                        <AiOutlinePlus />
                      </button>
                    </div>

                    <button
                      type='button'
                      className='remove-item'
                      onClick={() => dispatch(removeCartItem(item.pid))}
                    >
                      <TiDeleteOutline />
                    </button>
                  </div>
                </div>
              </div>
            ))}
        </div>

        {cartItems.length > 0 && (
          <div className='cart__total-section'>
            <div>
              <h4>SubTotal : {total}</h4>
            </div>
            <div>
              <button
                type='button'
                className='cart__btn'
                onClick={handlePayment}
              >
                Pay with Stripe
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Cart;
