export const getJwtPayload = () => {
  const token = localStorage.getItem('accessToken');
  if (!token) return null;

  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (error) {
    console.error('Failed to decode token:', error);
    return null;
  }
};

export const getCurrentUserId = () => {
  const payload = getJwtPayload();
  return payload?.userId || null;
};

export const getCurrentUsername = () => {
  const payload = getJwtPayload();
  return payload?.username || null;
};

export const createAuthorizedRequest = (data) => {
  const payload = getJwtPayload();
  if (!payload?.userId) throw new Error('User not authenticated');

  return {
    ...data,
    userId: payload.userId,
    username: payload.username // Добавляем username
  };
};

export const apiPost = async (url, data) => {
  try {
    const requestData = createAuthorizedRequest(data);
    const response = await axios.post(url, requestData, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
};

// Добавляем интерцептор для автоматической подстановки токена
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});