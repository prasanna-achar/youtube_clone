import { zodResolver } from '@hookform/resolvers/zod'
import React from 'react'
import { useForm } from 'react-hook-form'
import resetPasswordSchema from '../TypeSchemas/ResetPasswordSchema'

function ResetPassword() {

  const {
    register,
    handleSubmit,
    setError,
    formState:{errors}
  } = useForm({
    resolver: zodResolver(resetPasswordSchema),
    defaultValues:{
      "password" :""
    }
  })

  const onSubmit = (data) =>{
    console.log(data);
    
  }


  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <h3>Enter the password that you want to change</h3>
      </div>

      <label htmlFor="password">New Password</label>
      <input type="password" name="password" id="password" />

      <input type="submit" value="Reset Password" />
    </form>
  )
}

export default ResetPassword