import { noticeAxios } from '@/utils/axios'

const mapAPI = noticeAxios()

const getDealList = (range, success, fail) => {
  mapAPI.post('api/map/list', range).then(success).catch(fail)
}

export default { getDealList }
