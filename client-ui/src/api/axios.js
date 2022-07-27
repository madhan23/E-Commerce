import axios from "axios";
const baseURL = "http://localhost:8000/api/v1";
const http = axios.create({ baseURL });
export default http;
