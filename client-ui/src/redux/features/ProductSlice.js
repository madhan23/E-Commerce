import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import http from "../../api/axios";
const initialState = {
  products: [],
  totalRecords: 0,
  productDetails: null,
  loading: false,
  error: null,
};

export const getProductList = createAsyncThunk(
  "products",
  async (data, { rejectWithValue }) => {
    let url = "/products";
    let params = new URLSearchParams();
    if (data) {
      if (data.page) {
        params.append("page", data.page);
        params.append("limit", 5);
      }
      if (data.category) {
        params.append("categories", data.category);
      }
      if (data.size) {
        params.append("size", data.size);
      }

      if (data.orderBy) {
        params.append("sortBy", "price");
        params.append("orderBy", data.orderBy);
      }
    }
    if (params.toString().trim().length > 1) {
      url = url.concat(`?${params.toString()}`);
    }
    try {
      const { data: response } = await http.get(encodeURI(url));
      return response?.data;
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);

export const getProductDetail = createAsyncThunk(
  "products/:id",
  async (productId, { rejectWithValue }) => {
    try {
      const { data } = await http.get(`/products/${productId}`);
      return data.data;
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);
const productSlice = createSlice({
  name: "product",
  initialState,
  reducers: {
    clearProductItems: (state) => {
      state.products = [];
      state.error = null;
      state.loading = true;
    },
  },
  extraReducers: {
    [getProductList.fulfilled]: (state, action) => {
      const { products, totalRecords } = action.payload;
      state.products = products;
      state.totalRecords = totalRecords;
      state.loading = false;
      state.error = null;
    },
    [getProductList.pending]: (state) => {
      state.loading = true;
      state.products = [];
      state.totalRecords = 0;
    },
    [getProductList.rejected]: (state, action) => {
      state.error = action.payload
        ? action.payload
        : "oops something went wrong!";
      state.loading = false;
      state.products = [];
      state.totalRecords = 0;
    },

    [getProductDetail.fulfilled]: (state, action) => {
      state.productDetails = action.payload;
      state.loading = false;
      state.error = null;
    },
    [getProductDetail.pending]: (state) => {
      state.loading = true;
    },
    [getProductDetail.rejected]: (state, action) => {
      state.error = action.payload
        ? action.payload
        : "oops something went wrong!";
      state.loading = false;
    },
  },
});
export const { clearProductItems } = productSlice.actions;
export default productSlice.reducer;
