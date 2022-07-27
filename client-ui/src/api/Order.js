import http from "./axios";
import { toast } from "react-hot-toast";
export const createOrder = async (products, token) => {
  try {
    const totalQty = products.length;
    const totalAmount = products.reduce((acc, { price }) => acc + price, 0);
    const { data } = await http.post(
      "/orders/checkout",
      {
        totalAmount,
        totalQty,
        products,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    const { PaymentId, orderId, PaymentURL } = data?.data;
    localStorage.setItem("pid", PaymentId);
    localStorage.setItem("orderId", orderId);
    if (PaymentURL) window.location.href = PaymentURL;
  } catch (error) {
    toast.error(error.message);
  }
};

export const updateOrder = async (orderInfo, token) => {
  try {
    const { data } = await http.put("/orders/update", orderInfo);
    localStorage.removeItem("pid");
    localStorage.removeItem("orderId");
    return data;
  } catch (error) {
    toast.error(error.message);
  }
};
