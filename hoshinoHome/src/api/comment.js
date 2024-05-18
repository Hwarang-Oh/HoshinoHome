import { Axios } from '@/utils/axios'

const commentAPI = Axios()

const getCommentsByPost = (noticeId, success, error) => {
    commentAPI.get(`/api/comments/post/${noticeId}/board/notice`)
      .then(success)
      .catch(error);
  }
const addComment = (comment, success, error) => {
    commentAPI.post(`/api/comments`, comment)
      .then(success)
      .catch(error);
  }
const updateComment = (commentId, comment, success, error) => {
    commentAPI.put(`/api/comments/${commentId}`, comment)
      .then(success)
      .catch(error);
  }
const deleteComment = (commentId, success, error) => {
    commentAPI.delete(`/api/comments/${commentId}`)
      .then(success)
      .catch(error);
  }

export default { getCommentsByPost, addComment, updateComment, deleteComment }
