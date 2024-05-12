import { noticeAxios } from '@/utils/axios'

const mapAPI = noticeAxios()

const getDealList = (range, success, fail) => {
  mapAPI.post('api/map/list', range).then(success).then(fail)
}

export default { getDealList }
