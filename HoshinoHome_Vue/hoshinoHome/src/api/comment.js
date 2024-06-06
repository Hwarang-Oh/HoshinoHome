// src/api/comment.js

import axios from 'axios'

const API_URL = 'http://localhost:8080/api/comments'

export default {
  getCommentsByPostAndBoard(postId, boardType, success, error) {
    axios
      .get(`${API_URL}/post/${postId}/board/${boardType}`)
      .then(success)
      .catch(error)
  },
  addComment(comment, success, error) {
    axios
      .post(API_URL, comment)
      .then(success)
      .catch(error)
  },
  updateComment(commentId, comment, success, error) {
    axios
      .put(`${API_URL}/${commentId}`, comment)
      .then(success)
      .catch(error)
  },
  deleteComment(commentId, success, error) {
    axios
      .delete(`${API_URL}/${commentId}`)
      .then(success)
      .catch(error)
  }
}
