<!-- src/components/GPTChat.vue -->

<template>
    <div>
      <h1>GPT Chat</h1>
      <input v-model="prompt" placeholder="Enter your prompt here" />
      <button @click="sendPrompt">Send</button>
      <p v-if="response">Response: {{ response }}</p>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        prompt: '',
        response: '',
      };
    },
    methods: {
      async sendPrompt() {
        try {
          const result = await axios.get('http://localhost:8080/gpt/chat', {
            params: { prompt: this.prompt },
          });
          this.response = result.data;
        } catch (error) {
          console.error('Error fetching GPT response:', error);
          this.response = 'Error fetching response';
        }
      },
    },
  };
  </script>
  
  <style scoped>
  /* Add your styles here */
  </style>
  