import axios from 'axios'

const API_URL = 'http://localhost:8080/dong'

export default {
  getDongList(success, failure) {
    axios
      .get(API_URL)
      .then(success)
      .catch(failure)
  }
}
