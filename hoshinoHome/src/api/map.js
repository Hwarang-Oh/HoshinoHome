import { Axios } from '@/utils/axios'

const mapAPI = Axios()

const getDealList = (range, success, fail) => {
  mapAPI.post('api/map/list', range).then(success).catch(fail)
}

export default { getDealList }
