import { create } from "zustand";
import axiosInstance from "../libs/axiosInstance";

const authStore = create((set) =>({
    AuthUser: null,
    isLoading: false,
    verificationCode: "",
    response: null,
    login:async(data) =>{
        set({isLoading: true})
        try {
            const response = await axiosInstance.post("/auth/login", data)
            if(response.status >= 300){
                throw error;
            }
            set({response : response?.data})
            console.log(response);
            console.log(response.data)
            set({verificationCode : response.data?.data?.token})
        } catch (error) {
            throw error;
        }
        finally{
            set({isLoading: false})
        }
    },
    register:()=>{},
    verify:()=>{},
    forgotPassword:()=>{},
    resendOtp:()=>{},
    getme:()=>{},
    resetPassword:()=>{}
}))


export default authStore;