<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()

const user = ref({
  user_name: '',
  user_password: '',
  user_address: '',
  user_favorite_place: '',
  user_type: ''
})

const register = async () => {
  try {
    await axios.post('http://localhost:8080/auth/register', user.value)
    Swal.fire({
      icon: 'success',
      title: '회원가입 성공!',
      showConfirmButton: false,
      timer: 1500
    }).then(() => {
      emit('close') // 부모 컴포넌트에 close 이벤트 전송
      router.push('/')
    })
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '회원가입 실패!',
      text: '모든 필드를 확인하세요.'
    })
  }
}

const emit = defineEmits(['close'])

const showRegisterModal = () => {
  Swal.fire({
    title: '회원가입',
    html: `
      <input id="swal-input1" class="swal2-input" placeholder="Id">
      <input id="swal-input2" type="password" class="swal2-input" placeholder="Password">
      <input id="swal-input3" class="swal2-input" placeholder="주소">
      <input id="swal-input4" class="swal2-input" placeholder="관심 지역">
      <input id="swal-input5" class="swal2-input" placeholder="관리자 코드">
    `,
    focusConfirm: false,
    preConfirm: () => {
      const user_name = document.getElementById('swal-input1').value
      const user_password = document.getElementById('swal-input2').value
      const user_address = document.getElementById('swal-input3').value
      const user_favorite_place = document.getElementById('swal-input4').value
      const user_type = document.getElementById('swal-input5').value

      if (!user_name || !user_password || !user_address || !user_favorite_place || !user_type) {
        Swal.showValidationMessage('모든 필드를 입력하세요.')
        return false
      }

      user.value.user_name = user_name
      user.value.user_password = user_password
      user.value.user_address = user_address
      user.value.user_favorite_place = user_favorite_place
      user.value.user_type = user_type

      return register()
    },
    showCancelButton: true,
    confirmButtonText: '회원가입',
    cancelButtonText: '취소',
    showLoaderOnConfirm: true,
    allowOutsideClick: true
  }).then((result) => {
    if (
      result.dismiss === Swal.DismissReason.cancel ||
      result.dismiss === Swal.DismissReason.backdrop
    ) {
      emit('close') // 부모 컴포넌트에 close 이벤트 전송
    }
  })
}

// 컴포넌트가 마운트될 때 회원가입 모달을 표시합니다.
onMounted(() => {
  showRegisterModal()
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
