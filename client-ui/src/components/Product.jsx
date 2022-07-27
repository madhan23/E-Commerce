import React from "react";
import { Link } from "react-router-dom";
import "./Product.scss";
const Product = ({ item }) => {
  return (
    <Link style={{ textDecoration: "none" }} to={`/products/${item.id}`}>
      <div className='product'>
        <img
          className='product__image'
          src={item.images && item.images[0]}
          alt={item.title}
          width={250}
          height={250}
        />

        <div className='product__desc'>
          <p className='product__desc--name'>{item.title}</p>
          <p className='product__desc--price'> &#8360; {item.price}</p>
        </div>
      </div>
    </Link>
  );
};

export default Product;
