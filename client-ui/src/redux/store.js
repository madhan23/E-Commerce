import { configureStore, combineReducers } from "@reduxjs/toolkit";
import storage from "redux-persist/lib/storage";
import { persistReducer, persistStore } from "redux-persist";
import thunk from "redux-thunk";
import UserReducer from "./features/UserSlice";
import ProductReducer from "./features/ProductSlice";
import CartReducer from "./features/CartSlice";

const persistConfig = {
  key: "root",
  storage,
};

const rootReducer = combineReducers({
  user: UserReducer,
  product: ProductReducer,
  cart: CartReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  devTools: process.env.NODE_ENV !== "production",
  middleware: [thunk],
});

// export const store = configureStore({
//   reducer: { user: UserReducer, product: ProductReducer, cart: CartReducer },
// });

export const persistor = persistStore(store);
