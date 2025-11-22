import { zodResolver } from '@hookform/resolvers/zod'
import React from 'react'
import { useForm } from 'react-hook-form'
import forgotPasswordSchema from '../TypeSchemas/ForgotPasswordSchema'


function ForgotPassword() {

  const {
    register,
    handleSubmit,
    setError,
    formState:{errors}
  } = useForm({
    resolver: zodResolver(forgotPasswordSchema),
    defaultValues:{
      "email" :""
    }
  })


  const onSubmit = (data) =>{
    console.log(data)
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <h3>Enter the email</h3>
      </div>
      <label htmlFor="email">
        Email
      </label>
      <input type="email" name="email" id="email" />

      <input type="submit" value="submit" />
    </form>
  )
}

export default ForgotPassword