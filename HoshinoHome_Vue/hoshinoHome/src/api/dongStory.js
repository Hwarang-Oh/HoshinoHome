import axios from 'axios'

const API_URL = 'http://localhost:8080/api/dongstories'

export default {
  getDongStoryList(success, error) {
    axios
      .get(API_URL)
      .then(success)
      .catch(error)
  },
  addDongStory(dongStory, success, error) {
    axios
      .post(API_URL, dongStory)
      .then(success)
      .catch(error)
  },
  getDongStoryDetail(id, success, error) {
    axios
      .get(`${API_URL}/${id}`)
      .then(success)
      .catch(error)
  },
  removeDongStory(id, success, error) {
    axios
      .delete(`${API_URL}/${id}`)
      .then(success)
      .catch(error)
  },
  modifyDongStory(dongStory, success, error) {
    axios
      .put(`${API_URL}/${dongStory.post_id}`, dongStory)
      .then(success)
      .catch(error)
  }
}
