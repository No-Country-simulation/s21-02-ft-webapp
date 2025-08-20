import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../services/authService';
import { useAuthStore } from '../store/authStore';

export const useRegisterForm = () => {
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  
  const [formData, setFormData] = useState({
    fullName: '',
    dni: '',
    email: '',
    phoneNumber: '',
    password: ''
  });
  
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};
    
    if (!formData.fullName.trim()) newErrors.fullName = 'Nombre completo es requerido';
    if (!/^\d{8}$/.test(formData.dni)) newErrors.dni = 'DNI debe tener 8 dígitos';
    if (!/^\S+@\S+\.\S+$/.test(formData.email)) newErrors.email = 'Email inválido';
    if (!/^\+?\d{10,15}$/.test(formData.phoneNumber)) newErrors.phoneNumber = 'Teléfono inválido';
    if (formData.password.length < 6) newErrors.password = 'Mínimo 6 caracteres';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validateForm()) return;

    setIsLoading(true);
    try {
      await register(formData);
      navigate('/login');
    } catch (error) {
      setErrors({ submit: 'Error en el registro. Intente nuevamente.' });
    } finally {
      setIsLoading(false);
    }
  };

  // Redirigir si ya está autenticado
  if (isAuthenticated()) {
    navigate('/');
  }

  return {
    formData,
    errors,
    isLoading,
    handleChange,
    handleSubmit
  };
};