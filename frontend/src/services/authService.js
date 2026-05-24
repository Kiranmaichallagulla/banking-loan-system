import API from '../utils/axiosConfig';

export const authService = {
  register: (data) => API.post('/api/auth/register', data),
  login: (data) => API.post('/api/auth/login', data),
};
