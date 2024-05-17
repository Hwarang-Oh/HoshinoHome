<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Swal from 'sweetalert2'
import { useRouter } from 'vue-router'

const user = ref({
  user_name: '',
  user_address: '',
  user_favorite_place: '',
  user_type: ''
})

const router = useRouter()

const fetchUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    user.value = response.data
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '사용자 정보를 불러오지 못했습니다.',
      text: error.message
    })
  }
}

const updateUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    await axios.put('http://localhost:8080/auth/me', user.value, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    Swal.fire({
      icon: 'success',
      title: '사용자 정보가 성공적으로 업데이트되었습니다.',
      showConfirmButton: false,
      timer: 1500
    }).then(() => {
      emit('close')
    })
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '사용자 정보 업데이트에 실패했습니다.',
      text: error.message
    }).then(() => {
      emit('close')
    })
  }
}

const deleteUser = async () => {
  try {
    const token = localStorage.getItem('token')
    await axios.delete('http://localhost:8080/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    Swal.fire({
      icon: 'success',
      title: '회원 탈퇴가 성공적으로 완료되었습니다.',
      showConfirmButton: false,
      timer: 1500
    }).then(() => {
      localStorage.removeItem('token')
      router.push('/').then(() => {
        window.location.reload() // 홈 화면으로 이동 후 새로고침
      })
      emit('close')
    })
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '회원 탈퇴에 실패했습니다.',
      text: error.message
    }).then(() => {
      router.push('/')
      emit('close')
    })
  }
}

const emit = defineEmits(['close'])

const showMyPageModal = async () => {
  await fetchUserInfo()

  Swal.fire({
    title: '내 정보',
    html: `
      <label for="swal-input1" class="block text-gray-700">아이디:</label>
      <input id="swal-input1" class="swal2-input" style="width: 255px;" value="${user.value.user_name}" readonly>
      <label for="swal-input2" class="block text-gray-700">주소:</label>
      <input id="swal-input2" class="swal2-input" value="${user.value.user_address}">
      <label for="swal-input3" class="block text-gray-700">관심 지역:</label>
      <input id="swal-input3" class="swal2-input" value="${user.value.user_favorite_place}">
      <label for="swal-input4" class="block text-gray-700">관리자 코드:</label>
      <input id="swal-input4" class="swal2-input" value="${user.value.user_type}">
      <button id="delete-btn" class="swal2-cancel swal2-styled" style="display: inline-block; background-color: red; margin-top: 10px;">삭제</button>
    `,
    focusConfirm: false,
    preConfirm: () => {
      user.value.user_address = document.getElementById('swal-input2').value
      user.value.user_favorite_place = document.getElementById('swal-input3').value
      user.value.user_type = document.getElementById('swal-input4').value

      return updateUserInfo()
    },
    showCancelButton: true,
    confirmButtonText: '수정',
    cancelButtonText: '취소',
    showLoaderOnConfirm: true,
    allowOutsideClick: true,
    didOpen: () => {
      const deleteBtn = Swal.getPopup().querySelector('#delete-btn')
      deleteBtn.addEventListener('click', async () => {
        const result = await Swal.fire({
          title: '회원 탈퇴',
          text: '정말로 탈퇴하시겠습니까?',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: '탈퇴',
          cancelButtonText: '취소'
        })
        if (result.isConfirmed) {
          deleteUser()
        }
      })
    }
  }).then((result) => {
    if (
      result.dismiss === Swal.DismissReason.cancel ||
      result.dismiss === Swal.DismissReason.backdrop
    ) {
      emit('close') // 부모 컴포넌트에 close 이벤트 전송
    }
  })
}

// 컴포넌트가 마운트될 때 MyPage 모달을 표시합니다.
onMounted(() => {
  showMyPageModal()
})
</script>

<template>
  <!-- 빈 템플릿, 모든 작업은 스크립트에서 SweetAlert2를 통해 처리 -->
</template>

<style scoped>
body {
  background-color: #f3f4f6;
}
</style>
