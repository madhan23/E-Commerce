import "./index.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import Layout from "./components/Layout";
import NetworkDetector from "./components/NetworkDetector";
import Home from "./pages/Home";
import ProductDetail from "./pages/Product/ProductDetail";
import Login from "./pages/User/Login";
import Register from "./pages/User/Register";
import { Cancel, Success } from "./pages/Payment/";
import PageNotFound from "./pages/404";
import Products from "./pages/Product/Products";

function App() {
  return (
    <BrowserRouter>
      <div className='container'>
        <Toaster />
        <Routes>
          <Route path='/' element={<Layout />}>
            <Route path='' element={<Home />} />
            <Route path='/products' element={<Products />} />
            <Route path='products/:id' element={<ProductDetail />} />
          </Route>
          <Route path='login' element={<Login />} />
          <Route path='register' element={<Register />} />
          <Route path='success' element={<Success />} />
          <Route path='canceled' element={<Cancel />} />
          <Route path='*' element={<PageNotFound />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
export default NetworkDetector(App);
