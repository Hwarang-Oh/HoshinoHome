<template>
    <div>
      <h2>Register</h2>
      <form @submit.prevent="register">
        <input v-model="user.user_name" placeholder="Username" required>
        <input type="password" v-model="user.user_password" placeholder="Password" required>
        <input v-model="user.user_address" placeholder="Address">
        <input v-model="user.user_favorite_place" placeholder="Favorite Place">
        <input v-model="user.user_type" placeholder="Type">
        <button type="submit">Register</button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        user: {
          user_name: '',
          user_password: '',
          user_address: '',
          user_favorite_place: '',
          user_type: ''
        }
      };
    },
    methods: {
      async register() {
        try {
          const response = await axios.post('http://localhost:8080/auth/register', this.user);
          alert('Registration successful');
          this.$router.push('/login');
        } catch (error) {
          alert('Registration failed');
        }
      }
    }
  };
  </script>
  