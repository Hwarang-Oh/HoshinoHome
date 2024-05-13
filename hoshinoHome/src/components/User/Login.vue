<template>
    <div>
      <h2>Login</h2>
      <form @submit.prevent="login">
        <input v-model="loginInfo.user_name" placeholder="Username" required>
        <input type="password" v-model="loginInfo.user_password" placeholder="Password" required>
        <button type="submit">Login</button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        loginInfo: {
          user_name: '',
          user_password: ''
        }
      };
    },
    methods: {
      async login() {
        try {
          const response = await axios.post('http://localhost:8080/auth/login', this.loginInfo);
          localStorage.setItem('token', response.data);
          alert('Login successful');
          this.$router.push('/dashboard');
        } catch (error) {
          alert('Login failed');
        }
      }
    }
  };
  </script>
  