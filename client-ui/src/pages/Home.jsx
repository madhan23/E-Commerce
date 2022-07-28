import React, { useEffect } from "react";
import Banner from "../components/Banner";
import Product from "../components/Product";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { getProductList } from "../redux/features/ProductSlice";
import { getCartData } from "../redux/features/CartSlice";
import "./Home.scss";
const Home = () => {
  const navigate = useNavigate();
  const { loading, products, error } = useSelector((state) => state.product);
  const { username } = useSelector((state) => state.user.userDetails);
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getProductList({ page: 1, limit: 5 }));
    if (username) dispatch(getCartData());
  }, [dispatch, username]);
  return (
    <div>
      <Banner />
      <div className='product__section'>
        <h3 className='product__section__text'>Popular Products</h3>
        <div className='product__section__btn'>
          <button onClick={() => navigate("/products")}>
            Explore Products
          </button>
        </div>
        <div className='product__section__status-info'>
          {loading && <span>Loading...</span>}
          <p>{error}</p>
        </div>

        <div className='product__section__card'>
          {products &&
            products.map((product) => (
              <Product key={product.id} item={product} />
            ))}
        </div>
      </div>
    </div>
  );
};

export default Home;
