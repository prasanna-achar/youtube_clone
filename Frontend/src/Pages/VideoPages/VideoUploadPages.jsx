import { zodResolver } from '@hookform/resolvers/zod'
import React from 'react'
import { useForm } from 'react-hook-form'
import videoUploadSchema from '../../TypeSchemas/VideoUploadSchema'
import VideoUploadForm from '../../Components/VideoUploadForm'

function VideoUploadPages() {
    return (
        <VideoUploadForm />
    )
}

export default VideoUploadPages