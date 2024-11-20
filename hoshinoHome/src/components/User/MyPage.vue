<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import Swal from 'sweetalert2'
import { useRouter } from 'vue-router'

const user = ref({
  user_name: '',
  user_address: '',
  user_favorite_place: [],
  user_type: ''
})

const favoritePlacesDetails = ref([]) // 관심 지역 상세 정보 저장

const router = useRouter()

const fetchHouseInfo = async (houseCode) => {
  try {
    const response = await axios.get(`http://localhost:8080/api/map/houseInfo/${houseCode}`)
    return response.data
  } catch (error) {
    console.error('Failed to fetch house info:', error)
    return null
  }
}

const fetchUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    const response = await axios.get('http://localhost:8080/auth/me', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    user.value = response.data
    // user_favorite_place가 문자열인 경우 이를 배열로 변환합니다.
    if (typeof user.value.user_favorite_place === 'string') {
      user.value.user_favorite_place = JSON.parse(user.value.user_favorite_place)
    }
    
    // 관심 지역 정보 가져오기
    const favoritePlaces = await Promise.all(
      user.value.user_favorite_place.map(async (houseCode) => {
        const houseInfo = await fetchHouseInfo(houseCode)
        return houseInfo ? `${houseInfo.dong_name} ${houseInfo.house_name}` : `Unknown House (${houseCode})`
      })
    )
    favoritePlacesDetails.value = favoritePlaces
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
    // user_favorite_place를 JSON 문자열로 변환하여 서버에 전달
    user.value.user_favorite_place = JSON.stringify(user.value.user_favorite_place)
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
      <div style="display: flex; flex-direction: column; align-items: center;">
        <label for="swal-input1" class="block text-gray-700">아이디:</label>
        <input id="swal-input1" class="swal2-input" style="width: 300px;" value="${user.value.user_name ? user.value.user_name : 'Kakao ID'}" readonly>
        <label for="swal-input2" class="block text-gray-700 mt-5">주소:</label>
        <input id="swal-input2" class="swal2-input" style="width: 300px;" value="${user.value.user_address ? user.value.user_address : 'Kakao Address'}">
        <label for="swal-input3" class="block text-gray-700 mt-5">관심 지역:</label>
        <div style="display: flex; flex-direction: column; align-items: center; width: 300px;">
          ${favoritePlacesDetails.value.map(place => `<input class="swal2-input" style="width: 300px; margin-bottom: 5px;" value="${place}" readonly>`).join('')}
        </div>
        <label for="swal-input4" class="block text-gray-700 mt-5">관리자 코드:</label>
        <input id="swal-input4" class="swal2-input" style="width: 300px;" value="${user.value.user_type ? user.value.user_type : 'Kakao User'}">
      </div>
    `,
    focusConfirm: false,
    preConfirm: () => {
      user.value.user_address = document.getElementById('swal-input2').value
      user.value.user_type = document.getElementById('swal-input4').value

      return updateUserInfo()
    },
    showCancelButton: true,
    confirmButtonText: '수정',
    cancelButtonText: '취소',
    showLoaderOnConfirm: true,
    allowOutsideClick: true,
    didOpen: () => {
      const swalModal = Swal.getPopup()
      const cancelButton = swalModal.querySelector('.swal2-cancel')
      cancelButton.textContent = '탈퇴'
      cancelButton.style.backgroundColor = '#d33'
      cancelButton.addEventListener('click', async (event) => {
        event.stopImmediatePropagation()
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
    },
    willClose: () => {
      emit('close') // 창이 닫힐 때 close 이벤트를 전송합니다.
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
