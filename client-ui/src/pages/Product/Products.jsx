import React, { useEffect, useState, useRef } from "react";
import { BiChevronDown, BiChevronUp } from "react-icons/bi";
import { useDispatch, useSelector } from "react-redux";
import PageButton from "../../components/PageButton";
import Product from "../../components/Product";
import {
  getProductList,
  clearProductItems,
} from "../../redux/features/ProductSlice";
import "./Products.scss";
import "../../components/PageButton";

const Products = () => {
  const { loading, products, error, totalRecords } = useSelector(
    (state) => state.product
  );
  const [categoryParam, setCategoryParam] = useState("");
  const [sizeParam, setSizeParam] = useState("");
  const [orderBy, setOrderBy] = useState("asc");
  const [page, setPage] = useState(1);
  const [pageIndex, setPageIndex] = useState(0);
  const dispatch = useDispatch();
  const filterRef = useRef();
  useEffect(() => {
    dispatch(getProductList());
  }, [dispatch]);

  useEffect(() => {
    let category = categoryParam.trim().length > 1 ? categoryParam : null;
    let size = sizeParam.trim().length > 1 ? sizeParam : null;
    dispatch(clearProductItems());
    dispatch(getProductList({ category, size, orderBy, page }));
  }, [sizeParam, categoryParam, orderBy, page, dispatch]);
  const [toggle, setToggle] = useState({
    categorySection: false,
    sizeSection: false,
  });

  const pages = Array(Math.ceil(totalRecords / 5))
    .fill()
    .map((_, index) => index + 1);

  const [categoryList, setCategoryList] = useState([
    {
      name: "T-Shirt",
      isChecked: false,
    },
    {
      name: "Full Sleeve T-Shirt",
      isChecked: false,
    },
    {
      name: "Men",
      isChecked: false,
    },
    {
      name: "Women",
      isChecked: false,
    },
  ]);
  const [sizeList, setSizeList] = useState([
    {
      name: "S",
      isChecked: false,
    },
    {
      name: "M",
      isChecked: false,
    },
    {
      name: "L",
      isChecked: false,
    },
    {
      name: "XL",
      isChecked: false,
    },
  ]);

  const bindURIparmas = (type, items) => {
    let category = "";
    let size = "";
    if (type === "category" || type === "gender") {
      items
        .filter((item) => item.isChecked)
        .forEach((item) => {
          category = category.concat(`${item.name},`);
        });
      setCategoryParam(category);
    }
    if (type === "size") {
      items
        .filter((item) => item.isChecked)
        .forEach((item) => {
          size = size.concat(`${item.name},`);
        });
      setSizeParam(size);
    }
  };
  const handleOnChange = (type, value) => {
    if (type === "category") {
      let categorys = updateCheckBox(categoryList, value);
      setCategoryList(categorys);
      bindURIparmas("category", categorys);
    }
    if (type === "size") {
      let sizes = updateCheckBox(sizeList, value);
      setSizeList(sizes);
      bindURIparmas("size", sizes);
    }
  };
  const updateCheckBox = (items, searchvalue) => {
    return items.map((data) => {
      if (data.name === searchvalue) {
        data.isChecked = !data.isChecked;
      }
      return data;
    });
  };
  const { sizeSection, categorySection } = toggle;
  return (
    <div className='productCategory'>
      <div className='productCategory__filter'>
        <div className='accordion'>
          <h4>Filters</h4>

          <div className='filtersection'>
            <div
              className='label'
              onClick={() =>
                setToggle((p) => ({
                  ...p,
                  categorySection: !p.categorySection,
                }))
              }
            >
              <span>CATEGORIES</span>
              {categorySection ? (
                <BiChevronUp className='arrow' />
              ) : (
                <BiChevronDown className='arrow' />
              )}
            </div>
            {categorySection && (
              <div className='content'>
                {categoryList.map((item, index) => {
                  return (
                    <div key={index} style={{ margin: "10px 0" }}>
                      <input
                        type='checkbox'
                        name={index}
                        value={item.name}
                        onChange={() => handleOnChange("category", item.name)}
                        checked={item.isChecked}
                      />
                      <label> {item.name}</label>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
          <hr />
          <div className='filtersection'>
            <div
              className='label'
              onClick={() =>
                setToggle((p) => ({ ...p, sizeSection: !p.sizeSection }))
              }
            >
              <span>SIZE</span>
              {sizeSection ? (
                <BiChevronUp className='arrow' />
              ) : (
                <BiChevronDown className='arrow' />
              )}
            </div>
            {sizeSection && (
              <div className='content'>
                {sizeList.map((item, index) => {
                  return (
                    <div key={index} style={{ margin: "10px 0" }}>
                      <input
                        type='checkbox'
                        name={index}
                        value={item.name}
                        onChange={() => handleOnChange("size", item.name)}
                        checked={item.isChecked}
                      />
                      <label> {item.name}</label>
                    </div>
                  );
                })}
              </div>
            )}
          </div>
          <hr />
        </div>
      </div>
      <div className='productCategory__items'>
        <div className='dropdown_filter'>
          <p>
            List of products (
            <span style={{ color: "red" }}>{products.length} items</span>)
          </p>
          <div className='dropdown_filter__section'>
            <label htmlFor='orderBy'>Order By</label>
            <select
              id='orderBy'
              ref={filterRef}
              onChange={(e) =>
                setOrderBy(e.target[e.target.selectedIndex].value)
              }
            >
              <option value='asc' defaultValue>
                Price: Low to High
              </option>
              <option value='desc'>Price: High to Low</option>
            </select>
          </div>
        </div>
        <div>
          <div className='textSection'>
            {loading && <span>Loading...</span>}
            <p>{error}</p>
          </div>
          <div className='productCategory__items__section'>
            {products &&
              products.map((product) => (
                <Product key={product.id} item={product} />
              ))}
          </div>
          <div className='pagination'>
            {products.length > 0 && (
              <>
                <button
                  className='page-btn'
                  onClick={() => {
                    setPage((prev) => prev - 1);
                    setPageIndex((count) => count - 1);
                  }}
                  disabled={page === 1}
                >
                  prev
                </button>

                {pages.map((pg, ind) => (
                  <PageButton
                    key={pg}
                    pageNo={pg}
                    setPage={setPage}
                    pageIndex={pageIndex}
                    index={ind}
                    setPageIndex={setPageIndex}
                  />
                ))}
                <button
                  className='page-btn'
                  onClick={() => {
                    setPage((next) => next + 1);
                    setPageIndex((count) => count + 1);
                  }}
                  disabled={page === pages.length}
                >
                  next
                </button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Products;
