import axios from "axios";
import config from "./config";

const axiosInstance = axios.create({
    baseURL: config.backend_url,
    withCredentials: true
})

console.log(axiosInstance)

export default axiosInstance;