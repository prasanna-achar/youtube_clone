import {z} from "zod"


const videoUploadSchema = z.object({
    title: z.string().min(3, "Title must be at least 3 characters"),
  description: z.string().min(5, "Description is required"),
  video: z.any().refine((files) => files?.length === 1, "Video is required"),
  thumbnail: z.any().refine((files) => files?.length === 1, "Thumbnail is required"),
})  

export default videoUploadSchema;