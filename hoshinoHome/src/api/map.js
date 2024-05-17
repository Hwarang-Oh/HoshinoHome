import { Axios } from '@/utils/axios'

const mapAPI = Axios()

const getHouseDealVoList = (range, success, fail) => {
  mapAPI.post('api/map/list', range).then(success).catch(fail)
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

export default { getHouseDealVoList, getHouseDealList, getHouseInfo, searchHouseInfoByQuery }
