import {z} from "zod";

const LoginSchema = z.object({
    email : z.email(),
    password: z
    .string()
    .max(16, "Maximum 16 characters")
    .min(8, "Minimum 8 characters")
})

export default  LoginSchema;