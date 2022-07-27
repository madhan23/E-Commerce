import React, { useEffect, useState } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { createOrder } from "../../api/Order";
import { useDispatch, useSelector } from "react-redux";
import { getProductDetail } from "../../redux/features/ProductSlice";
import { getCartData } from "../../redux/features/CartSlice";
import { addCartItem } from "../../redux/features/CartSlice";
import { AiOutlineMinus, AiOutlinePlus } from "react-icons/ai";
import { FaShoppingCart, FaArrowCircleLeft } from "react-icons/fa";
import { BsArrowRightCircle } from "react-icons/bs";
import "./ProductDetail.scss";

const ProductDetail = () => {
  const { id } = useParams();
  const {
    loading,
    error,
    productDetails: product,
  } = useSelector((state) => state.product);

  const token = useSelector((state) => state.user.userDetails.token);
  const dispatch = useDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const [index, setIndex] = useState(0);
  const [qty, setQty] = useState(1);
  const [colorIndex, setColorIndex] = useState(0);
  const [sizeIndex, setSizeIndex] = useState(null);
  const [errorMsg, setErrorMsg] = useState({ size: null });

  useEffect(() => {
    dispatch(getProductDetail(id));
    dispatch(getCartData());
  }, [id, dispatch]);

  const bindpayload = (product) => {
    const item = {
      pid: product.id,
      title: product.title,
      image: product.images[0],
      quantity: qty,
      price: product.price,
    };

    if (product.color?.[colorIndex]) {
      item.color = product.color[colorIndex].name;
    }

    if (product.size?.[sizeIndex]) {
      item.size = product.size[sizeIndex];
    }
    return item;
  };
  const addTocart = (product) => {
    const item = bindpayload(product);
    if (sizeIndex === null) {
      setErrorMsg((data) => ({ ...data, size: "Please select size" }));

      return;
    }

    if (!token) {
      navigate("/login", { state: { from: location.pathname }, replace: true });
    } else {
      dispatch(addCartItem(item));
    }
  };

  const handleBuyNow = () => {
    const item = bindpayload(product);
    if (!token) {
      navigate("/login", { state: { from: location.pathname }, replace: true });
    } else {
      createOrder([item], token);
    }
  };
  return (
    <div>
      {loading || error ? (
        <div className='textSection'>
          {loading && <span>Loading...</span>}
          {error && <span>{error}</span>}
        </div>
      ) : (
        <div className='product-detail-container'>
          <button className='arrow-icon' onClick={() => navigate("/")}>
            <FaArrowCircleLeft />
          </button>
          {product && (
            <>
              <div>
                <div className='image-container'>
                  <img
                    src={product.images && product.images[index]}
                    alt=''
                    className='product-detail-image'
                  />
                </div>
                <div className='small-images-container'>
                  {product.images.map((item, i) => (
                    <img
                      key={i}
                      className={
                        i === index
                          ? "small-image selected-image"
                          : "small-image"
                      }
                      onMouseEnter={() => setIndex(i)}
                      src={item}
                      alt={item.title}
                    />
                  ))}
                </div>
              </div>

              <div className='product-detail-desc'>
                <h3>{product.title}</h3>
                <p>{product.desc}</p>
                <p className='price'>
                  &#x20b9;
                  {product.price}
                </p>
                <div className='quantity'>
                  <div className='quantity-desc'>
                    <button
                      className='minus'
                      onClick={() => setQty((qty) => qty - 1)}
                      disabled={qty > 1 ? false : true}
                    >
                      <AiOutlineMinus />
                    </button>
                    <span className='num'>{qty}</span>
                    <button
                      className='plus'
                      onClick={() => setQty((qty) => qty + 1)}
                    >
                      <AiOutlinePlus />
                    </button>
                  </div>
                </div>
                {product.color && (
                  <div className='select-product-color'>
                    <div className='title'>color</div>
                    <div className='colorItemList'>
                      {product.color.map(({ code, name }, index) => (
                        <React.Fragment key={index}>
                          <span
                            className={index === colorIndex ? "active" : ""}
                            style={{ backgroundColor: code }}
                          ></span>
                          <label>{name}</label>
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                )}
                {product.size && (
                  <div className='select-product-size'>
                    <div className='title'>Size</div>
                    <div className='size-box'>
                      {product.size.map((data, index) => (
                        <div
                          key={index}
                          className={
                            sizeIndex === index ? "size active" : "size"
                          }
                          onClick={() => {
                            setSizeIndex(index);
                            if (errorMsg.size !== null) {
                              setErrorMsg((val) => ({ ...val, size: null }));
                            }
                          }}
                        >
                          {data}
                        </div>
                      ))}
                    </div>
                  </div>
                )}

                <div className='errorText'>{errorMsg && errorMsg.size}</div>
                <div className='product-btn-section'>
                  <button
                    type='button'
                    className='add-to-cart'
                    onClick={() => addTocart(product)}
                  >
                    <span className='img-icon'>
                      <FaShoppingCart />
                      Add to Cart
                    </span>
                  </button>
                  <button
                    type='button'
                    className='buy-now'
                    onClick={handleBuyNow}
                  >
                    <span className='img-icon'>
                      <BsArrowRightCircle />
                      Buy now
                    </span>
                  </button>
                </div>
              </div>
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default ProductDetail;
