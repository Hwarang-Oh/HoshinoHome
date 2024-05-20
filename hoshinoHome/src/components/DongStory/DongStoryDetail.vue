<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import dongStoryAPI from '@/api/dongStory.js';
import commentAPI from '@/api/comment.js';
import axios from 'axios';
import Swal from 'sweetalert2';

const selectedDongStory = ref({});
const comments = ref([]);
const newComment = ref('');
const isAdmin = ref(false);
const editingCommentId = ref(null);
const editingCommentContent = ref('');
const userId = ref(null);
const userName = ref('');

const router = useRouter();
const route = useRoute();

const toDongStoryModify = (id) => {
  router.push({ name: 'dongStoryModify', params: { id } });
};

const removeDongStory = (id) => {
  dongStoryAPI.removeDongStory(id, 
    () => {
      router.push({ name: 'dongStory' });
    },
    (error) => {
      console.error('DongStory 데이터를 삭제하는 데 실패했습니다.', error);
    });
};

const toDongStoryList = () => {
  router.push({ name: 'dongStory' });
};

const checkAdmin = async () => {
  try {
    const token = localStorage.getItem('token');
    const response = await axios.get('http://localhost:8080/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    const userData = response.data;
    userId.value = userData.user_id;
    userName.value = userData.user_name;
    isAdmin.value = response.data.user_type === '1234';
  } catch (error) {
    console.error('사용자 정보를 불러오지 못했습니다.', error);
  }
};


const fetchComments = () => {
  commentAPI.getCommentsByPostAndBoard(selectedDongStory.value.post_id, 'dongstory',
    (response) => {
      comments.value = response.data;
    },
    (error) => {
      console.error('댓글 데이터를 불러오는 데 실패했습니다.', error);
    });
};

const addComment = () => {
  const token = localStorage.getItem('token');
  if (!token) {
    Swal.fire({
      icon: 'error',
      title: '로그인이 필요한 서비스입니다.',
      showConfirmButton: true,
      confirmButtonText: '확인'
    });
    return;
  }

  const comment = {
    post_id: selectedDongStory.value.post_id,
    board_type: 'dongstory',
    user_id: userId.value,
    user_name: userName.value,
    content: newComment.value,
    
  };
  commentAPI.addComment(comment, 
    () => {
      newComment.value = '';
      fetchComments();
    },
    (error) => {
      console.error('댓글을 추가하는 데 실패했습니다.', error);
    });
};

const editComment = (comment) => {
  editingCommentId.value = comment.comment_id;
  editingCommentContent.value = comment.content;
};

const updateComment = (commentId) => {
  const updatedComment = {
    comment_id: commentId,
    content: editingCommentContent.value,
  };
  commentAPI.updateComment(commentId, updatedComment, 
    () => {
      editingCommentId.value = null;
      editingCommentContent.value = '';
      fetchComments();
    },
    (error) => {
      console.error('댓글을 수정하는 데 실패했습니다.', error);
    });
};

const deleteComment = (commentId) => {
  commentAPI.deleteComment(commentId, 
    () => {
      fetchComments();
    },
    (error) => {
      console.error('댓글을 삭제하는 데 실패했습니다.', error);
    });
};

const fetchSelectedDongStory = (id) => {
  dongStoryAPI.getDongStoryDetail(id, 
    (response) => {
      selectedDongStory.value = response.data;
      fetchComments();
    },
    (error) => {
      console.error('DongStory 데이터를 불러오는 데 실패했습니다.', error);
    });
};

onMounted(async () => {
  await checkAdmin();
  const dongStoryId = route.params.id;
  fetchSelectedDongStory(dongStoryId);
});
</script>

<template>
  <div class="min-h-screen bg-blue-50 py-8">
    <div class="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-lg">
      <h1 class="text-2xl font-bold text-blue-600 text-center mb-6">DongStory 상세</h1>
      <div v-if="selectedDongStory.title" class="bg-blue-100 text-blue-700 py-2 px-4 rounded-md mb-4">
        <div class="flex justify-between">
          <span>제목</span>
          <span>등록일</span>
        </div>
        <div class="flex justify-between mt-2">
          <span>{{ selectedDongStory.title }}</span>
          <span>{{ new Date(selectedDongStory.date).toLocaleString() }}</span>
        </div>
        <div class="flex justify-between mt-2">
          <span>작성자: {{ selectedDongStory.user_name }}</span>
        </div>
      </div>
      <div v-else class="text-center text-gray-500">로딩 중...</div>
      <div class="mt-4" v-if="selectedDongStory.content">
        <p class="text-gray-700">{{ selectedDongStory.content }}</p>
      </div>
      <div class="mt-6 flex justify-end space-x-4">
        <button
          @click="toDongStoryList"
          class="bg-gradient-to-r from-blue-400 to-blue-600 hover:from-blue-500 hover:to-blue-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
        >
          목록
        </button>
        <template v-if="isAdmin || selectedDongStory.user_id === userId">
          <button
            @click="toDongStoryModify(selectedDongStory.post_id)"
            class="bg-gradient-to-r from-orange-400 to-orange-600 hover:from-orange-500 hover:to-orange-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            수정하기
          </button>
          <button
            @click="removeDongStory(selectedDongStory.post_id)"
            class="bg-gradient-to-r from-orange-600 to-red-600 hover:from-red-500 hover:to-orange-800 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
          >
            삭제하기
          </button>
        </template>
      </div>

      <!-- Comment Section -->
      <div class="mt-8">
        <h2 class="text-xl font-bold text-blue-600 mb-4">댓글</h2>
        <div v-for="comment in comments" :key="comment.comment_id" class="mb-4 p-4 bg-gray-100 rounded-lg">
          <p class="font-bold">{{ comment.user_name }}</p>
          <p class="text-gray-700" v-if="editingCommentId !== comment.comment_id">{{ comment.content }}</p>
          <div v-else>
            <textarea v-model="editingCommentContent" rows="3" class="w-full p-2 border rounded-lg"></textarea>
            <div class="flex justify-end mb-3">
            <button
              @click="updateComment(comment.comment_id)"
              class="mt-2 bg-gradient-to-r from-blue-400 to-blue-600 hover:from-blue-500 hover:to-blue-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
            >
              댓글 수정
            </button></div>
          </div>
          <div class="text-right text-sm text-gray-500">
            <span>{{ new Date(comment.date).toLocaleString() }}</span>
            <template v-if="comment.user_id === userId">
              <button @click="editComment(comment)" class="ml-4 text-blue-500">수정</button>
              <button @click="deleteComment(comment.comment_id)" class="ml-2 text-red-500">삭제</button>
            </template>
          </div>
        </div>
        <div class="mt-6">
          <textarea v-model="newComment" rows="3" class="w-full p-2 border rounded-lg" placeholder="댓글을 입력하세요"></textarea>
          <div class="flex justify-end space-x-4">
            <button
              @click="addComment"
              class="mt-2 bg-gradient-to-r from-blue-400 to-blue-600 hover:from-blue-500 hover:to-blue-700 text-white font-bold py-2 px-6 rounded-lg shadow-lg hover:shadow-xl transition duration-300"
            >
              댓글 추가
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
