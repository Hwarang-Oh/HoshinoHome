import { Axios } from '@/utils/axios'

const noticeAPI = Axios()

const getNoticeList = (success, fail) => {
  noticeAPI.get('/api/notices').then(success).catch(fail)
}
const registerNotice = (notice, success, fail) => {
  noticeAPI.post('api/notices', notice).then(success).catch(fail)
}
const modifyNotice = (notice, success, fail) => {
  noticeAPI.put(`api/notices/${notice.post_id}`, notice).then(success).catch(fail)
}
const removeNotice = (post_id, success, fail) => {
  noticeAPI.delete(`api/notices/${post_id}`).then(success).catch(fail)
}
const getNoticeDetail = (post_id, success, fail) => {
  noticeAPI.get(`/api/notices/${post_id}`).then(success).catch(fail)
}

export default { getNoticeList, registerNotice, modifyNotice, removeNotice, getNoticeDetail }
