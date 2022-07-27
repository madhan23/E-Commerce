import http from "./axios";
const updateCartItem = async (token, cartItems) => {
  await http.post(
    "/cart",
    { products: cartItems },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
};

const removeCartItem = async (token, productId) => {
  await http.delete(`/cart/${productId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};
export { updateCartItem, removeCartItem };
