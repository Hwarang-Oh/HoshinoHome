import { Axios } from '@/utils/axios'

const mapAPI = Axios()

const getHouseDealVoList = (range, success, fail) => {
  mapAPI.post('api/map/list', range).then(success).catch(fail)
}

const getHouseDealVoList2 = (extendRange, success, fail) => {
  mapAPI.post('api/map/list2', extendRange).then(success).catch(fail)
}

const getFilteredHouseDeals = (houseTypes, dealTypes, success, fail) => {
  mapAPI.get('api/map/houseDeals', { params: { houseTypes, dealTypes } }).then(success).catch(fail)
}

const getHouseDealList = (house_code, success, fail) => {
  mapAPI.get(`api/map/dealList/${house_code}`).then(success).catch(fail)
}

const getHouseInfo = (house_code, success, fail) => {
  mapAPI.get(`api/map/houseInfo/${house_code}`).then(success).catch(fail)
}

const searchHouseInfoByQuery = (query, success, fail) => {
  mapAPI.get(`api/map/search`, { params: { query } }).then(success).catch(fail)
}

const toggleFavoritePlace = (houseCode, success, fail) => {
  const token = localStorage.getItem('token')
  mapAPI
    .post('/auth/me/favorite', { houseCode }, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .then(success)
    .catch(fail)
}

const getFavoritePlaces = (success, fail) => {
  const token = localStorage.getItem('token')
  mapAPI
    .get('/auth/me/favorites', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .then(success)
    .catch(fail)
}

export default {
  getHouseDealVoList,
  getHouseDealVoList2,
  getFilteredHouseDeals,
  getHouseDealList,
  getHouseInfo,
  searchHouseInfoByQuery,
  toggleFavoritePlace,
  getFavoritePlaces
}
