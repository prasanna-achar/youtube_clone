import { useState } from 'react'

import './App.css'
import { LoginPage, SignupPage } from './Pages/AuthPages'
import VideoUploadPages from './Pages/VideoPages/VideoUploadPages'
import VideoPlayer from './Components/VideoPlayer'
function App() {
  const [count, setCount] = useState(0)


  const videoSources = {
    "480p":"https://res.cloudinary.com/dnd8nzkxl/raw/upload/v1763436067/users/95b92b45-01b3-4447-838c-67e2ea42d9d9/videos/P8NMQLVOG0/95b92b45-01b3-4447-838c-67e2ea42d9d9_1763435899251_hls/480/index.m3u8",
    "720p":"https://res.cloudinary.com/dnd8nzkxl/raw/upload/v1763436123/users/95b92b45-01b3-4447-838c-67e2ea42d9d9/videos/P8NMQLVOG0/95b92b45-01b3-4447-838c-67e2ea42d9d9_1763435899251_hls/720/index.m3u8",
    "auto":"https://res.cloudinary.com/dnd8nzkxl/raw/upload/v1763436189/users/95b92b45-01b3-4447-838c-67e2ea42d9d9/videos/P8NMQLVOG0/95b92b45-01b3-4447-838c-67e2ea42d9d9_1763435899251_hls/master.m3u8",
    "1080p":null
  }

  
  return (
    <div >
      <LoginPage />
    </div>
  );
  
}

export default App
