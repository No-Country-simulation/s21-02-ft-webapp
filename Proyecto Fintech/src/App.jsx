import { useState } from 'react'
import Navbar from './components/navbar/userNavbar.jsx'
import LpNavbar from './components/navbar/lpnavbar.jsx'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Navbar />
    </>
  )
}

export default App
