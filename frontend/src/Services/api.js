import axios from 'axios';


const api = axios.create({
    baseURL: 'http://localhost:9091/',
    headers: {
        'Content-Type': 'application/json',
    }
});

 api.interceptors.request.use(config => {
     const token = localStorage.getItem('Token');
     if (token) {
         config.headers.Authorization = `Bearer ${token}`;
     }
     return config;
 }, error => {
     return Promise.reject(error);
 });



export default api;
