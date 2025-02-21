import { useState } from 'react'
import UserNavbar from './components/navbar/userNavbar.jsx'
import LpNavbar from './components/navbar/lpnavbar.jsx'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <UserNavbar />
    </>
  )
}

export default App
