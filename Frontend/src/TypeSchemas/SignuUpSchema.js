import { z } from "zod";

const SignupSchema = z.object({
  username: z
    .string()
    .min(6, "User name should have more than 6 characters")
    .max(16, "User should have less than 16 characters")
    .regex(/^[a-z][a-z0-9_]*$/, "Invalid username format"),
    
  email: z.email(),
  
  password: z
    .string()
    .min(8, "Minimum 8 characters")
    .max(16, "Maximum 16 characters")
});

export default SignupSchema;
