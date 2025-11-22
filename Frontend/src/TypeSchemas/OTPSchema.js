import {z} from "zod"

const OTPSchema = z.object({
    otp : z.string()
        .min(6).max(6)
        .regex(/^[0-9]*$/, "Invalid input")
})

export default OTPSchema