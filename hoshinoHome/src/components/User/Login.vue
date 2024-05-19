<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import Swal from 'sweetalert2'

const router = useRouter()

const loginInfo = ref({
  user_name: '',
  user_password: ''
})

const login = async () => {
  try {
    const response = await axios.post('http://localhost:8080/auth/login', loginInfo.value)
    localStorage.setItem('token', response.data)
    Swal.fire({
      icon: 'success',
      title: '로그인 성공!',
      showConfirmButton: false,
      timer: 1500
    }).then(() => {
      emit('close') // 부모 컴포넌트에 close 이벤트 전송
      router.push('/').then(() => {
        window.location.reload() // 홈 화면으로 이동 후 새로고침
      })
    })
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: '로그인 실패!',
      text: '아이디 또는 비밀번호를 확인하세요.'
    }).then(() => {
      emit('close')
    })
  }
}

const emit = defineEmits(['close'])

const showLoginModal = () => {
  Swal.fire({
    title: '로그인',
    html: `
      <input id="swal-input1" class="swal2-input" placeholder="Id">
      <input id="swal-input2" type="password" class="swal2-input" placeholder="Password">
    `,
    focusConfirm: false,
    preConfirm: () => {
      const user_name = document.getElementById('swal-input1').value
      const user_password = document.getElementById('swal-input2').value

      if (!user_name || !user_password) {
        Swal.showValidationMessage('모든 필드를 입력하세요.')
        return false
      }

      loginInfo.value.user_name = user_name
      loginInfo.value.user_password = user_password

      return login()
    },
    showCancelButton: true,
    confirmButtonText: '로그인',
    cancelButtonText: '취소',
    showLoaderOnConfirm: true,
    allowOutsideClick: true,
    didOpen: () => {
      document.getElementById('swal-input1').focus()

      document.getElementById('swal-input1').addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
          Swal.clickConfirm()
        }
      })

      document.getElementById('swal-input2').addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
          Swal.clickConfirm()
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

  // 버튼 아래에 카카오 로그인 버튼 추가
  Swal.update({
    html: `
      <input id="swal-input1" class="swal2-input" placeholder="Id">
      <input id="swal-input2" type="password" class="swal2-input" placeholder="Password">
      <div id="kakao-login-btn" style="margin-top: 20px; text-align: center; margin-left:80px">
        <img
          src="/kakao_login_large_wide.png"
          width=290
          alt="카카오 로그인 버튼"
        />
      </div>
    `
  })

  document.getElementById('kakao-login-btn').addEventListener('click', kakaoLogin)
}

// 카카오 SDK 초기화
const initKakao = () => {
  if (!window.Kakao.isInitialized()) {
    window.Kakao.init('b6fc8f0d4cfc8c0ade4b040f2f2daf35') // 여기를 실제 JavaScript 키로 교체
  }
}

// 로그인 함수
const kakaoLogin = () => {
  window.Kakao.Auth.login({
    scope: 'profile_image',
    success: (authObj) => {
      getKakaoAccount(authObj)
      Swal.fire({
      icon: 'success',
      title: '로그인 성공!',
      showConfirmButton: false,
      timer: 1500
    }).then(() => {
      emit('close') // 부모 컴포넌트에 close 이벤트 전송
      router.push('/').then(() => {
        window.location.reload() // 홈 화면으로 이동 후 새로고침
      })
    })
    },
    fail: (err) => {
      console.error(err)
      alert('로그인 실패!')
    }
  })
}

// 사용자 정보 요청 함수
const getKakaoAccount = (authObj) => {
  window.Kakao.API.request({
    url: '/v2/user/me',
    success: (res) => {
      const kakao_account = res.kakao_account
      const nickname = kakao_account.profile.nickname
      console.log('nickname', nickname)

      // 로그인 처리 및 로컬스토리지에 토큰 저장
      localStorage.setItem('token', authObj.access_token)
    },
    fail: (error) => {
      console.log(error)
    }
  })
}


// 컴포넌트가 마운트될 때 카카오 SDK 초기화
onMounted(() => {
  initKakao()
  showLoginModal()
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
