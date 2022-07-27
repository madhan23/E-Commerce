import { createContext, useContext, useEffect, useState } from "react";
import { removeCartItem, updateCartItem } from "../api/Product";
import { toast } from "react-hot-toast";
const Context = createContext(null);
export const StoreContext = ({ children }) => {
  const [auth, setAuth] = useState(null);
  const [showCart, setShowCart] = useState(false);
  const [qty, setQty] = useState(0);
  const [cartItems, setCartItems] = useState([]);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    if (cartItems) updateCartItem(auth?.token, cartItems);
  }, [cartItems, auth?.token]);
  const addItem = async (item) => {
    try {
      let product = cartItems.find((product) => product.id === item.id);
      if (product) {
        setTotal((total) => total - product.price * product.quantity);
        const updateItems = cartItems.map((product) => {
          if (product.id === item.id) {
            return {
              ...product,
              quantity: item.quantity,
            };
          }
          return product;
        });
        setCartItems(updateItems);
      } else {
        setCartItems((product) => [...product, item]);
        setQty((qty) => qty + 1);
      }
      setTotal((total) => total + item.price * item.quantity);
      toast.success(`Product : ${item.title} added to cart`);
    } catch (error) {
      toast.error("something went wrong");
    }
  };
  const removeItem = (productId) => {
    try {
      removeCartItem(auth.token, productId);

      let product = cartItems.find((product) => product.id === productId);
      let updateItems = cartItems.filter((product) => product.id !== productId);

      setTotal((total) =>
        total > 0 ? total - product.price * product.quantity : 0
      );
      setCartItems(updateItems);
      setQty((qty) => qty - 1);
    } catch (error) {
      toast.error("something went wrong");
    }
  };

  const toggleCartItemQty = (type, id) => {
    let product = cartItems.find((item) => item.id === id);
    let items;
    if (type === "INC") {
      items = cartItems.map((product) => {
        if (product.id === id) {
          return {
            ...product,
            quantity: product.quantity + 1,
          };
        }
        return product;
      });
      setTotal((total) => total + product.price);
    } else {
      items = cartItems.map((product) => {
        if (product.id === id) {
          return {
            ...product,
            quantity: product.quantity - 1,
          };
        }
        return product;
      });
      setTotal((total) => total - product.price);
    }
    setCartItems(items);
  };

  return (
    <Context.Provider
      value={{
        auth,
        setAuth,
        showCart,
        setShowCart,
        cartItems,
        setCartItems,
        qty,
        setQty,
        addItem,
        removeItem,
        total,
        setTotal,
        toggleCartItemQty,
      }}
    >
      {children}
    </Context.Provider>
  );
};
export const useStoreContext = () => useContext(Context);
