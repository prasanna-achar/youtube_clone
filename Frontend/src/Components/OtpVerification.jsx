import { zodResolver } from '@hookform/resolvers/zod'
import React from 'react'
import { useForm } from 'react-hook-form'
import OTPSchema from '../TypeSchemas/OTPSchema'

function OtpVerification() {


  const {
    register,
    handleSubmit,
    setError,
    formState:{errors}
  } = useForm({
    resolver: zodResolver(OTPSchema)
  })

  const onSubmit = (data) =>{
    console.log(data);
    
  }


  return (
    <form onSubmit={handleSubmit(onSubmit)}
    clasName="">

      <div>
        <h3>Verify With the OTP that has sent to your email</h3>
      </div>
      <div>
        <label htmlFor="otp">Enter OTP:</label>
        <input type="text" name="otp" id="otp" maxLength={6} {...register("otp")}/>
      </div>
    </form>
  )
}



export default OtpVerification