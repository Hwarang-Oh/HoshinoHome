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
}

// 컴포넌트가 마운트될 때 로그인 모달을 표시합니다.
onMounted(() => {
  showLoginModal()
})
</script>
