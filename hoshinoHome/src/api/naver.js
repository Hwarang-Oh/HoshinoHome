import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/search/news';

const searchNews = () => {
  return axios.get(API_BASE_URL);
};

export default { searchNews };
