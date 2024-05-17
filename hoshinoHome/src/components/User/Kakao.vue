<template>
    <div>
      <a id="custom-login-btn" @click="kakaoLogin">
        <img
          src="https://k.kakaocdn.net/14/dn/btroDszwNrM/I6efHub1SN5KCJqLm1Ovx1/o.jpg"
          width="222"
          alt="카카오 로그인 버튼"
        />
      </a>
      <div @click="kakaoLogout">로그아웃</div>
    </div>
  </template>
  
  <script setup>
  import { onMounted } from 'vue';
  import { useRouter } from 'vue-router';

  const router = useRouter();

  // 카카오 SDK 초기화
  const initKakao = () => {
    if (!window.Kakao.isInitialized()) {
      window.Kakao.init('b6fc8f0d4cfc8c0ade4b040f2f2daf35');
    }
  };
  
  // 로그인 함수
  const kakaoLogin = () => {
    window.Kakao.Auth.login({
      scope: "profile_image",
      success: (authObj) => {
        getKakaoAccount(authObj);
      },
      fail: (err) => {
        console.error(err);
        alert("로그인 실패!");
      },
    });
  };
  
  // 사용자 정보 요청 함수
  const getKakaoAccount = (authObj) => {
    window.Kakao.API.request({
      url: "/v2/user/me",
      success: (res) => {
        const kakao_account = res.kakao_account;
        const nickname = kakao_account.profile.nickname;
        console.log("nickname", nickname);
  
        // 로그인 처리 및 로컬스토리지에 토큰 저장
        localStorage.setItem('token', authObj.access_token);
        alert("로그인 성공!");
        router.push('/').then(() => {
        window.location.reload(); // 홈 화면으로 이동 후 새로고침
      });
      },
      fail: (error) => {
        console.log(error);
      },
    });
  };
  
  // 로그아웃 함수
  const kakaoLogout = () => {
    window.Kakao.Auth.logout(() => {
      localStorage.removeItem('token');
      console.log("로그아웃 성공");
      alert("로그아웃 성공!");
      router.push('/').then(() => {
        window.location.reload(); // 홈 화면으로 이동 후 새로고침
      });
    });
  };
  
  // 컴포넌트가 마운트될 때 카카오 SDK 초기화
  onMounted(() => {
    initKakao();
  });
  </script>
  