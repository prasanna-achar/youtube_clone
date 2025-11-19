import { useState } from 'react'

import './App.css'
import Login from './Pages/LoginPage'
import SignupPage from './Pages/SignupPage'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="w-full h-full">
      <SignupPage / >
    </div>
  )
}

export default App
