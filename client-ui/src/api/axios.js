import axios from "axios";
const baseURL = "https://e-trendz-api.herokuapp.com/api/v1";
const http = axios.create({ baseURL });
export default http;
