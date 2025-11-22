import {z} from "zod";

const resetPasswordSchema = z.object({
    password:  z
    .string()
    .min(8, "Minimum 8 characters")
    .max(16, "Maximum 16 characters")
})

export default resetPasswordSchema;