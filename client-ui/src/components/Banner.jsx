import React from "react";
import "./Banner.scss";
const Banner = () => {
  const heroBanner = {
    id: 2,
    img: "https://i.ibb.co/DG69bQ4/2.png",
    title: "AUTUMN COLLECTION",
    desc: "DON'T COMPROMISE ON STYLE! GET FLAT 30% OFF FOR NEW ARRIVALS.",
    bg: "lightgrey",
  };

  return (
    <div className='banner'>
      <div className='banner__card'>
        <div className='banner__card__desc'>
          <h3>{heroBanner.title}</h3>
          <p>{heroBanner.desc}</p>
          <button>Shop Now</button>
        </div>
        <div className='banner__card__image'>
          <img src={heroBanner.img} alt={heroBanner.title} />
        </div>
      </div>
    </div>
  );
};

export default Banner;
