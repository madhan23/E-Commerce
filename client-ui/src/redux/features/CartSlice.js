import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import http from "../../api/axios";
import { toast } from "react-hot-toast";
const initialState = {
  cartItems: [],
  total: 0,
  cartQty: 0,
  showCart: false,
};

const apiRequest = async (cartItems, cartQty, total, token) => {
  const products = cartItems.map((item) => ({
    pid: item.pid,
    price: item.price,
    quantity: item.quantity,
    image: item.image,
    title: item.title,
    size: item.size,
    color: item.color,
  }));
  const cart = {
    products,
    totalQty: cartQty,
    totalAmount: total,
  };

  await http.post("/cart", cart, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getCartData = createAsyncThunk(
  "cart/retrieve",
  async (_, thunkApi, rejectWithValue) => {
    try {
      const {
        user: { userDetails },
      } = thunkApi.getState();
      const { data } = await http.get("/cart", {
        headers: {
          Authorization: `Bearer ${userDetails.token}`,
        },
      });
      return data.data;
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);

export const addCartItem = createAsyncThunk(
  "cart/add",
  async (product, thunkApi, rejectWithValue) => {
    thunkApi.dispatch(addItem(product));
    const {
      cart: { cartItems, total, cartQty },
      user: { userDetails },
    } = thunkApi.getState();

    try {
      apiRequest(cartItems, cartQty, total, userDetails.token);
      toast.success(`Product ${product.title} added to cart.`);
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);

export const removeCartItem = createAsyncThunk(
  "cart/remove",
  async (productId, thunkApi, rejectWithValue) => {
    const {
      user: { userDetails },
    } = thunkApi.getState();
    try {
      thunkApi.dispatch(removeItem(productId));
      await http.delete(`/cart/${productId}`, {
        headers: {
          Authorization: `Bearer ${userDetails.token}`,
        },
      });
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);

export const toggleCartQtyItem = createAsyncThunk(
  "cart/qty",
  (data, thunkApi, rejectWithValue) => {
    thunkApi.dispatch(toggleCartQty(data));
    const {
      cart: { cartItems, cartQty, total },
      user: { userDetails },
    } = thunkApi.getState();
    try {
      apiRequest(cartItems, cartQty, total, userDetails.token);
    } catch (error) {
      if (error.response) return rejectWithValue(error.response.data.message);
      return rejectWithValue(error.message);
    }
  }
);
const cartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    addItem: (state, action) => {
      let item = action.payload;

      let product = state.cartItems.find((product) => product.pid === item.pid);
      if (product) {
        state.total = state.total - product.price * product.quantity;
        product.quantity = item.quantity;
        product.color = item.color;
        product.size = item.size;
      } else {
        state.cartItems.push(item);
        state.cartQty = state.cartQty + 1;
      }
      state.total = state.total + item.price * item.quantity;
    },
    removeItem: (state, action) => {
      const id = action.payload;
      const product = state.cartItems.find((product) => product.pid === id);
      state.cartItems = state.cartItems.filter((product) => product.pid !== id);
      state.cartQty = state.cartQty - 1;
      state.total = state.total - product.quantity * product.price;
    },
    clearCart: (state) => {
      state.cartItems = [];
      state.total = 0;
      state.cartQty = 0;
    },
    toggleCartQty: (state, action) => {
      const { type, pid } = action.payload;
      const product = state.cartItems.find((product) => product.pid === pid);
      if (type === "INC") {
        state.cartItems.map((product) => {
          if (product.pid === pid) {
            product.quantity = product.quantity + 1;
          }
          return product;
        });
        state.total += product.price;
      } else {
        state.cartItems.map((product) => {
          if (product.pid === pid) {
            product.quantity = product.quantity - 1;
          }
          return product;
        });
        state.total -= product.price;
      }
    },
    toggleCart: (state) => {
      state.showCart = !state.showCart;
    },
  },
  extraReducers: {
    [getCartData.fulfilled]: (state, action) => {
      state.cartItems = action.payload.products;
      state.total = action.payload.totalAmount;
      state.cartQty = action.payload.totalQty;
    },
  },
});

export const { toggleCart, addItem, removeItem, toggleCartQty, clearCart } =
  cartSlice.actions;
export default cartSlice.reducer;
